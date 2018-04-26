package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import lct.mysymphony.R;

public class ImageViewActivity extends AppCompatActivity {

    ///int price;
    String wallpaerUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        wallpaerUrl = (String) getIntent().getSerializableExtra("wallpaper");
        ///price=getIntent().getIntExtra("price",0);
        ///Log.d("price",Integer.toString(price));

        Glide.with(this).load(wallpaerUrl).into((ImageView) findViewById(R.id.imageViewWallpaper));
    }

    public void purChase(View view) {
        Intent purchase = new Intent(ImageViewActivity.this, PaymentMethod.class);
        Log.d("img_image",wallpaerUrl);
        purchase.putExtra("imageUrl",wallpaerUrl);
        ///purchase.putExtra("price",price);
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
