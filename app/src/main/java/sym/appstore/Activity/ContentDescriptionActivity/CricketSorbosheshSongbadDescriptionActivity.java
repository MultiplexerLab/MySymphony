package sym.appstore.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sym.appstore.Activity.KheladhulaActivity;
import sym.appstore.ModelClass.DataBaseData;
import sym.appstore.ModelClass.KheladhulaCricketSorbosheshSongbad;
import sym.appstore.R;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class CricketSorbosheshSongbadDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsPublishedAt, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_sorboshesh_songbad_description);
        newsImageView = findViewById(R.id.imgCricketSorbosheshSongbadDescription);
        newsTitle = findViewById(R.id.newsTitleCricketSorbosheshSongbadDescription);
        newsPublishedAt = findViewById(R.id.newsPublishedatCricketSorbosheshSongbadDescription);
        newsDescription = findViewById(R.id.newsdescriptionCricketSorbosheshSongbadDescription);
        setDescripTionData();
    }

    public void setDescripTionData() {
        KheladhulaCricketSorbosheshSongbad object = (KheladhulaCricketSorbosheshSongbad) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsPublishedAt.setText(object.getPublishedAt());
        newsDescription.setText(object.getContentDescription());
        Glide.with(CricketSorbosheshSongbadDescriptionActivity.this).load(object.getImageUrl()).into(newsImageView);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), KheladhulaActivity.class);
        this.startActivity(myIntent);
    }

    public void mullochar(View view) {
        KheladhulaCricketSorbosheshSongbad object = (KheladhulaCricketSorbosheshSongbad) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData = new DataBaseData(object.getContentTitle(), object.getContentcat(), object.getContentType(), object.getContentDescription(), object.getThumbnailImgUrl(), "free", object.getContentId());
        purchase.putExtra("dataBaseData", dataBaseData);
        purchase.putExtra("imageUrl", object.getImageUrl());
        purchase.putExtra("cameFromWhichActivity", "CricketSorbosheshSongbadDescriptionActivity");
        startActivity(purchase);
    }
}
