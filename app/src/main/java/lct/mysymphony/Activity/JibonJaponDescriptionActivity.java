package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.ModelClass.JibonJapon;
import lct.mysymphony.R;

public class JibonJaponDescriptionActivity extends AppCompatActivity {

    ImageView newsImageView;
    TextView newsTitle, newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jibon_japon_description);

        newsImageView = findViewById(R.id.imgJibonJaponDescription);
        newsTitle = findViewById(R.id.newsTitleJibonJaponDescription);
        newsDescription = findViewById(R.id.newsdescriptionJibonJaponDescription);

        setDescripTionData();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), JibonJaponActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();


    }

    public void setDescripTionData() {
        JibonJapon object = (JibonJapon) getIntent().getSerializableExtra("Data");

        newsTitle.setText(object.getContentTitle());
        newsDescription.setText(object.getContentDescription());
        Glide.with(JibonJaponDescriptionActivity.this)
                .load(object.getImageUrl())
                .into(newsImageView);

    }

    public void mullochar(View view) {
        JibonJapon object = (JibonJapon) getIntent().getSerializableExtra("Data");
        Intent purchase = new Intent(JibonJaponDescriptionActivity.this, PaymentMethod.class);
        purchase.putExtra("imageUrl",object.getImageUrl());
        startActivity(purchase);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

    }
}
