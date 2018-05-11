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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lct.mysymphony.R;
import lct.mysymphony.helper.Endpoints;

public class SignInActivity extends AppCompatActivity {

    EditText userName, password;
    lct.mysymphony.helper.ProgressDialog progressDialog;
    RequestQueue queue;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        progressDialog=new lct.mysymphony.helper.ProgressDialog(this);
        userName = findViewById(R.id.txtUserName);
        password = findViewById(R.id.txtpassword);
        queue = Volley.newRequestQueue(SignInActivity.this);
        checkPermissions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(myIntent);
    }

    public void startForgotPasswordActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
        this.startActivity(myIntent);
    }

    public void startHomePage(View view) {
        if (internetConnected()) {
           progressDialog.showProgressDialog();
            signInRequest();
        } else
            Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();

    }
    public void checkCredintial() {

        if (internetConnected()) {
            if (password.getText().toString().equals("12345") && userName.getText().toString().equals("admin")) {
                homePageStart();
            } else {
                Toast.makeText(this, "আপনার তথ্য সঠিক নয় ", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
    }

    public void homePageStart() {
        SharedPreferences.Editor editor;
        editor = getSharedPreferences("login", MODE_PRIVATE).edit();
        editor.putInt("loginStatus", 1);
        editor.apply();
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        this.startActivity(myIntent);
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

    public void signInRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.USER_SIGN_IN_POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseInSignIn ", response);
                       progressDialog.hideProgressDialog();
                        if (response.contains("SUCCESS")) {
                            if (internetConnected()) {
                                SharedPreferences.Editor editor = getSharedPreferences("phoneNumber", MODE_PRIVATE).edit();
                                editor.putString("phoneNo", userName.getText().toString());
                                editor.apply();
                                homePageStart();
                            } else
                                Toast.makeText(SignInActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();

                        } else
                        {
                            LinearLayout linearLayout=findViewById(R.id.shake);
                            Animation shake = AnimationUtils.loadAnimation(SignInActivity.this, R.anim.shake);
                            linearLayout.startAnimation(shake);
                            userName.getText().clear();
                            password.getText().clear();
                            Toast.makeText(SignInActivity.this, "আপনার তথ্য সঠিক নয় ", Toast.LENGTH_SHORT).show();
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
                params.put("userid", userName.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
