package tanvir.multiplexer.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tanvir.multiplexer.Fragment.Cricket_fragment;
import tanvir.multiplexer.Fragment.Football_fragment;
import tanvir.multiplexer.Fragment.newwallPaperFragment;
import tanvir.multiplexer.Fragment.popularWallPaperFragment;
import tanvir.multiplexer.R;
import tanvir.multiplexer.ViewpagerAdapter.ViewPagerAdapterForGallery;
import tanvir.multiplexer.ViewpagerAdapter.ViewPagerAdapterForSports;

public class GalleryActivity extends AppCompatActivity {


    private android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapterForGallery viewPagerAdapterForGallery;

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

        viewPagerAdapterForGallery = new ViewPagerAdapterForGallery(getSupportFragmentManager());
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
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }
}
