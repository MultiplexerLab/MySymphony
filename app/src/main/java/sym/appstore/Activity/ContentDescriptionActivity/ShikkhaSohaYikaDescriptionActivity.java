package sym.appstore.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sym.appstore.Activity.HomePage;
import sym.appstore.ModelClass.ShikkhaSohaYika;
import sym.appstore.R;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class ShikkhaSohaYikaDescriptionActivity extends AppCompatActivity {

    TextView newsTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shikha_details);
        ShikkhaSohaYika shikhkha = (ShikkhaSohaYika) getIntent().getSerializableExtra("shikkha");
        Glide.with(this).load(shikhkha.getImageURL()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        TextView descriptio = findViewById(R.id.description);
        descriptio.setText(shikhkha.getContentTitle());
        newsTV = findViewById(R.id.newsdescriptionInShikkhYaDescription);
        newsTV.setText(shikhkha.getContentDescription());
    }

    public void purChase(View view) {
        ShikkhaSohaYika shikhkha = (ShikkhaSohaYika) getIntent().getSerializableExtra("shikkha");
        Intent purchase = new Intent(ShikkhaSohaYikaDescriptionActivity.this, PaymentMethod.class);
        purchase.putExtra("imageUrl", shikhkha.getContentUrl());
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
    }
}
