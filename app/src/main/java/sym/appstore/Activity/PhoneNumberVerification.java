package sym.appstore.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sym.appstore.R;
import sym.appstore.helper.Endpoints;

public class PhoneNumberVerification extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    EditText phoneNumberET;
    RequestQueue queue;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,};
    SharedPreferences.Editor editor;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verification);
        editor = getSharedPreferences("phoneNumber", MODE_PRIVATE).edit();
        toolbar = findViewById(R.id.toolbarlayoutinsign);
        setSupportActionBar(toolbar);
        phoneNumberET = findViewById(R.id.phoneNumberInPhoneVarification);
        queue = Volley.newRequestQueue(PhoneNumberVerification.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(myIntent);
    }

    public void startPinActivity(View view) {
        if (internetConnected()) {
            phoneNumber = phoneNumberET.getText().toString();

            if (phoneNumber.length() > 0) {
                if (phoneNumber.startsWith("+88") || phoneNumber.startsWith("880")) {
                    Toast.makeText(this, "+880 ছাড়া নম্বর প্রদান করুন", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.length() == 11 && (phoneNumber.startsWith("017") || phoneNumber.startsWith("018") || phoneNumber.startsWith("019") || phoneNumber.startsWith("016") || phoneNumber.startsWith("015"))) {
                    phoneNumberEntry();
                } else
                    Toast.makeText(this, "সঠিক ফোন নাম্বার প্রদান করুন", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "ফোন নাম্বার দিন", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
    }

    private void sendMobileNumberToServer() {
        editor.putString("phoneNo", phoneNumber);
        editor.apply();
        String url = Endpoints.SEND_MOBILE_NUMBER_TO_SERVER_URL + phoneNumber;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsePhone ", response);
                        String genRef = "";
                        JSONObject postInfo = null;
                        try {
                            postInfo = new JSONObject(response);
                            genRef = postInfo.getString("genRef");
                            editor.putString("genRef", genRef);
                            editor.putString("cameFromWhichActivity", "PhoneNumberVerification");
                            editor.apply();
                            Log.d("resultMobileNumber ", genRef);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (genRef.contains("-1") || genRef.contains("-2") || genRef.contains("-3")) {
                            Toast.makeText(PhoneNumberVerification.this, "মোবাইল পিন তৈরিতে সমস্যা", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PhoneNumberVerification.this, "আপনার নাম্বার এ ছয় ডিজিটের পিন পাঠানো হয়েছে\nআপনার পিনের মেয়াদ পাচ মিনিট", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getApplicationContext(), PinActivity.class);
                            startActivity(myIntent);
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

    public void phoneNumberEntry() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.PHONE_NUMBER_ENTRY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        String result = "";
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() == 0)
                                Toast.makeText(PhoneNumberVerification.this, "jsonArray blank", Toast.LENGTH_SHORT).show();
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
                            sendMobileNumberToServer();
                        } else {
                            Log.e("VolleyErrorInPhoneNumbe", result.toString());
                            Toast.makeText(getApplicationContext(), "এই নাম্বারটি দিয়ে পুর্বে রেজিস্টার করা হয়েছে", Toast.LENGTH_SHORT).show();
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
                params.put("partnerId", phoneNumberET.getText().toString());
                params.put("partnerName", "");
                params.put("email", "miral.trainer@gmail.com");
                params.put("country", "Bangladesh");

                return params;
            }
        };
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
}
