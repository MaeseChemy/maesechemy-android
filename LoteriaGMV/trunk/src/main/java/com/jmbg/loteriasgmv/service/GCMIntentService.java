package com.jmbg.loteriasgmv.service;

import com.google.android.gcm.GCMBaseIntentService;
import com.jmbg.loteriasgmv.MainActivity;
import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.util.Constantes;
import com.jmbg.loteriasgmv.util.Constantes.NotificationTypes;
import com.jmbg.loteriasgmv.util.LogManager;
import com.jmbg.loteriasgmv.view.BetActivity;
import com.jmbg.loteriasgmv.view.ParticipantActivity;
import com.jmbg.loteriasgmv.view.PotActivity;
import com.jmbg.loteriasgmv.view.PriceActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class GCMIntentService extends GCMBaseIntentService {
	
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 1;
    
	private LogManager LOG = LogManager.getLogger(this.getClass());

    public GCMIntentService() {
        super(Constantes.SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
    	LOG.info("Device registered: regId = " + registrationId);
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
    	LOG.info("Device unregistered");
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
    	LOG.info("Received message");
        String message = intent.getExtras().getString("notification");
        String typeMessage = intent.getExtras().getString("notification_type");

        //Notificamos al usuario de datos nuevos en la aplicacion
        generateNotification(context, typeMessage, message);
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
    	LOG.info("Received deleted messages notification");
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
    	LOG.info("Received error: " + errorId);
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
    	LOG.info("Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
    
    private void generateNotification(Context context, String typeMessage, String message) {
    	NotificationTypes notif = NotificationTypes.valueOf(typeMessage);
    	int icon;
    	String title;
    	String text = message;
        PendingIntent contentIntent;
        switch (notif) {
		case UPD_BETS:
			icon = R.drawable.ic_bets;
			title = "Apuestas";
			contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, BetActivity.class), 0);
			break;
		case UPD_PARTICIPANTS:
			icon = R.drawable.ic_participants;
			title = "Participantes";
			contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, ParticipantActivity.class), 0);
			break;
		case UPD_POT:
			icon = R.drawable.ic_pot;
			title = "Fondo";
			contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, PotActivity.class), 0);
			break;
		case UPD_PRICES:
			icon = R.drawable.ic_prize;
			title = "Premios";
			contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, PriceActivity.class), 0);
			break;
		default:
			icon = R.drawable.ic_launcher;
			title = typeMessage;
			contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, MainActivity.class), 0);
			break;
        }
        
        sendNotification(icon, title, text, contentIntent);			
    }
    
    private void sendNotification(int idIcon, String title, String text, PendingIntent contentIntent) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(idIcon)
        .setContentTitle(title)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(text))
        .setContentText(text);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}