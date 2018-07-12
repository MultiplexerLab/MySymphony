package harmony.app.Activity.ContentDescriptionActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.looselycoupled.subscriptionintegration.OnSubscriptionListener;
import com.looselycoupled.subscriptionintegration.SubscribeUsingPaymentGateway;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import harmony.app.Activity.PlayAudioActivity;
import harmony.app.Activity.ProfileActivity;
import harmony.app.Helper.InsertPayment;
import harmony.app.ModelClass.DataBaseData;
import harmony.app.ModelClass.CategoryContent;
import harmony.app.ModelClass.SliderImage;
import harmony.app.R;
import harmony.app.Helper.AppLogger;
import harmony.app.Helper.CheckPermission;
import harmony.app.Helper.DataHelper;
import harmony.app.Helper.DownloadApk;
import harmony.app.Helper.DownloadImage;
import harmony.app.Helper.ProgressDialog;
import harmony.app.Helper.PushDataToSharedPref;

public class SliderContentDescriptionActivity extends AppCompatActivity implements DownloadImage.AsyncResponse, DownloadApk.AsyncResponse {

    String imageUrl;
    SliderImage sliderImage;
    TextView newsTitleTv, newsDescriptionTV;
    DataHelper dataHelper;
    LinearLayout buyOrDownLoadLL, bishesOfferLL;
    Button buyOrDownloadBTN;
    TextView priceTV;
    DataBaseData dataBaseData;
    harmony.app.Helper.ProgressDialog progressDialog;
    Date currenTime;
    DateFormat dateFormat;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

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
        sliderImage = (SliderImage) getIntent().getSerializableExtra("sliderImage");
        newsTitleTv = findViewById(R.id.newsTiTleInSlideDetails);
        newsDescriptionTV = findViewById(R.id.newsdescriptionInSliderDetails);
        buyOrDownloadBTN = findViewById(R.id.buyOrDownloadBTNInSliderDetails);
        imageUrl = sliderImage.getContentUrl();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", sliderImage.getId()+"",
                "VISITED", "CategoryContent: "+sliderImage.getContentCat(), "content");

        if (sliderImage.getContentUrl().contains("apk")) {
            Glide.with(this).load(sliderImage.getThumbNail_image()).into((ImageView) findViewById(R.id.imageViewWallpaper));

        } else {
            Glide.with(this).load(sliderImage.getContentUrl()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        }
        newsTitleTv.setText(sliderImage.getContentTitle());
        newsDescriptionTV.setText(sliderImage.getContentDescription());
        if (dataHelper.checkDownLoadedOrNot(sliderImage.getContentCat(), sliderImage.getId())) {
            buyOrDownLoadLL.setVisibility(View.GONE);
        }
    }

    public void downloadSlider(View view) {
        String priceStatus = "free";
        dataBaseData = new DataBaseData(sliderImage.getContentTitle(), sliderImage.getContentCat(), sliderImage.getContentType(), "", sliderImage.getThumbNail_image(), priceStatus, sliderImage.getId());

        PushDataToSharedPref pushDataToSharedPref = new PushDataToSharedPref();
        pushDataToSharedPref.pushDatabaseDataToSharedPref(dataBaseData, imageUrl, SliderContentDescriptionActivity.this);

        Log.i("DataType", dataBaseData.getContentType());

        if (dataBaseData.getContentType().contains("image")) {
            progressDialog.showProgressDialog();

            if (checkPermissions()) {
                if (sliderImage.getContentPrice() > 0) {
                    initSubscription(sliderImage.getContentPrice());
                } else {
                    Gson gson = new Gson();
                    SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                    String json = preferences.getString("databaseData", "");
                    String imageURL = preferences.getString("imageUrl", "");
                    DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                    DownloadImage downloadImage = new DownloadImage();
                    downloadImage.downloadImage(imageURL, SliderContentDescriptionActivity.this, dataBaseData);
                }
            } else {
                Toast.makeText(this, "ডাওনলোড এর পূর্বে ডিভাইস এক্সেস দিয়ে নিন", Toast.LENGTH_SHORT).show();
            }

        } else if (dataBaseData.getContentType().contains("apk")) {
            if (checkPermissions()) {
                if (sliderImage.getContentPrice() > 0) {
                    initSubscription(sliderImage.getContentPrice());
                } else {
                    Gson gson = new Gson();
                    SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                    String json = preferences.getString("databaseData", "");
                    DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                    Log.i("APK", json);
                    DownloadApk downloadApk = new DownloadApk();
                    downloadApk.downLoadAPK(sliderImage.getContentUrl(), SliderContentDescriptionActivity.this,
                            dataBaseData);
                }
            } else {
                Toast.makeText(this, "ডাওনলোড এর পূর্বে ডিভাইস এক্সেস দিয়ে নিন", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void initSubscription(final int price) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    final String deviceId = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    AppLogger.insertLogs(SliderContentDescriptionActivity.this, dateFormat.format(currenTime), "N", sliderImage.getId() + "",
                            "PAYMENT_INITIATED", deviceId, "content");
                    SubscribeUsingPaymentGateway obj = new SubscribeUsingPaymentGateway();
                    obj.setData("test", "test123", "1234", (float) price, deviceId, sliderImage.getContentTitle(), SliderContentDescriptionActivity.this, new OnSubscriptionListener() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String transactionStatus = result.getString("transactionStatus");
                                String paymentID = result.getString("paymentID");
                                String paymentMethod = result.getString("paymentMethod");
                                String referenceCode = result.getString("referenceCode");
                                Long amount = result.getLong("amount");
                                if (transactionStatus.equals("Completed")) {
                                    String deviceId = Settings.Secure.getString(getContentResolver(),
                                            Settings.Secure.ANDROID_ID);
                                    InsertPayment.insertPayment(SliderContentDescriptionActivity.this, sliderImage.getId(), amount, paymentID, paymentMethod, referenceCode, deviceId, sliderImage.getContentTitle());
                                    AppLogger.insertLogs(SliderContentDescriptionActivity.this, dateFormat.format(currenTime), "N", sliderImage.getId()+"",
                                            "PAYMENT_DONE", result.getString("paymentMethod"), "content");

                                    Toast.makeText(SliderContentDescriptionActivity.this, "আপনার পেমেন্ট সফল হয়েছে, ডাউনলোড হচ্ছে", Toast.LENGTH_LONG).show();
                                    if(sliderImage.getContentType().equals("image")){
                                        Gson gson = new Gson();
                                        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
                                        String json = preferences.getString("databaseData", "");
                                        String imageURL = preferences.getString("imageUrl", "");
                                        DataBaseData dataBaseData = gson.fromJson(json, DataBaseData.class);
                                        DownloadImage downloadImage = new DownloadImage();
                                        downloadImage.downloadImage(imageURL, SliderContentDescriptionActivity.this, dataBaseData);
                                    }else{
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("tranResult", "transactionResult: " + result);
                        }

                        @Override
                        public void onError(JSONObject result) {
                            AppLogger.insertLogs(SliderContentDescriptionActivity.this, dateFormat.format(currenTime), "N", sliderImage.getId() + "",
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    private boolean checkPermissions() {
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
