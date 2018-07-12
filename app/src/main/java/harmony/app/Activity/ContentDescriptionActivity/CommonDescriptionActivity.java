package harmony.app.Activity.ContentDescriptionActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import harmony.app.ModelClass.CategoryContent;
import harmony.app.R;
import harmony.app.Helper.AppLogger;

public class CommonDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    WebView webView;
    String source;
    Date currenTime;
    DateFormat dateFormat;
    RelativeLayout rootLaoutDescription;
    Snackbar snackbar;
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

        rootLaoutDescription = findViewById(R.id.rootLaoutDescription);

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (internetConnected()) {
                            if(snackbar!=null){
                                if(snackbar.isShown()) {
                                    snackbar.dismiss();
                                }
                            }
                            setDescripTionData();
                            mySwipeRefreshLayout.setRefreshing(false);
                        } else {
                            showSnackBar();
                        }
                    }
                }
        );
    }

    public void showSnackBar() {
        snackbar = Snackbar
                .make(rootLaoutDescription, "ইন্টারনেটের সাথে সংযুক্ত নেই!", Snackbar.LENGTH_INDEFINITE)
                .setAction("সংযুক্ত করুন", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(settingsIntent, 9003);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private boolean internetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(source!=null){
            /*Intent home = new Intent(this, HomePage.class);
            startActivity(home);*/
            finish();
        }else {
            /*Intent myIntent = new Intent(getApplicationContext(), CategoryActivity.class);
            this.startActivity(myIntent);*/
            finish();
        }
    }

    public void setDescripTionData() {
        CategoryContent object = (CategoryContent) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", object.getContentId()+"",
                "VISITED", "CategoryContent: "+object.getContentCat(), "content");

        newsDescription.setText(object.getContentDescription());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(object.getContentUrl());
        Glide.with(CommonDescriptionActivity.this).load(object.getThumbnailImgUrl()).into(newsImageView);
    }
}
