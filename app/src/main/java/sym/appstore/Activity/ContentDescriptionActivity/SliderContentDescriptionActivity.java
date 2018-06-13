package sym.appstore.Activity.ContentDescriptionActivity;

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

import sym.appstore.Activity.HomePage;
import sym.appstore.Activity.ImageViewActivity;
import sym.appstore.Activity.ProfileActivity;
import sym.appstore.ModelClass.DataBaseData;
import sym.appstore.ModelClass.Porashuna;
import sym.appstore.ModelClass.SliderImage;
import sym.appstore.R;
import sym.appstore.helper.CheckPermission;
import sym.appstore.helper.DataHelper;
import sym.appstore.helper.DownloadApk;
import sym.appstore.helper.DownloadAudio;
import sym.appstore.helper.DownloadImage;
import sym.appstore.helper.DownloadVideo;
import sym.appstore.helper.ProgressDialog;
import sym.appstore.helper.PushDataToSharedPref;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class SliderContentDescriptionActivity extends AppCompatActivity implements DownloadImage.AsyncResponse, DownloadApk.AsyncResponse {

    String imageUrl;
    Porashuna sliderImage;
    TextView newsTitleTv, newsDescriptionTV;
    DataHelper dataHelper;
    LinearLayout buyOrDownLoadLL, bishesOfferLL;
    Button buyOrDownloadBTN;
    boolean isThisContentFree = false;
    TextView priceTV;
    DataBaseData dataBaseData;
    sym.appstore.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_details_view);
        CheckPermission checkPermission = new CheckPermission(this);
        checkPermission.checkPermissions();
        buyOrDownLoadLL = findViewById(R.id.buyOrDownLoadLLInSliderDetails);
        bishesOfferLL = findViewById(R.id.bisheshOfferLLInSliderDetails);
        dataHelper = new DataHelper(SliderContentDescriptionActivity.this);
        priceTV = findViewById(R.id.priceTVinSliderDetails);
        progressDialog = new ProgressDialog(SliderContentDescriptionActivity.this);
        sliderImage = (Porashuna) getIntent().getSerializableExtra("sliderImage");
        newsTitleTv = findViewById(R.id.newsTiTleInSlideDetails);
        newsDescriptionTV = findViewById(R.id.newsdescriptionInSliderDetails);
        buyOrDownloadBTN = findViewById(R.id.buyOrDownloadBTNInSliderDetails);
        imageUrl = sliderImage.getContentUrl();

        if (sliderImage.getContentUrl().contains("apk")) {
            Glide.with(this).load(sliderImage.getThumbnailImgUrl()).into((ImageView) findViewById(R.id.imageViewWallpaper));

        } else {
            Glide.with(this).load(sliderImage.getContentUrl()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        }
        newsTitleTv.setText(sliderImage.getContentTitle());
        newsDescriptionTV.setText(sliderImage.getContentDescription());
        if (dataHelper.checkDownLoadedOrNot(sliderImage.getContentCat(), sliderImage.getContentId())) {
            buyOrDownLoadLL.setVisibility(View.GONE);
        } /*else if (sliderImage.get == 0) {
            bishesOfferLL.setVisibility(View.GONE);
            buyOrDownloadBTN.setText("ডাউনলোড করুন");
            isThisContentFree = true;
        } else if (sliderImage.getContentPrice() > 0) {
            bishesOfferLL.setVisibility(View.VISIBLE);
            Log.d("price", Integer.toString(sliderImage.getContentPrice()));
            priceTV.setText(Integer.toString(sliderImage.getContentPrice()));
            isThisContentFree = false;
        }*/
    }

    public void downloadSlider(View view) {
        //Intent purchase = new Intent(SliderContentDescriptionActivity.this, PaymentMethod.class);
        String priceStatus = null;
        priceStatus = "free";
        dataBaseData = new DataBaseData(sliderImage.getContentTitle(), sliderImage.getContentCat(), sliderImage.getContentType(), "", sliderImage.getThumbnailImgUrl(), priceStatus, sliderImage.getContentId());

        PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
        pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, SliderContentDescriptionActivity.this);
        Log.d("enterFree", "enterFree");

        Log.i("DataType", dataBaseData.getContentType());

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
        } else if (dataBaseData.getContentType().contains("apk")) {

            Gson gson = new Gson();
            SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
            String json = preferences.getString("databaseData", "");
            DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
            Log.i("APK", json);
            DownloadApk downloadApk = new DownloadApk();
            downloadApk.downLoadAPK(sliderImage.getContentUrl(), SliderContentDescriptionActivity.this,
                    dataBaseData);
        }
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
                    } else if (dataBaseData.getContentType().contains("apk")) {
                        progressDialog.showProgressDialog();
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                        String json = preferences.getString("databaseData", "");
                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                        Log.i("APK", json);
                        DownloadApk downloadApk = new DownloadApk();
                        downloadApk.downLoadAPK(sliderImage.getContentUrl(), SliderContentDescriptionActivity.this,
                                dataBaseData);
                    }
                }
            } else
                Log.d("errorInonActivityResult", "errorInonActivityResult");
        }
    }

    @Override
    public void processFinish(String output) {
        Log.d("processFinished", "processFinished");
        if (output.equals("complete")) {
            progressDialog.hideProgressDialog();
            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            ///Toast.makeText(SliderContentDescriptionActivity.this, "ধন্যবাদ কিছুক্ষন পরে মাইআইটেম লিস্ট এ আপনার আইটেমটি দেখতে পারবেন", Toast.LENGTH_SHORT).show();
            myIntent.putExtra("imageUrl", imageUrl);
            myIntent.putExtra("dataBaseData", dataBaseData);
            myIntent.putExtra("cameFromWhichActivity", "ImageViewActivity");
            this.startActivity(myIntent);
        } else if (output.contains("completeAPK")) {

        } else {
            Log.d("errorInprocessFinish", "errorInprocessFinish");
        }
    }
   /* @Override
    public void onPause() {
        super.onPause();
        if (progressDialog!=null)
            progressDialog.hideProgressDialog();
    }*/
}
