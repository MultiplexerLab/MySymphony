package sym.appstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import sym.appstore.Fragment.CricketFragment;
import sym.appstore.Fragment.FootballFragment;
import sym.appstore.Fragment.OtherSportsFragment;
import sym.appstore.R;
import sym.appstore.ViewpagerAdapter.ViewPagerAdapter;
import sym.appstore.helper.ProgressDialog;

public class KheladhulaActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapterForSports;
    Context context;
    sym.appstore.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        toolbar = findViewById(R.id.toolbarlayoutinsports);
        setSupportActionBar(toolbar);
        context = KheladhulaActivity.this;
        progressDialog=new ProgressDialog(KheladhulaActivity.this);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPagerInSports);
        viewPagerAdapterForSports = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPagerAdapterForSports.addFragments(new CricketFragment(), "ক্রিকেট");
        viewPagerAdapterForSports.addFragments(new FootballFragment(), "ফুটবল");
        viewPagerAdapterForSports.addFragments(new OtherSportsFragment(), "অন্যান্য");
        viewPager.setAdapter(viewPagerAdapterForSports);
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

        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
            this.startActivity(myIntent);
        } else {
            getFragmentManager().popBackStack();
        }
    }
}