package lct.mysymphony.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import lct.mysymphony.Fragment.Cricket_fragment;
import lct.mysymphony.Fragment.Football_fragment;
import lct.mysymphony.Fragment.other_sports_fragment;
import lct.mysymphony.R;
import lct.mysymphony.ViewpagerAdapter.ViewPagerAdapter;

public class KheladhulaActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapterForSports;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        toolbar = findViewById(R.id.toolbarlayoutinsports);
        setSupportActionBar(toolbar);

        context = KheladhulaActivity.this;

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPagerInSports);

        viewPagerAdapterForSports = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);

        viewPagerAdapterForSports.addFragments(new Cricket_fragment(), "ক্রিকেট");
        viewPagerAdapterForSports.addFragments(new Football_fragment(), "ফুটবল");
        viewPagerAdapterForSports.addFragments(new other_sports_fragment(), "অন্যান্য");


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
            //overridePendingTransition(R.anim.right_in, R.anim.right_out);
            finish();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }
}
