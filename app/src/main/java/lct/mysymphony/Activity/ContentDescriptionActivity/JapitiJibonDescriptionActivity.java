package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.Activity.PaymentMethod;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.JapitoJibonMC;
import lct.mysymphony.R;

public class JapitiJibonDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    VideoView videoView;
    TextView newPrice,previousPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japiti_jibon_description);

        newsImageView = findViewById(R.id.imgJapitiJibonDescription);
        newsTitle = findViewById(R.id.newsTitleJapitiJibonDescription);
        newsDescription = findViewById(R.id.newsdescriptionJapitiJibonDescription);
        videoView=findViewById(R.id.videoViewJapitojibon);
//        newPrice=findViewById(R.id.newPriceTVinJapitoJibonDescription);
//        previousPrice=findViewById(R.id.previousPriceTVinJapitoJibonDescription);
//        previousPrice.setPaintFlags(previousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

            Log.i("japitojibon","japitojibon");
        }
        else
        {
            newsImageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(JapitiJibonDescriptionActivity.this)
                    .load(object.getImageUrl())
                    .into(newsImageView);
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
        JapitoJibonMC object = (JapitoJibonMC) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData=new DataBaseData(object.getContentTitle(),object.getConntentCat(),object.getContentType(),object.getContentDescription(),"free",object.getContentId());
        purchase.putExtra("dataBaseData",dataBaseData);
        purchase.putExtra("imageUrl",object.getImageUrl());
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
