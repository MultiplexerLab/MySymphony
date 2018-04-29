package lct.mysymphony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lct.mysymphony.ModelClass.GoromKhobor;
import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForGoromKhobor;
import lct.mysymphony.helper.Endpoints;

public class GoromKhoborActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForGoromKhobor;
    public RecyclerAdapterForGoromKhobor adapterForGoromKhobor;
    ArrayList<GoromKhobor> goromKhoborArrayList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorom_khobor_activity);

        toolbar = findViewById(R.id.toolbarlayoutingoromkhobor);
        setSupportActionBar(toolbar);
        goromKhoborArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(GoromKhoborActivity.this);
        loadDataFromVolley();
    }

    private void loadDataFromVolley() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.HOT_NEWS_GET_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonHotNewsContentArr = response.getJSONArray("contents");
                            setGoromKhoborContent(jsonHotNewsContentArr);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getApplicationContext(), "সার্ভারে সমস্যা দয়া করে আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void setGoromKhoborContent(JSONArray jsonHotNewsContentArr) {

        if (jsonHotNewsContentArr.length() > 0) {
            for (int i = 0; i < jsonHotNewsContentArr.length(); i++) {
                try {
                    String imageUrl = jsonHotNewsContentArr.getJSONObject(i).getString("imageUrl");
                    String contentTitle = jsonHotNewsContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = jsonHotNewsContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = jsonHotNewsContentArr.getJSONObject(i).getString("contentDescription");
                    String publishedAt = jsonHotNewsContentArr.getJSONObject(i).getString("publishedAt");

                    goromKhoborArrayList.add(new GoromKhobor(contentTitle, contentDescription, contentType, imageUrl, publishedAt));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeGoromKhoborRecyclerView();

        } else {
            Toast.makeText(GoromKhoborActivity.this, "No data found", Toast.LENGTH_SHORT).show();

        }
    }

    private void initializeGoromKhoborRecyclerView() {

        recyclerViewForGoromKhobor = findViewById(R.id.RV_GoromKhobor);

        recyclerViewForGoromKhobor.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartGoromKhobor = new GravitySnapHelper(Gravity.START);
        snapHelperStartGoromKhobor.attachToRecyclerView(recyclerViewForGoromKhobor);
        recyclerViewForGoromKhobor.setHasFixedSize(true);
        adapterForGoromKhobor = new RecyclerAdapterForGoromKhobor(this,goromKhoborArrayList);
        recyclerViewForGoromKhobor.setAdapter(adapterForGoromKhobor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in , R.anim.right_out);
        finish();
    }
}
