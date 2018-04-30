package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;

import com.android.volley.RequestQueue;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import lct.mysymphony.ModelClass.Notifications;
import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterNotification;

public class NotificationActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewNotification;
    public RecyclerAdapterNotification adapterNotification;
    RecyclerView.LayoutManager mLayoutManager;


    ArrayList<Notifications> notificationsArrayList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.toolbarlayoutNotification);
        setSupportActionBar(toolbar);

        notificationsArrayList = new ArrayList<>();

        notificationsArrayList.add(new Notifications("ট্র্যানজেক্শন সম্পন্ন হয়েছে", "আপনার ১০০ তম ট্রানজেকশন সম্পন্ন হয়েছে", "১০ মিনিট আগে"));
        notificationsArrayList.add(new Notifications("ট্র্যানজেক্শন সম্পন্ন হয়েছে", "আপনার ১০১ তম ট্রানজেকশন সম্পন্ন হয়েছে", "৬ মিনিট আগে"));
        notificationsArrayList.add(new Notifications("ট্র্যানজেক্শন সম্পন্ন হয়েছে", "আপনার ১০২ তম ট্রানজেকশন সম্পন্ন হয়েছে", "১ মিনিট আগে"));

        recyclerViewNotification = findViewById(R.id.RVNotification);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewNotification.setLayoutManager(mLayoutManager);
        recyclerViewNotification.setHasFixedSize(true);
        adapterNotification = new RecyclerAdapterNotification(NotificationActivity.this, notificationsArrayList);
        recyclerViewNotification.setAdapter(adapterNotification);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();


    }
}
