package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.AuttoHashiActivity;
import lct.mysymphony.ModelClass.Auttohashi;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class AuttohasiDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auttohasi_description);
        newsImageView = findViewById(R.id.imgAuttohasiDescription);
        newsTitle = findViewById(R.id.newsTitleAuttohasiDescription);
        newsDescription = findViewById(R.id.newsdescriptionAuttohasiDescription);
        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), AuttoHashiActivity.class);
        this.startActivity(myIntent);
    }

    public void setDescripTionData() {
        Auttohashi object = (Auttohashi) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(AuttohasiDescriptionActivity.this).load(object.getImageUrl()).into(newsImageView);
    }

    public void mullochar(View view) {
        Auttohashi object = (Auttohashi) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData = new DataBaseData(object.getContentTitle(), object.getContentCat(), object.getContentType(), object.getContentDescription(), object.getThumbnailImgUrl(), "free", object.getContentId());
        purchase.putExtra("DataBaseData", dataBaseData);
        purchase.putExtra("cameFromWhichActivity", "AuttohasiDescriptionActivity");
        purchase.putExtra("imageUrl", object.getImageUrl());
        startActivity(purchase);
    }
}
