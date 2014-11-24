package tk.cryptalker.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import tk.cryptalker.activity.ChatActivity;
import tk.cryptalker.activity.HomeActivity;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.model.Message;
import tk.cryptalker.model.UserInfo;

public class GcmIntentService extends IntentService
{

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public GcmIntentService()
    {
        super("GcmIntentService");
    }

    public static final String TAG = "GcmIntentService";

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

                sendNotification("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                //@TODO: we get gcm pus before the app is initialised (null userInfo) :(

                // "Switch" push type
                String type = extras.getString("type");

                if (type.equals("new_message")) {
                    UserInfo userInfo = CrypTalkerApplication.getUserInfo();

                    Message message = new Message();
                    message.setFrom(extras.getString("from_user"));
                    message.setMessage(extras.getString("message"));
                    message.setDatetime(extras.getString("date"));

                    userInfo.addMessageToRoom(extras.getInt("room_id"), message);

                    Log.i(TAG, "Message received: " + extras.getString("message"));

                    if (ChatActivity.isActive() && ChatActivity.getRoomId() == extras.getInt("room_id")) {
                        //@TODO: restart activity
                    }
                }

            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    /**
     * Put the message into a notification and post it.
     * This is just one simple example of what you might choose to do with
     * a GCM message.
     */
    private void sendNotification(String msg) {
        Log.i(TAG, msg);
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("GCM Notification")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
