package tanvir.multiplexer.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tanvir.multiplexer.R;
import tanvir.multiplexer.helper.Endpoints;

public class SignInActivity extends AppCompatActivity {


    EditText userName, password;

    RequestQueue queue;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userName = findViewById(R.id.txtUserName);
        password = findViewById(R.id.txtpassword);

        queue = Volley.newRequestQueue(SignInActivity.this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }

    public void startForgotPasswordActivity(View view) {

        Intent myIntent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }

    public void startHomePage(View view) {
        ///checkCredintial();
        //homePageStart();
        signInRequest();

    }

//    public void checkCredintial() {
//
//        if (internetConnected()) {
//            if (password.getText().toString().equals("12345") && userName.getText().toString().equals("admin")) {
//                homePageStart();
//            } else {
//                Toast.makeText(this, "আপনার তথ্য সঠিক নয় ", Toast.LENGTH_SHORT).show();
//            }
//        } else
//            Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
//
//
//    }

    public void homePageStart() {
        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
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

                        /// Toast.makeText(SignInActivity.this, "response : "+response, Toast.LENGTH_LONG).show();
                        Log.d("responseInSignIn ",response);

                        if (response.contains("SUCCESS")) {
                            if (internetConnected()) {
                                homePageStart();


                            } else
                                Toast.makeText(SignInActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(SignInActivity.this, "আপনার তথ্য সঠিক নয় ", Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInSignIn", error.toString());

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
