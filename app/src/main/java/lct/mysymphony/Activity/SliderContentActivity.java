package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.ModelClass.SliderImage;
import lct.mysymphony.R;

public class SliderContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_details_view);

        SliderImage wallpaerUrl = (SliderImage) getIntent().getSerializableExtra("wallpaper");

        Glide.with(this).load(wallpaerUrl.getImage_url()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        TextView descriptio = findViewById(R.id.description);
        descriptio.setText(wallpaerUrl.getDescription());
    }

    public void purChase(View view) {
        Intent purchase = new Intent(SliderContentActivity.this, PaymentMethod.class);
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
