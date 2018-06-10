package sym.appstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sym.appstore.ModelClass.GoromKhobor;
import sym.appstore.R;
import sym.appstore.RecyclerViewAdapter.RecyclerAdapterForGoromKhobor;
import sym.appstore.helper.Endpoints;

public class GoromKhoborActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForGoromKhobor;
    public RecyclerAdapterForGoromKhobor adapterForGoromKhobor;
    ArrayList<GoromKhobor> goromKhoborArrayList;
    RecyclerView.LayoutManager mLayoutManager;
    RequestQueue queue;
    sym.appstore.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorom_khobor_activity);
        toolbar = findViewById(R.id.toolbarlayoutingoromkhobor);
        setSupportActionBar(toolbar);
        goromKhoborArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(GoromKhoborActivity.this);
        progressDialog=new sym.appstore.helper.ProgressDialog(this);
        progressDialog.showProgressDialog();
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
                           progressDialog.hideProgressDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideProgressDialog();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();            }
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
                    progressDialog.hideProgressDialog();
                }
            }
            initializeGoromKhoborRecyclerView();

        } else {
            Toast.makeText(GoromKhoborActivity.this, "No data found", Toast.LENGTH_SHORT).show();

        }
    }

    private void initializeGoromKhoborRecyclerView() {
        recyclerViewForGoromKhobor = findViewById(R.id.RV_GoromKhobor);
        mLayoutManager = new LinearLayoutManager(GoromKhoborActivity.this);
        recyclerViewForGoromKhobor.setLayoutManager(mLayoutManager);
        recyclerViewForGoromKhobor.setHasFixedSize(true);
        adapterForGoromKhobor = new RecyclerAdapterForGoromKhobor(this,goromKhoborArrayList);
        recyclerViewForGoromKhobor.setAdapter(adapterForGoromKhobor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
    }
}
