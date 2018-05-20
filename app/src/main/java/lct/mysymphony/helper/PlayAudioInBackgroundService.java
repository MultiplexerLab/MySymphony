package lct.mysymphony.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

public class PlayAudioInBackgroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    MediaPlayer mediaPlayer;
    ProgressDialog progressDialog;
    static Context context;

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null)
        {
            Log.d("serviceEntered","serviceEntered");
            String audioUrl=intent.getStringExtra("audioUrl");
            String message=intent.getStringExtra("message");
            context= (Context) intent.getSerializableExtra("context");
            if (context==null)
                Log.d("contextIsNull","contextIsNull");

            if (message!=null)
            {
                if (message.contains("stop"))
                {
                    if (mediaPlayer!=null)
                    {
                        mediaPlayer.stop();
                        mediaPlayer=null;
                    }
                    else
                        Log.d("mediaplayerNull","mediaplayerNull");
                }
            }
            else if (audioUrl==null) Log.d("audioUrlIsNull","null");
            else if (mediaPlayer==null)
            {
                mediaPlayer=new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioUrl);
                } catch (IOException e) {
                    Log.d("medaPlaerDtsourceExcptn",e.toString());
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    AudioAttributes aa = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
                    mediaPlayer.setAudioAttributes(aa);
                } else {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                ///try {
                    /*mediaPlayer.prepare();
                    mediaPlayer.start();*/
                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            /*sendMessageToActivity("success");*/
                        }
                    });
                    mediaPlayer.prepareAsync();
                } catch (IllegalStateException e) {
                    /*sendMessageToActivity("failed");*/
                    e.printStackTrace();
                }


                /*} catch (IOException e) {
                    e.printStackTrace();
                    Log.d("medipalyerException",e.toString());
                }*/
            }
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    /*private void sendMessageToActivity(String msg) {
        Intent intent = new Intent("intentKey");
// You can also include some extra data.
        intent.putExtra("key", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }*/
}
