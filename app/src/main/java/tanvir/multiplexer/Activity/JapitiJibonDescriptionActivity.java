package tanvir.multiplexer.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import tanvir.multiplexer.ModelClass.JapitoJibonMC;
import tanvir.multiplexer.R;

public class JapitiJibonDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japiti_jibon_description);

        newsImageView = findViewById(R.id.imgJapitiJibonDescription);
        newsTitle = findViewById(R.id.newsTitleJapitiJibonDescription);
        newsDescription = findViewById(R.id.newsdescriptionJapitiJibonDescription);
        videoView=findViewById(R.id.videoViewJapitojibon);

        setDescripTionData();
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

    public void setDescripTionData() {
        JapitoJibonMC object = (JapitoJibonMC) getIntent().getSerializableExtra("Data");

        if (object.getContentType().equals("video"))
        {
            newsImageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            videoView.setVideoURI(Uri.parse("https://looselycoupled-my.sharepoint.com/personal/tanim_looselycoupled_org/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Ftanim%5Flooselycoupled%5Forg%2FDocuments%2FAttachments%2Fsample2%2Emp4&parent=%2Fpersonal%2Ftanim%5Flooselycoupled%5Forg%2FDocuments%2FAttachments&slrid=cd95609e%2Df08a%2D5000%2Db7ff%2D73de2b588dce"));
            videoView.start();

            Log.i("japitojibon","japitojibon");

//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(object.getContentUrl())));

        }
        else
        {
            newsImageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(JapitiJibonDescriptionActivity.this)
                    .load(object.getImageUrl())
                    .into(newsImageView);
        }



        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());


    }

    public void purChase(View view) {
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        startActivity(purchase);
    }

    public void mullochar(View view) {
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        startActivity(purchase);
    }
}
