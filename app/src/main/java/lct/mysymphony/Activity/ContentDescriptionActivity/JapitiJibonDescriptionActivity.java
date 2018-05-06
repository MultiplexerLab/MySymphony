package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.Activity.ProfileActivity;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.JapitoJibonMC;
import lct.mysymphony.R;
import lct.mysymphony.helper.DataHelper;
import lct.mysymphony.helper.DownloadImage;
import lct.mysymphony.helper.PushDataToSharedPref;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class JapitiJibonDescriptionActivity extends AppCompatActivity implements DownloadImage.AsyncResponse{

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    VideoView videoView;
    TextView newPrice,previousPrice;
    DataBaseData dataBaseData;
    DataHelper dataHelper;
    LinearLayout buyOrDownloadLL,bisheshOfferLL;
    Button buyOrDownloadBTN;
    String imageUrl;
    ProgressBar imageProgressBar,videoProgressBar;
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japiti_jibon_description);

        dataHelper=new DataHelper(JapitiJibonDescriptionActivity.this);

        newsImageView = findViewById(R.id.imgJapitiJibonDescription);
        newsTitle = findViewById(R.id.newsTitleJapitiJibonDescription);
        newsDescription = findViewById(R.id.newsdescriptionJapitiJibonDescription);
        videoView=findViewById(R.id.videoViewJapitojibon);
        newPrice=findViewById(R.id.newPriceTVinJapitoJibonDescription);
        dataHelper=new DataHelper(JapitiJibonDescriptionActivity.this);
        bisheshOfferLL=findViewById(R.id.bisheshOfferLLInJapitoJibonDescription);
        buyOrDownloadLL=findViewById(R.id.buyOrDownloadLLInJapitoJibonDescription);
        buyOrDownloadBTN=findViewById(R.id.buyOrDownloadBTNInJapitoJibonDescription);
        videoProgressBar=findViewById(R.id.progressBarInVideoViewInJapitiJibonDescription);

        progressDialog = new lct.mysymphony.helper.ProgressDialog(this);

//        newPrice=findViewById(R.id.newPriceTVinJapitoJibonDescription);
//        previousPrice=findViewById(R.id.previousPriceTVinJapitoJibonDescription);
//        previousPrice.setPaintFlags(previousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        setDescripTionData();
        setBuyOrDownLoadButtonVisibility();

    }

    private void setBuyOrDownLoadButtonVisibility() {

        JapitoJibonMC object = (JapitoJibonMC) getIntent().getSerializableExtra("Data");
        imageUrl = object.getImageUrl();
        String contentTitle = object.getContentTitle();
        String contentCat = object.getContentCat();
        String contentDesc = "";
        String contentType = object.getContentType();
        Log.d("newPrice",Integer.toString(object.getContentPrice()));
        Log.d("japitojibonid",Integer.toString(object.getContentId()));

        ///previousPrice.setVisibility(View.GONE);
        newPrice.setText(Integer.toString(object.getContentPrice()));
        Log.d("japitojibonPrice",Integer.toString(object.getContentPrice()));

        String priceStatus;
        if (object.getContentPrice()==0)
        {
            priceStatus="free";
        }
        else
            priceStatus="paid";

        dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, priceStatus, object.getContentId());

        Boolean check=dataHelper.checkDownLoadedOrNot(object.getContentCat(), object.getContentId());
        Log.d("checkJapitoJibon",check.toString());
        if (dataHelper.checkDownLoadedOrNot(object.getContentCat(), object.getContentId())) {

            Log.d("enter","japitojibon");
            buyOrDownloadLL.setVisibility(View.GONE);
        } else if (object.getContentPrice()==0){
            Log.d("enter","notjapitojibon");
            bisheshOfferLL.setVisibility(View.GONE);
            buyOrDownloadBTN.setText("ডাউনলোড করুন");
        }
        else if (object.getContentPrice()>0){
            Log.d("enter","not");
            buyOrDownloadLL.setVisibility(View.VISIBLE);
//            previousPrice.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    public void setDescripTionData() {
        JapitoJibonMC object = (JapitoJibonMC) getIntent().getSerializableExtra("Data");

        if (object.getContentType().equals("video"))
        {
            newsImageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse("http://jachaibd.com/files/sample.mp4"));
            videoView.start();

            videoProgressBar.setVisibility(View.VISIBLE);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.start();
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int what,
                                                       int arg2) {
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

            Log.i("japitojibon","japitojibon");
        }
        else
        {
            newsImageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(JapitiJibonDescriptionActivity.this)
                    .load(object.getImageUrl())
                    .into(newsImageView);
            videoProgressBar.setVisibility(View.GONE);
        }
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
    }

//    public void purChase(View view) {
//        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
//        startActivity(purchase);
//        overridePendingTransition(R.anim.left_in, R.anim.left_out);
//    }

    public void mullochar(View view) {
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
        pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, JapitiJibonDescriptionActivity.this);
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
                    Gson gson = new Gson();
                    SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                    String json = preferences.getString("databaseData", "");
                    String imageURL = preferences.getString("imageUrl", "");
                    DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                    DownloadImage downloadImage = new DownloadImage();
                    downloadImage.downloadImage(imageURL, JapitiJibonDescriptionActivity.this, dataBaseData);
                }
            }
        }
    }

    @Override
    public void processFinish(String output) {
        progressDialog.hideProgressDialog();
        Log.d("processFinished", "processFinished");
        if (output.contains("complete")) {
            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            Toast.makeText(JapitiJibonDescriptionActivity.this, "ধন্যবাদ কিছুক্ষন পরে মাইআইটেম লিস্ট এ আপনার আইটেমটি দেখতে পারবেন", Toast.LENGTH_SHORT).show();
            myIntent.putExtra("imageUrl", imageUrl);
            myIntent.putExtra("dataBaseData", dataBaseData);
            myIntent.putExtra("cameFromWhichActivity", "payWithRocket");
            this.startActivity(myIntent);
            //overridePendingTransition(R.anim.left_in, R.anim.left_out);
            //finish();
        }
    }
}
