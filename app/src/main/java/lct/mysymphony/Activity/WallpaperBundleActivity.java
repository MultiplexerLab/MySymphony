package lct.mysymphony.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import lct.mysymphony.R;

public class WallpaperBundleActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    TextView strikethroughTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_bundle);

        toolbar = findViewById(R.id.toolbarlayoutinwallpaperBundle);
        setSupportActionBar(toolbar);

        strikethroughTextView=findViewById(R.id.strikeThroughInWallPaperBundle);
        strikethroughTextView.setPaintFlags(strikethroughTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent myIntent = new Intent(getApplicationContext(), KheladhulaActivity.class);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();

    }
}
