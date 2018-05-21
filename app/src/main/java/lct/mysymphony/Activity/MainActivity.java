package lct.mysymphony.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import lct.mysymphony.R;
import lct.mysymphony.helper.DataHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*FirebaseMessaging.getInstance().subscribeToTopic("NewOffer");
        FirebaseMessaging.getInstance().subscribeToTopic("NewsLink");
        FirebaseMessaging.getInstance().subscribeToTopic("NewApp");
        FirebaseMessaging.getInstance().subscribeToTopic("Male");*/

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        int loginStatus = prefs.getInt("loginStatus", 0);
        if(loginStatus==1){

            String cameFromWhichActivity=getIntent().getStringExtra("cameFromWhichActivity");
            if (cameFromWhichActivity!=null)
            {
                if (cameFromWhichActivity.contains("HomePage"))
                {
                    ActivityCompat.finishAffinity(this);
                }
            }
            else
            {
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
            }
        }
        imageView = findViewById(R.id.IV);
        Glide.with(this).load(R.drawable.symphoni).into(imageView);
        dbHelper=new DataHelper(this);
    }

    public void startSignUpActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PhoneNumberVerification.class);
        this.startActivity(myIntent);
    }

    public void startSignInActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), SignInActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }
}