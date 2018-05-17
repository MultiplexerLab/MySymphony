package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.Activity.ProfileActivity;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.JapitoJibon;
import lct.mysymphony.R;
import lct.mysymphony.helper.DataHelper;
import lct.mysymphony.helper.DownloadImage;
import lct.mysymphony.helper.DownloadVideo;
import lct.mysymphony.helper.FullScreenMediaController;
import lct.mysymphony.helper.PushDataToSharedPref;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class JapitoJibonDescriptionActivity extends AppCompatActivity implements DownloadImage.AsyncResponse {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    VideoView videoView;
    TextView newPrice, previousPrice;
    DataBaseData dataBaseData;
    DataHelper dataHelper;
    LinearLayout buyOrDownloadLL, bisheshOfferLL;
    Button buyOrDownloadBTN;
    String imageUrl;
    ProgressBar imageProgressBar, videoProgressBar;
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japiti_jibon_description);

        dataHelper = new DataHelper(JapitoJibonDescriptionActivity.this);
        newsImageView = findViewById(R.id.imgJapitiJibonDescription);
        newsTitle = findViewById(R.id.newsTitleJapitiJibonDescription);
        newsDescription = findViewById(R.id.newsdescriptionJapitiJibonDescription);
        videoView = findViewById(R.id.videoViewJapitojibon);
        newPrice = findViewById(R.id.newPriceTVinJapitoJibonDescription);
        dataHelper = new DataHelper(JapitoJibonDescriptionActivity.this);
        bisheshOfferLL = findViewById(R.id.bisheshOfferLLInJapitoJibonDescription);
        buyOrDownloadLL = findViewById(R.id.buyOrDownloadLLInJapitoJibonDescription);
        buyOrDownloadBTN = findViewById(R.id.buyOrDownloadBTNInJapitoJibonDescription);
        videoProgressBar = findViewById(R.id.progressBarInVideoViewInJapitiJibonDescription);
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
        Log.d("thumbNail_image",object.getThumbNail_image());
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
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
    }

    public void setDescripTionData() {
        JapitoJibon object = (JapitoJibon) getIntent().getSerializableExtra("Data");
        videoProgressBar.setVisibility(View.VISIBLE);
        if (object.getContentType().equals("video")) {
            newsImageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            MediaController mediaController;

            if(isLandScape()){
                mediaController = new FullScreenMediaController(this);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4"));
                videoView.start();
            }else {
                mediaController = new FullScreenMediaController(this);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4"));
                videoView.start();
            }

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int what, int arg2) {
                            videoProgressBar.setVisibility(View.GONE);
                            mp.start();

                            if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                                videoProgressBar.setVisibility(View.GONE);
                            }
                            if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                                videoProgressBar.setVisibility(View.VISIBLE);
                            }
                            if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                                videoProgressBar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });

            /*if(isLandScape()){
                DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
                android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
                params.width =  metrics.widthPixels;
                params.height = metrics.heightPixels;
                params.leftMargin = 0;
                videoView.setLayoutParams(params);
                videoView.start();
            }else{
                DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
                android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
                params.width =  (int) (300*metrics.density);
                params.height = (int) (250*metrics.density);
                params.leftMargin = 30;
                videoView.setLayoutParams(params);
                videoView.start();
            }*/

        } else {
            newsImageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(JapitoJibonDescriptionActivity.this).load(object.getImageUrl()).into(newsImageView);
            videoProgressBar.setVisibility(View.GONE);
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

    private boolean isLandScape(){
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int rotation = display.getRotation();

        if (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) {
            return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                Log.i("ResultFromLib", returnedResult);
                if (returnedResult.equals("Success")) {
                    progressDialog.showProgressDialog();
                    if (dataBaseData.getContentType().contains("image"))
                    {
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        String imageURL = preferences.getString("imageUrl", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        DownloadImage downloadImage = new DownloadImage();
                        downloadImage.downloadImage(imageURL, JapitoJibonDescriptionActivity.this, dataBaseData);
                    }
                    else if (dataBaseData.getContentType().contains("video")) {
                        progressDialog.showProgressDialog();
                        Log.d("videoInImageView","videoInImageView");
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        Log.i("VideoCOntent", "video");
                        DownloadVideo downLoadVideo = new DownloadVideo();
                        ///downLoadVideo.downloadVideo("http://mp4videos.fusionbd.com/All_Files/320x240_Pixels/Bollywood_Promo_Videos-MP4/O_Saathi-Baaghi_2-Promo_FusionBD.Com.mp4", JapitoJibonDescriptionActivity.this, dataBaseData);
                        ///http://jachaibd.com/files/sample.mp4
                       ///downLoadVideo.downloadVideo("http://jachaibd.com/files/sample.mp4", JapitoJibonDescriptionActivity.this, dataBaseData);
                        downLoadVideo.downloadVideo("https://fsa.zobj.net/download/bztTqNHqgr0dug1iOwSoDq7Pp6Czdcalekon2tBpAJFeTMZa2WVQka2Dm18rAvddZw9JmlX3IQladYbM4PYgBASpPB-yBUIUBbm91yAK0QvANRE2dB8ZzCy-hFRY/?a=web&c=72&f=let_me_love_you.mp3&special=1525951411-ky2356gba1OZDBXCdm4ekc2OWgrp%2FZ1brCOPlJo1Aro%3D", JapitoJibonDescriptionActivity.this, dataBaseData);
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
        if (progressDialog.getAlertDialog()!=null)
            progressDialog.hideProgressDialog();
    }
}
