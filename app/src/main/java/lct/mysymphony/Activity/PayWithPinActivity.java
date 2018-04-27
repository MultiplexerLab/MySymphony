package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import lct.mysymphony.R;

public class PayWithPinActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_pin);

        toolbar = findViewById(R.id.toolbarlayoutinpaywithpin);
        setSupportActionBar(toolbar);

        imageUrl=getIntent().getStringExtra("imageUrl");
    }

    public void jumpToProfile(View view) {
        Intent intent = new Intent(PayWithPinActivity.this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), PaymentMethod.class);
        myIntent.putExtra("imageUrl",imageUrl);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}
