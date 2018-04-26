package lct.mysymphony.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import lct.mysymphony.R;
import lct.mysymphony.helper.DownloadImage;
import lct.mysymphony.helper.Endpoints;

public class PayWithRocketActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    TextView billNumberTV;
    String deviceId,userId;
    RequestQueue queue;
    String imageUrl;
   /// Integer price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_rocket);
        SharedPreferences prefs = getSharedPreferences("phoneNumber", MODE_PRIVATE);
        userId= prefs.getString("phoneNo", "");
        Log.d("userId", userId);
        queue = Volley.newRequestQueue(PayWithRocketActivity.this);
        toolbar = findViewById(R.id.toolbarlayoutinpaywithrocket);
        setSupportActionBar(toolbar);
        billNumberTV=findViewById(R.id.billNumberInPayWithRocket);
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("deviceId",deviceId);
        sendRocketVerificationToServer();
        imageUrl=getIntent().getStringExtra("imageUrl");
        Log.d("url",imageUrl);
    }
    private void sendRocketVerificationToServer() {
        Log.i("Enter", "Enter in rocket");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.GET_REFEREL_CODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rockeTverification",response);
                        String jsonFormattedString = response.replaceAll("\\\\", "");
                        String result = "";

                        try {
                            JSONArray jsonArray = new JSONArray(jsonFormattedString);

                            if (jsonArray.length() == 0)
                                Toast.makeText(PayWithRocketActivity.this, "jsonArray blank", Toast.LENGTH_SHORT).show();
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject postInfo = jsonArray.getJSONObject(i);
                                    result = postInfo.getString("refCode");
                                    billNumberTV.setText(result);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            billNumberTV.setText("3452");
                           Log.d("jsnexception",e.toString());
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyErrorInPinActvt", error.toString());
                Toast.makeText(PayWithRocketActivity.this, "সার্ভারে সমস্যা দয়া করে আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("amount", "10");
                params.put("paymentMethod", "rocket");
                params.put("contentId", "1");
                params.put("deviceId", "132654");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void startUserProfileActivity(View view) {

        Log.d("image_url",imageUrl);
        DownloadImage downloadImage=new DownloadImage();
        downloadImage.downloadImage(imageUrl,PayWithRocketActivity.this);
        Toast.makeText(PayWithRocketActivity.this, "ধন্যবাদ আপনার পেমেন্ট টি যাচাই করা হচ্ছে", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        myIntent.putExtra("cameFromWhichActivity","payWithRocket");
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }
}
