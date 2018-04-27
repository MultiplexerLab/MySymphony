package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.ModelClass.BigganOProjukti;
import lct.mysymphony.R;

public class BigganOProjuktiDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biggan_oprojukti_description);

        newsImageView = findViewById(R.id.imgBigganOProjuktiDescription);
        newsTitle = findViewById(R.id.newsTitleBigganOProjuktiDescription);
        newsDescription = findViewById(R.id.newsdescriptionBigganOProjuktiDescription);
        setDescripTionData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), BigganOProjuktiActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    public void setDescripTionData() {
        BigganOProjukti object = (BigganOProjukti) getIntent().getSerializableExtra("Data");
        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(BigganOProjuktiDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);
    }

    public void mullochar(View view) {
        BigganOProjukti object = (BigganOProjukti) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(getApplicationContext(), PaymentMethod.class);
        purchase.putExtra("imageUrl",object.getImageUrl());
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }
}
