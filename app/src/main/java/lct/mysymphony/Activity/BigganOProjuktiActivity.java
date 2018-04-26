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
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForBigganOProjukti;
import lct.mysymphony.helper.Endpoints;

public class BigganOProjuktiActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForBigganOProjukti;
    public RecyclerAdapterForBigganOProjukti adapterForBigganOProjukti;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<BigganOProjukti> bigganOProjuktiArrayList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biggan_oprojukti);

        toolbar = findViewById(R.id.toolbarlayoutinbigganOProjukti);
        setSupportActionBar(toolbar);
        bigganOProjuktiArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(BigganOProjuktiActivity.this);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

                    if (contentType.equals("video")) {
                        String contentUrl = pachMishaliContentArr.getJSONObject(i).getString("thumbNail_image");
                        bigganOProjuktiArrayList.add(new BigganOProjukti(contentTitle, contentType, contentDescription, contentUrl));
                    } else {
                        bigganOProjuktiArrayList.add(new BigganOProjukti(contentTitle, contentType, contentDescription, pachMishaliContentArr.getJSONObject(i).getString("contentUrl")));
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
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}
