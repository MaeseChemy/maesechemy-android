package com.jmbg.apuestasgmv.control.service;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jmbg.apuestasgmv.Constants;
import com.jmbg.apuestasgmv.Constants.NotificationTypes;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.utils.LogManager;
import com.jmbg.apuestasgmv.views.BetActivity;
import com.jmbg.apuestasgmv.views.HomeActivity;
import com.jmbg.apuestasgmv.views.ParticipantActivity;
import com.jmbg.apuestasgmv.views.PotActivity;
import com.jmbg.apuestasgmv.views.PriceActivity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GCMIntentService extends IntentService {

	private NotificationManager mNotificationManager;

	private LogManager logger = LogManager.getLogger(this.getClass());

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				logger.info("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				logger.info("Deleted messages");

				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				logger.info("Received message");
				String message = intent.getExtras().getString("notification");
				String typeMessage = intent.getExtras().getString(
						"notification_type");

				// Notificamos al usuario de datos nuevos en la aplicacion
				generateNotification(this, typeMessage, message);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
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