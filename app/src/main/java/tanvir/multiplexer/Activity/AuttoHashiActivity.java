package tanvir.multiplexer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import tanvir.multiplexer.ModelClass.Auttohashi;
import tanvir.multiplexer.ModelClass.JibonJapon;
import tanvir.multiplexer.R;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForAuttohashi;
import tanvir.multiplexer.helper.Endpoints;

public class AuttoHashiActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;


    private RecyclerView recyclerViewForAuttohashi;
    public RecyclerAdapterForAuttohashi adapterForAuttohashi;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Auttohashi> auttohashiArrayList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autto_hashi);

        toolbar = findViewById(R.id.toolbarlayoutinauttohashi);
        setSupportActionBar(toolbar);

        auttohashiArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(AuttoHashiActivity.this);

        loadDataFromVolley();

    }


    private void loadDataFromVolley() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.AUTTOHASI_GET_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray AuttoHashiContentArr = response.getJSONArray("contents");
                            setAuttoHashiContent(AuttoHashiContentArr);
                            //settop_contents(jsontop_contentsArr);
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

    private void setAuttoHashiContent(JSONArray auttoHashiContentArr) {


        if (auttoHashiContentArr.length() > 0) {
            for (int i = 0; i < auttoHashiContentArr.length(); i++) {
                try {
                    String contentTitle = auttoHashiContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = auttoHashiContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = auttoHashiContentArr.getJSONObject(i).getString("contentDescription");

                    if (contentType.equals("video")) {
                        String contentUrl = auttoHashiContentArr.getJSONObject(i).getString("thumbNail_image");
                        auttohashiArrayList.add(new Auttohashi(contentTitle, contentType, contentDescription, contentUrl));
                    } else {
                        auttohashiArrayList.add(new Auttohashi(contentTitle, contentType, contentDescription, auttoHashiContentArr.getJSONObject(i).getString("contentUrl")));
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
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
