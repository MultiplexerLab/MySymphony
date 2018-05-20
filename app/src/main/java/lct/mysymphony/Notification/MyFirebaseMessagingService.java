package lct.mysymphony.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import lct.mysymphony.Activity.AuttoHashiActivity;
import lct.mysymphony.Activity.BigganOProjuktiActivity;
import lct.mysymphony.Activity.CartoonActivity;
import lct.mysymphony.Activity.GoromKhoborActivity;
import lct.mysymphony.Activity.JibonJaponActivity;
import lct.mysymphony.Activity.KheladhulaActivity;
import lct.mysymphony.Activity.MainActivity;
import lct.mysymphony.Activity.PachMishaliActivity;
import lct.mysymphony.Activity.PorashunaActivity;
import lct.mysymphony.Activity.WallpaperBundleActivity;
import lct.mysymphony.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationManager notificationManager;
    private String channel = "Exclusive Offer";
    private final String USERNAME = "[NAME]";


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
            Log.e(TAG, "getDataFromRemoteMessage: " + remoteMessage.getData().toString());
            Map<String, String> data = remoteMessage.getData();
            int notificationId = Integer.parseInt(data.get("notificationId"));
            String title = data.get("title");
            String description = data.get("description");
            String KEY = data.get("KEY");
            String notificationCategory = data.get("notificationCategory");
            String imageLink = data.get("imageLink");
            SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
            String userName = prefs.getString("username", "");
            if (notificationId == 1) {
                String activity = data.get("activityName");
                //Notification Id 1 = Contain Image - No Button - Open specific Activity
                initNotificationForImageWithOutButton(true, title, description, imageLink, activity, userName);
            } else if (notificationId == 2) {
                //Notification Id 1 = Contain Image - No Button - Open redirect Link
                String link = data.get("activityName");
                initNotificationForImageWithOutButton(false, title, description, imageLink, link, userName);

            } else if (notificationId == 3) {

                String link = data.get("activityName");
                int totalButton = Integer.parseInt(data.get("totalButton"));
                String s = data.get("buttonList");
                JsonParser jsonParser = new JsonParser();
                String jsonArrayString = s.toString();
                JsonArray arrayFromString = jsonParser.parse(jsonArrayString).getAsJsonArray();
                Log.e(TAG, "getDataFromRemoteMessage: " + arrayFromString.get(0).toString());
                List<NotificationButton> listBtn = new ArrayList<>();
                for (int i = 0; i < arrayFromString.size(); i++) {
                    String dat = arrayFromString.get(i).toString();
                    Gson gson = new Gson();
                    NotificationButton button = gson.fromJson(dat, NotificationButton.class);
                    Log.e(TAG, "getDataFromRemoteMessage: " + button.toString());
                    listBtn.add(button);
                }

                initNotificationForImageWithMultipleButton(totalButton, listBtn, title, description, imageLink, userName);
            }


            Log.e(TAG, "getDataFromRemoteMessage: " + notificationId);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initNotificationForImageWithMultipleButton(int totalButton, List<NotificationButton> listBtn, String title, String description, final String imgeLink, String userName) {
        try {

            if (title.contains(USERNAME)) {
                Log.e(TAG, "initNotificationForImageWithMultipleButton: " + userName );
                title = title.replace(USERNAME, userName);
            }


            RemoteViews remoteView1 = null;
            final int notificationId = 1459756;
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = null;


            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels();
            }
            if (totalButton == 1) {

                Intent notificationIntent1 = getIntentForActivity(listBtn.get(0).getActivityName());
                PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_CANCEL_CURRENT);


                remoteView1 = new RemoteViews(getPackageName(), R.layout.notification_image_btn_one);
                remoteView1.setTextViewText(R.id.notificationTitle, title);
                remoteView1.setTextViewText(R.id.notificationDes, description);
                remoteView1.setTextViewText(R.id.notificationButton1, listBtn.get(0).getButtonLabel());
                remoteView1.setOnClickPendingIntent(R.id.notificationButton1, pendingIntent1);

                notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent1)
                        .setCustomBigContentView(remoteView1)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


            } else if (totalButton == 2) {
                Intent notificationIntent1 = getIntentForActivity(listBtn.get(0).getActivityName());
                PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_CANCEL_CURRENT);

                Intent notificationIntent2 = getIntentForActivity(listBtn.get(1).getActivityName());
                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, notificationIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
                remoteView1 = new RemoteViews(getPackageName(), R.layout.notification_image_btn_two);

                remoteView1.setTextViewText(R.id.notificationTitle, title);
                remoteView1.setTextViewText(R.id.notificationDes, description);
                remoteView1.setTextViewText(R.id.notificationButton1, listBtn.get(0).getButtonLabel());
                remoteView1.setTextViewText(R.id.notificationButton2, listBtn.get(1).getButtonLabel());
                remoteView1.setOnClickPendingIntent(R.id.notificationButton1, pendingIntent1);
                remoteView1.setOnClickPendingIntent(R.id.notificationButton2, pendingIntent2);


                notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent1)
                        .setCustomBigContentView(remoteView1)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


            } else if (totalButton == 3) {
                Intent notificationIntent1 = getIntentForActivity(listBtn.get(0).getActivityName());
                PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_CANCEL_CURRENT);

                Intent notificationIntent2 = getIntentForActivity(listBtn.get(1).getActivityName());
                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, notificationIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
                Intent notificationIntent3 = getIntentForActivity(listBtn.get(2).getActivityName());
                PendingIntent pendingIntent3 = PendingIntent.getActivity(this, 0, notificationIntent3, PendingIntent.FLAG_CANCEL_CURRENT);
                remoteView1 = new RemoteViews(getPackageName(), R.layout.notification_image_btn_three);

                remoteView1.setTextViewText(R.id.notificationTitle, title);
                remoteView1.setTextViewText(R.id.notificationDes, description);
                remoteView1.setTextViewText(R.id.notificationButton1, listBtn.get(0).getButtonLabel());
                remoteView1.setTextViewText(R.id.notificationButton2, listBtn.get(1).getButtonLabel());
                remoteView1.setTextViewText(R.id.notificationButton3, listBtn.get(2).getButtonLabel());
                remoteView1.setOnClickPendingIntent(R.id.notificationButton1, pendingIntent1);
                remoteView1.setOnClickPendingIntent(R.id.notificationButton2, pendingIntent2);
                remoteView1.setOnClickPendingIntent(R.id.notificationButton3, pendingIntent3);

                notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent1)
                        .setCustomBigContentView(remoteView1)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

            }


            final Notification notification = notificationBuilder.build();
            Handler uiHandler = new Handler(Looper.getMainLooper());
            final RemoteViews finalRemoteView = remoteView1;
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Picasso.get()
                                .load(imgeLink)
                                .into(finalRemoteView, R.id.imageLarge, notificationId, notification);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            });

            notificationManager.notify(notificationId, notification);


        } catch (Exception e)

        {
            e.printStackTrace();
        }

    }

    private void initNotificationForImageWithOutButton(boolean isOpenActivity, String title, String description, final String imageLink, String redirect, String userName) {
        try {
            if (title.contains(USERNAME)) {
                title = title.replace(USERNAME, userName);
            }

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels();
            }
            Intent notificationIntent = getIntentForActivity(redirect);


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_image_only);

            remoteViews.setTextViewText(R.id.notificationTitle, title);
            remoteViews.setTextViewText(R.id.notificationDes, description);
            final int notificationId = 1459756;
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setCustomBigContentView(remoteViews)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

            final Notification notification = notificationBuilder.build();
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Picasso.get()
                                .load(imageLink)
                                .into(remoteViews, R.id.imageLarge, notificationId, notification);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            });

            notificationManager.notify(notificationId, notification);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }

    private Intent getIntentForActivity(String action) {
        Log.e(TAG, "getIntentForActivity: " + action);

        if (action.equals("home")) {
            return new Intent(this, MainActivity.class);
        } else if (action.equals("joke")) {
            return new Intent(this, AuttoHashiActivity.class);
        } else if ((action == "study")) {
            return new Intent(this, PorashunaActivity.class);
        } else if (action.equals("news")) {
            return new Intent(this, GoromKhoborActivity.class);
        } else if (action.equals("sports")) {
            return new Intent(this, KheladhulaActivity.class);
        } else if (action.equals("lifestyle")) {
            return new Intent(this, JibonJaponActivity.class);
        } else if (action.equals("mix")) {
            return new Intent(this, PachMishaliActivity.class);
        } else if (action.equals("science")) {
            return new Intent(this, BigganOProjuktiActivity.class);
        } else if (action.equals("cartoon")) {
            return new Intent(this, CartoonActivity.class);
        } else if (action.equals("wallpaper")) {
            return new Intent(this, WallpaperBundleActivity.class);
        }


        return new Intent(this, MainActivity.class);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        try {
            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel(channel, "Ramadan Eid OFFER", NotificationManager.IMPORTANCE_HIGH);
            adminChannel.setDescription("50% Offer in Every Products");
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

    class NotificationButton {

        @SerializedName("activityName")
        @Expose
        private String activityName;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("buttonLabel")
        @Expose
        private String buttonLabel;

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getButtonLabel() {
            return buttonLabel;
        }

        public void setButtonLabel(String buttonLabel) {
            this.buttonLabel = buttonLabel;
        }

        @Override
        public String toString() {
            return activityName + " " + id;
        }
    }
}

