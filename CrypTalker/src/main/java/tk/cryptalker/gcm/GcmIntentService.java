package tk.cryptalker.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.greenrobot.event.EventBus;
import tk.cryptalker.activity.ChatActivity;
import tk.cryptalker.activity.HomeActivity;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.event.MessageEvent;
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

                // Get push type
                String type = extras.getString("type");

                if (type.equals("new_message")) {
                    newMessage(extras);
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

    private void newMessage(Bundle extras)
    {
        UserInfo userInfo = CrypTalkerApplication.getUserInfo();

        // If the app isn't initialised yet, just wait a little...
        try {
            while (userInfo == null) {
                Thread.sleep(5000);
                userInfo = CrypTalkerApplication.getUserInfo();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int roomId = Integer.parseInt(extras.getString("room_id"));

        // Add a new message to UserInfo + send a new message event
        Message message = new Message();
        message.setFrom(extras.getString("from_user"));
        message.setMessage(extras.getString("message"));
        message.setDatetime(extras.getString("date"));
        message.setRoom_id(roomId);

        // Store the new message to UserInfo
        userInfo.addMessageToRoom(roomId, message);

        Log.i(TAG, "Message received: " + message.getMessage() + " with id_room: " + String.valueOf(message.getRoom_id()) + " from: " + message.getFrom());

        if (ChatActivity.isActive() && ChatActivity.getRoomId() == roomId) {
            MessageEvent messageEvent = new MessageEvent(message);

            EventBus.getDefault().post(messageEvent);
        } else {
            sendNotification(message.getMessage());
        }
    }
}
