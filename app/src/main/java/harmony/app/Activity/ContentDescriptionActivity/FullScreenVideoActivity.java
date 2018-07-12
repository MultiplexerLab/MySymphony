package harmony.app.Activity.ContentDescriptionActivity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import harmony.app.R;
import harmony.app.Helper.FullScreenMediaController;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_videoview);

        videoView = findViewById(R.id.videoView);

        String fullScreen =  getIntent().getStringExtra("fullScreenInd");
        if("y".equals(fullScreen)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //getSupportActionBar().hide();
        }

        videoView.setVideoURI(Uri.parse("https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4"));

        mediaController = new FullScreenMediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.start();
    }
}