package lct.mysymphony.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import lct.mysymphony.R;
import lct.mysymphony.helper.Endpoints;

public class SignInActivity extends AppCompatActivity {

    EditText userName, password;
    TextView forgotPassTitle;
    lct.mysymphony.helper.ProgressDialog progressDialog;
    RequestQueue queue;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        forgotPassTitle = findViewById(R.id.forgotPassTitle);
        progressDialog = new lct.mysymphony.helper.ProgressDialog(this);
        userName = findViewById(R.id.txtUserName);
        password = findViewById(R.id.txtpassword);
        forgotPassTitle.setPaintFlags(forgotPassTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
            String name = userName.getText().toString();
            String passwordStr = password.getText().toString();
            if (name.isEmpty() || passwordStr.isEmpty()) {
                if (progressDialog.getAlertDialog() != null) progressDialog.hideProgressDialog();
                Toast.makeText(this, "সবগুলো তথ্য প্রদান করুন", Toast.LENGTH_SHORT).show();
            } else {
                signInRequest(name, passwordStr);
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

    public void signInRequest(final String name, final String passwordStr) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.USER_SIGN_IN_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseInSignIn ", response);
                progressDialog.hideProgressDialog();
                if (response.contains("SUCCESS")) {
                    if (internetConnected()) {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://bot.sharedtoday.com:9500/getSessionInfo", new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("LoginData", response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LoginData", error.toString());
                            }
                        });
                        queue.add(jsonObjectRequest);
                        SharedPreferences.Editor editor = getSharedPreferences("phoneNumber", MODE_PRIVATE).edit();
                        editor.putString("phoneNo", userName.getText().toString());
                        editor.apply();
                        homePageStart();
                    } else
                        Toast.makeText(SignInActivity.this, "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();

                } else {
                    LinearLayout linearLayout = findViewById(R.id.shake);
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
                params.put("userid", name);
                params.put("password", passwordStr);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void guestLogin(View view) {
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        String userName = prefs.getString("username", "");
        if (userName.equals("Guest")) {
            Intent intent = new Intent(SignInActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        } else {
            if (internetConnected()) {
                progressDialog.showProgressDialog();
                String androidId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                phoneNumberEntry(androidId);
                //signInBeforeSignupRequest("Guest", "default", androidId);
                //signInRequest("Guest", "default");
            } else
                Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();

        }
    }

    public void phoneNumberEntry(final String deviceId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.PHONE_NUMBER_ENTRY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        String result = "";
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() == 0)
                                Toast.makeText(SignInActivity.this, "jsonArray blank", Toast.LENGTH_SHORT).show();
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject postInfo = jsonArray.getJSONObject(i);
                                    result = postInfo.getString("msgType");
                                    Log.d("resultSignUp ", result);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.contains("SUCCESS")) {
                            signInBeforeSignupRequest("Guest", "default", deviceId);
                        } else {
                            Intent intent = new Intent(SignInActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorInSignUp", error.toString());

            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("partnerType", "Retailer");
                params.put("referralCode", "");
                params.put("defaultPassword", "01717");
                params.put("registeredName", "farhan");
                params.put("shopAddress", "");
                params.put("partnerFirstName", "");
                params.put("partnerLastName", "");
                params.put("partnerId", deviceId);
                params.put("partnerName", "");
                params.put("email", "miral.trainer@gmail.com");
                params.put("country", "Bangladesh");

                return params;
            }
        };
        queue.add(stringRequest);
    }


    public void signInBeforeSignupRequest(final String userName, final String passwordStr, final String deviceId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.USER_SIGN_IN_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseInSignIn", response);

                if (response.contains("SUCCESS")) {
                    if (internetConnected()) {
                        signUpRequest(userName, passwordStr, deviceId);
                    } else {
                        progressDialog.hideProgressDialog();
                        Toast.makeText(SignInActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.hideProgressDialog();
                    Log.i("Errr", response);
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
                params.put("userid", deviceId);
                params.put("password", "01717");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void signUpRequest(final String userName, final String passwordStr, final String deviceId) {
        String url = "http://bot.sharedtoday.com:9500/ws/commonUpdateForArrayJSON?tbl=Partner&keyname=partnerId&id=" + deviceId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseSignUp", response);
                if (response.contains("SUCCESS")) {
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("username", userName);
                    editor.apply();
                    sendUpdatedPasswordToserver(passwordStr, deviceId);
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
                params.put("defaultPassword", passwordStr);
                params.put("partnerStatus", "Signup complete");
                params.put("applicantBirthDate", "");
                params.put("partnerId", deviceId);
                params.put("partnerName", userName);
                params.put("orgId", "maxis");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void sendUpdatedPasswordToserver(String password, String deviceId) {
        String url = Endpoints.UPDATE_DEFAULT_PASSWORD_POST_URL + password + "&userid=" + deviceId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseInSetUpdtedPass", response);
                if (response.contains("SUCCESS")) {
                    progressDialog.hideProgressDialog();
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putInt("loginStatus", 1);
                    editor.apply();
                    Toast.makeText(SignInActivity.this, "আপনার সাইন আপ সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.hideProgressDialog();
                    Toast.makeText(SignInActivity.this, "আপনার সাইন আপ সম্পন্ন হয়নি\n" + "আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
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
