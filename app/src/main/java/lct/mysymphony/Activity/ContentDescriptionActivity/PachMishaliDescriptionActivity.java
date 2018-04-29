package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.PachMishaliActivity;
import lct.mysymphony.Activity.PaymentMethod;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.PacMishali;
import lct.mysymphony.R;

public class PachMishaliDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    TextView newPrice,previousPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pach_mishali_description);

        newsImageView = findViewById(R.id.imgPachMishaliDescription);
        newsTitle = findViewById(R.id.newsTitlePachMishaliDescription);
        newsDescription = findViewById(R.id.newsdescriptionPachMishaliDescription);
//        newPrice=findViewById(R.id.newPriceTVinPachMishaliDescription);
//        previousPrice=findViewById(R.id.previousPriceTVinPachMishaliDescription);
        ///previousPrice.setPaintFlags(previousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        setDescripTionData();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), PachMishaliActivity.class);
        
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    public void setDescripTionData() {
        PacMishali object = (PacMishali) getIntent().getSerializableExtra("Data");

        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(PachMishaliDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);

    }

    public void mullochar(View view) {
        PacMishali object = (PacMishali) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData=new DataBaseData(object.getContentTitle(),object.getContentCat(),object.getContentType(),object.getContentDescription(),"free",object.getContentId());
        purchase.putExtra("dataBaseData",dataBaseData);
        purchase.putExtra("imageUrl",object.getImageUrl());
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}