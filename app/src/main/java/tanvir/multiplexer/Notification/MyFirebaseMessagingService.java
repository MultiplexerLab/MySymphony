package tanvir.multiplexer.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.util.Map;

import tanvir.multiplexer.Activity.MainActivity;
import tanvir.multiplexer.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        try {
            if (remoteMessage.getData() != null) {
                getDataFromRemoteMessage(remoteMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromRemoteMessage(RemoteMessage remoteMessage) {
        try {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String description = data.get("description");
            String imageLink = data.get("imageLink");
            String action = data.get("action");
            String redirectLink = data.get("redirectLink");
            String additionalData = data.get("additionalData");

            initNotification(title, description, imageLink, redirectLink);

            Log.e(TAG, "getDataFromRemoteMessage: " + title);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initNotification(String title, String description, final String imageLink, String redirect) {
        try {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels();
            }
            Intent notificationIntent;
            if (!redirect.isEmpty()) {
                notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirect));
            } else {
                notificationIntent = new Intent(this, MainActivity.class);

            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.notification);
            final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_big);
            final int notificationId = 1459756;
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "PushNotificationDemo")
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(title)      //TODO Need to change with appropriate Name
                    .setContentText(description)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(icon)
                    .setCustomBigContentView(remoteViews)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSound(defaultSoundUri);

            final Notification notification = notificationBuilder.build();
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Picasso.get()
                                .load(imageLink)
                                .error(R.drawable.dafault)
                                .into(remoteViews, R.id.imageLarge, notificationId, notification);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Picasso.get()
                                .load(R.drawable.dafault)
                                .into(remoteViews, R.id.imageLarge, notificationId, notification);
                    }
                }
            });

            notificationManager.notify(notificationId, notification);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        try {
            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel("PushNotificationDemo", "Demo Notification", NotificationManager.IMPORTANCE_HIGH);
            adminChannel.setDescription("DemoPushNotificationDescription");
            adminChannel.enableLights(true);
            adminChannel.setLightColor(Color.RED);
            adminChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(adminChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}