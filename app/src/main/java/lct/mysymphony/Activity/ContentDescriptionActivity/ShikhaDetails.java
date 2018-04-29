package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.Activity.PaymentMethod;
import lct.mysymphony.ModelClass.ShikkhaSohaYika;
import lct.mysymphony.R;

public class ShikhaDetails extends AppCompatActivity {

    TextView newPrice,previousPrice;
    TextView newsTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shikha_details);

        ShikkhaSohaYika shikhkha = (ShikkhaSohaYika) getIntent().getSerializableExtra("shikkha");

        Glide.with(this).load(shikhkha.getImageURL()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        TextView descriptio = findViewById(R.id.description);
        descriptio.setText(shikhkha.getContentTitle());
        newsTV=findViewById(R.id.newsdescriptionInShikkhYaDescription);
        newsTV.setText(shikhkha.getContentDescription());
//        newPrice=findViewById(R.id.newPriceTVinShikkhaDetails);
//        previousPrice=findViewById(R.id.previousPriceTVinShikkhaDetails);
       //previousPrice.setPaintFlags(previousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void purChase(View view) {
        ShikkhaSohaYika shikhkha = (ShikkhaSohaYika) getIntent().getSerializableExtra("shikkha");
        Intent purchase = new Intent(ShikhaDetails.this, PaymentMethod.class);
        purchase.putExtra("imageUrl",shikhkha.getContentUrl());
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}
