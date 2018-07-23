package harmony.app.Activity;

import android.content.Context;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import harmony.app.Fragment.MusicFragment;
import harmony.app.Fragment.VideoFragment;
import harmony.app.Helper.Endpoints;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.ModelClass.Subscription;
import harmony.app.ModelClass.SubscriptionConfig;
import harmony.app.R;
import harmony.app.RecyclerViewAdapter.HorizontalAdapter;
import harmony.app.ViewpagerAdapter.ViewPagerAdapterMusic;
import harmony.app.Helper.AppLogger;

public class MusicVideoActivity extends AppCompatActivity {

    Date currenTime;
    DateFormat dateFormat;
    TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView horizontalListView;
    ArrayList<MusicVideo> audioList;
    ArrayList<MusicVideo> dataList;
    Map<String, ArrayList<MusicVideo>> dataMap;
    LinearLayout layout;
    TextView subCatName;
    RequestQueue queue;
    String subscriptionConfigStr;
    String subscriptionStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__video);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        loadSubscriptionConfig();
        tabLayout.setupWithViewPager(viewPager);

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swipeRefreshMusicVideo);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadSubscriptionConfig();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void setViewPagerData() {
        String videoListStr = getIntent().getStringExtra("videoList");
        String audioListStr = getIntent().getStringExtra("audioList");
        String categoryDefStr = getIntent().getStringExtra("categoryDefList");
        Bundle args = new Bundle();
        args.putString("audioListStr", audioListStr);
        args.putString("videoListStr", videoListStr);
        args.putString("categoryDefStr", categoryDefStr);
        args.putString("subscriptionConfigStr", subscriptionConfigStr);
        args.putString("subscriptionStr", subscriptionStr);

        Fragment musicFragment = new MusicFragment();
        Fragment videoFragment = new VideoFragment();



        musicFragment.setArguments(args);
        videoFragment.setArguments(args);

        ViewPagerAdapterMusic adapter = new ViewPagerAdapterMusic(getSupportFragmentManager());
        adapter.addFragment(musicFragment, "MUSIC");
        adapter.addFragment(videoFragment, "VIDEO");
        viewPager.setAdapter(adapter);
    }

    private void loadSubscriptionConfig() {
        queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_SUBSCRIPTION_CONFIG, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("loadSubscriptionConfig",response.toString());
                    subscriptionConfigStr = response.toString();
                    getUserSubsscription(MusicVideoActivity.this, "");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("loadSubscriptionConfig", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void getUserSubsscription(final Context context, String userId) {


        if(userId == null || userId == "") {
            userId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        queue = Volley.newRequestQueue(context);
        String url = Endpoints.GET_USER_SUBSCRIPTION+userId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("getUserSubsscription",response.toString());
                    subscriptionStr = response.toString();
                    setViewPagerData();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("getUserSubsscription", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
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
