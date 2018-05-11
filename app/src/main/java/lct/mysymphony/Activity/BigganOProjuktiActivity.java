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

import lct.mysymphony.ModelClass.BigganOProjukti;
import lct.mysymphony.R;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForBigganOProjukti;
import lct.mysymphony.helper.Endpoints;
import lct.mysymphony.helper.ProgressDialog;

public class BigganOProjuktiActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForBigganOProjukti;
    public RecyclerAdapterForBigganOProjukti adapterForBigganOProjukti;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<BigganOProjukti> bigganOProjuktiArrayList;
    RequestQueue queue;
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biggan_oprojukti);
        progressDialog=new lct.mysymphony.helper.ProgressDialog(this);
        progressDialog=new ProgressDialog(this);
        toolbar = findViewById(R.id.toolbarlayoutinbigganOProjukti);
        setSupportActionBar(toolbar);
        bigganOProjuktiArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(BigganOProjuktiActivity.this);
        progressDialog.showProgressDialog();
        loadDataFromVolley();
    }

    private void loadDataFromVolley() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.BIGGAN_O_PROJUKTI_GET_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray pachMishaliContentArr = response.getJSONArray("contents");
                            setporashunaiContent(pachMishaliContentArr);
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

    private void setporashunaiContent(JSONArray pachMishaliContentArr) {

        if (pachMishaliContentArr.length() > 0) {
            for (int i = 0; i < pachMishaliContentArr.length(); i++) {
                try {
                    String contentTitle = pachMishaliContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = pachMishaliContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = pachMishaliContentArr.getJSONObject(i).getString("contentDescription");
                    int contentId=pachMishaliContentArr.getJSONObject(i).getInt("contentId");
                    String contentCat=pachMishaliContentArr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image=pachMishaliContentArr.getJSONObject(i).getString("thumbNail_image");

                    if (contentType.equals("video")) {
                        String contentUrl = pachMishaliContentArr.getJSONObject(i).getString("thumbNail_image");
                        bigganOProjuktiArrayList.add(new BigganOProjukti(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat,contentId));
                    } else {
                        bigganOProjuktiArrayList.add(new BigganOProjukti(contentTitle, contentType, contentDescription, pachMishaliContentArr.getJSONObject(i).getString("contentUrl"), thumbNail_image, contentCat,contentId));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeRecyclerView();

        } else {
            Toast.makeText(BigganOProjuktiActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }
    private void initializeRecyclerView() {

        recyclerViewForBigganOProjukti = findViewById(R.id.RV_BigganOProjukti);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewForBigganOProjukti.setLayoutManager(mLayoutManager);
        recyclerViewForBigganOProjukti.setHasFixedSize(true);
        adapterForBigganOProjukti= new RecyclerAdapterForBigganOProjukti(this, bigganOProjuktiArrayList);
        recyclerViewForBigganOProjukti.setAdapter(adapterForBigganOProjukti);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
        finish();
    }
}
