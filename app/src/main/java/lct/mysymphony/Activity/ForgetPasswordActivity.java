package lct.mysymphony.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import lct.mysymphony.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText userPhoneNumber;
    RequestQueue queue;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        userPhoneNumber=findViewById(R.id.txtMobileNumInForgotPassword);
        editor = getSharedPreferences("phoneNumber", MODE_PRIVATE).edit();
        queue = Volley.newRequestQueue(this);
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

    public void resetPassword(View view) {

        sendUserPhoneNumberToServer();
    }

    private void sendUserPhoneNumberToServer() {
        String phoneNumber=userPhoneNumber.getText().toString();
        editor.putString("phoneNo", phoneNumber);
        String url= "http://bot.sharedtoday.com:9500/ws/gen2FACode?prcName=forgotPass&uid="+userPhoneNumber.getText().toString();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseprofile",response.toString());

                        Log.d("responsePhone ", response);

                        String genRef = "";
                        JSONObject postInfo = null;
                        try {
                            postInfo = new JSONObject(response);
                            genRef = postInfo.getString("genRef");
                            editor.putString("genRef", genRef);
                            editor.putString("cameFromWhichActivity","forgetPassword");
                            editor.apply();
                            Log.d("resultMobileNumber ", genRef);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (genRef.contains("-1") || genRef.contains("-2") || genRef.contains("-3")) {
                            Toast.makeText(ForgetPasswordActivity.this, "মোবাইল পিন তৈরিতে সমস্যা", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "আপনার নাম্বার এ ছয় ডিজিটের পিন পাঠানো হয়েছে\nআপনার পিনের মেয়াদ পাচ মিনিট", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getApplicationContext(), PinActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });

        queue.add(stringRequest);
    }
}
