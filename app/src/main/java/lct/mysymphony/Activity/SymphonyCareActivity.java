package lct.mysymphony.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import lct.mysymphony.Fragment.CustomerCareFragment;
import lct.mysymphony.Fragment.HandsetFitureFragment;
import lct.mysymphony.Fragment.ZogazogFragment;
import lct.mysymphony.R;
import lct.mysymphony.ViewpagerAdapter.ViewPagerAdapter;


public class SymphonyCareActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symphony_care);

        bottomNavigationView = findViewById(R.id.btmNavigationInSymphonyCareActivity);
        bottomNavigationView.getMenu().findItem(R.id.symphony_bottom_navigation).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home_bottom_navigation) {
                    Intent symphony = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(symphony);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
                return true;
            }
        });

        tabLayout = findViewById(R.id.tabLayoutInSyphonyCare);
        viewPager = findViewById(R.id.viewPagerInSymphonyCare);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPagerAdapter.addFragments(new HandsetFitureFragment(), "হ্যান্ডসেট ফিচার");
        viewPagerAdapter.addFragments(new CustomerCareFragment(), "কাস্টোমার কেয়ার");
        viewPagerAdapter.addFragments(new ZogazogFragment(), "যোগাযোগ");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
