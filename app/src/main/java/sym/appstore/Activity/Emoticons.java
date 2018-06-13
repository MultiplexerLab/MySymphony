package sym.appstore.Activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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

import sym.appstore.ModelClass.AppData;
import sym.appstore.ModelClass.SliderImage;
import sym.appstore.R;
import sym.appstore.RecyclerViewAdapter.AppListAdapter;
import sym.appstore.RecyclerViewAdapter.EmoticonsAdapter;
import sym.appstore.helper.AppLogger;
import sym.appstore.helper.DownloadImage;
import sym.appstore.helper.Endpoints;

public class Emoticons extends AppCompatActivity implements DownloadImage.AsyncResponse {

    GridView gridView;
    EmoticonsAdapter adapter;
    ArrayList<SliderImage> emoticonList;
    Date startTime;
    DateFormat dateFormat;
    RequestQueue queue;
    LinearLayout rootLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoticons);

        rootLayout = findViewById(R.id.rootLayout);
        gridView = findViewById(R.id.gridViewEmoticons);
        queue = Volley.newRequestQueue(this);

        if(internetConnected()){
            getEmoticons();
        }else{
            showSnackBar();
        }
        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (internetConnected()) {
                            if(snackbar!=null){
                                if(snackbar.isShown()) {
                                    snackbar.dismiss();
                                }
                            }
                            getEmoticons();
                            mySwipeRefreshLayout.setRefreshing(false);
                        } else {
                            showSnackBar();
                        }
                    }
                }
        );
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

    public void getEmoticons() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_EMOTICONS_INFO, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Respponse", response.toString());
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<SliderImage>>() {
                    }.getType();
                    emoticonList = gson.fromJson(response.toString(), type);
                    Log.i("SliderImages", emoticonList.toString());
                    adapter = new EmoticonsAdapter(Emoticons.this, emoticonList);
                    gridView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.d("exceptionLoadData4rmvly", e.toString());
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
        AppLogger.insertLogs(this, dateFormat.format(startTime), "Y", "Emoticons",
                "LEAVE", "Leave from Emoticons Page", "page");
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        startTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(startTime), "N", "Emoticons",
                "IN", "Entrance to Emoticons Page", "page");
    }

    @Override
    public void processFinish(String output) {
        //progressDialog.hideProgressDialog();
        Log.i("processFinished", "Emoticon");

        if (output.contains("complete")) {
            SharedPreferences preferences = getSharedPreferences("tempEmoticon", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = preferences.getString("emoticonObj", "");
            String imageUrl = preferences.getString("emoticonUrl", "");
            SliderImage sliderImage = gson.fromJson(json, SliderImage.class);

            Intent myIntent;
            myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            myIntent.putExtra("imageUrl", imageUrl);
            myIntent.putExtra("dataBaseData", sliderImage);
            myIntent.putExtra("cameFromWhichActivity", "Emoticons");
            this.startActivity(myIntent);
        } else
            Log.d("errorInprocessFinish", "errorInprocessFinish");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9003) {
            if (internetConnected()) {
                getEmoticons();
            } else {
                showSnackBar();
            }
        }
    }
}
