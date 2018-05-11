package lct.mysymphony.Activity;

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

import lct.mysymphony.ModelClass.PacMishali;
import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForPachMishali;
import lct.mysymphony.helper.Endpoints;
import lct.mysymphony.helper.ProgressDialog;

public class PachMishaliActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForPachMishali;
    public RecyclerAdapterForPachMishali adapterForPachMishali;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<PacMishali> pacMishaliArrayList;
    RequestQueue queue;
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pach_mishali);
        toolbar = findViewById(R.id.toolbarlayoutinpachMishali);
        setSupportActionBar(toolbar);
        pacMishaliArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(PachMishaliActivity.this);
        progressDialog = new ProgressDialog(PachMishaliActivity.this);
        loadDataFromVolley();
    }

    private void loadDataFromVolley() {
        progressDialog.showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.PACHMISHALI_GET_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hideProgressDialog();
                try {
                    JSONArray pachMishaliContentArr = response.getJSONArray("contents");
                    setporashunaiContent(pachMishaliContentArr);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.hideProgressDialog();
                }
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

    private void setporashunaiContent(JSONArray pachMishaliContentArr) {
        if (pachMishaliContentArr.length() > 0) {
            for (int i = 0; i < pachMishaliContentArr.length(); i++) {
                try {
                    String contentTitle = pachMishaliContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = pachMishaliContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = pachMishaliContentArr.getJSONObject(i).getString("contentDescription");
                    int contentId = pachMishaliContentArr.getJSONObject(i).getInt("contentId");
                    String contentCat = pachMishaliContentArr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image = pachMishaliContentArr.getJSONObject(i).getString("thumbNail_image");

                    if (contentType.equals("video")) {
                        String contentUrl = pachMishaliContentArr.getJSONObject(i).getString("thumbNail_image");
                        pacMishaliArrayList.add(new PacMishali(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentId));
                    } else {
                        pacMishaliArrayList.add(new PacMishali(contentTitle, contentType, contentDescription, pachMishaliContentArr.getJSONObject(i).getString("contentUrl"), thumbNail_image, contentCat, contentId));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeRecyclerView();

        } else {
            Toast.makeText(PachMishaliActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
    }

    public void initializeRecyclerView() {
        recyclerViewForPachMishali = findViewById(R.id.RV_PachMishali);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewForPachMishali.setLayoutManager(mLayoutManager);
        recyclerViewForPachMishali.setHasFixedSize(true);
        adapterForPachMishali = new RecyclerAdapterForPachMishali(this, pacMishaliArrayList);
        recyclerViewForPachMishali.setAdapter(adapterForPachMishali);
    }
}
