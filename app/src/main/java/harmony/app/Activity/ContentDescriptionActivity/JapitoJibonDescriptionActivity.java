package harmony.app.Activity.ContentDescriptionActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.looselycoupled.subscriptionintegration.OnSubscriptionListener;
import com.looselycoupled.subscriptionintegration.SubscribeUsingPaymentGateway;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import harmony.app.Activity.HomePage;
import harmony.app.Activity.ImageViewActivity;
import harmony.app.Activity.PlayAudioActivity;
import harmony.app.Activity.ProfileActivity;
import harmony.app.ModelClass.DataBaseData;
import harmony.app.ModelClass.JapitoJibon;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.ModelClass.Porashuna;
import harmony.app.R;
import harmony.app.helper.AppLogger;
import harmony.app.helper.CheckPermission;
import harmony.app.helper.DataHelper;
import harmony.app.helper.DownloadImage;
import harmony.app.helper.DownloadVideo;
import harmony.app.helper.InsertPayment;
import harmony.app.helper.PushDataToSharedPref;
import harmony.app.universalvideoview.UniversalMediaController;
import harmony.app.universalvideoview.UniversalVideoView;

public class JapitoJibonDescriptionActivity extends AppCompatActivity implements DownloadImage.AsyncResponse {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    TextView newPrice, previousPrice;
    DataBaseData dataBaseData;
    DataHelper dataHelper;
    LinearLayout buyOrDownloadLL, bisheshOfferLL;
    Button buyOrDownloadBTN;
    String imageUrl;
    ProgressBar imageProgressBar;
    harmony.app.helper.ProgressDialog progressDialog;
    private android.support.v7.widget.Toolbar toolbar;
    View mBottomLayout;
    View videoLayout;
    MusicVideo musicVideoObj;
    UniversalVideoView videoView;
    UniversalMediaController mediaController;
    String videoTitle;
    private int cachedHeight;
    private boolean isFullscreen;
    private int mSeekPosition;
    RelativeLayout imageLayout;
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    Date currenTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japiti_jibon_description);
        toolbar = findViewById(R.id.toolbarLayoutInnJapitoJibonDescriptionActivity);
        setSupportActionBar(toolbar);
        dataHelper = new DataHelper(JapitoJibonDescriptionActivity.this);
        newsImageView = findViewById(R.id.imgJapitiJibonDescription);
        newsTitle = findViewById(R.id.newsTitleJapitiJibonDescription);
        newsDescription = findViewById(R.id.newsdescriptionJapitiJibonDescription);
        imageLayout = findViewById(R.id.imageLayout);
        newPrice = findViewById(R.id.newPriceTVinJapitoJibonDescription);
        dataHelper = new DataHelper(JapitoJibonDescriptionActivity.this);
        bisheshOfferLL = findViewById(R.id.bisheshOfferLLInJapitoJibonDescription);
        buyOrDownloadLL = findViewById(R.id.buyOrDownloadLLInJapitoJibonDescription);
        buyOrDownloadBTN = findViewById(R.id.buyOrDownloadBTNInJapitoJibonDescription);
        mBottomLayout = findViewById(R.id.bottom_layout);
        videoLayout = findViewById(R.id.video_layout);
        videoView = findViewById(R.id.videoView);
        mediaController = findViewById(R.id.media_controller);
        videoView.setMediaController(mediaController);
        progressDialog = new harmony.app.helper.ProgressDialog(this);
        setDescripTionData();
        setBuyOrDownLoadButtonVisibility();
    }

    private void setBuyOrDownLoadButtonVisibility() {
        musicVideoObj = (MusicVideo) getIntent().getSerializableExtra("Data");
        imageUrl = musicVideoObj.getThumbnailImgUrl();
        String contentTitle = musicVideoObj.getContentTitle();
        String contentCat = musicVideoObj.getContentCat();
        String contentDesc = "";
        String contentType = musicVideoObj.getContentType();
        String thumbNail_image = musicVideoObj.getThumbnailImgUrl();
        int price = musicVideoObj.getContentPrice();
        String priceStatus = "free";
        if (price > 0) {
            newPrice.setText(price + " Tk");
            priceStatus = "paid";
            buyOrDownloadBTN.setVisibility(View.VISIBLE);
        } else {
            newPrice.setText("ফ্রি দেখুন!");
            priceStatus = "free";
        }
        dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNail_image, priceStatus, musicVideoObj.getContentId());
        Boolean check = dataHelper.checkDownLoadedOrNot(musicVideoObj.getContentCat(), musicVideoObj.getContentId());
        Log.d("checkJapitoJibon", check.toString());
        if (dataHelper.checkDownLoadedOrNot(musicVideoObj.getContentCat(), musicVideoObj.getContentId())) {
            Log.d("enter", "japitojibon");
            buyOrDownloadLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (this.isFullscreen) {
            videoView.setFullscreen(false);
        } else {
            super.onBackPressed();
            /*Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
            this.startActivity(myIntent);*/
        }

    }

    public void setDescripTionData() {
        musicVideoObj = (MusicVideo) getIntent().getSerializableExtra("Data");

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", musicVideoObj.getContentId() + "",
                "VIDEO_VISITED", "Category: " + musicVideoObj.getContentCat(), "content");
        if (musicVideoObj.getContentType().equals("video")) {
            videoTitle = musicVideoObj.getContentTitle();
            setVideoAreaSize();
            playVideo();

        } else {
            videoLayout.setVisibility(View.GONE);
            imageLayout.setVisibility(View.VISIBLE);
            newsImageView.setVisibility(View.VISIBLE);
            Glide.with(JapitoJibonDescriptionActivity.this).load(musicVideoObj.getThumbnailImgUrl()).into(newsImageView);
        }
        newsTitle.setText(musicVideoObj.getContentTitle());
        newsDescription.setText(musicVideoObj.getContentDescription());
    }

    public void downloadVideo(View view) {
        /*Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
        pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, JapitoJibonDescriptionActivity.this);
        SharedPreferences preferences = getSharedPreferences("phoneNumber", MODE_PRIVATE);
        purchase.putExtra("userId", preferences.getString("phoneNo", ""));
        startActivityForResult(purchase, 1);*/
        //progressDialog.showProgressDialog();
        if (checkPermissions()) {
            initSubscription(musicVideoObj.getContentPrice());
        } else {
            Toast.makeText(JapitoJibonDescriptionActivity.this, "ডাওনলোড এর পূর্বে ডিভাইস এক্সেস দিয়ে নিন", Toast.LENGTH_SHORT).show();
        }

    }

    private void initSubscription(final int price) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String deviceId = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    SubscribeUsingPaymentGateway obj = new SubscribeUsingPaymentGateway();
                    obj.setData("test", "test123", "1234", (float) price, deviceId, musicVideoObj.getContentTitle(), JapitoJibonDescriptionActivity.this, new OnSubscriptionListener() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String transactionStatus = result.getString("transactionStatus");
                                String paymentID = result.getString("paymentID");
                                String paymentMethod = result.getString("paymentMethod");
                                String referenceCode = result.getString("referenceCode");
                                Long amount = result.getLong("amount");
                                if (transactionStatus.equals("Completed")) {
                                    downloadVideo();
                                    String deviceId = Settings.Secure.getString(getContentResolver(),
                                            Settings.Secure.ANDROID_ID);
                                    InsertPayment.insertPayment(JapitoJibonDescriptionActivity.this, musicVideoObj.getContentId(), amount, paymentID, paymentMethod, referenceCode, deviceId, musicVideoObj.getContentTitle());
                                    Date currenTime;
                                    DateFormat dateFormat;
                                    dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                    currenTime = new Date();
                                    AppLogger.insertLogs(JapitoJibonDescriptionActivity.this, dateFormat.format(currenTime), "N", musicVideoObj.getContentId() + "",
                                            "PAYMENT_DONE", result.getString("paymentMethod"), "content");
                                    Toast.makeText(JapitoJibonDescriptionActivity.this, "আপনার পেমেন্ট সফল হয়েছে, গান ডাউনলোড হচ্ছে", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("tranResult", "transactionResult: " + result);
                        }

                        @Override
                        public void onError(JSONObject result) {
                            AppLogger.insertLogs(JapitoJibonDescriptionActivity.this, dateFormat.format(currenTime), "N", musicVideoObj.getContentId() + "",
                                    "PAYMENT_FAILED", result.toString(), "content");

                            Log.e("tranError", "transactionResult: " + result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void downloadVideo() {
        DownloadVideo downLoadVideo = new DownloadVideo();
        downLoadVideo.downloadVideo(musicVideoObj.getContentUrl(), JapitoJibonDescriptionActivity.this, dataBaseData);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                Log.i("ResultFromLib", returnedResult);
                if (returnedResult.equals("Success")) {
                    //progressDialog.showProgressDialog();
                    if (dataBaseData.getContentType().contains("image")) {
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        String imageURL = preferences.getString("imageUrl", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        DownloadImage downloadImage = new DownloadImage();
                        downloadImage.downloadImage(imageURL, JapitoJibonDescriptionActivity.this, dataBaseData);
                    } else if (dataBaseData.getContentType().contains("video")) {
                        //progressDialog.showProgressDialog();
                        Log.d("videoInImageView", "videoInImageView");
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        Log.i("VideoCOntent", "video");
                        DownloadVideo downLoadVideo = new DownloadVideo();
                        ///downLoadVideo.downloadVideo("http://mp4videos.fusionbd.com/All_Files/320x240_Pixels/Bollywood_Promo_Videos-MP4/O_Saathi-Baaghi_2-Promo_FusionBD.Com.mp4", JapitoJibonDescriptionActivity.this, dataBaseData);
                        ///http://jachaibd.com/files/sample.mp4
                        ///downLoadVideo.downloadVideo("http://jachaibd.com/files/sample.mp4", JapitoJibonDescriptionActivity.this, dataBaseData);
                        downLoadVideo.downloadVideo(musicVideoObj.getContentUrl(), JapitoJibonDescriptionActivity.this, dataBaseData);
                        ///downLoadVideo.downloadVideo(" https://doc-08-18-docs.googleusercontent.com/docs/securesc/ttgc3apsofa38nborrtf61rcpkosuqub/5n6ltam4bar31f4v8in8fodjf639o32i/1526040000000/03058776128903223293/08098124281392257082/1dIUl53KTJOJADmLvlIcL8bJ7Uxr8YD7_?e=download&nonce=ka6t7ersmglss&user=08098124281392257082&hash=m7tvis9qiqvpn9evhp4e9le3rnksjrbu", JapitoJibonDescriptionActivity.this, dataBaseData);
                    }
                }
            }
        }
    }

    @Override
    public void processFinish(String output) {
        //progressDialog.hideProgressDialog();
        Log.d("procesFinishedJpitoJibn", "processFinished");
        if (output.contains("complete")) {
            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            ///Toast.makeText(JapitoJibonDescriptionActivity.this, "ধন্যবাদ কিছুক্ষন পরে মাইআইটেম লিস্ট এ আপনার আইটেমটি দেখতে পারবেন", Toast.LENGTH_SHORT).show();
            myIntent.putExtra("imageUrl", imageUrl);
            myIntent.putExtra("dataBaseData", dataBaseData);
            myIntent.putExtra("cameFromWhichActivity", "JapitoJibonDescriptionActivity");
            this.startActivity(myIntent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPauseoriginal", "onPauseoriginal");
        if (progressDialog.getAlertDialog() != null) progressDialog.hideProgressDialog();
        if (videoView != null && videoView.isPlaying()) {
            mSeekPosition = videoView.getCurrentPosition();
            Log.d("mSeekPosition", "onPause mSeekPosition=" + mSeekPosition);
            videoView.pause();
        }
    }

    private void setVideoAreaSize() {
        videoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = videoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                Log.d("cashedInsetVideoArea", Integer.toString(cachedHeight));
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = videoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                videoLayout.setLayoutParams(videoLayoutParams);
                ///videoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                DataHelper dataHelper = new DataHelper(getApplicationContext());
                if(dataHelper.getDownloadedPath(musicVideoObj.getContentId()).equals("")) {
                    videoView.setVideoPath(musicVideoObj.getContentUrl());
                }else{
                    videoView.setVideoPath(dataHelper.getDownloadedPath(musicVideoObj.getContentId()));
                }
                videoView.setVideoPath(musicVideoObj.getContentUrl());
                videoView.requestFocus();
            }
        });
    }

    public void setFullscreen(Boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.d(TAG, "onSaveInstanceState Position=" + videoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        ///Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }

    public void playVideo() {
        Date currenTime;
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", musicVideoObj.getContentId() + "",
                "VIDEO_PLAYED", musicVideoObj.getContentDescription(), "content");
        if (mSeekPosition > 0) {
            videoView.seekTo(mSeekPosition);
        }
        videoView.start();
        if (videoTitle.length() > 0)
            mediaController.setTitle(videoTitle);
        else
            mediaController.setTitle("");
        videoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            public boolean isFullscreen2 = isFullscreen;

            @Override
            public void onScaleChange(boolean isFullscreen) {
                this.isFullscreen2 = isFullscreen;
                setFullscreen(isFullscreen);
                if (isFullscreen) {
                    Log.d("LandScape", "LandScape");
                    toolbar.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    videoLayout.setLayoutParams(layoutParams);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    int width = displayMetrics.widthPixels;
                    Log.d("height&width", Integer.toString(height) + " : " + Integer.toString(width));
                    videoView.getLayoutParams().height = height;

                    //GONE the unconcerned views to leave room for video and controller
                    mBottomLayout.setVisibility(View.GONE);
                } else {
                    Log.d("Potrait", "Potrait");
                    ///Log.d("cachedHeight2",Integer.toString(cachedHeight2));
                    videoView.getLayoutParams().height = 300;
                    toolbar.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = cachedHeight;
                    videoLayout.setLayoutParams(layoutParams);
                    mBottomLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d("onPause", "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d("onStart", "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d("onBufferingStart", "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d("onBufferingEnd", "onBufferingEnd UniversalVideoView callback");
            }

        });
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
