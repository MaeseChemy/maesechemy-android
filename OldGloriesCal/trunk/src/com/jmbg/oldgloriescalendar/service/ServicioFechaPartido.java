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
import com.jmbg.oldgloriescalendar.data.PartidosSQLite;
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

public class ServicioFechaPartido extends Service {

	private PartidosSQLite partidosSQLite;
	private Vector<Partido> partidos;

	private NotificationManager nm;
	private static final int ID_NOTIF_CREAR = 1;

	@Override
	public void onCreate() {
		// Toast.makeText(this,
		// "[PARTIDO_CHECKER] Servicio de chequeo de fecha creado",
		// Toast.LENGTH_SHORT).show();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.partidosSQLite = new PartidosSQLite(this, "DBCalendar", null, 1);
		// Fecha actual
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		this.partidos = this.partidosSQLite.listaPartidos();

	}

	@Override
	public int onStartCommand(Intent intent, int fllags, int idArranque) {
		// Toast.makeText(this,
		// "[PARTIDO_CHECKER] Servicio arrancado ["+idArranque+"]",
		// Toast.LENGTH_SHORT).show();

		SimpleDateFormat dfday = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);

		String fechaActSt = dfday.format(Calendar.getInstance().getTime());
		for (Partido partido : this.partidos) {
			String fechaPartSt = dfday.format(partido.getFecha());
			int dias = -1;
			try {
				dias = diffDays(fechaActSt, fechaPartSt);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (0 <= dias && dias <= 2) {
				if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					generarNotificacionCompat(dias, partido);
				} else {
					generarNotificacion(dias, partido);
				}
			}
		}
		return START_STICKY;
	}

	@SuppressWarnings("deprecation")
	private void generarNotificacion(int dias, Partido partido) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
		String textoDias = "Partido en " + dias + " dias";
		String textoEncuentro = partido.getJornada()
				+ "-"
				+ (partido.isLocal() ? Constantes.equipo + " - "
						+ partido.getOponente() : partido.getOponente() + " - "
						+ Constantes.equipo);
		String textoLugar = df.format(partido.getFecha()) + " - "
				+ partido.getLugar();

		Notification notif = new Notification(R.drawable.ic_campo,
				textoDias, System.currentTimeMillis());
		PendingIntent inPen = PendingIntent.getActivity(this, 0, new Intent(
				this, MainActivity.class), 0);
		notif.setLatestEventInfo(this, textoEncuentro, textoLugar, inPen);

		nm.notify(ID_NOTIF_CREAR, notif);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void generarNotificacionCompat(int dias, Partido partido) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
		String textoDias = "Partido en " + dias + " dias";
		String textoEncuentro = partido.isLocal() ? Constantes.equipo + " - "
				+ partido.getOponente() : partido.getOponente() + " - "
				+ Constantes.equipo;
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
		// Toast.makeText(this, "[PARTIDO_CHECKER] Servicio destruido",
		// Toast.LENGTH_SHORT).show();
		nm.cancel(ID_NOTIF_CREAR);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private int diffDays(String dateStart, String dateEnd)
			throws ParseException {
		SimpleDateFormat dfday = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);
		Date d1 = dfday.parse(dateStart);
		Date d2 = dfday.parse(dateEnd);

		long diff = d2.getTime() - d1.getTime();
		double diffDays = diff / (24 * 60 * 60 * 1000);
				if(isDouble(diffDays))
			return -1;
		else
			return (int) diffDays;
	}
	
	private boolean isDouble(Double number){
		return number % 1 != 0;
	}

}
