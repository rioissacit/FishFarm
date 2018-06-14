package helperClass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import fishfarm.gotech.HomeActivity;
import fishfarm.gotech.R;

/**
 * Created by aruna.ramakrishnan on 3/5/2018.
 */
public class FcmMessageService extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;
    RemoteViews contentViewBig,contentViewSmall;

    static void updateMyActivity(Context context, String count) {

        Intent intent = new Intent("notifications");

        //put whatever data you want to send, if any
        intent.putExtra("count", count);

        //send broadcast
        context.sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //onMessageReceived will be called when ever you receive new message from server.. (app in background and foreground )
        Log.d("FCM", "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification()!=null){
            Log.d("FCM", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            Map<String, String> notify;
            notify = remoteMessage.getData();
            String message = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
            String msgType = notify.get("msg_type");
            String notification_Id = notify.get("Notification_Id");
            String count = notify.get("Count");
            //if(notify !=null && !notify.matches("null null") && !notify.isEmpty()&& !notify.matches("")) {
            sendNotification(message,title,msgType,notification_Id,count);
        }
    }

    private void sendNotification(String msg, String title, String msgType, String notificationId, String notificationCount) {

        updateMyActivity(this, notificationCount);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

//        contentViewBig = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
//        contentViewBig.setTextViewText(R.id.title, title);
//        contentViewBig.setTextViewText(R.id.text, msg);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("FromNotification", true);
        intent.putExtra("PAGE_NAME", "Notification");
        intent.putExtra("STATUS", "Notification");
        intent.putExtra("NotificationType", msgType);
        intent.putExtra("NotificationId", notificationId);
        intent.putExtra("NotificationDate", System.currentTimeMillis());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.app_icon)
                .setCustomContentView(contentViewBig)
                .setContentIntent(contentIntent);

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }


}
