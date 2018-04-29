package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.KheladhulaActivity;
import lct.mysymphony.Activity.PaymentMethod;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.Kheladhula_Cricket_sorboshesh_sobgbad;
import lct.mysymphony.R;

public class CricketSorbosheshSongbadDescriptionActivity extends AppCompatActivity {


    ImageView newsImageView;
    TextView newsTitle, newsPublishedAt, newsDescription;
    TextView newPrice,previousPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_sorboshesh_songbad_description);

        newsImageView = findViewById(R.id.imgCricketSorbosheshSongbadDescription);
        newsTitle = findViewById(R.id.newsTitleCricketSorbosheshSongbadDescription);
        newsPublishedAt = findViewById(R.id.newsPublishedatCricketSorbosheshSongbadDescription);
        newsDescription = findViewById(R.id.newsdescriptionCricketSorbosheshSongbadDescription);
//        newPrice=findViewById(R.id.newPriceTVinCricketSorbosheshSongbadDescription);
//        previousPrice=findViewById(R.id.previousPriceTVinCricketSorbosheshSongbadDescription);
        ///previousPrice.setPaintFlags(previousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        setDescripTionData();
    }

    public void setDescripTionData() {
        Kheladhula_Cricket_sorboshesh_sobgbad object = (Kheladhula_Cricket_sorboshesh_sobgbad) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsPublishedAt.setText(object.getPublishedAt());
        newsDescription.setText(object.getContentDescription());
        Glide.with(CricketSorbosheshSongbadDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        Intent myIntent = new Intent(getApplicationContext(), KheladhulaActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    public void mullochar(View view) {
        Kheladhula_Cricket_sorboshesh_sobgbad object = (Kheladhula_Cricket_sorboshesh_sobgbad) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData=new DataBaseData(object.getContentTitle(),object.getContentcat(),object.getContentType(),object.getContentDescription(),"free",object.getContentId());
        purchase.putExtra("dataBaseData",dataBaseData);
        purchase.putExtra("imageUrl",object.getImageUrl());
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
