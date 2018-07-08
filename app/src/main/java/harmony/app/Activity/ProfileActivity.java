package harmony.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import harmony.app.Fragment.MyInfoFragmentInProfileActivity;
import harmony.app.Fragment.MyItemFragmentInProfileActivity;
import harmony.app.Fragment.PaymentListFragmentInProfileActivity;
import harmony.app.R;
import harmony.app.ViewpagerAdapter.ViewPagerAdapter;
import harmony.app.helper.AppLogger;

public class ProfileActivity extends AppCompatActivity implements MyInfoFragmentInProfileActivity.ActivityCommunicator {

    private android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapterForSports;
    ImageView logoutIcon;
    TextView nameTVInActivityUserProfile;
    TextView nameTV;
    String cameFromWhichActivity;
    Date currenTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbarlayoutinprofile);
        setSupportActionBar(toolbar);
        logoutIcon = toolbar.findViewById(R.id.logoutIcon);
        nameTVInActivityUserProfile = findViewById(R.id.nameTVInActivityUserProfile);
        nameTV=findViewById(R.id.nameTVInActivityUserProfile);

        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog;
                final View logoutDialogView;
                AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater layoutInflater= (LayoutInflater) ProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                logoutDialogView=layoutInflater.inflate(R.layout.logout_confirmation,null);
                builder.setView(logoutDialogView);
                alertDialog=builder.create();
                alertDialog.show();
                Button yesBTN=logoutDialogView.findViewById(R.id.yesBTN);
                Button noBTN=logoutDialogView.findViewById(R.id.noBTN);
                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("yesBtn","Enter");
                        SharedPreferences.Editor editor;
                        editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                        editor.putInt("loginStatus", 0);
                        editor.apply();
                        Intent intent = new Intent(ProfileActivity.this, HomePage.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                });
                noBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("noBtn","Enter");
                        alertDialog.dismiss();
                    }
                });
            }
        });

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        String userName = prefs.getString("username", "");
        nameTVInActivityUserProfile.setText(userName);
        tabLayout = findViewById(R.id.tabLayoutProfile);
        viewPager = findViewById(R.id.viewPagerProfile);
        viewPagerAdapterForSports = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPagerAdapterForSports.addFragments(new MyItemFragmentInProfileActivity(), "মাই আইটেম");
        viewPagerAdapterForSports.addFragments(new PaymentListFragmentInProfileActivity(), "পেমেন্ট লিস্ট");
        //viewPagerAdapterForSports.addFragments(new MyInfoFragmentInProfileActivity(), "মাই ইনফো");
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
        finish();
    }

    @Override
    public void passDataToActivity(String someValue) {
        Log.d("enteractivity",someValue);
        nameTV.setText(someValue);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", "DownloadedItems",
                "IN", "Entrance", "page");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "Y", "DownloadedItems",
                "LEAVE", "Leave", "page");
    }
}
