package tanvir.multiplexer.Activity;

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

import tanvir.multiplexer.ModelClass.JibonJapon;
import tanvir.multiplexer.R;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForJibonJapon;
import tanvir.multiplexer.helper.Endpoints;

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
        loadDataFromVolley();    }

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.JIBON_JAPON_GET_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonJibonJaponContentArr = response.getJSONArray("contents");

                            setJibonJaponContent(jsonJibonJaponContentArr);


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

    private void setJibonJaponContent(JSONArray jsonJibonJaponContentArr) {

        if (jsonJibonJaponContentArr.length() > 0) {
            for (int i = 0; i < jsonJibonJaponContentArr.length(); i++) {
                try {
                    String contentTitle = jsonJibonJaponContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = jsonJibonJaponContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = jsonJibonJaponContentArr.getJSONObject(i).getString("contentDescription");

                    if (contentType.equals("video")) {
                        String contentUrl = jsonJibonJaponContentArr.getJSONObject(i).getString("thumbNail_image");
                       jibonJaponActivityArrayList.add(new JibonJapon(contentTitle, contentType, contentDescription, contentUrl));
                    } else {
                        jibonJaponActivityArrayList.add(new JibonJapon( contentTitle,contentType,contentDescription, jsonJibonJaponContentArr.getJSONObject(i).getString("contentUrl")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeJibonJaponArraylist();
        } else {
            Toast.makeText(JibonJaponActivity.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeJibonJaponArraylist()
    {
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
