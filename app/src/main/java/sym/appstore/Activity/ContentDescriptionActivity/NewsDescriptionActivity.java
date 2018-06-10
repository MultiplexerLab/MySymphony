package sym.appstore.Activity.ContentDescriptionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import sym.appstore.Activity.GoromKhoborActivity;
import sym.appstore.ModelClass.GoromKhobor;
import sym.appstore.R;

public class NewsDescriptionActivity extends AppCompatActivity implements Serializable {

    ImageView newsImageView;
    TextView newsTitle, newsPublishedAt, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_description);
        newsImageView = findViewById(R.id.imgNewsPaperinNewsDescription);
        newsTitle = findViewById(R.id.newsTitleInNewsDetailActivity);
        newsPublishedAt = findViewById(R.id.newsPublishedatNewsDetailsActivity);
        newsDescription = findViewById(R.id.newsdescriptionInNewsPaperActivity);
        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), GoromKhoborActivity.class);
        this.startActivity(myIntent);
    }

    public void setDescripTionData() {
        GoromKhobor object = (GoromKhobor) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsPublishedAt.setText(object.getPublishedAt());
        newsDescription.setText(object.getContentDescription());
        Glide.with(NewsDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);
    }
}
