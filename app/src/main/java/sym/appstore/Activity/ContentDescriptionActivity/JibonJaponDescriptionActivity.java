package sym.appstore.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sym.appstore.Activity.JibonJaponActivity;
import sym.appstore.ModelClass.DataBaseData;
import sym.appstore.ModelClass.JibonJapon;
import sym.appstore.R;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class JibonJaponDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jibon_japon_description);
        newsImageView = findViewById(R.id.imgJibonJaponDescription);
        newsTitle = findViewById(R.id.newsTitleJibonJaponDescription);
        newsDescription = findViewById(R.id.newsdescriptionJibonJaponDescription);
        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), JibonJaponActivity.class);
        this.startActivity(myIntent);
    }

    public void setDescripTionData() {
        JibonJapon object = (JibonJapon) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(JibonJaponDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);
    }

    public void mullochar(View view) {
        JibonJapon object = (JibonJapon) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(JibonJaponDescriptionActivity.this, PaymentMethod.class);
        DataBaseData dataBaseData=new DataBaseData(object.getContentTitle(),object.getContentCat(),object.getContentType(),object.getContentDescription(), object.getThumbnailImgUrl(), "free",object.getContentId());
        purchase.putExtra("dataBaseData",dataBaseData);
        purchase.putExtra("imageUrl",object.getImageUrl());
        purchase.putExtra("cameFromWhichActivity", "JibonJaponDescriptionActivity");
        startActivity(purchase);
    }
}
