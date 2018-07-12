package harmony.app.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import harmony.app.Fragment.MusicFragment;
import harmony.app.Fragment.VideoFragment;
import harmony.app.R;
import harmony.app.ViewpagerAdapter.ViewPagerAdapterMusic;
import harmony.app.Helper.AppLogger;

public class MusicVideoActivity extends AppCompatActivity {

    Date currenTime;
    DateFormat dateFormat;
    TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__video);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setViewPagerData();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setViewPagerData() {
        String videoListStr = getIntent().getStringExtra("videoList");
        String audioListStr = getIntent().getStringExtra("audioList");
        Bundle args = new Bundle();
        args.putString("audioListStr", audioListStr);
        args.putString("videoListStr", videoListStr);

        Fragment musicFragment = new MusicFragment();
        Fragment videoFragment = new VideoFragment();

        musicFragment.setArguments(args);
        videoFragment.setArguments(args);

        ViewPagerAdapterMusic adapter = new ViewPagerAdapterMusic(getSupportFragmentManager());
        adapter.addFragment(musicFragment, "MUSIC");
        adapter.addFragment(videoFragment, "VIDEO");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(MusicVideoActivity.this, dateFormat.format(currenTime), "N", "MusicVideo",
                "IN", "Entrance", "page");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(MusicVideoActivity.this, dateFormat.format(currenTime), "Y", "MusicVideo",
                "LEAVE", "Leave", "page");
    }
}
