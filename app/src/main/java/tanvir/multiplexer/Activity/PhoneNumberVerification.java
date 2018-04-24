package tanvir.multiplexer.Activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import tanvir.multiplexer.R;
import tanvir.multiplexer.helper.Endpoints;

public class PhoneNumberVerification extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    EditText phoneNumberET;

    RequestQueue queue;


    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.toolbarlayoutinsign);
        setSupportActionBar(toolbar);
        phoneNumberET = findViewById(R.id.phoneNumberInPhoneVarification);
        queue = Volley.newRequestQueue(PhoneNumberVerification.this);

        //checkPermissions();

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

    public void startPinActivity(View view) {

        //sendMobileNumberToServer();
        signUpRequest();
//        Intent myIntent = new Intent(getApplicationContext(),PinActivity.class);
//        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        this.startActivity(myIntent);
//        overridePendingTransition(R.anim.left_in, R.anim.left_out);
//        finish();
    }

    public void main() {
        ///sensMobileNumberToServer();

//        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        this.startActivity(myIntent);
//        overridePendingTransition(R.anim.left_in, R.anim.left_out);
//        finish();

    }

    private void sendMobileNumberToServer() {


        final String phoneNumber = phoneNumberET.getText().toString();
        String url = "http://bot.sharedtoday.com:9500/ws/gen2FACode?prcName=signUp&uid=" + phoneNumber;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(PhoneNumberVerification.this, response, Toast.LENGTH_LONG).show();
                        Log.d("responsePhone ", response);

                        String result = "";

                        JSONObject postInfo = null;
                        try {
                            postInfo = new JSONObject(response);
                            result = postInfo.getString("genRef");
                            Log.d("resultMobileNumber ", result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (result.contains("-1") || result.contains("-2") || result.contains("-3")) {
                            Toast.makeText(PhoneNumberVerification.this, "Mobile pin generation failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PhoneNumberVerification.this, "আপনার নাম্বার এ ছয় ডিজিটের পিন পাঠানো হয়েছে", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getApplicationContext(), PinActivity.class);
                            myIntent.putExtra("genref", result);
                            myIntent.putExtra("phoneNumber", phoneNumber);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInPhoneNumbe", error.toString());
                Toast.makeText(PhoneNumberVerification.this, "সার্ভারে সমস্যা দয়া করে আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    public void signUpRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.USER_SIGN_UP_POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ///Toast.makeText(PhoneNumberVerification.this, response, Toast.LENGTH_SHORT).show();
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
                                    ///Toast.makeText(PhoneNumberVerification.this, "array : "+postInfo.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PhoneNumberVerification.this, "jsonException : " + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        if (result.contains("SUCCESS")) {
                            sendMobileNumberToServer();
                        } else {
                            Toast.makeText(PhoneNumberVerification.this, "Problem in database", Toast.LENGTH_SHORT).show();
                        }
//                        Intent myIntent = new Intent(getApplicationContext(), HomePage.class);
//                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(myIntent);
//                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                        finish();
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
                params.put("defaultPassword", "12345");
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
}
