package com.jmbg.oldgloriescalendar.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.MainActivity;
import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.data.Partido;
import com.jmbg.oldgloriescalendar.data.LigaDBSQLite;
import com.jmbg.oldgloriescalendar.partido.PartidoDetailActivity;
import com.jmbg.oldgloriescalendar.util.Constantes;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ServicioFechaPartido extends Service {

	private LigaDBSQLite liga;
	private Vector<Partido> partidos;

	private NotificationManager nm;
	private static final int ID_NOTIF_CREAR = 1;

	@Override
	public void onCreate() {
		Log.i(Constantes.TAG, "[" + ServicioFechaPartido.class.getName()
				+ ".onCreate] Creando servicio de notificacion de partidos...");
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.liga = new LigaDBSQLite(this, "DBCalendar", null);
		// Fecha actual
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		this.partidos = this.liga.listaPartidos();

	}

	@Override
	public int onStartCommand(Intent intent, int fllags, int idArranque) {
		Log.d(Constantes.TAG,
				"["
						+ ServicioFechaPartido.class.getName()
						+ ".onStartCommand] Creado servicio de notificacion de partidos...");
		SimpleDateFormat dfday = new SimpleDateFormat("dd/MM/yyyy HH",
				Locale.US);

		String fechaActSt = dfday.format(Calendar.getInstance().getTime());

		/*
		 * Avisamos al usuario sobre los partidos que se van a jugar de la hora
		 * actual a DIAS_AVISO_MAX dias. Si el partido es a las 18 avisaremos al
		 * usuario a las 18 de los dias previos.
		 */
		Log.d(Constantes.TAG, "[" + ServicioFechaPartido.class.getName()
				+ ".onStartCommand] Buscamos partidos próximos a la fecha ["
				+ fechaActSt + "]...");
		for (Partido partido : this.partidos) {
			String fechaPartSt = dfday.format(partido.getFecha());
			double dias = -1;
			try {
				dias = diffDays(fechaActSt, fechaPartSt);
			} catch (ParseException exPE) {
				Log.e(Constantes.TAG,
						"["
								+ ServicioFechaPartido.class.getName()
								+ ".onStartCommand] Error parseando una de las fechas ["
								+ fechaActSt + "," + fechaPartSt + "]. ["
								+ exPE.getMessage() + "]");
			}

			// Si el resultado no es entero, no es un dia exacto.
			if (!isDouble(dias)) {
				if (0 <= dias && dias <= Constantes.DIAS_AVISO_MAX) {
					Log.d(Constantes.TAG,
							"["
									+ ServicioFechaPartido.class.getName()
									+ ".onStartCommand] Partido localizado. Generamos notificación. ["
									+ partido.toString() + "]");
					if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						generarNotificacionCompat((int) dias, partido);
					} else {
						generarNotificacion((int) dias, partido);
					}
				}
			}
		}
		return START_STICKY;
	}

	@SuppressWarnings("deprecation")
	private void generarNotificacion(int dias, Partido partido) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm",
				Locale.US);
		String textoDias = "Partido en " + dias + " dias";
		String textoEncuentro = partido.getJornada()
				+ "-"
				+ (partido.isLocal() ? Constantes.EQUIPO + " - "
						+ partido.getOponente() : partido.getOponente() + " - "
						+ Constantes.EQUIPO);
		String textoLugar = df.format(partido.getFecha()) + " - "
				+ partido.getLugar();

		Notification notif = new Notification(R.drawable.ic_campo, textoDias,
				System.currentTimeMillis());
		PendingIntent inPen = PendingIntent.getActivity(this, 0, new Intent(
				this, MainActivity.class), 0);
		notif.setLatestEventInfo(this, textoEncuentro, textoLugar, inPen);

		nm.notify(ID_NOTIF_CREAR, notif);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void generarNotificacionCompat(int dias, Partido partido) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm",
				Locale.US);
		String textoDias = "Partido en " + dias + " dias";
		String textoEncuentro = partido.isLocal() ? Constantes.EQUIPO + " - "
				+ partido.getOponente() : partido.getOponente() + " - "
				+ Constantes.EQUIPO;
		String textoLugar = df.format(partido.getFecha()) + " - "
				+ partido.getLugar();

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_campo).setTicker(textoDias)
				.setContentTitle(partido.getJornada())
				.setContentText(textoEncuentro).setSubText(textoLugar);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, PartidoDetailActivity.class);
			resultIntent.putExtra("partido", partido);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(MainActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
		}
		nm.notify(ID_NOTIF_CREAR, mBuilder.build());

	}

	@Override
	public void onDestroy() {
		nm.cancel(ID_NOTIF_CREAR);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private double diffDays(String dateStart, String dateEnd)
			throws ParseException {
		SimpleDateFormat dfday = new SimpleDateFormat("dd/MM/yyyy HH",
				Locale.US);
		Calendar d1 = Calendar.getInstance();
		d1.setTime(dfday.parse(dateStart));
		Calendar d2 = Calendar.getInstance();
		d2.setTime(dfday.parse(dateEnd));

		System.out.println("getTime start = " + d1.getTimeInMillis());
		System.out.println("getTime end = " + d2.getTimeInMillis());

		double diff = d2.getTimeInMillis() - d1.getTimeInMillis();

		double diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays;
	}

	private boolean isDouble(Double number) {
		return number % 1 != 0;
	}

}
