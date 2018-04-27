package lct.mysymphony.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import lct.mysymphony.Fragment.newwallPaperFragment;
import lct.mysymphony.R;
import lct.mysymphony.ViewpagerAdapter.ViewPagerAdapter;

public class GalleryActivity extends AppCompatActivity {


    private android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapterForGallery;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        toolbar = findViewById(R.id.toolbarlayoutingallery);
        setSupportActionBar(toolbar);
        context = GalleryActivity.this;
        tabLayout = findViewById(R.id.tabLayoutinGalleryActivity);
        viewPager = findViewById(R.id.viewPagerInGallery);
        viewPagerAdapterForGallery = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapterForGallery.addFragments(new newwallPaperFragment(), "নতুন");
        viewPagerAdapterForGallery.addFragments(new newwallPaperFragment(), "জনপ্রিয়");
        viewPager.setAdapter(viewPagerAdapterForGallery);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    ///Toast.makeText(context, "Safe", Toast.LENGTH_SHORT).show();
                } else {
                    ///Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}
