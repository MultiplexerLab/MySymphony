package lct.mysymphony.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lct.mysymphony.R;
import lct.mysymphony.helper.Endpoints;

public class PinActivity extends AppCompatActivity {
    PinView pinview;
    private android.support.v7.widget.Toolbar toolbar;
    String pinText = "";
    RequestQueue queue;
    String genref = "";
    String phoneNumber;
    boolean isResendAlreadyused = false;
    String cameFromWhichActivity = "";
    lct.mysymphony.helper.SimpleSmsReciever simpleSmsReceiver = new lct.mysymphony.helper.SimpleSmsReciever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        toolbar = findViewById(R.id.toolbarlayoutinpin);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(PinActivity.this);
        SharedPreferences prefs = getSharedPreferences("phoneNumber", MODE_PRIVATE);
        genref = prefs.getString("genRef", "");
        cameFromWhichActivity = prefs.getString("cameFromWhichActivity", "");
        phoneNumber = prefs.getString("phoneNo", "");
        pinview = findViewById(R.id.pinView);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String address = extras.getString("MessageNumber");
            String message = extras.getString("Message");
            Log.d("MessageNumber", address);
            Log.d("Message", message);
            if (address.contains("01552146213")) {
                message = message.replaceAll("Generated Code is:", "");
                message = message.replaceAll(" ", "");
                pinview.setText(message);
                pinText = message;
                SharedPreferences.Editor editor = getSharedPreferences("isBroadcastAlreadyUsed", MODE_PRIVATE).edit();
                editor.putString("used", "true");
                editor.apply();
                if (internetConnected()) {
                    if (cameFromWhichActivity.contains("forgetPassword")) {
                        sendVerificationCodeForReset();
                    } else sendVarificationCodeToServer();

                } else {
                    Toast.makeText(PinActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        } else {
            Log.d("Bundle", "null");
        }

        pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 6) {
                    pinText = String.valueOf(charSequence);
                    Log.d("cameFromWhichActivity", cameFromWhichActivity);
                    if (internetConnected()) {
                        if (cameFromWhichActivity.contains("forgetPassword")) {
                            sendVerificationCodeForReset();

                        } else sendVarificationCodeToServer();

                    } else {
                        Toast.makeText(PinActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void sendVerificationCodeForReset() {
        Log.i("Phonereset", phoneNumber);
        Log.i("GenRefreset", genref);
        Log.i("Pintext", pinText);

        if (isResendAlreadyused == false) {
            isResendAlreadyused = true;
            String url = "http://bot.sharedtoday.com:9500/ws/validate2FACode?prcName=forgotPass&uid=" + phoneNumber + "&genRef=" + genref + "&code=" + pinText;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("reset", response);
                    String result = "";
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        if (jsonArray.length() == 0)
                            Toast.makeText(PinActivity.this, "jsonArray blank", Toast.LENGTH_SHORT).show();
                        else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject postInfo = jsonArray.getJSONObject(i);
                                result = postInfo.getString("msgType");
                                ///Toast.makeText(PhoneNumberVerification.this, "array : "+postInfo.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PinActivity.this, "jsonException : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    if (result.contains("SUCCESS")) {
                        Toast.makeText(PinActivity.this, "আপনার পিন সঠিক হয়েছে", Toast.LENGTH_SHORT).show();

                        Log.d("Enter", "enter");
                        sendUserNewPassword();
                    } else {
                        Toast.makeText(PinActivity.this, "আপনার পিন ভুল হয়েছে" + "\n" + "আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                    Log.d("responsePin ", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyErrorInPinActvt", error.toString());
                    Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (cameFromWhichActivity.contains("forgetPassword")) {
            Intent myIntent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
            this.startActivity(myIntent);
        } else {
            Intent myIntent = new Intent(getApplicationContext(), PhoneNumberVerification.class);
            this.startActivity(myIntent);
            finish();
        }
    }

    private void sendVarificationCodeToServer() {
        Log.i("Phone", phoneNumber);
        Log.i("GenRef", genref);

        String url = "http://bot.sharedtoday.com:9500/ws/validate2FACode?prcName=signUp&uid=" + phoneNumber + "&genRef=" + genref + "&code=" + pinText;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = "";
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0)
                        Toast.makeText(PinActivity.this, "jsonArray blank", Toast.LENGTH_SHORT).show();
                    else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject postInfo = jsonArray.getJSONObject(i);
                            result = postInfo.getString("msgType");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PinActivity.this, "jsonException : " + e.toString(), Toast.LENGTH_SHORT).show();
                }

                if (result.contains("SUCCESS")) {
                    Toast.makeText(PinActivity.this, "আপনার পিন সঠিক হয়েছে", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                    myIntent.putExtra("phoneNumber", phoneNumber);
                    PinActivity.this.startActivity(myIntent);
                    finish();
                } else {
                    Toast.makeText(PinActivity.this, "আপনার পিন ভুল হয়েছে" + "\n" + "আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
                Log.d("responsePin ", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInPinActvt", error.toString());
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void resendMobileVerificationNumber() {
        String url = Endpoints.SEND_MOBILE_NUMBER_TO_SERVER_URL + phoneNumber;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsePhone ", response);
                String genRef = "";
                JSONObject postInfo = null;
                try {
                    postInfo = new JSONObject(response);
                    genRef = postInfo.getString("genRef");
                    Log.d("resultMobileNumber ", genRef);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (genRef.contains("-1") || genRef.contains("-2") || genRef.contains("-3")) {
                    Toast.makeText(PinActivity.this, "কিছুক্ষন পরে আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("isBroadcastAlreadyUsed", MODE_PRIVATE).edit();
                    editor.putString("used", "true");
                    editor.apply();
                    Toast.makeText(PinActivity.this, "আপনার নাম্বার এ ছয় ডিজিটের পিন পাঠানো হয়েছে", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInPhoneNumbe", error.toString());
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
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

    public void resendVerificationNumber(View view) {

        if (internetConnected()) {
            resendMobileVerificationNumber();
        } else
            Toast.makeText(PinActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
    }

    public void sendUserNewPassword() {
        Log.d("newpassword", "new password");
        String url = "http://bot.sharedtoday.com:9500/ws/resetRandomPassword?userid=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseNewPassword", response);
                if (response.contains("SUCCESS")) {
                    Intent myIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    PinActivity.this.startActivity(myIntent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInPhoneNumbe", error.toString());
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(simpleSmsReceiver, new android.content.IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(simpleSmsReceiver);
    }


}

