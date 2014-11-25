package tk.cryptalker.gcm;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.greenrobot.event.EventBus;
import tk.cryptalker.R;
import tk.cryptalker.activity.AbstractActivity;
import tk.cryptalker.activity.ChatActivity;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.event.MessageEvent;
import tk.cryptalker.model.Message;
import tk.cryptalker.model.UserInfo;

import android.support.v4.app.NotificationManagerCompat;

public class GcmIntentService extends IntentService
{

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

        Log.i(TAG, "Received GCM");

        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

                sendNotification("Error", "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

                sendNotification("Error", "Deleted messages on server: " + extras.toString());
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
    private void sendNotification(String title, String message) {

        int notificationId = 001;

        // Build intent for notification content
        Intent viewIntent = new Intent(this, ChatActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(viewPendingIntent)
                .setVibrate(new long[]{0, 600, 1000})
                .setLights(Color.BLUE, 3000, 3000);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

        // Wake screen
        PowerManager pm = (PowerManager)AbstractActivity.getContext().getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();

        if (!isScreenOn)
        {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE, "MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }
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
            sendNotification(message.getFrom(), message.getMessage());
        }
    }
}
