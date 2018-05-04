package lct.mysymphony.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;
import lct.mysymphony.helper.DownloadImage;

public class PayWithPinActivity extends AppCompatActivity implements DownloadImage.AsyncResponse {

    private android.support.v7.widget.Toolbar toolbar;
    String imageUrl;
    DataBaseData dataBaseData;
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_pin);

        toolbar = findViewById(R.id.toolbarlayoutinpaywithpin);
        setSupportActionBar(toolbar);
        progressDialog=new lct.mysymphony.helper.ProgressDialog(this);

        imageUrl=getIntent().getStringExtra("imageUrl");
        dataBaseData= (DataBaseData) getIntent().getSerializableExtra("dataBaseData");
        if (dataBaseData==null)
            Log.d("dataBaseDataPin","null");
    }

    public void startUserProfileActivity(View view) {
        Log.d("image_url",imageUrl);

        if(!dataBaseData.getContentType().contains("video") && !imageUrl.contains("mp4") && !imageUrl.contains("youtube") && !imageUrl.contains("music") && !imageUrl.contains("videos")) {
            progressDialog.showProgressDialog();
            DownloadImage downloadImage = new DownloadImage();
            downloadImage.downloadImage(imageUrl, PayWithPinActivity.this,dataBaseData);

        }else{
            Toast.makeText(PayWithPinActivity.this, "ধন্যবাদ আপনার পেমেন্ট টি যাচাই করা হচ্ছে", Toast.LENGTH_SHORT).show();
            Toast.makeText(PayWithPinActivity.this, "ভিডিও কন্টেন্ট পরবর্তীতে পাবেন", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
            
            myIntent.putExtra("cameFromWhichActivity","payWithPin");
            this.startActivity(myIntent);
            //overridePendingTransition(R.anim.left_in, R.anim.left_out);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), PaymentMethod.class);
        myIntent.putExtra("imageUrl",imageUrl);
        
        this.startActivity(myIntent);
        //overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    @Override
    public void processFinish(String output) {
        progressDialog.hideProgressDialog();
        if (output.contains("complete"))
        {
            Toast.makeText(PayWithPinActivity.this, "ধন্যবাদ আপনার পেমেন্ট টি যাচাই করা হচ্ছে", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            myIntent.putExtra("cameFromWhichActivity","payWithPin");
            this.startActivity(myIntent);
            //overridePendingTransition(R.anim.left_in, R.anim.left_out);
            finish();
        }
    }
}
