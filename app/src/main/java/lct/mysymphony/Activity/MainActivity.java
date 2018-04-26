package lct.mysymphony.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import lct.mysymphony.R;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("NewOffer");
        FirebaseMessaging.getInstance().subscribeToTopic("NewsLink");
        FirebaseMessaging.getInstance().subscribeToTopic("NewApp");
        FirebaseMessaging.getInstance().subscribeToTopic("Male");

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        int loginStatus = prefs.getInt("loginStatus", 0);
        if(loginStatus==1){
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
        }
        imageView = findViewById(R.id.IV);
        Glide.with(this).load(R.drawable.symphoni).into(imageView);
    }

    public void startSignUpActivity(View view) {


        Intent myIntent = new Intent(getApplicationContext(), PhoneNumberVerification.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }

    public void startSignInActivity(View view) {


        Intent myIntent = new Intent(getApplicationContext(), SignInActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }

    @Override
    public void onBackPressed() {

        ActivityCompat.finishAffinity(this);

    }
}