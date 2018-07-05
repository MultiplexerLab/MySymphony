package harmony.app.Activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import harmony.app.ModelClass.DataBaseData;
import harmony.app.ModelClass.GamesZone;
import harmony.app.ModelClass.JapitoJibon;
import harmony.app.ModelClass.MulloChar;
import harmony.app.ModelClass.SeraChobi;
import harmony.app.ModelClass.ShocolChobi;
import harmony.app.R;
import harmony.app.helper.CheckPermission;
import harmony.app.helper.DataHelper;
import harmony.app.helper.DownloadApk;
import harmony.app.helper.DownloadAudio;
import harmony.app.helper.DownloadImage;
import harmony.app.helper.DownloadVideo;
import harmony.app.helper.PlayAudioInBackgroundService;
import harmony.app.helper.ProgressDialog;
import harmony.app.helper.PushDataToSharedPref;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class ImageViewActivity extends AppCompatActivity implements DownloadImage.AsyncResponse {

    String imageUrl, audioUrl;
    LinearLayout bisheshOfferLinearLayout;
    String contentTitle, contentCat, contentDesc, contentType, thumbNailImgUrl;
    TextView previousPrice, newPrice;
    String cameFromWhichActivity;
    DataBaseData dataBaseData;
    Button buyOrDownloadBTN;
    Button playAudioBTN;
    boolean isDownloaded = false;
    boolean isItFree = false;
    DataHelper dataHelper;
    LinearLayout buyOrDownloadLinearLayout;
    ProgressBar progressBar;
    Context context;
    harmony.app.helper.ProgressDialog progressDialog;
    SharedPreferences preferences;
    private String audioTitle;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        bisheshOfferLinearLayout = findViewById(R.id.bisheshOfferLL);
        buyOrDownloadBTN = findViewById(R.id.buyOrDownloadBTN);
        dataHelper = new DataHelper(ImageViewActivity.this);
        bisheshOfferLinearLayout = findViewById(R.id.bisheshOfferLL);
        buyOrDownloadLinearLayout = findViewById(R.id.buyOrDownloadLL);
        previousPrice = findViewById(R.id.previousPriceTVinImageViewActivity);
        playAudioBTN = findViewById(R.id.playAudioBTN);
        newPrice = findViewById(R.id.newPriceTVinImageViewActivity);
        progressDialog = new harmony.app.helper.ProgressDialog(this);
        progressBar = findViewById(R.id.progressBarInImageView);
        preferences = getSharedPreferences("tempData", MODE_PRIVATE);
        checkPermissions();
        context = ImageViewActivity.this;
        cameFromWhichActivity = getIntent().getStringExtra("cameFromWhichActivity");
        /*LocalBroadcastManager.getInstance(ImageViewActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("intentKey"));*/

        if (cameFromWhichActivity != null) {
            if (cameFromWhichActivity.contains("MulloChar")) {
                MulloChar Data = (MulloChar) getIntent().getSerializableExtra("wallpaper");
                imageUrl = Data.getImageUrl();
                contentTitle = Data.getContentTile();
                contentCat = Data.getContentCat();
                contentDesc = "";
                contentType = Data.getContentType();
                if (contentType.contains("audio")) {
                    audioUrl = Data.getContentUrl();
                    audioTitle = Data.getContentTile();
                    playAudioBTN.setVisibility(View.VISIBLE);
                } else {
                    playAudioBTN.setVisibility(View.GONE);
                }
                thumbNailImgUrl = Data.getThumbNailImgUrl();

                newPrice.setText(Integer.toString(Data.getNewPrice()));
                previousPrice.setText(Integer.toString(Data.getPreviousPrice()));
                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, "free", Data.getContentId());
                setImage(Data.getImageUrl());
                PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
                pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, ImageViewActivity.this);

                if (dataHelper.checkDownLoadedOrNot(Data.getContentCat(), Data.getContentId())) {
                    buyOrDownloadLinearLayout.setVisibility(View.GONE);
                    Log.d("enter", "MulloChar");
                    isDownloaded = true;
                }

            } else if (cameFromWhichActivity.contains("music_video")) {
                JapitoJibon Data = (JapitoJibon) getIntent().getSerializableExtra("data");
                imageUrl = Data.getImageUrl();
                contentTitle = Data.getContentTitle();
                contentCat = Data.getContentCat();
                contentDesc = "";
                contentType = Data.getContentType();
                audioUrl = Data.getContentUrl();
                audioTitle = Data.getContentTitle();
                playAudioBTN.setVisibility(View.VISIBLE);
                thumbNailImgUrl = Data.getThumbNail_image();

                //newPrice.setText(Integer.toString(Data.getNewPrice()));
                //previousPrice.setText(Integer.toString(Data.getPreviousPrice()));
                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, "paid", Data.getContentId());
                setImage(Data.getImageUrl());
                PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
                pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, ImageViewActivity.this);

                if (dataHelper.checkDownLoadedOrNot(Data.getContentCat(), Data.getContentId())) {
                    buyOrDownloadLinearLayout.setVisibility(View.GONE);
                    Log.d("enter", "MulloChar");
                    isDownloaded = true;
                }

            } else if (cameFromWhichActivity.contains("GameZone")) {
                GamesZone Data = (GamesZone) getIntent().getSerializableExtra("wallpaper");
                imageUrl = Data.getContentUrl();
                contentTitle = Data.getContentTile();
                contentCat = Data.getContentCat();
                contentDesc = "";
                contentType = Data.getContentType();
                thumbNailImgUrl = Data.getThumbNailImgUrl();
                Log.d("thumbNailImgUrl", thumbNailImgUrl);
                if (contentType.contains("audio")) {
                    audioUrl = Data.getContentUrl();
                    playAudioBTN.setVisibility(View.VISIBLE);
                } else {
                    playAudioBTN.setVisibility(View.GONE);
                }
                Log.d("newPrice", Integer.toString(Data.getNewPrice()));
                Log.d("previousPrice", Integer.toString(Data.getPreviousPrice()));
                previousPrice.setVisibility(View.GONE);
                newPrice.setText(Integer.toString(Data.getNewPrice()));

                String priceStatus;
                if (Data.getNewPrice() == 0 && Data.getPreviousPrice() == 0) {
                    isItFree = true;
                    priceStatus = "free";
                } else {
                    isItFree = false;
                    priceStatus = "paid";
                }

                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, priceStatus, Data.getContentId());
                setImage(Data.getContentUrl());
                PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
                pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, ImageViewActivity.this);

                if (dataHelper.checkDownLoadedOrNot(Data.getContentCat(), Data.getContentId())) {
                    buyOrDownloadLinearLayout.setVisibility(View.GONE);
                    isDownloaded = true;
                    Log.d("enter", "GameZone");
                }
            } else if (cameFromWhichActivity.contains("ShocolChobi")) {
                ShocolChobi Data = (ShocolChobi) getIntent().getSerializableExtra("wallpaper");
                imageUrl = Data.getContentUrl();
                contentTitle = Data.getContentTile();
                contentCat = Data.getContentCat();
                contentDesc = "";
                contentType = Data.getContentType();
                thumbNailImgUrl = Data.getThumbNailImgUrl();
                if (contentType.contains("audio")) {
                    audioUrl = Data.getContentUrl();
                    playAudioBTN.setVisibility(View.VISIBLE);
                } else {
                    playAudioBTN.setVisibility(View.GONE);
                }

                String priceStatus;
                if (Data.getContentPrice() == 0) {
                    priceStatus = "free";
                    isItFree = true;
                } else {
                    isItFree = false;
                    priceStatus = "paid";
                }

                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, priceStatus, Data.getContentId());
                setImage(Data.getContentUrl());
                PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
                pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, ImageViewActivity.this);

                if (dataHelper.checkDownLoadedOrNot(Data.getContentCat(), Data.getContentId())) {
                    isDownloaded = true;
                    Log.d("enter", "SeraChobi");
                    buyOrDownloadLinearLayout.setVisibility(View.GONE);
                } else if (Data.getContentPrice() == 0) {
                    Log.d("enter", "not");
                    bisheshOfferLinearLayout.setVisibility(View.GONE);
                    buyOrDownloadBTN.setText("ডাউনলোড করুন");
                } else if (Data.getContentPrice() > 0) {
                    Log.d("enter", "not");
                    buyOrDownloadLinearLayout.setVisibility(View.VISIBLE);
                    previousPrice.setVisibility(View.GONE);
                }

            } else if (cameFromWhichActivity.contains("SeraChobi")) {
                SeraChobi Data = (SeraChobi) getIntent().getSerializableExtra("wallpaper");
                imageUrl = Data.getImage_url();
                contentTitle = "";
                contentCat = Data.getContentCat();
                isDownloaded = true;
                contentDesc = "";
                contentType = Data.getContentType();
                thumbNailImgUrl = Data.getThumbNailImgUrl();
                String priceStatus;
                if (Data.getContentPrice() == 0) {
                    isItFree = true;
                    priceStatus = "free";
                } else {
                    isItFree = false;
                    priceStatus = "paid";
                }

                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, priceStatus, Data.getContentId());
                setImage(Data.getImage_url());
                PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
                pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, ImageViewActivity.this);
                if (dataHelper.checkDownLoadedOrNot(Data.getContentCat(), Data.getContentId())) {
                    isDownloaded = true;
                    Log.d("enter", "SeraChobi");
                    buyOrDownloadLinearLayout.setVisibility(View.GONE);
                } else if (Data.getContentPrice() == 0) {
                    Log.d("enter", "SeraChobiFree");
                    bisheshOfferLinearLayout.setVisibility(View.GONE);
                    buyOrDownloadBTN.setText("ডাউনলোড করুন");
                } else if (Data.getContentPrice() > 0) {
                    Log.d("enter", "SeraChobiPaid");
                    buyOrDownloadLinearLayout.setVisibility(View.VISIBLE);
                    previousPrice.setVisibility(View.GONE);
                }
                Log.d("isItFrreSeraChobi", String.valueOf(isItFree));
            }

        }
        Log.d("isDownloaded", String.valueOf(isDownloaded));

        previousPrice = findViewById(R.id.previousPriceTVinImageViewActivity);
        previousPrice.setPaintFlags(previousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void purChase(View view) {
        if (dataBaseData == null) Log.d("databaseDataImageView", "null");
        Log.d("isItFreeInPurchase", String.valueOf(isItFree));
        Intent myIntent;
        if (isItFree == false) {
            SharedPreferences preferences = getSharedPreferences("phoneNumber", MODE_PRIVATE);
            myIntent = new Intent(getApplicationContext(), PaymentMethod.class);
            myIntent.putExtra("userId", preferences.getString("phoneNo", ""));
            this.startActivityForResult(myIntent, 1);
        } else {
            progressDialog.showProgressDialog();
            DownloadImage downloadImage = new DownloadImage();
            downloadImage.downloadImage(imageUrl, ImageViewActivity.this, dataBaseData);
        }
    }

/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
        finish();
    }*/

    @Override
    public void processFinish(String output) {
        progressDialog.hideProgressDialog();
        Log.d("processFinished", "processFinished");
        Log.d("outputAudio", output);
        if (output.contains("complete")) {
            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            ///Toast.makeText(ImageViewActivity.this, "ধন্যবাদ কিছুক্ষন পরে মাইআইটেম লিস্ট এ আপনার আইটেমটি দেখতে পারবেন", Toast.LENGTH_SHORT).show();
            myIntent.putExtra("imageUrl", imageUrl);
            myIntent.putExtra("dataBaseData", dataBaseData);
            myIntent.putExtra("cameFromWhichActivity", "ImageViewActivity");
            this.startActivity(myIntent);
            Log.d("onProcessFinishedComplt", "onProcessFinishedComplt");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                Log.i("ResultFromLib", returnedResult);
                if (returnedResult.equals("Success")) {

                    Log.i("TagContent", dataBaseData.getContentType());

                    if (dataBaseData.getContentType().contains("image")) {
                        progressDialog.showProgressDialog();
                        Log.i("ImageContent", "image");
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        String imageURL = preferences.getString("imageUrl", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        DownloadImage downloadImage = new DownloadImage();
                        downloadImage.downloadImage(imageURL, ImageViewActivity.this, dataBaseData);
                    } else if (dataBaseData.getContentType().contains("video")) {
                        progressDialog.showProgressDialog("ভিডিও ডাউনলোড হচ্ছে");
                        Log.d("videoInImageView", "videoInImageView");
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        Log.i("VideoCOntent", "video");
                        DownloadVideo downLoadVideo = new DownloadVideo();
                        downLoadVideo.downloadVideo("http://jachaibd.com/files/sample.mp4", ImageViewActivity.this, dataBaseData);
                    } else if (dataBaseData.getContentType().contains("audio")) {
                        Log.d("audioUrl", audioUrl);
                        if (audioUrl.length() > 0) {
                            progressDialog.showProgressDialog("গান ডাউনলোড হচ্ছে");
                            Gson gson = new Gson();
                            SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                            String json = preferences.getString("databaseData", "");
                            DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                            DownloadAudio downloadAudio = new DownloadAudio();
                            //downloadAudio.downloadAudio("http://jachaibd.com/files/eminem.mp3", ImageViewActivity.this, dataBaseData);
                            ///downloadAudio.downloadAudio("https://fsa.zobj.net/download/bztTqNHqgr0dug1iOwSoDq7Pp6Czdcalekon2tBpAJFeTMZa2WVQka2Dm18rAvddZw9JmlX3IQladYbM4PYgBASpPB-yBUIUBbm91yAK0QvANRE2dB8ZzCy-hFRY/?a=web&c=72&f=let_me_love_you.mp3&special=1525951411-ky2356gba1OZDBXCdm4ekc2OWgrp%2FZ1brCOPlJo1Aro%3D", ImageViewActivity.this, dataBaseData);
                        } else Log.d("audioUrlNotFound", "audioUrlNotFound");

                    } else if (dataBaseData.getContentType().contains("apk")) {
                        //progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        String imageURL = preferences.getString("imageUrl", "");

                        /*Process p = null;
                        try {
                            p = Runtime.getRuntime().exec("su");
                            DataOutputStream os = new DataOutputStream(p.getOutputStream());
                            os.writeBytes("settings put global install_non_market_apps 1");
                            os.writeBytes("exit\n");
                            os.flush();
                        } catch (IOException e) {
                            Log.i("Errr",e.toString());
                        }*/

                        /*boolean isNonPlayAppAllowed = false;
                        try {
                            isNonPlayAppAllowed = Settings.Secure.getInt(getContentResolver(), Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES) == 1;
                        } catch (Settings.SettingNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(isNonPlayAppAllowed==false){
                            progressDialog.showProgressDialogAPK();
                            DownloadApk downloadApk = new DownloadApk();
                            downloadApk.downLoadAPK("http://jachaibd.com/files/royalty.apk", ImageViewActivity.this, dataBaseData);
                        }else{
                            progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
                            DownloadApk downloadApk = new DownloadApk();
                            downloadApk.downLoadAPK("http://jachaibd.com/files/royalty.apk", ImageViewActivity.this, dataBaseData);
                        }*/

                        SharedPreferences preferences1 = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
                        int flag = preferences1.getInt("unknownSource", 0);
                        if (flag == 0) {
                            progressDialog.showProgressDialogAPK();
                            DownloadApk downloadApk = new DownloadApk();
                            downloadApk.downLoadAPK("http://jachaibd.com/files/royalty.apk", ImageViewActivity.this, dataBaseData);
                        } else {
                            progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
                            DownloadApk downloadApk = new DownloadApk();
                            downloadApk.downLoadAPK("http://jachaibd.com/files/royalty.apk", ImageViewActivity.this, dataBaseData);
                        }


                      /*  DownloadApk downloadApk = new DownloadApk();
                        downloadApk.downLoadAPK("http://jachaibd.com/files/royalty.apk", ImageViewActivity.this,dataBaseData);
                    */
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("progressDialog", progressDialog.toString());
        if (progressDialog.getAlertDialog() != null) progressDialog.hideProgressDialog();
    }

    public void setImage(String url) {
        Glide.with(ImageViewActivity.this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into((ImageView) findViewById(R.id.imageViewWallpaper));
    }

    public void startPlayAudioActivity(View view) {
        Log.d("audioUrlstartPlayAudio", audioUrl);
        showNotification();
    }

    public void showNotification() {
        final String channel = "Music";
        Intent stopButton = getServiceIntent("stop");
        final PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(ImageViewActivity.this, 0, stopButton, PendingIntent.FLAG_CANCEL_CURRENT);
        final RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.audio_controller_notification_layout);
        notificationView.setTextViewText(R.id.notif_title, audioTitle);
        try {
            progressDialog.showProgressDialog();
            Glide.with(this).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    notificationView.setImageViewBitmap(R.id.audioImage, resource);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setupChannels(notificationManager);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ImageViewActivity.this, channel).setSmallIcon(R.drawable.icon).setTicker("Ticker Text").setContent(notificationView);
                    notificationView.setOnClickPendingIntent(R.id.stopButton, pendingSwitchIntent);
                    builder.setContentIntent(pendingSwitchIntent);
                    notificationManager.notify(1, builder.build());
                    startAudioBackgroundService();
                    progressDialog.hideProgressDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.hideProgressDialog();
            Log.d("glideException", e.toString());
            notificationView.setImageViewUri(R.id.audioImage, Uri.parse(imageUrl));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ImageViewActivity.this, channel).setSmallIcon(R.drawable.icon).setTicker("Ticker Text").setContent(notificationView);
            notificationView.setOnClickPendingIntent(R.id.stopButton, pendingSwitchIntent);
            builder.setContentIntent(pendingSwitchIntent);
            notificationManager.notify(1, builder.build());
            startAudioBackgroundService();
        }
    }

    private Intent getServiceIntent(String tag) {
        Intent intent = new Intent(ImageViewActivity.this, PlayAudioInBackgroundService.class);
        intent.putExtra("message", tag);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        try {
            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel("Music", "Music is playing", NotificationManager.IMPORTANCE_HIGH);
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

    public void startAudioBackgroundService() {
        Intent intent = new Intent(ImageViewActivity.this, PlayAudioActivity.class);
        intent.putExtra("audioUrl", audioUrl);
        startService(intent);
    }


    public boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
}

