package harmony.app.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import harmony.app.Activity.AppList;
import harmony.app.Activity.ContentDescriptionActivity.JapitoJibonDescriptionActivity;
import harmony.app.Activity.ContentDescriptionActivity.PorashunaDescriptionActivity;
import harmony.app.Activity.Emoticons;
import harmony.app.Activity.HomePage;
import harmony.app.Activity.PlayAudioActivity;
import harmony.app.Activity.PorashunaActivity;
import harmony.app.ModelClass.AppData;
import harmony.app.ModelClass.Porashuna;
import harmony.app.R;
import harmony.app.helper.AppLogger;
import harmony.app.helper.Endpoints;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationManager notificationManager;
    private String channel = "Exclusive Offer";
    private final String USERNAME = "[NAME]";
    private String appContentId;
    int notificationId;
    Intent ccontentIntent = null;
    Date currenTime;
    DateFormat dateFormat;
    RequestQueue queue;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        queue = Volley.newRequestQueue(this);

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
            notificationId = Integer.parseInt(data.get("notificationId"));
            Log.i("notificationId", notificationId + "");
            String title = data.get("title");
            String description = data.get("description");

            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            currenTime = new Date();
            AppLogger.insertLogs(this, dateFormat.format(currenTime), "Y", notificationId + "",
                    "RECEIVED", data.toString(), "notification");

            String KEY = data.get("KEY");
            appContentId = data.get("appContentId");
            String notificationCategory = data.get("notificationCategory");
            Log.i("Cat", notificationCategory);
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
                Log.i("LinkUrl", link);
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
            } else if (notificationCategory.equals("apkLink")) {
                int totalButton = Integer.parseInt(data.get("totalButton"));
                String s = data.get("buttonList");
                JsonParser jsonParser = new JsonParser();
                String jsonArrayString = s.toString();
                JsonArray arrayFromString = jsonParser.parse(jsonArrayString).getAsJsonArray();
                List<NotificationButton> listBtn = new ArrayList<>();
                for (int i = 0; i < arrayFromString.size(); i++) {
                    String dat = arrayFromString.get(i).toString();
                    Gson gson = new Gson();
                    NotificationButton button = gson.fromJson(dat, NotificationButton.class);
                    Log.e(TAG, "getDataFromRemoteMessage: " + button.toString());
                    listBtn.add(button);
                }
                initNotificationForImageWithMultipleButton(totalButton, listBtn, title, description, imageLink, userName);

            } else if (notificationCategory.equals("contentRedirect")) {
                int contentId = Integer.parseInt(data.get("contentId"));
                Log.i("ContentId", contentId + "");
                initNotificationForContentId(title, description, imageLink, contentId);
            }
            Log.e(TAG, "getDataFromRemoteMessage: " + notificationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initNotificationForContentId(String title, String description, final String imageLink, int contentId) {
        try {
            if (title.contains(USERNAME)) {
                title = title.replace(USERNAME, title);
            }

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels();
            }
            Intent notificationIntent = getIntentForContentId(contentId);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (notificationIntent == null) {

            }else{
                Log.i("notiInten", notificationIntent.getExtras().toString());
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
            }

            //Log.i("notificationIntent", notificationIntent.toString());


        } catch (Exception e) {
            Log.e("NotifException", e.toString());
        } finally {

        }
    }

    private void initNotificationForImageWithMultipleButton(int totalButton, List<NotificationButton> listBtn, String title, String description, final String imgeLink, String userName) {
        try {
            if (title.contains(USERNAME)) {
                Log.e(TAG, "initNotificationForImageWithMultipleButton: " + userName);
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
                Intent notificationIntent1 = getIntentForActivity(listBtn.get(0).getActivityName(), title);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_CANCEL_CURRENT);

                remoteView1 = new RemoteViews(getPackageName(), R.layout.notification_image_btn_one);
                remoteView1.setTextViewText(R.id.notificationTitle, title);
                remoteView1.setTextViewText(R.id.notificationDes, description);
                remoteView1.setTextViewText(R.id.notificationButton1, listBtn.get(0).getButtonLabel());
                remoteView1.setOnClickPendingIntent(R.id.notificationButton1, pendingIntent1);

                notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent1)
                        .setCustomBigContentView(remoteView1)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


            } else if (totalButton == 2) {
                Intent notificationIntent1 = getIntentForActivity(listBtn.get(0).getActivityName(), title);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_CANCEL_CURRENT);

                Intent notificationIntent2 = getIntentForActivity(listBtn.get(1).getActivityName(), title);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, notificationIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
                remoteView1 = new RemoteViews(getPackageName(), R.layout.notification_image_btn_two);

                remoteView1.setTextViewText(R.id.notificationTitle, title);
                remoteView1.setTextViewText(R.id.notificationDes, description);
                remoteView1.setTextViewText(R.id.notificationButton1, listBtn.get(0).getButtonLabel());
                remoteView1.setTextViewText(R.id.notificationButton2, listBtn.get(1).getButtonLabel());
                remoteView1.setOnClickPendingIntent(R.id.notificationButton1, pendingIntent1);
                remoteView1.setOnClickPendingIntent(R.id.notificationButton2, pendingIntent2);


                notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent1)
                        .setCustomBigContentView(remoteView1)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


            } else if (totalButton == 3) {
                Intent notificationIntent1 = getIntentForActivity(listBtn.get(0).getActivityName(), title);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_CANCEL_CURRENT);

                Intent notificationIntent2 = getIntentForActivity(listBtn.get(1).getActivityName(), title);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, notificationIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
                Intent notificationIntent3 = getIntentForActivity(listBtn.get(2).getActivityName(), title);
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
                        .setSmallIcon(R.drawable.icon)
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

        } catch (Exception e) {
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
            Intent notificationIntent = getIntentForActivity(redirect, title);

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

    private Intent getIntentForActivity(String action, String title) {
        Log.e(TAG, "getIntentForActivity: " + action);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "Y", notificationId + "",
                "CLICKED", "this notification was clicked", "notification");
        if (action.equals("home")) {
            return new Intent(this, HomePage.class);
        } else if (action.equals("joke")) {
            Intent myIntent = new Intent(this, PorashunaActivity.class);
            myIntent.putExtra("tag", "autto_hashi");
            return myIntent;
        } else if (action.equals("study")) {
            Intent myIntent = new Intent(this, PorashunaActivity.class);
            myIntent.putExtra("tag", "education");
            return myIntent;
        } else if (action.equals("news")) {
            Intent news = new Intent(this, PorashunaActivity.class);
            news.putExtra("tag", "news");
            return news;
        } else if (action.equals("sports")) {
            Intent myIntent = new Intent(this, PorashunaActivity.class);
            myIntent.putExtra("tag", "sports");
            return myIntent;
        } else if (action.equals("lifestyle")) {
            Intent myIntent = new Intent(this, PorashunaActivity.class);
            myIntent.putExtra("tag", "daily_life");
            return myIntent;
        } else if (action.equals("mix")) {
            Intent myIntent = new Intent(this, PorashunaActivity.class);
            myIntent.putExtra("tag", "mixed");
            return myIntent;
        } else if (action.equals("science")) {
            Intent myIntent = new Intent(this, PorashunaActivity.class);
            myIntent.putExtra("tag", "science");
            return myIntent;
        } else if (action.equals("applist")) {
            Intent myIntent = new Intent(this, AppList.class);
            return myIntent;
        } else if (action.equals("cartoon")) {
            Intent myIntent = new Intent(this, PorashunaActivity.class);
            myIntent.putExtra("tag", "kk_mela");
            return myIntent;
        } else if (action.equals("wallpaper")) {
            return new Intent(this, Emoticons.class);
        } else if (action.equals("apkDownload")) {
            Intent downLoadIntent = new Intent(this, HomePage.class);
            downLoadIntent.putExtra("appContentId", appContentId);
            downLoadIntent.putExtra("notificationTitle", title);
            return downLoadIntent;
        } else if (action.contains("http")) {
            String url = action;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            return i;
        }

        return new Intent(this, HomePage.class);
    }


    private Intent getIntentForContentId(int contentId) {
        GetContentData object = new MyFirebaseMessagingService.GetContentData();
        object.execute(contentId + "");
        return ccontentIntent;
    }

    class GetContentData extends AsyncTask<String, Void, String> {
        private Exception exception;
        String contentId;

        protected void onPreExecute() {
        }

        protected String doInBackground(String... ids) {
            contentId = ids[0];
            String url = "http://bot.sharedtoday.com:9500/ws/mysymphony/getSpecificContent?id=" + contentId;
            Log.i("urlData", url);

            getDataFromContentId(url);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                SharedPreferences mPrefs = getSharedPreferences("tempData", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = mPrefs.getString("jsonDataObject", "");
                JSONObject jsonDataObject = gson.fromJson(json, JSONObject.class);
                Log.i("sharedData", jsonDataObject.toString());

                mPrefs.edit().remove("jsonDataObject").commit();

                String contentCat = jsonDataObject.getString("contentCat");
                String id = jsonDataObject.getString("id");
                String contentType = jsonDataObject.getString("contentType");
                String contentTitle = jsonDataObject.getString("contentTitle");
                String contentDescription = jsonDataObject.getString("contentDescription");
                String thumbNail_image = Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("thumbNail_image");
                int contentPrice = jsonDataObject.getInt("contentPrice");

                if (contentCat.equals("mobile_app")) {
                    ArrayList<AppData> appList = new ArrayList<>();
                    String packageName = jsonDataObject.getString("reference1");
                    String versionName = jsonDataObject.getString("reference2");
                    String versionCode = jsonDataObject.getString("reference3");
                    String contentUrl = Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("contentUrl");
                    appList.add(new AppData(id + "", contentTitle, contentDescription, thumbNail_image, contentUrl, packageName, versionCode));

                    ccontentIntent = new Intent(MyFirebaseMessagingService.this, AppList.class);
                    Gson gson2 = new Gson();
                    String appListJson = gson2.toJson(appList);
                    ccontentIntent.putExtra("appList", appListJson);
                } else if (contentCat.equals("music_video")) {
                    if (contentType.equals("audio")) {
                        String contentUrl = Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("contentUrl");
                        Porashuna porashuna = new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, Integer.parseInt(contentId));
                        ccontentIntent = new Intent(MyFirebaseMessagingService.this, PlayAudioActivity.class);
                        ccontentIntent.putExtra("cameFromWhichActivity", "music_video");
                        ccontentIntent.putExtra("data", (Serializable) porashuna);
                    } else {
                        String contentUrl = Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("contentUrl");
                        Porashuna porashuna = new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, Integer.parseInt(contentId));
                        ccontentIntent = new Intent(MyFirebaseMessagingService.this, JapitoJibonDescriptionActivity.class);
                        ccontentIntent.putExtra("Data", (Serializable) porashuna);
                        ccontentIntent.putExtra("cameFromWhichActivity", "music_video");
                    }
                } else {
                    String contentUrl = jsonDataObject.getString("contentUrl");
                    Porashuna porashuna = new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, Integer.parseInt(contentId));
                    ccontentIntent = new Intent(MyFirebaseMessagingService.this, PorashunaDescriptionActivity.class);
                    ccontentIntent.putExtra("Data", (Serializable) porashuna);
                }
            } catch (ExceptionInInitializerError e) {
                Log.e("AnoError", e.toString());
            } catch (JSONException e) {
                Log.e("JSONError", e.toString());
            }
        }
    }

    public void getDataFromContentId(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i("responseContent", response.toString());
                    JSONObject jsonDataObject = response.getJSONObject(0);
                    SharedPreferences mPrefs = getSharedPreferences("tempData", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    mPrefs.edit().remove("jsonDataObject").commit();
                    Gson gson = new Gson();
                    String jsonNew = gson.toJson(jsonDataObject);
                    prefsEditor.putString("jsonDataObject", jsonNew);
                    prefsEditor.commit();
                    Log.i("jsonDataObject", jsonDataObject.toString());
                } catch (JSONException e) {
                    Log.e("JSONErrorParse", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });
        jsonArrayRequest.setShouldCache(false);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(2),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonArrayRequest);
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

