package sym.appstore.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sym.appstore.Activity.HomePage;
import sym.appstore.Activity.PorashunaActivity;
import sym.appstore.ModelClass.DataBaseData;
import sym.appstore.ModelClass.JapitoJibon;
import sym.appstore.ModelClass.Porashuna;
import sym.appstore.R;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class PorashunaDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    WebView webView;
    String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porashuna_description);
        newsImageView = findViewById(R.id.imgPorashunaDescription);
        newsTitle = findViewById(R.id.newsTitlePorashunaDescription);
        newsDescription = findViewById(R.id.newsdescriptionPorashunaDescription);
        webView = findViewById(R.id.webView);
        setDescripTionData();
        source = getIntent().getStringExtra("source");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(source!=null){
            Intent home = new Intent(this, HomePage.class);
            startActivity(home);
        }else {
            Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
            this.startActivity(myIntent);
        }
    }

    public void setDescripTionData() {
        Porashuna object = (Porashuna) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(object.getContentUrl());
        Glide.with(PorashunaDescriptionActivity.this).load(object.getThumbnailImgUrl()).into(newsImageView);

    }

    public void mullochar(View view) {
        Porashuna object = (Porashuna) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        DataBaseData dataBaseData = new DataBaseData(object.getContentTitle(), object.getContentCat(), object.getContentType(), object.getContentDescription(), object.getThumbnailImgUrl(), "free", object.getContentId());
        purchase.putExtra("dataBaseData", dataBaseData);
        purchase.putExtra("imageUrl", object.getThumbnailImgUrl());
        purchase.putExtra("cameFromWhichActivity", "PorashunaDescriptionActivity");
        startActivity(purchase);
    }
}
