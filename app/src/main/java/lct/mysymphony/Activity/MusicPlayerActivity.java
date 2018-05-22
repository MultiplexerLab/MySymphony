package lct.mysymphony.Activity;


import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.ModelClass.MulloChar;
import lct.mysymphony.R;
import lct.mysymphony.helper.DownloadAudio;
import lct.mysymphony.helper.MusicController;
import lct.mysymphony.helper.MusicService;
import lct.mysymphony.helper.ProgressDialog;


public class MusicPlayerActivity extends Activity implements MediaPlayerControl, DownloadAudio.AsyncResponse {

    lct.mysymphony.helper.ProgressDialog progressDialog;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;
    MulloChar data;
    DataBaseData dataBaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        progressDialog = new ProgressDialog(this);
        data = (MulloChar) getIntent().getSerializableExtra("mullochar");
        if(data!=null) {
            String imageUrl = data.getImageUrl();
            Glide.with(this).load(imageUrl).into((ImageView) findViewById(R.id.coverPic));
            String contentTitle = data.getContentTile();
            String contentCat = data.getContentCat();
            String contentDesc = "";
            String contentType = data.getContentType();
            String thumbNailImgUrl = data.getThumbNailImgUrl();
            dataBaseData = new DataBaseData(contentTitle, contentCat, contentType, contentDesc, thumbNailImgUrl, "paid", data.getContentId());
        }
        setController();

        final Button playBtn = findViewById(R.id.playBtn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playBtn.performClick();
            }
        }, 1000);
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //start and bind the service when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null)
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null)
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.frameLayout));
        controller.setEnabled(true);
    }

    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    public void playSong(View view) {
        if(data!=null) {
            musicSrv.setTitle(data.getContentTile());
        }
        musicSrv.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    public void downLoadAudio(View view) {
        progressDialog.showProgressDialog("গান ডাউনলোড হচ্ছে");
        DownloadAudio downloadAudio = new DownloadAudio();
        downloadAudio.downloadAudio("http://jachaibd.com/files/eminem.mp3", MusicPlayerActivity.this, dataBaseData);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        stopService(playIntent);
        musicSrv=null;
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
}
