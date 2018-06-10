package sym.appstore.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sym.appstore.Activity.CartoonActivity;
import sym.appstore.ModelClass.Cartoon;
import sym.appstore.ModelClass.DataBaseData;
import sym.appstore.R;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class CartoonDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_description);
        newsImageView = findViewById(R.id.imgCartoonDescription);
        newsTitle = findViewById(R.id.newsTitleCartoonDescription);
        newsDescription = findViewById(R.id.newsdescriptionCartoonDescription);
        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), CartoonActivity.class);
        this.startActivity(myIntent);
    }

    public void setDescripTionData() {
        Cartoon object = (Cartoon) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(CartoonDescriptionActivity.this).load(object.getImageUrl()).into(newsImageView);
    }

    public void mullochar(View view) {
        Cartoon object = (Cartoon) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData = new DataBaseData(object.getContentTitle(), object.getContentCat(), object.getContentType(), object.getContentDescription(), object.getThumbnailImgUrl(), "free", object.getContentId());
        purchase.putExtra("dataBaseData", dataBaseData);
        purchase.putExtra("imageUrl", object.getImageUrl());
        purchase.putExtra("cameFromWhichActivity", "CartoonDescriptionActivity");
        startActivity(purchase);
    }
}
