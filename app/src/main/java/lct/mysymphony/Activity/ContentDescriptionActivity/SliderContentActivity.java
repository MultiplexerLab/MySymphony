package lct.mysymphony.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.SliderImage;
import lct.mysymphony.R;
import lct.mysymphony.helper.DataHelper;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class SliderContentActivity extends AppCompatActivity {

    String imageUrl;
    SliderImage wallpaerUrl;
    TextView newsTitleTv, newsDescriptionTV;
    DataHelper dataHelper;
    LinearLayout buyOrDownLoadLL,bishesOfferLL;
    Button buyOrDownloadBTN;
    boolean isThisContentFree=false;
    TextView priceTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_details_view);
        buyOrDownLoadLL=findViewById(R.id.buyOrDownLoadLLInSliderDetails);
        bishesOfferLL=findViewById(R.id.bisheshOfferLLInSliderDetails);
        dataHelper=new DataHelper(SliderContentActivity.this);
        priceTV=findViewById(R.id.priceTVinSliderDetails);

        wallpaerUrl = (SliderImage) getIntent().getSerializableExtra("wallpaper");
        newsTitleTv=findViewById(R.id.newsTiTleInSlideDetails);
        newsDescriptionTV=findViewById(R.id.newsdescriptionInSliderDetails);
        buyOrDownloadBTN=findViewById(R.id.buyOrDownloadBTNInSliderDetails);

        imageUrl=wallpaerUrl.getImage_url();
        Glide.with(this).load(wallpaerUrl.getImage_url()).into((ImageView) findViewById(R.id.imageViewWallpaper));
        newsTitleTv.setText(wallpaerUrl.getContentTitle());
        newsDescriptionTV.setText(wallpaerUrl.getDescription());

        if (dataHelper.checkDownLoadedOrNot(wallpaerUrl.getContentCat(), wallpaerUrl.getContentId())) {

            buyOrDownLoadLL.setVisibility(View.GONE);
        } else if (wallpaerUrl.getContentPrice()==0){
            bishesOfferLL.setVisibility(View.GONE);
            buyOrDownloadBTN.setText("ডাউনলোড করুন");
            isThisContentFree=true;
        }
        else if (wallpaerUrl.getContentPrice()>0)
        {
            bishesOfferLL.setVisibility(View.VISIBLE);
            Log.d("price",Integer.toString(wallpaerUrl.getContentPrice()));
            priceTV.setText(Integer.toString(wallpaerUrl.getContentPrice()));
            isThisContentFree=false;
        }

    }

    public void purChase(View view) {

        Intent purchase = new Intent(SliderContentActivity.this, PaymentMethod.class);

        String priceStatus = null;

        if (isThisContentFree)
            priceStatus="free";
        else
            priceStatus="paid";

        DataBaseData dataBaseData=new DataBaseData(wallpaerUrl.getContentTitle(),wallpaerUrl.getContentCat(),wallpaerUrl.getContentType(),"",priceStatus,wallpaerUrl.getContentId());
        purchase.putExtra("dataBaseData",dataBaseData);
        purchase.putExtra("imageUrl",imageUrl);
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
