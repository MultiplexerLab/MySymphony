package harmony.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import java.util.List;

import harmony.app.ModelClass.AppData;
import harmony.app.R;
import harmony.app.RecyclerViewAdapter.AppListAdapter;
import harmony.app.RecyclerViewAdapter.EmoticonsAdapter;
import harmony.app.Helper.AppLogger;
import harmony.app.Helper.DownloadApk;
import harmony.app.Helper.Endpoints;
import harmony.app.Helper.ProgressDialog;

public class AppList extends AppCompatActivity implements DownloadApk.AsyncResponse{

    ListView listView;
    AppListAdapter adapter;
    ArrayList<AppData> appList;
    Date startTime;
    DateFormat dateFormat;
    RequestQueue queue;
    Snackbar snackbar;
    LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        queue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.listViewApps);
        rootLayout = findViewById(R.id.rootLayoutAppList);
        appList = new ArrayList<>();
        /*String jsonData = getIntent().getStringExtra("appList");
        Gson gson = new Gson();
        Type type = new TypeToken<List<AppData>>(){}.getType();
        appList = gson.fromJson(jsonData, type);*/
        if(internetConnected()){
            getAppList();
        }else{
            showSnackBar();
        }
    }

    public void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, "ইন্টারনেটের সাথে সংযুক্ত নেই!", Snackbar.LENGTH_INDEFINITE)
                .setAction("সংযুক্ত করুন", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(settingsIntent, 9003);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private boolean internetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void getAppList() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_APPS_INFO, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Response", response.toString());
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<AppData>>() {
                    }.getType();
                    appList = gson.fromJson(response.toString(), type);
                    Log.i("Apps", appList.get(0).toString());
                    adapter = new AppListAdapter(AppList.this, appList);
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.d("AppException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                //Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }
    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(this, dateFormat.format(startTime), "Y", "AppList",
                "LEAVE", "Leave from Applist", "page");
    }

    @Override
    protected void onResume(){
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        startTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(startTime), "N", "AppList",
                "IN", "Entrance to Applist", "page");
    }

    @Override
    public void processFinish(String output) {

    }
}
