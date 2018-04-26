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

import lct.mysymphony.ModelClass.Porashuna;
import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForPorashuna;
import lct.mysymphony.helper.Endpoints;

public class PorashunaActivity extends AppCompatActivity {


    private android.support.v7.widget.Toolbar toolbar;


    private RecyclerView recyclerViewForPorashuna;
    public RecyclerAdapterForPorashuna adapterForPorashuna;
    RecyclerView.LayoutManager mLayoutManager;


    ArrayList<Porashuna> porashunaArrayList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porashuna);

        toolbar = findViewById(R.id.toolbarlayoutinporashuna);
        setSupportActionBar(toolbar);

        porashunaArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(PorashunaActivity.this);
        loadDataFromVolley();

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

    private void loadDataFromVolley() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.PORASHUNA_GET_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray porashunaContentArr = response.getJSONArray("contents");
                            setporashunaiContent(porashunaContentArr);
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

    private void setporashunaiContent(JSONArray porashunaContentArr) {

        if (porashunaContentArr.length() > 0) {
            for (int i = 0; i < porashunaContentArr.length(); i++) {
                try {
                    String contentTitle = porashunaContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = porashunaContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = porashunaContentArr.getJSONObject(i).getString("contentDescription");

                    if (contentType.equals("video")) {
                        String contentUrl = porashunaContentArr.getJSONObject(i).getString("thumbNail_image");
                        porashunaArrayList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl));
                    } else {
                        porashunaArrayList.add(new Porashuna(contentTitle, contentType, contentDescription, porashunaContentArr.getJSONObject(i).getString("contentUrl")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeRecyclerView();

        } else {
            Toast.makeText(PorashunaActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeRecyclerView() {
        recyclerViewForPorashuna = findViewById(R.id.RV_Porashuna);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewForPorashuna.setLayoutManager(mLayoutManager);

        recyclerViewForPorashuna.setHasFixedSize(true);
        adapterForPorashuna = new RecyclerAdapterForPorashuna(this, porashunaArrayList);

        recyclerViewForPorashuna.setAdapter(adapterForPorashuna);
    }
}
