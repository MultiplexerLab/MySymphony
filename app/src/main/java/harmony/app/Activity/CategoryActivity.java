package harmony.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import harmony.app.ModelClass.CategoryContent;
import harmony.app.R;
import harmony.app.RecyclerViewAdapter.RecyclerAdapterForCategory;
import harmony.app.Helper.AppLogger;
import harmony.app.Helper.Endpoints;
import harmony.app.Helper.ProgressDialog;

public class CategoryActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForPorashuna;
    public RecyclerAdapterForCategory adapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CategoryContent> porashunaArrayList;
    RequestQueue queue;
    harmony.app.Helper.ProgressDialog progressDialog;
    Date currenTime;
    DateFormat dateFormat;
    String tag;
    LinearLayout rootLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porashuna);
        toolbar = findViewById(R.id.toolbarlayoutinporashuna);
        setSupportActionBar(toolbar);

        rootLayout = findViewById(R.id.rootLayout);
        porashunaArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(CategoryActivity.this);
        queue = Volley.newRequestQueue(CategoryActivity.this);
        if(internetConnected()){
            loadDataFromVolley();
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
                            loadDataFromVolley();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);*/
        finish();
    }

    private void loadDataFromVolley() {
        progressDialog.showProgressDialog();
        tag = getIntent().getStringExtra("tag");
        SharedPreferences.Editor editor = getSharedPreferences("tagCategory", MODE_PRIVATE).edit();
        if (tag == null) {
            SharedPreferences pref = getSharedPreferences("tagCategory", MODE_PRIVATE);
            tag = pref.getString("currenttag","");
            if(tag.isEmpty()){
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
            }
        } else {
            editor.putString("currenttag", tag);
            editor.commit();
        }

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.CATEGORY_BASE_URL + tag,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.hideProgressDialog();
                        setporashunaiContent(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hideProgressDialog();
                Log.e("Volley", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setporashunaiContent(JSONArray porashunaContentArr) {
        if (porashunaContentArr.length() > 0) {
            for (int i = 0; i < porashunaContentArr.length(); i++) {
                try {
                    Log.i("DataPorashuna", porashunaContentArr.getJSONObject(i).toString());
                    String contentTitle = porashunaContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = porashunaContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = porashunaContentArr.getJSONObject(i).getString("contentDescription");
                    int contentId = porashunaContentArr.getJSONObject(i).getInt("id");
                    String contentCat = porashunaContentArr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image = porashunaContentArr.getJSONObject(i).getString("thumbNail_image");
                    String contentUrl = porashunaContentArr.getJSONObject(i).getString("contentUrl");

                    porashunaArrayList.add(new CategoryContent(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentId));

                } catch (JSONException e) {
                    Log.i("ExceptionPorashuna", e.toString());
                }
            }
            initializeRecyclerView();

        } else {
            //Toast.makeText(CategoryActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(CategoryActivity.this, dateFormat.format(currenTime), "N", tag,
                "IN", "Entrance to " + tag + " page", "page");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppLogger.insertLogs(CategoryActivity.this, dateFormat.format(currenTime), "Y", tag,
                "LEAVE", "Leave from " + tag + " page", "page");
    }

    public void initializeRecyclerView() {
        recyclerViewForPorashuna = findViewById(R.id.RV_Porashuna);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewForPorashuna.setLayoutManager(mLayoutManager);
        recyclerViewForPorashuna.setHasFixedSize(true);
        adapter = new RecyclerAdapterForCategory(this, porashunaArrayList);
        recyclerViewForPorashuna.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9003) {
            if (internetConnected()) {
                loadDataFromVolley();
            } else {
                showSnackBar();
            }
        }
    }
}
