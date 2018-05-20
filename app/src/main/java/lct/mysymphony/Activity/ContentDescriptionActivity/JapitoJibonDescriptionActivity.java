package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.Activity.ProfileActivity;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.JapitoJibon;
import lct.mysymphony.R;
import lct.mysymphony.helper.CheckPermission;
import lct.mysymphony.helper.DataHelper;
import lct.mysymphony.helper.DownloadImage;
import lct.mysymphony.helper.DownloadVideo;
import lct.mysymphony.helper.PushDataToSharedPref;
import lct.mysymphony.universalvideoview.UniversalMediaController;
import lct.mysymphony.universalvideoview.UniversalVideoView;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class JapitoJibonDescriptionActivity extends AppCompatActivity implements DownloadImage.AsyncResponse {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    TextView newPrice, previousPrice;
    DataBaseData dataBaseData;
    DataHelper dataHelper;
    LinearLayout buyOrDownloadLL, bisheshOfferLL;
    Button buyOrDownloadBTN;
    String imageUrl;
    JapitoJibon object;
    ProgressBar imageProgressBar;
    lct.mysymphony.helper.ProgressDialog progressDialog;
    private android.support.v7.widget.Toolbar toolbar;
    View mBottomLayout;
    View videoLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japiti_jibon_description);
        toolbar = findViewById(R.id.toolbarLayoutInnJapitoJibonDescriptionActivity);
        setSupportActionBar(toolbar);
        checkPermissions();
        dataHelper = new DataHelper(JapitoJibonDescriptionActivity.this);
        newsImageView = findViewById(R.id.imgJapitiJibonDescription);
        newsTitle = findViewById(R.id.newsTitleJapitiJibonDescription);
        newsDescription = findViewById(R.id.newsdescriptionJapitiJibonDescription);
        imageLayout=findViewById(R.id.imageLayout);
        ///videoView = findViewById(R.id.videoViewJapitojibon);
        newPrice = findViewById(R.id.newPriceTVinJapitoJibonDescription);
        dataHelper = new DataHelper(JapitoJibonDescriptionActivity.this);
        bisheshOfferLL = findViewById(R.id.bisheshOfferLLInJapitoJibonDescription);
        buyOrDownloadLL = findViewById(R.id.buyOrDownloadLLInJapitoJibonDescription);
        buyOrDownloadBTN = findViewById(R.id.buyOrDownloadBTNInJapitoJibonDescription);
        mBottomLayout = findViewById(R.id.bottom_layout);
        ///videoProgressBar = findViewById(R.id.progressBarInVideoViewInJapitiJibonDescription);
        videoLayout = findViewById(R.id.video_layout);
        videoView = findViewById(R.id.videoView);
        mediaController =findViewById(R.id.media_controller);
        videoView.setMediaController(mediaController);
        progressDialog = new lct.mysymphony.helper.ProgressDialog(this);
        setDescripTionData();
        setBuyOrDownLoadButtonVisibility();
    }

    private void setBuyOrDownLoadButtonVisibility() {
        JapitoJibon object = (JapitoJibon) getIntent().getSerializableExtra("Data");
        imageUrl = object.getImageUrl();
        String contentTitle = object.getContentTitle();
        String contentCat = object.getContentCat();
        String contentDesc = "";
        String contentType = object.getContentType();
        String thumbNail_image = object.getThumbNail_image();
        Log.d("newPrice", Integer.toString(object.getContentPrice()));
        Log.d("japitojibonid", Integer.toString(object.getContentId()));
        Log.d("thumbNail_image", object.getThumbNail_image());
        newPrice.setText(Integer.toString(object.getContentPrice()));
        Log.d("japitojibonPrice", Integer.toString(object.getContentPrice()));
        String priceStatus;
        if (object.getContentPrice() == 0) {
            priceStatus = "free";
        } else priceStatus = "paid";
        dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNail_image, priceStatus, object.getContentId());
        Boolean check = dataHelper.checkDownLoadedOrNot(object.getContentCat(), object.getContentId());
        Log.d("checkJapitoJibon", check.toString());
        if (dataHelper.checkDownLoadedOrNot(object.getContentCat(), object.getContentId())) {
            Log.d("enter", "japitojibon");
            buyOrDownloadLL.setVisibility(View.GONE);
        } else if (object.getContentPrice() == 0) {
            Log.d("enter", "notjapitojibon");
            bisheshOfferLL.setVisibility(View.GONE);
            buyOrDownloadBTN.setText("ডাউনলোড করুন");
        } else if (object.getContentPrice() > 0) {
            Log.d("enter", "not");
            buyOrDownloadLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

        if (this.isFullscreen) {
            videoView.setFullscreen(false);
        } else {
            super.onBackPressed();
            Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
            this.startActivity(myIntent);
        }

    }

    public void setDescripTionData() {
        object = (JapitoJibon) getIntent().getSerializableExtra("Data");
        if (object.getContentType().equals("video")) {
            videoTitle=object.getContentTitle();
            setVideoAreaSize();
            playVideo();

        } else {
            videoLayout.setVisibility(View.GONE);
            imageLayout.setVisibility(View.VISIBLE);
            newsImageView.setVisibility(View.VISIBLE);
            Glide.with(JapitoJibonDescriptionActivity.this).load(object.getImageUrl()).into(newsImageView);
        }
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
    }

    public void mullochar(View view) {
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
        pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, JapitoJibonDescriptionActivity.this);
        SharedPreferences preferences = getSharedPreferences("phoneNumber", MODE_PRIVATE);
        purchase.putExtra("userId", preferences.getString("phoneNo", ""));
        startActivityForResult(purchase, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                Log.i("ResultFromLib", returnedResult);
                if (returnedResult.equals("Success")) {
                    progressDialog.showProgressDialog();
                    if (dataBaseData.getContentType().contains("image")) {
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        String imageURL = preferences.getString("imageUrl", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        DownloadImage downloadImage = new DownloadImage();
                        downloadImage.downloadImage(imageURL, JapitoJibonDescriptionActivity.this, dataBaseData);
                    } else if (dataBaseData.getContentType().contains("video")) {
                        progressDialog.showProgressDialog();
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
                        downLoadVideo.downloadVideo("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4", JapitoJibonDescriptionActivity.this, dataBaseData);
                        ///downLoadVideo.downloadVideo(" https://doc-08-18-docs.googleusercontent.com/docs/securesc/ttgc3apsofa38nborrtf61rcpkosuqub/5n6ltam4bar31f4v8in8fodjf639o32i/1526040000000/03058776128903223293/08098124281392257082/1dIUl53KTJOJADmLvlIcL8bJ7Uxr8YD7_?e=download&nonce=ka6t7ersmglss&user=08098124281392257082&hash=m7tvis9qiqvpn9evhp4e9le3rnksjrbu", JapitoJibonDescriptionActivity.this, dataBaseData);
                    }
                }
            }
        }
    }

    @Override
    public void processFinish(String output) {
        progressDialog.hideProgressDialog();
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
                Log.d("cashedInsetVideoArea",Integer.toString(cachedHeight));
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = videoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                videoLayout.setLayoutParams(videoLayoutParams);
                ///videoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                videoView.setVideoPath("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4");
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
     if (mSeekPosition > 0) {
            videoView.seekTo(mSeekPosition);
        }
        videoView.start();
     if (videoTitle.length()>0)
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
                    Log.d("LandScape","LandScape");
                    toolbar.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    videoLayout.setLayoutParams(layoutParams);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    int width = displayMetrics.widthPixels;
                    Log.d("height&width", Integer.toString(height)+" : "+Integer.toString(width));
                    videoView.getLayoutParams().height= height;

                    //GONE the unconcerned views to leave room for video and controller
                    mBottomLayout.setVisibility(View.GONE);
                } else {
                    Log.d("Potrait","Potrait");
                    ///Log.d("cachedHeight2",Integer.toString(cachedHeight2));
                    videoView.getLayoutParams().height=300;
                   toolbar.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height =cachedHeight;
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
