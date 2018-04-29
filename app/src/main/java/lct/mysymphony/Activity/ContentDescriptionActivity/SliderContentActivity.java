package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.Activity.PaymentMethod;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.SliderImage;
import lct.mysymphony.R;

public class SliderContentActivity extends AppCompatActivity {

    String imageUrl;
    SliderImage wallpaerUrl;
    TextView newsTitleTv, newsDescriptionTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_details_view);

        wallpaerUrl = (SliderImage) getIntent().getSerializableExtra("wallpaper");
        newsTitleTv=findViewById(R.id.newsTiTleInSlideDetails);
        newsDescriptionTV=findViewById(R.id.newsdescriptionInSliderDetails);

        imageUrl=wallpaerUrl.getImage_url();
        Glide.with(this).load(wallpaerUrl.getImage_url()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        newsTitleTv.setText(wallpaerUrl.getContentTitle());
        newsDescriptionTV.setText(wallpaerUrl.getDescription());

    }

    public void purChase(View view) {
        Intent purchase = new Intent(SliderContentActivity.this, PaymentMethod.class);
        DataBaseData dataBaseData=new DataBaseData(wallpaerUrl.getContentTitle(),wallpaerUrl.getContentCat(),wallpaerUrl.getContentType(),"","free",wallpaerUrl.getContentId());
        purchase.putExtra("dataBaseData",dataBaseData);
        purchase.putExtra("imageUrl",imageUrl);
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
