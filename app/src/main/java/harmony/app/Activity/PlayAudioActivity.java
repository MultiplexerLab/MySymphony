package harmony.app.Activity;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import harmony.app.ModelClass.DataBaseData;
import harmony.app.ModelClass.MulloChar;
import harmony.app.ModelClass.Porashuna;
import harmony.app.R;
import harmony.app.helper.AppLogger;
import harmony.app.helper.DownloadAudio;
import harmony.app.helper.PlayerInService;
import harmony.app.helper.Utility;

public class PlayAudioActivity extends AppCompatActivity implements DownloadAudio.AsyncResponse {

    public static ImageButton btnPlay, btnStop, btnFastForward, btnRewind;
    public static TextView textViewSongTime;
    private Intent playerService;
    public static SeekBar seekBar;
    Utility utility;
    Porashuna data;
    DataBaseData dataBaseData;
    ImageView imageView;
    public harmony.app.helper.ProgressDialog progressDialog;
    Date currenTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        utility = new Utility();
        progressDialog = new harmony.app.helper.ProgressDialog(PlayAudioActivity.this);
        SharedPreferences.Editor editor = getSharedPreferences("audioData", MODE_PRIVATE).edit();
        data = (Porashuna) getIntent().getSerializableExtra("data");

        if (data != null) {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            currenTime = new Date();
            AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", data.getContentId() + "",
                    "SONG_VISITED", "Category: " + data.getContentCat(), "content");
            String imageUrl = data.getThumbnailImgUrl();
            editor.putString("imageUrl", imageUrl);
            editor.apply();
            if (imageUrl.isEmpty() || imageUrl == null) {

            } else {
                Glide.with(this).load(imageUrl).into((ImageView) findViewById(R.id.songimageView));
            }
            String contentTitle = data.getContentTitle();
            String contentCat = data.getContentCat();
            String contentDesc = "";
            String contentType = data.getContentType();
            String thumbNailImgUrl = data.getThumbnailImgUrl();
            editor.putString("songUrl", data.getContentUrl());
            editor.putString("songTitle", data.getContentTitle());
            editor.commit();

            dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, "free", data.getContentId());
            Gson gson = new Gson();
            String json = gson.toJson(dataBaseData);
            SharedPreferences.Editor editor1 = getSharedPreferences("tempData", MODE_PRIVATE).edit();
            editor1.putString("databaseData", json);
            editor1.commit();

            if (isMyServiceRunning(PlayerInService.class)) {
                Log.d("Servicerunning", "ServiceRunning");
                playerService = new Intent(PlayAudioActivity.this, PlayerInService.class);
                playerService.putExtra("songUrl", data.getContentUrl());
                playerService.putExtra("songTitle", data.getContentTitle());
                playerService.putExtra("songId", data.getContentId());
                stopService(playerService);
            }
            utility.cancelNotification();

        } else {
            SharedPreferences prefs = getSharedPreferences("imageUrlSp", MODE_PRIVATE);
            String restoredText = prefs.getString("imageUrl", null);
        }
        initView();
        SharedPreferences prefs = getSharedPreferences("audioData", MODE_PRIVATE);

        playerService = new Intent(PlayAudioActivity.this, PlayerInService.class);
        playerService.putExtra("songUrl", prefs.getString("songUrl", ""));
        playerService.putExtra("songTitle", prefs.getString("songTitle", ""));
        if (data != null) {
            playerService.putExtra("songId", data.getContentId());
        }else{
            playerService.putExtra("songId", 0);
        }
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
        btnPlay = findViewById(R.id.btnPlay);
        /*btnStop = (ImageButton) findViewById(R.id.btnStop);*/
        btnFastForward = findViewById(R.id.btnFastForward);
        btnRewind = findViewById(R.id.btnRewind);
        seekBar = findViewById(R.id.seekBar);
        textViewSongTime = findViewById(R.id.textViewSongTime);
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
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        try {
            if (PlayerInService.mp != null) {
                if (!PlayerInService.mp.isPlaying()) {
                    btnPlay.setBackgroundResource(R.drawable.player);
                } else {
                    dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    currenTime = new Date();
                    if (data != null) {
                        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", data.getContentId() + "",
                                "SONG_PLAYED", data.getContentTitle(), "content");
                    }
                    btnPlay.setBackgroundResource(R.drawable.pause);
                }
            }
        } catch (Exception e) {
            Log.e("AudioException", "" + e.getMessage() + e.getStackTrace() + e.getCause());
            if (data != null) {
                AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", data.getContentId() + "",
                        "SONG_PLAYING_FAILED", e.toString(), "content");
            }
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        SharedPreferences prefs = getSharedPreferences("audioData", MODE_PRIVATE);
        Utility.initNotification(prefs.getString("songTitle", ""), this);
        super.onStop();
    }

    public void downLoadAudio(View view) {
        Gson gson = new Gson();
        SharedPreferences preferences = getSharedPreferences("tempData", MODE_PRIVATE);
        String json = preferences.getString("databaseData", "");
        DataBaseData dataBaseData1 = gson.fromJson(json, DataBaseData.class);

        SharedPreferences prefs = getSharedPreferences("audioData", MODE_PRIVATE);
        //progressDialog.showProgressDialog("গান ডাউনলোড হচ্ছে");

        Toast.makeText(this, "গান ডাউনলোড হচ্ছে", Toast.LENGTH_LONG).show();
        DownloadAudio downloadAudio = new DownloadAudio();
        downloadAudio.downloadAudio(prefs.getString("songUrl", ""), prefs.getString("songTitle", ""), PlayAudioActivity.this, dataBaseData1);
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
