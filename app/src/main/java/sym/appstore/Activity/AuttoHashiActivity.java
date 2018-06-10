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

import sym.appstore.ModelClass.Auttohashi;
import sym.appstore.R;
import sym.appstore.RecyclerViewAdapter.RecyclerAdapterForAuttohashi;
import sym.appstore.helper.Endpoints;

public class AuttoHashiActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForAuttohashi;
    public RecyclerAdapterForAuttohashi adapterForAuttohashi;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Auttohashi> auttohashiArrayList;
    RequestQueue queue;
    sym.appstore.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autto_hashi);
        progressDialog = new sym.appstore.helper.ProgressDialog(this);
        toolbar = findViewById(R.id.toolbarlayoutinauttohashi);
        setSupportActionBar(toolbar);
        auttohashiArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(AuttoHashiActivity.this);
        progressDialog.showProgressDialog();
        loadDataFromVolley();
    }

    private void loadDataFromVolley() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.AUTTOHASI_GET_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray AuttoHashiContentArr = response.getJSONArray("contents");
                    setAuttoHashiContent(AuttoHashiContentArr);
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
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void setAuttoHashiContent(JSONArray auttoHashiContentArr) {
        if (auttoHashiContentArr.length() > 0) {
            for (int i = 0; i < auttoHashiContentArr.length(); i++) {
                try {
                    String contentTitle = auttoHashiContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = auttoHashiContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = auttoHashiContentArr.getJSONObject(i).getString("contentDescription");
                    int contentId = auttoHashiContentArr.getJSONObject(i).getInt("contentId");
                    String contentCat = auttoHashiContentArr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image = auttoHashiContentArr.getJSONObject(i).getString("thumbNail_image");

                    if (contentType.equals("video")) {
                        String contentUrl = auttoHashiContentArr.getJSONObject(i).getString("thumbNail_image");
                        auttohashiArrayList.add(new Auttohashi(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentId));
                    } else {
                        auttohashiArrayList.add(new Auttohashi(contentTitle, contentType, contentDescription, auttoHashiContentArr.getJSONObject(i).getString("contentUrl"), thumbNail_image, contentCat, contentId));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeRecyclerView();

        } else {
            Toast.makeText(AuttoHashiActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
        finish();
    }

    public void initializeRecyclerView() {
        recyclerViewForAuttohashi = findViewById(R.id.RV_Auttoahshi);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewForAuttohashi.setLayoutManager(mLayoutManager);
        recyclerViewForAuttohashi.setHasFixedSize(true);
        adapterForAuttohashi = new RecyclerAdapterForAuttohashi(this, auttohashiArrayList);
        recyclerViewForAuttohashi.setAdapter(adapterForAuttohashi);
    }
}