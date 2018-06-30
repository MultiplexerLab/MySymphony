package sym.appstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sym.appstore.ModelClass.Icon;
import sym.appstore.R;
import sym.appstore.helper.AppLogger;
import sym.appstore.helper.Endpoints;

public class ContactActivity extends AppCompatActivity {

    RequestQueue queue;
    LinearLayout rootLayout;
    EditText eTname, eTemail, eTphone, eTcompany, eTcomment;
    Spinner spinner;
    Date currenTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        queue = Volley.newRequestQueue(this);

        rootLayout = findViewById(R.id.rootLayout);
        eTname = findViewById(R.id.your_name);
        eTemail = findViewById(R.id.your_email);
        eTphone = findViewById(R.id.your_phone);
        eTcomment = findViewById(R.id.your_message);
        spinner = findViewById(R.id.your_objective);

        if(internetConnected()){
            getSpinnerData();
        }else{
            showSnackBar();
        }
        /*ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("যোগাযোগের উদ্দেশ্য");
        arrayList.add("আমি এন্ড্রয়েড ডেভেলপার, আমার অ্যাপটি শেয়ার করতে চাই");
        arrayList.add("আমি পেমেন্ট সরবরাহকারী, পেমেন্ট চ্যানেল যুক্ত করতে চাই");
        arrayList.add("আমি আর্টিস্ট, গান অথবা মিউজিক ভিডিও শেয়ার করতে চাই");
        arrayList.add("বিজ্ঞাপন দিতে চাই");
        arrayList.add("অন্যান্য");
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                arrayList));*/
    }

    private void getSpinnerData() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.OBJECTIVES_GET_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for(int i=0; i<response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        arrayList.add(jsonObject.getString("purposeTitle"));
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(ContactActivity.this, android.R.layout.simple_spinner_dropdown_item,
                            arrayList));

                } catch (Exception e) {
                    Log.d("PurposeError", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(ContactActivity.this, "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void sendMessage(View view) {
        if(internetConnected()) {
            final String name = eTname.getText().toString();
            final String email = eTemail.getText().toString();
            final String phone = eTphone.getText().toString();
            final String objective = spinner.getSelectedItem().toString();
            final String comment = eTcomment.getText().toString();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || objective.equals("যোগাযোগের উদ্দেশ্য") || comment.isEmpty()) {
                Toast.makeText(this, "সকল ডাটা প্রদান করুন", Toast.LENGTH_SHORT).show();
            } else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.CONTACTUS_POST_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ResponseContact", response);
                        if (response.contains("SUCCESS")) {
                            Intent intent = new Intent(ContactActivity.this, HomePage.class);
                            startActivity(intent);
                            Toast.makeText(ContactActivity.this, "আপনার তথ্য সাবমিট হয়েছে, ধন্যবাদ!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ContactActivity.this, "ইন্টারনেটে সমস্যা!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ErrorVolley", error.toString());

                    }
                }) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name);
                        params.put("email", email);
                        params.put("phoneNumber", phone);
                        params.put("role", objective);
                        params.put("comments", comment);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        }else{
            showSnackBar();
        }
    }

    public void showSnackBar() {
        Snackbar snackbar = Snackbar
                .make(rootLayout, "ইন্টারনেটের সাথে সংযুক্ত নেই!", Snackbar.LENGTH_INDEFINITE)
                .setAction("সংযুক্ত করুন", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(settingsIntent, 9003);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private boolean internetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", "Contact",
                "IN", "Entrance", "page");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "Y", "Contact",
                "LEAVE", "Leave", "page");
    }
}
