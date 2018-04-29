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
import lct.mysymphony.Activity.PaymentMethod;
import lct.mysymphony.ModelClass.Auttohashi;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;

public class AuttohasiDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    TextView newPrice,previousPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auttohasi_description);
        newsImageView = findViewById(R.id.imgAuttohasiDescription);
        newsTitle = findViewById(R.id.newsTitleAuttohasiDescription);
        newsDescription = findViewById(R.id.newsdescriptionAuttohasiDescription);
//        newPrice=findViewById(R.id.newPriceTVinAuttoHashiDescription);
//        previousPrice=findViewById(R.id.previousPriceTVinAuttoHashiDescription);
        previousPrice.setPaintFlags(previousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), AuttoHashiActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();

    }

    public void setDescripTionData() {
        Auttohashi object = (Auttohashi) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(AuttohasiDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);
    }

    public void mullochar(View view) {
        Auttohashi object = (Auttohashi) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);

        DataBaseData dataBaseData=new DataBaseData(object.getContentTitle(),object.getContentCat(),object.getContentType(),object.getContentDescription(),"free",object.getContentId());
        purchase.putExtra("DataBaseData",dataBaseData);
        purchase.putExtra("imageUrl",object.getImageUrl());
        startActivity(purchase);
        purchase.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
