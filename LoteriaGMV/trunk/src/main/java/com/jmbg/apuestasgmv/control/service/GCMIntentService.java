package com.jmbg.apuestasgmv.control.service;

import com.google.android.gcm.GCMBaseIntentService;
import com.jmbg.apuestasgmv.Constants;
import com.jmbg.apuestasgmv.Constants.NotificationTypes;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.utils.LogManager;
import com.jmbg.apuestasgmv.views.BetActivity;
import com.jmbg.apuestasgmv.views.HomeActivity;
import com.jmbg.apuestasgmv.views.ParticipantActivity;
import com.jmbg.apuestasgmv.views.PotActivity;
import com.jmbg.apuestasgmv.views.PriceActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class GCMIntentService extends GCMBaseIntentService {

	private NotificationManager mNotificationManager;

	private LogManager logger = LogManager.getLogger(this.getClass());

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		logger.info("Device registered: regId = " + registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		logger.info("Device unregistered");
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		logger.info("Received message");
		String message = intent.getExtras().getString("notification");
		String typeMessage = intent.getExtras().getString("notification_type");

		// Notificamos al usuario de datos nuevos en la aplicacion
		generateNotification(context, typeMessage, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		logger.info("Received deleted messages notification");
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		logger.info("Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		logger.info("Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	private void generateNotification(Context context, String typeMessage,
			String message) {
		NotificationTypes notif;
		try {
			notif = NotificationTypes.valueOf(typeMessage);
		} catch (IllegalArgumentException iae) {
			logger.info("Problem with notification type, setting default value: "
					+ iae.getMessage());
			notif = NotificationTypes.UPD_OTHER;
		}
		String text = message;

		int icon;
		String title;
		PendingIntent contentIntent;
		Intent notificationIntent;

		switch (notif) {
		case UPD_BETS:
			icon = R.drawable.ic_bets;
			title = "Apuestas";
			notificationIntent = new Intent(context, BetActivity.class);
			notificationIntent.putExtra("forceUpdate", true);
			contentIntent = PendingIntent.getActivity(context,
					notificationIdByTypeGCM(notif), notificationIntent, 0);
			break;
		case UPD_PARTICIPANTS:
			icon = R.drawable.ic_participants;
			title = "Participantes";
			notificationIntent = new Intent(context, ParticipantActivity.class);
			notificationIntent.putExtra("forceUpdate", true);
			contentIntent = PendingIntent.getActivity(context,
					notificationIdByTypeGCM(notif), notificationIntent, 0);
			break;
		case UPD_POT:
			icon = R.drawable.ic_pot;
			title = "Fondo";
			notificationIntent = new Intent(context, PotActivity.class);
			notificationIntent.putExtra("forceUpdate", true);
			contentIntent = PendingIntent.getActivity(context,
					notificationIdByTypeGCM(notif), notificationIntent, 0);
			break;
		case UPD_PRICES:
			icon = R.drawable.ic_prize;
			title = "Premios";
			notificationIntent = new Intent(context, PriceActivity.class);
			notificationIntent.putExtra("forceUpdate", true);
			contentIntent = PendingIntent.getActivity(context,
					notificationIdByTypeGCM(notif), notificationIntent, 0);
			break;
		case UPD_OTHER:
		default:
			icon = R.drawable.ic_launcher;
			title = typeMessage;
			notificationIntent = new Intent(context, HomeActivity.class);
			contentIntent = PendingIntent.getActivity(context,
					notificationIdByTypeGCM(notif), notificationIntent, 0);
			break;
		}

		sendNotification(icon, title, text, contentIntent, notif);
	}

	private void sendNotification(int idIcon, String title, String text,
			PendingIntent contentIntent, NotificationTypes notif) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(idIcon).setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
				.setContentText(text).setOnlyAlertOnce(true)
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(notificationIdByTypeGCM(notif),
				mBuilder.build());
	}

	private int notificationIdByTypeGCM(NotificationTypes type) {
		switch (type) {
		case UPD_BETS:
			return 0;
		case UPD_PARTICIPANTS:
			return 1;
		case UPD_POT:
			return 2;
		case UPD_PRICES:
			return 3;
		default:
			return 4;
		}
	}
}