package lct.mysymphony.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.GamesZone;
import lct.mysymphony.ModelClass.MulloChar;
import lct.mysymphony.ModelClass.SeraChobi;
import lct.mysymphony.ModelClass.ShocolChobi;
import lct.mysymphony.R;
import lct.mysymphony.helper.DataHelper;
import lct.mysymphony.helper.DownloadImage;

public class ImageViewActivity extends AppCompatActivity implements DownloadImage.AsyncResponse {

    ///int price;
    String imageUrl;
    LinearLayout bisheshOfferLinearLayout;
    String contentTitle, contentCat, contentDesc, contentType;
    TextView previousPrice, newPrice;
    String cameFromWhichActivity;
    DataBaseData dataBaseData;
    Button buyOrDownloadBTN;
    boolean isDownloaded = false;
    boolean isItFree = false;
    DataHelper dataHelper;
    LinearLayout buyOrDownloadLinearLayout;
    lct.mysymphony.helper.ProgressDialog progressDialog;

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
        newPrice = findViewById(R.id.newPriceTVinImageViewActivity);
        progressDialog=new lct.mysymphony.helper.ProgressDialog(this);



        cameFromWhichActivity = getIntent().getStringExtra("cameFromWhichActivity");

        if (cameFromWhichActivity != null) {

            if (cameFromWhichActivity.contains("MulloChar")) {
                MulloChar Data = (MulloChar) getIntent().getSerializableExtra("wallpaper");
                imageUrl = Data.getContentUrl();
                contentTitle = Data.getContentTile();
                contentCat = Data.getContentCat();
                contentDesc = "";
                contentType = Data.getContentType();
                newPrice.setText(Integer.toString(Data.getNewPrice()));
                previousPrice.setText(Integer.toString(Data.getPreviousPrice()));
                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, "paid", Data.getContentId());
                Glide.with(this).load(Data.getImageUrl()).into((ImageView) findViewById(R.id.imageViewWallpaper));

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


                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, priceStatus, Data.getContentId());
                Glide.with(this).load(Data.getContentUrl()).into((ImageView) findViewById(R.id.imageViewWallpaper));

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

                String priceStatus;
                if (Data.getContentPrice() == 0) {
                    priceStatus = "free";
                    isItFree = true;
                } else {
                    isItFree = false;
                    priceStatus = "paid";
                }


                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, priceStatus, Data.getContentId());
                Glide.with(this).load(Data.getContentUrl()).into((ImageView) findViewById(R.id.imageViewWallpaper));

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

                String priceStatus;
                if (Data.getContentPrice() == 0) {
                    isItFree = true;
                    priceStatus = "free";
                } else {
                    isItFree = false;
                    priceStatus = "paid";
                }

                dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, priceStatus, Data.getContentId());
                Glide.with(this).load(Data.getImage_url()).into((ImageView) findViewById(R.id.imageViewWallpaper));

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


        if (!dataBaseData.getContentType().contains("video") && !imageUrl.contains("mp4") && !imageUrl.contains("youtube") && !imageUrl.contains("music") && !imageUrl.contains("videos")) {


            Log.d("isItFreeInPurchase",String.valueOf(isItFree));
            Intent myIntent;
            if (isItFree==false) {

                myIntent = new Intent(getApplicationContext(), PaymentMethod.class);
                myIntent.putExtra("imageUrl", imageUrl);
                myIntent.putExtra("dataBaseData", dataBaseData);
                myIntent.putExtra("cameFromWhichActivity", "payWithRocket");
                this.startActivity(myIntent);
                //overridePendingTransition(R.anim.left_in, R.anim.left_out);
                //finish();
            }
            else
            {
                progressDialog.showProgressDialog();
                DownloadImage downloadImage = new DownloadImage();
                downloadImage.downloadImage(imageUrl, ImageViewActivity.this, dataBaseData);
            }
//

        } else {
            Toast.makeText(ImageViewActivity.this, "ভিডিও কন্টেন্ট পরবর্তীতে পাবেন", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
            myIntent.putExtra("cameFromWhichActivity", "payWithRocket");
            this.startActivity(myIntent);
            //overridePendingTransition(R.anim.left_in, R.anim.left_out);
            //finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
        //overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }


    @Override
    public void processFinish(String output) {

        progressDialog.hideProgressDialog();
        Log.d("processFinished", "processFinished");
        if (output.contains("complete")) {
            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            Toast.makeText(ImageViewActivity.this, "ধন্যবাদ কিছুক্ষন পরে মাইআইটেম লিস্ট এ আপনার আইটেমটি দেখতে পারবেন", Toast.LENGTH_SHORT).show();
            myIntent.putExtra("imageUrl", imageUrl);
            myIntent.putExtra("dataBaseData", dataBaseData);
            myIntent.putExtra("cameFromWhichActivity", "payWithRocket");
            this.startActivity(myIntent);
            //overridePendingTransition(R.anim.left_in, R.anim.left_out);
            //finish();
        }
    }


}
