package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.Activity.ProfileActivity;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.SliderImage;
import lct.mysymphony.R;
import lct.mysymphony.helper.CheckPermission;
import lct.mysymphony.helper.DataHelper;
import lct.mysymphony.helper.DownloadImage;
import lct.mysymphony.helper.DownloadVideo;
import lct.mysymphony.helper.ProgressDialog;
import lct.mysymphony.helper.PushDataToSharedPref;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class SliderContentDescriptionActivity extends AppCompatActivity implements DownloadImage.AsyncResponse {

    String imageUrl;
    SliderImage wallpaerUrl;
    TextView newsTitleTv, newsDescriptionTV;
    DataHelper dataHelper;
    LinearLayout buyOrDownLoadLL, bishesOfferLL;
    Button buyOrDownloadBTN;
    boolean isThisContentFree = false;
    TextView priceTV;
    DataBaseData dataBaseData;
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_details_view);
        CheckPermission checkPermission=new CheckPermission(this);
        checkPermission.checkPermissions();
        buyOrDownLoadLL = findViewById(R.id.buyOrDownLoadLLInSliderDetails);
        bishesOfferLL = findViewById(R.id.bisheshOfferLLInSliderDetails);
        dataHelper = new DataHelper(SliderContentDescriptionActivity.this);
        priceTV = findViewById(R.id.priceTVinSliderDetails);
        progressDialog = new ProgressDialog(SliderContentDescriptionActivity.this);
        wallpaerUrl = (SliderImage) getIntent().getSerializableExtra("wallpaper");
        newsTitleTv = findViewById(R.id.newsTiTleInSlideDetails);
        newsDescriptionTV = findViewById(R.id.newsdescriptionInSliderDetails);
        buyOrDownloadBTN = findViewById(R.id.buyOrDownloadBTNInSliderDetails);
        imageUrl = wallpaerUrl.getImage_url();
        Glide.with(this).load(wallpaerUrl.getImage_url()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        newsTitleTv.setText(wallpaerUrl.getContentTitle());
        newsDescriptionTV.setText(wallpaerUrl.getDescription());
        if (dataHelper.checkDownLoadedOrNot(wallpaerUrl.getContentCat(), wallpaerUrl.getContentId())) {
            buyOrDownLoadLL.setVisibility(View.GONE);
        } else if (wallpaerUrl.getContentPrice() == 0) {
            bishesOfferLL.setVisibility(View.GONE);
            buyOrDownloadBTN.setText("ডাউনলোড করুন");
            isThisContentFree = true;
        } else if (wallpaerUrl.getContentPrice() > 0) {
            bishesOfferLL.setVisibility(View.VISIBLE);
            Log.d("price", Integer.toString(wallpaerUrl.getContentPrice()));
            priceTV.setText(Integer.toString(wallpaerUrl.getContentPrice()));
            isThisContentFree = false;
        }
    }

    public void purChase(View view) {
        //Intent purchase = new Intent(SliderContentDescriptionActivity.this, PaymentMethod.class);
        String priceStatus = null;

        if (isThisContentFree)
        {
            priceStatus = "free";
            dataBaseData = new DataBaseData(wallpaerUrl.getContentTitle(), wallpaerUrl.getContentCat(), wallpaerUrl.getContentType(), "", wallpaerUrl.getThumbNail_image(), priceStatus, wallpaerUrl.getContentId());
            progressDialog.showProgressDialog();
            DownloadImage downloadImage = new DownloadImage();
            downloadImage.downloadImage(imageUrl, SliderContentDescriptionActivity.this, dataBaseData);
            PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
            pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, SliderContentDescriptionActivity.this);
            Log.d("enterFree","enterFree");
        }
        else
        {
            Intent myIntent;
            priceStatus = "paid";
            dataBaseData = new DataBaseData(wallpaerUrl.getContentTitle(), wallpaerUrl.getContentCat(), wallpaerUrl.getContentType(), "", wallpaerUrl.getThumbNail_image(), priceStatus, wallpaerUrl.getContentId());
            SharedPreferences preferences = getSharedPreferences("phoneNumber", MODE_PRIVATE);
            myIntent = new Intent(getApplicationContext(), PaymentMethod.class);
            myIntent.putExtra("userId", preferences.getString("phoneNo", ""));
            Log.d("enterPaid","enterPaid");
            PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
            pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, SliderContentDescriptionActivity.this);
            this.startActivityForResult(myIntent, 1);
        }


        /*purchase.putExtra("dataBaseData", dataBaseData);
        SharedPreferences preferences = getSharedPreferences("phoneNumber", MODE_PRIVATE);
        purchase.putExtra("userId", preferences.getString("phoneNo", ""));
        purchase.putExtra("imageUrl", imageUrl);
        progressDialog.showProgressDialog();
        DownloadImage downloadImage = new DownloadImage();
        downloadImage.downloadImage(imageUrl, SliderContentDescriptionActivity.this, dataBaseData);*/
        ///this.startActivityForResult(purchase, 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
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
                        downloadImage.downloadImage(imageURL, SliderContentDescriptionActivity.this, dataBaseData);
                    } else if (dataBaseData.getContentType().contains("video")) {
                        progressDialog.showProgressDialog();
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        Log.i("VideoCOntent", "video");
                        DownloadVideo downLoadVideo = new DownloadVideo();
                        downLoadVideo.downloadVideo("http://dev.exiv2.org/attachments/341/video-2012-07-05-02-29-27.mp4", SliderContentDescriptionActivity.this, dataBaseData);
                    }
                }
            }
            else
                Log.d("errorInonActivityResult","errorInonActivityResult");
        }
    }

    @Override
    public void processFinish(String output) {
        progressDialog.hideProgressDialog();
        Log.d("processFinished", "processFinished");
        if (output.contains("complete")) {
            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            ///Toast.makeText(SliderContentDescriptionActivity.this, "ধন্যবাদ কিছুক্ষন পরে মাইআইটেম লিস্ট এ আপনার আইটেমটি দেখতে পারবেন", Toast.LENGTH_SHORT).show();
            myIntent.putExtra("imageUrl", imageUrl);
            myIntent.putExtra("dataBaseData", dataBaseData);
            myIntent.putExtra("cameFromWhichActivity", "ImageViewActivity");
            this.startActivity(myIntent);
        }
        else
            Log.d("errorInprocessFinish","errorInprocessFinish");
    }
   /* @Override
    public void onPause() {
        super.onPause();
        if (progressDialog!=null)
            progressDialog.hideProgressDialog();
    }*/
}
