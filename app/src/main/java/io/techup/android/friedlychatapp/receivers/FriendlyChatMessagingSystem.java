package io.techup.android.friedlychatapp.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.activities.MainActivity;

/**
 * Created by ceosilvajr on 15/08/16.
 */
public class FriendlyChatMessagingSystem extends FirebaseMessagingService {

  private static final String TAG = "MyFirebaseMsgService";

  /**
   * Called when message is received.
   *
   * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
   */
  @Override public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.d(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.d(TAG, "Message data payload: " + remoteMessage.getData());
    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
      sendNotification(remoteMessage.getNotification().getBody());
    }
  }
  
  /**
   * Create and show a simple notification containing the received FCM message.
   *
   * @param messageBody FCM message body received.
   */
  private void sendNotification(String messageBody) {
    final Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    final PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

    final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    final NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(this).setSmallIcon(R.drawable.img_logo)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent);

    final NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
  }
}
