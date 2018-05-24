package lct.mysymphony.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import lct.mysymphony.Activity.PlayAudioActivity;
import lct.mysymphony.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class Utility {
    //Notification
    // Set up the notification ID
    public static final int NOTIFICATION_ID = 1;
    @SuppressWarnings("unused")
   /* public static Notification mNotificationManager;*/
    private static NotificationManager manager;
    private static Notification myNotication;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    // Create Notification
    @SuppressWarnings("deprecation")
    public static void initNotification(String songTitle, Context mContext) {
        try {
            /*String ns = Context.NOTIFICATION_SERVICE;
            mNotificationManager = (NotificationManager) mContext.getSystemService(ns);
            int icon = R.drawable.music_showcase;
            CharSequence tickerText = "Playing your song";
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon, tickerText, when);
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            Context context = mContext.getApplicationContext();
            CharSequence songName = "" + songTitle;
            Intent notificationIntent = new Intent(mContext, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            notification.setLatestEventInfo(context, songName, null, contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, notification);*/

            manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

            Intent intent = new Intent(mContext,PlayAudioActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            Notification.Builder builder = new Notification.Builder(mContext);

            builder.setAutoCancel(false);
            builder.setTicker("this is ticker text");
            builder.setContentTitle("Song is playing");
          /*  builder.setContentText("You have a new message");*/
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);
       /*     builder.setSubText("This is subtext..."); */  //API level 16
            builder.setNumber(100);
            builder.build();

            myNotication = builder.getNotification();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert manager != null;
                builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                manager.createNotificationChannel(notificationChannel);
            }
            assert manager != null;
            manager.notify(NOTIFICATION_ID, myNotication);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        //Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Pre appending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public void cancelNotification()
    {
       /* if (mNotificationManager!=null)
        {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
        else Log.d("mNotificationManager","null");*/
        if (manager!=null)
        {
            manager.cancel(NOTIFICATION_ID);
        }
        else Log.d("mNotificationManager","null");

    }
}