package lct.mysymphony.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lct.mysymphony.ModelClass.Porashuna;
import lct.mysymphony.R;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForPorashuna;
import lct.mysymphony.helper.Endpoints;
import lct.mysymphony.helper.ProgressDialog;

public class PorashunaActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerViewForPorashuna;
    public RecyclerAdapterForPorashuna adapterForPorashuna;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Porashuna> porashunaArrayList;
    RequestQueue queue;
    lct.mysymphony.helper.ProgressDialog progressDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porashuna);
        toolbar = findViewById(R.id.toolbarlayoutinporashuna);
        setSupportActionBar(toolbar);
        porashunaArrayList = new ArrayList<>();
        progressDialog=new ProgressDialog(PorashunaActivity.this);
        queue = Volley.newRequestQueue(PorashunaActivity.this);
        loadDataFromVolley();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
        finish();
    }

    private void loadDataFromVolley() {
        progressDialog.showProgressDialog();
        String tag = getIntent().getStringExtra("tag");

        SharedPreferences pref = getSharedPreferences("porashuna", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("porashuna", MODE_PRIVATE).edit();
        if(tag==null){
            Intent intent = new Intent(PorashunaActivity.this, HomePage.class);
            startActivity(intent);
        }else{
            editor.putString("currenttag", tag);
            editor.commit();
        }
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.CATEGORY_BASE_URL+tag,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.hideProgressDialog();
                            setporashunaiContent(response);
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

    private void setporashunaiContent(JSONArray porashunaContentArr) {

        if (porashunaContentArr.length() > 0) {
            for (int i = 0; i < porashunaContentArr.length(); i++) {
                try {
                    Log.i("DataPorashuna", porashunaContentArr.getJSONObject(i).toString());
                    String contentTitle = porashunaContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = porashunaContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = porashunaContentArr.getJSONObject(i).getString("contentDescription");
                    int contentId=porashunaContentArr.getJSONObject(i).getInt("id");
                    String contentCat=porashunaContentArr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image=porashunaContentArr.getJSONObject(i).getString("thumbNail_image");
                    String contentUrl = porashunaContentArr.getJSONObject(i).getString("contentUrl");

                    porashunaArrayList.add(new Porashuna(contentTitle, contentType, contentDescription,contentUrl ,thumbNail_image, contentCat,contentId));

                } catch (JSONException e) {
                    Log.i("ExceptionPorashuna", e.toString());
                }
            }
            initializeRecyclerView();

        } else {
            //Toast.makeText(PorashunaActivity.this, "No data found", Toast.LENGTH_SHORT).show();
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
