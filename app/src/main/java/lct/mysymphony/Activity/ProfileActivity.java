package lct.mysymphony.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import lct.mysymphony.Fragment.MyInfoFragmentInProfileActivity;
import lct.mysymphony.Fragment.MyItemFragmentInProfileActivity;
import lct.mysymphony.Fragment.PaymentListFragmentInProfileActivity;
import lct.mysymphony.R;
import lct.mysymphony.ViewpagerAdapter.ViewPagerAdapterForSports;

public class ProfileActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapterForSports viewPagerAdapterForSports;
    ImageView logoutIcon;
    TextView nameTVInActivityUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbarlayoutinprofile);
        setSupportActionBar(toolbar);

        logoutIcon = toolbar.findViewById(R.id.logoutIcon);
        nameTVInActivityUserProfile = findViewById(R.id.nameTVInActivityUserProfile);

        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor;
                editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                editor.putInt("loginStatus", 0);
                editor.apply();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        String userName = prefs.getString("username", "");
        nameTVInActivityUserProfile.setText(userName);

        tabLayout = findViewById(R.id.tabLayoutProfile);
        viewPager = findViewById(R.id.viewPagerProfile);

        viewPagerAdapterForSports = new ViewPagerAdapterForSports(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);

        viewPagerAdapterForSports.addFragments(new PaymentListFragmentInProfileActivity(), "পেমেন্ট লিস্ট");
        viewPagerAdapterForSports.addFragments(new MyItemFragmentInProfileActivity(), "মাই আইটেম");
        viewPagerAdapterForSports.addFragments(new MyInfoFragmentInProfileActivity(), "মাই ইনফো");


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
        super.onBackPressed();

        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }
}
