package com.jmbg.oldgloriescalendar.service;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
 
import com.google.android.gcm.GCMBaseIntentService;
import com.jmbg.oldgloriescalendar.MainActivity;
import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.dao.LigaDBSQLite;
import com.jmbg.oldgloriescalendar.dao.entities.Partido;
import com.jmbg.oldgloriescalendar.util.Constantes;
import com.jmbg.oldgloriescalendar.util.Constantes.NotificationTypes;
import com.jmbg.oldgloriescalendar.view.ClasificacionActivity;
import com.jmbg.oldgloriescalendar.view.PartidoDetailActivity;
import com.jmbg.oldgloriescalendar.view.PlantillaActivity;
 
public class GCMIntentService extends GCMBaseIntentService {
 
    private static final String DISPLAY_MESSAGE_ACTION =
            "com.jmbg.oldgloriescalendar.DISPLAY_MESSAGE";
 
    private static final String EXTRA_MESSAGE = "message";
	
    
	
    public GCMIntentService() {
        super(Constantes.SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(Constantes.TAG_INTENT, "Device registered: regId = " + registrationId);
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(Constantes.TAG_INTENT, "Device unregistered");
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(Constantes.TAG_INTENT, "Received message");
        String message = intent.getExtras().getString("notification");
        String typeMessage = intent.getExtras().getString("notification_type");
        // notifies user
        generateNotification(context, typeMessage, message);
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(Constantes.TAG_INTENT, "Received deleted messages notification");
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(Constantes.TAG_INTENT, "Received error: " + errorId);
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(Constantes.TAG_INTENT, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String typeMessage, String message) {
    	NotificationTypes notif = NotificationTypes.valueOf(typeMessage);
    	boolean validNotif = false;
    	int icon = -1;
        Intent notificationIntent = null;
        String subMessage = "";
        String action = "";
        
    	switch (notif) {
		case PROX_PARTIDO:
			icon = R.drawable.ic_campo;
			notificationIntent = new Intent(context, PartidoDetailActivity.class);
			Partido partido = findPartido(message);
			if(partido != null){
				validNotif = true;
				notificationIntent.putExtra("partido", partido);
				message = "Partido Proximo: "+partido.getJornada();
				subMessage = (partido.isLocal() ? Constantes.EQUIPO + " - "
								+ partido.getOponente() : partido.getOponente() + " - "
								+ Constantes.EQUIPO);
				notificationIntent.putExtra("partido", partido);
				action = "Ver datos del partido";
			}else{
				validNotif = false;
			}
			break;
		case UPD_CLASIFICACION:
			icon = R.drawable.ic_clasificacion;
			notificationIntent = new Intent(context, ClasificacionActivity.class);
			action = "Ver y Actualizar Clasificacion";
			validNotif = true;
			break;
		case UPD_PLANTILLA:
			icon = R.drawable.ic_plantilla;
			notificationIntent = new Intent(context, PlantillaActivity.class);
			action = "Ver y Actualizar Plantilla";
			validNotif = true;
			break;
		default:
			validNotif = false;
			break;
		}
    	
    	if (validNotif){
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				generateNotificationJelly(context, notificationIntent, icon, message, subMessage, action);
			}else{
				generateNotification(context, notificationIntent, icon, message);
			}
  
    	}
    }
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void generateNotificationJelly(Context context, Intent notificationIntent, int icon, String message, String subMessage, String action){
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
    	String title = context.getString(R.string.app_name);
    	
    	PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
    	
    	Builder builder = new Notification.Builder(this);
    	builder.setSmallIcon(R.drawable.ic_launcher);
    	builder.setTicker(title);
    	builder.setContentTitle(title);
    	builder.setContentText(message);
    	builder.setSubText(subMessage);
    	builder.setAutoCancel(true);
    	builder.addAction(icon, action, intent);
    	
    	notificationManager.notify(0, builder.build());   
    }
    
    private void generateNotification(Context context, Intent notificationIntent, int icon, String message){
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);   
    }
    
    private Partido findPartido(String jornada){
    	LigaDBSQLite liga = new LigaDBSQLite(this, "DBCalendar", null);
    	Vector<Partido> partidos = liga.listaPartidos();
    	
		for (Partido partido : partidos) {
			if(partido.getJornada().equals(jornada))
				return partido;
		}
		return null;
    }

 
}
