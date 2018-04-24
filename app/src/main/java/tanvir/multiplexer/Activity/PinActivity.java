package tanvir.multiplexer.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tanvir.multiplexer.R;
import tanvir.multiplexer.helper.Endpoints;

public class PinActivity extends AppCompatActivity {
    Pinview pinview;
    private android.support.v7.widget.Toolbar toolbar;
    String pinText = "";
    RequestQueue queue;
    String genref = "";
    String phoneNumber;

    //RadioGroup radioGroup;
    //RadioButton radioButton1,radioButton2,radioButton3,radioButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        toolbar = findViewById(R.id.toolbarlayoutinpin);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(PinActivity.this);

        /*radioGroup=findViewById(R.id.radioGroupInPinActivity);
        radioButton1=findViewById(R.id.firstRadioButton);
        radioButton2=findViewById(R.id.secondRadioButton);
        radioButton3=findViewById(R.id.thirdRadioButton);
        radioButton4=findViewById(R.id.fourthRadioButton);*/

        genref = getIntent().getStringExtra("genref");
        phoneNumber = getIntent().getStringExtra("phoneNumber");


        pinview = findViewById(R.id.pinview);

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {

                pinText = pinview.getValue().toString();
                sendVarificationCodeToServer();
                ///if (pinview.getValue().toString().contains("1234"))
                ///{
                ///Toast.makeText(PinActivity.this, "Pin matches successfully", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
//                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                PinActivity.this.startActivity(myIntent);
//                overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                finish();
                //}
                //else
                ///Toast.makeText(PinActivity.this, "Pin is wrong ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent myIntent = new Intent(getApplicationContext(), PhoneNumberVerification.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();

    }

    private void sendVarificationCodeToServer() {


        String url = "http://bot.sharedtoday.com:9500/ws/validate2FACode?prcName=signUp&uid=" + phoneNumber + "&genRef=" + genref + "&code=" + pinText;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
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
                                    ///Toast.makeText(PhoneNumberVerification.this, "array : "+postInfo.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PinActivity.this, "jsonException : " + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        if (result.contains("SUCCESS")) {
                            Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                            myIntent.putExtra("phoneNumber",phoneNumber);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            PinActivity.this.startActivity(myIntent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();


                        } else
                            Toast.makeText(PinActivity.this, "আপনার পিন ভুল হয়েছে", Toast.LENGTH_SHORT).show();

                        ///Toast.makeText(PinActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.d("responsePin ", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInPinActvt", error.toString());
                Toast.makeText(PinActivity.this, "সার্ভারে সমস্যা দয়া করে আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
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
}
