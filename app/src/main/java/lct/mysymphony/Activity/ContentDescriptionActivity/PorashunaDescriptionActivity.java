package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.PorashunaActivity;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.Porashuna;
import lct.mysymphony.R;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class PorashunaDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porashuna_description);
        newsImageView = findViewById(R.id.imgPorashunaDescription);
        newsTitle = findViewById(R.id.newsTitlePorashunaDescription);
        newsDescription = findViewById(R.id.newsdescriptionPorashunaDescription);
        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        this.startActivity(myIntent);
    }

    public void setDescripTionData() {
        Porashuna object = (Porashuna) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(PorashunaDescriptionActivity.this).load(object.getImageUrl()).into(newsImageView);
    }

    public void mullochar(View view) {
        Porashuna object = (Porashuna) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData = new DataBaseData(object.getContentTitle(), object.getContentCat(), object.getContentType(), object.getContentDescription(), object.getThumbnailImgUrl(), "free", object.getContentId());
        purchase.putExtra("dataBaseData", dataBaseData);
        purchase.putExtra("imageUrl", object.getImageUrl());
        purchase.putExtra("cameFromWhichActivity", "PorashunaDescriptionActivity");
        startActivity(purchase);
    }
}
