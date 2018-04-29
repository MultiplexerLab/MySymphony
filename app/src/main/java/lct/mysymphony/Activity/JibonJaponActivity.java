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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lct.mysymphony.ModelClass.JibonJapon;
import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForJibonJapon;
import lct.mysymphony.helper.Endpoints;

public class JibonJaponActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForJibonJapon;
    public RecyclerAdapterForJibonJapon adapterForJibonJapon;

    LinearLayoutManager linearLayoutManager;
    ArrayList<JibonJapon> jibonJaponActivityArrayList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jibon_japon);

        toolbar = findViewById(R.id.toolbarlayoutinjibonjapon);
        setSupportActionBar(toolbar);

        jibonJaponActivityArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(JibonJaponActivity.this);
        loadDataFromVolley();
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

    private void loadDataFromVolley() {

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, Endpoints.JAPITO_JIBON_GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        try {
                            String jsonFormattedString = response.replaceAll("\\\\", "");
                            jsonFormattedString = jsonFormattedString.substring(1, jsonFormattedString.length() - 1);
                            jsonFormattedString = jsonFormattedString.replaceAll("\"\\[", "[");
                            jsonFormattedString = jsonFormattedString.replaceAll("\\]\"", "]");
                            Log.d("jsonFormattedString", jsonFormattedString);
                            jsonObject = new JSONObject(jsonFormattedString);
                            Log.d("obj", jsonObject.toString());
                            setJibonJaponContent(jsonObject.getJSONArray("contents"));
                        } catch (JSONException e) {
                            Log.d("JSON Error", e.getMessage());
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

    private void setJibonJaponContent(JSONArray jsonJibonJaponContentArr) {

        if (jsonJibonJaponContentArr.length() > 0) {
            for (int i = 0; i < jsonJibonJaponContentArr.length(); i++) {
                try {
                    String contentTitle = jsonJibonJaponContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = jsonJibonJaponContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = jsonJibonJaponContentArr.getJSONObject(i).getString("contentDescription");
                    int contentId=jsonJibonJaponContentArr.getJSONObject(i).getInt("id");
                    String contentCat=jsonJibonJaponContentArr.getJSONObject(i).getString("contentCat");

                    if (contentType.equals("video")) {
                        String contentUrl = jsonJibonJaponContentArr.getJSONObject(i).getString("thumbNail_image");
                        jibonJaponActivityArrayList.add(new JibonJapon(contentTitle, contentType, contentDescription, contentUrl,contentCat,contentId));
                    } else {
                        jibonJaponActivityArrayList.add(new JibonJapon(contentTitle, contentType, contentDescription, jsonJibonJaponContentArr.getJSONObject(i).getString("contentUrl"),contentCat,contentId));
                    }

                } catch (JSONException e) {
                    Log.e("ErrorJapito", e.toString());
                }
            }
            initializeJibonJaponArraylist();
        } else {
            Toast.makeText(JibonJaponActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeJibonJaponArraylist() {
        recyclerViewForJibonJapon = findViewById(R.id.RV_JibonJapon);

        recyclerViewForJibonJapon.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJibonJapon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJibonJapon.attachToRecyclerView(recyclerViewForJibonJapon);
        recyclerViewForJibonJapon.setHasFixedSize(true);
        adapterForJibonJapon = new RecyclerAdapterForJibonJapon(this, jibonJaponActivityArrayList);
        recyclerViewForJibonJapon.setAdapter(adapterForJibonJapon);
    }
}
