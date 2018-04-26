package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.ModelClass.ShikkhaSohaYika;
import lct.mysymphony.R;

public class ShikhaDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shikha_details);

        ShikkhaSohaYika shikhkha = (ShikkhaSohaYika) getIntent().getSerializableExtra("shikkha");

        Glide.with(this).load(shikhkha.getImageURL()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        TextView descriptio = findViewById(R.id.description);
        descriptio.setText(shikhkha.getContentTitle());
    }

    public void purChase(View view) {
        ShikkhaSohaYika shikhkha = (ShikkhaSohaYika) getIntent().getSerializableExtra("shikkha");
        Intent purchase = new Intent(ShikhaDetails.this, PaymentMethod.class);
        purchase.putExtra("imageUrl",shikhkha.getContentUrl());
        startActivity(purchase);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}
