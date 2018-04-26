package tanvir.multiplexer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tanvir.multiplexer.ModelClass.PacMishali;
import tanvir.multiplexer.R;

public class PachMishaliDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pach_mishali_description);

        newsImageView = findViewById(R.id.imgPachMishaliDescription);
        newsTitle = findViewById(R.id.newsTitlePachMishaliDescription);
        newsDescription = findViewById(R.id.newsdescriptionPachMishaliDescription);

        setDescripTionData();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), PachMishaliActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    public void setDescripTionData() {
        PacMishali object = (PacMishali) getIntent().getSerializableExtra("Data");

        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(PachMishaliDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);

    }

    public void mullochar(View view) {
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        startActivity(purchase);
    }
}
