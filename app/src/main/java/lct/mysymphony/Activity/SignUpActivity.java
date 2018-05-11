package lct.mysymphony.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lct.mysymphony.R;
import lct.mysymphony.helper.Endpoints;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private android.support.v7.widget.Toolbar toolbar;
    RequestQueue queue;
    EditText DOB;
    String DOBTV;
    String phoneNumber = "";
    private EditText txtpassword, txtUserName;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,};
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = findViewById(R.id.toolbarlayoutinmainactivity);
        setSupportActionBar(toolbar);
        DOB = findViewById(R.id.txtDOB);
        txtpassword = findViewById(R.id.txtpassword);
        txtUserName = findViewById(R.id.txtUserName);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        queue = Volley.newRequestQueue(SignUpActivity.this);
        checkPermissions();
        progressDialog=new lct.mysymphony.helper.ProgressDialog(this);
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(myIntent);;
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

    public void signUpRequest(View view) {
        if (internetConnected()) {
            progressDialog.showProgressDialog();
            signInRequest();
        } else {
            Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DOBTV = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        DOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }

    public void showDatePickerDialog(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                SignUpActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void signUpRequest() {
        String url = "http://bot.sharedtoday.com:9500/ws/commonUpdateForArrayJSON?tbl=Partner&keyname=partnerId&id=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ResponseSignUp", response);
                        if(response.contains("SUCCESS")){
                            SharedPreferences.Editor editor;
                            editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                            editor.putString("username", txtUserName.getText().toString());
                            editor.apply();
                            sendUpdatedPasswordToserver();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorInSignUp", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("partnerType", "Retailer");
                params.put("defaultPassword", txtpassword.getText().toString());
                params.put("partnerStatus", "Signup complete");
                params.put("applicantBirthDate", DOBTV);
                params.put("partnerId", phoneNumber);
                params.put("partnerName", txtUserName.getText().toString());
                params.put("orgId", "maxis");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void signInRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.USER_SIGN_IN_POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseInSignIn", response);

                        if (response.contains("SUCCESS")) {
                            if (internetConnected()) {
                                signUpRequest();
                            } else
                            {
                                progressDialog.hideProgressDialog();
                                Toast.makeText(SignUpActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                            }

                        } else
                        {
                            progressDialog.hideProgressDialog();
                            Toast.makeText(SignUpActivity.this, "আপনার তথ্য সঠিক নয় ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInSignIn", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", phoneNumber);
                params.put("password", "01717");
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void sendUpdatedPasswordToserver() {
        String url = Endpoints.UPDATE_DEFAULT_PASSWORD_POST_URL+txtpassword.getText().toString()+"&userid="+phoneNumber;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseInSetUpdtedPass", response);

                        if(response.contains("SUCCESS")){
                            progressDialog.hideProgressDialog();
                            SharedPreferences.Editor editor;
                            editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                            editor.putInt("loginStatus", 1);
                            editor.apply();
                            Toast.makeText(SignUpActivity.this, "আপনার সাইন আপ সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, HomePage.class);
                            startActivity(intent);
                        }
                        else
                        {
                            progressDialog.hideProgressDialog();
                            Toast.makeText(SignUpActivity.this, "আপনার সাইন আপ সম্পন্ন হয়নি\n" +
                                    "আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInSignIn", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
