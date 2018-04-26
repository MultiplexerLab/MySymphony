package lct.mysymphony.Activity;

import android.content.Intent;
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
    private DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("NewOffer");
        FirebaseMessaging.getInstance().subscribeToTopic("NewsLink");
        FirebaseMessaging.getInstance().subscribeToTopic("NewApp");
        FirebaseMessaging.getInstance().subscribeToTopic("Male");

        imageView = findViewById(R.id.IV);
        Glide.with(this).load(R.drawable.symphoni).into(imageView);

        dbHelper=new DataHelper(this);

        this.downloadImage();
    }

    public void downloadImage(){
        Log.i("downloadImage","Inside download image");
        BackTask bt=new BackTask();
        String imgURL="http://paimages.prothom-alo.com/contents/cache/images/640x360x1/uploads/media/2018/04/22/0055799d71293e70a941a8b7e8861b0b-5adc299d79982.jpg";
        if(!imgURL.trim().equals("")){
            bt.execute(imgURL);
        }

    }

    // AsynnTask to run download an image in background
    private class BackTask extends AsyncTask<String,Void,Bitmap>{
        TextView tv;
        protected void onPreExecute(){
            Log.i("Donwload","Downloading the image. Please wait...");
        }
        protected Bitmap doInBackground(String...params){
            Bitmap bitmap=null;
            try {
                // Download the image
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                // Decode image to get smaller image to save memory
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize=4;
                bitmap = BitmapFactory.decodeStream(is,null, options);
                is.close();
            }
            catch(IOException e){
                return null;
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result){
            //tv.setVisibility(TextView.GONE);
            // Insert bitmap to the database
            dbHelper.insertBitmap(result);
            //ImageView imgView=(ImageView)findViewById(R.id.imgview);
            // Read the first image from database and show it in ImageView
            //imgView.setImageBitmap(dbHelper.getBitmap(1));
            Log.i("downlaoded image", dbHelper.getBitmap(1).toString());
        }
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