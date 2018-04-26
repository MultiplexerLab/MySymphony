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

import lct.mysymphony.ModelClass.Cartoon;
import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForCartoon;
import lct.mysymphony.helper.Endpoints;

public class CartoonActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;


    private RecyclerView recyclerViewForCartoon;
    public RecyclerAdapterForCartoon adapterForCartoon;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Cartoon> cartoonArrayList;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon);

        toolbar = findViewById(R.id.toolbarlayoutincartoon);
        setSupportActionBar(toolbar);

        cartoonArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(CartoonActivity.this);

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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();


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
                        cartoonArrayList.add(new Cartoon(contentTitle, contentType, contentDescription, contentUrl));
                    } else {
                        cartoonArrayList.add(new Cartoon(contentTitle, contentType, contentDescription, auttoHashiContentArr.getJSONObject(i).getString("contentUrl")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeRecyclerView();

        } else {
            Toast.makeText(CartoonActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeRecyclerView() {
        recyclerViewForCartoon = findViewById(R.id.RV_Cartoon);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewForCartoon.setLayoutManager(mLayoutManager);

        recyclerViewForCartoon.setHasFixedSize(true);
        adapterForCartoon= new RecyclerAdapterForCartoon(this, cartoonArrayList);

        recyclerViewForCartoon.setAdapter(adapterForCartoon);
    }
}
