package lct.mysymphony.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.MulloChar;
import lct.mysymphony.R;
import lct.mysymphony.helper.DownloadAudio;
import lct.mysymphony.helper.PlayerInService;
import lct.mysymphony.helper.Utility;
import paymentgateway.lct.lctpaymentgateway.ProgressDialog;

public class PlayAudioActivity extends AppCompatActivity implements DownloadAudio.AsyncResponse {

    public static ImageButton btnPlay, btnStop;
    public static TextView textViewSongTime;
    private Intent playerService;
    public static SeekBar seekBar;
    Utility utility;
    MulloChar data;
    DataBaseData dataBaseData;
    ImageView imageView;
    lct.mysymphony.helper.ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        utility = new Utility();
        progressDialog = new lct.mysymphony.helper.ProgressDialog(PlayAudioActivity.this);
        SharedPreferences.Editor editor = getSharedPreferences("imageUrlSp", MODE_PRIVATE).edit();
        data = (MulloChar) getIntent().getSerializableExtra("mullochar");
        if (data != null) {
            String imageUrl = data.getImageUrl();
            editor.putString("imageUrl", imageUrl);
            editor.apply();
            Glide.with(this).load(imageUrl).into((ImageView) findViewById(R.id.songimageView));
            String contentTitle = data.getContentTile();
            String contentCat = data.getContentCat();
            String contentDesc = "";
            String contentType = data.getContentType();
            String thumbNailImgUrl = data.getThumbNailImgUrl();
           if (isMyServiceRunning(PlayerInService.class))
           {
               Log.d("Servicerunning","ServiceRunning");
               playerService = new Intent(PlayAudioActivity.this, PlayerInService.class);
               stopService(playerService);
           }
            utility.cancelNotification();
            dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, "paid", data.getContentId());
        } else {
            SharedPreferences prefs = getSharedPreferences("imageUrlSp", MODE_PRIVATE);
            String restoredText = prefs.getString("imageUrl", null);
            if (restoredText != null) {
                Glide.with(this).load(restoredText).into((ImageView) findViewById(R.id.songimageView));
            }
        }
        initView();
        playerService = new Intent(PlayAudioActivity.this, PlayerInService.class);
        startService(playerService);

    }

    @Override
    public void onBackPressed() {
        try {
            if (!PlayerInService.mp.isPlaying()) {
                /*btnPlay.setBackgroundResource(R.drawable.player);*/
                utility.cancelNotification();

            } else {
                /*btnPlay.setBackgroundResource(R.drawable.pause);*/

            }
        } catch (Exception e) {
            Log.e("ExceptionBack", "" + e.getMessage() + e.getStackTrace() + e.getCause());
        }

        super.onBackPressed();
    }

    private void initView() {
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnStop = (ImageButton) findViewById(R.id.btnStop);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textViewSongTime = (TextView) findViewById(R.id.textViewSongTime);
    }

    @Override
    protected void onDestroy() {

        if (!PlayerInService.mp.isPlaying()) {
            PlayerInService.mp.stop();
            stopService(playerService);
            utility.cancelNotification();
        } else {
            btnPlay.setBackgroundResource(R.drawable.pause);
            /*utility.cancelNotification();*/
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        try {
            if (!PlayerInService.mp.isPlaying()) {
                btnPlay.setBackgroundResource(R.drawable.player);

            } else {
                btnPlay.setBackgroundResource(R.drawable.pause);
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage() + e.getStackTrace() + e.getCause());
        }

        super.onResume();
    }

    public void downLoadAudio(View view) {
        progressDialog.showProgressDialog("গান ডাউনলোড হচ্ছে");
        DownloadAudio downloadAudio = new DownloadAudio();
        downloadAudio.downloadAudio("http://jachaibd.com/files/eminem.mp3", PlayAudioActivity.this, dataBaseData);
    }

    @Override
    public void processFinish(String output) {
        progressDialog.hideProgressDialog();
        Log.d("processFinished", "processFinished");
        Log.d("outputAudio", output);
        if (output.contains("complete")) {
            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            myIntent.putExtra("cameFromWhichActivity", "MusicPlayerActivity");
            this.startActivity(myIntent);
            Log.d("onProcessFinishedComplt", "onProcessFinishedComplt");
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}