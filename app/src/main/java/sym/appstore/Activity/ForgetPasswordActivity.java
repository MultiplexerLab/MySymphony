package sym.appstore.Activity;

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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sym.appstore.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText userPhoneNumber;
    RequestQueue queue;
    SharedPreferences.Editor editor;
    String[] permissions = new String[]{Manifest.permission.READ_SMS,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        userPhoneNumber = findViewById(R.id.txtMobileNumInForgotPassword);
        editor = getSharedPreferences("phoneNumber", MODE_PRIVATE).edit();
        queue = Volley.newRequestQueue(this);
        checkPermissions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(myIntent);
        finish();
    }

    public void resetPassword(View view) {
        if (internetConnected()) {
            sendUserPhoneNumberToServer();
        } else Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
    }

    private void sendUserPhoneNumberToServer() {
        String phoneNumber = userPhoneNumber.getText().toString();
        if (phoneNumber.length() > 0) {
            if (phoneNumber.length() == 11 && (phoneNumber.startsWith("017") || phoneNumber.startsWith("018") || phoneNumber.startsWith("019") || phoneNumber.startsWith("016") || phoneNumber.startsWith("015"))) {
                editor.putString("phoneNo", phoneNumber);
                final String url = "http://bot.sharedtoday.com:9500/ws/gen2FACode?prcName=forgotPass&uid=" + userPhoneNumber.getText().toString();
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("url", url);
                        Log.d("responseprofile", response.toString());
                        String genRef = "";
                        JSONObject postInfo = null;
                        try {
                            postInfo = new JSONObject(response);
                            genRef = postInfo.getString("genRef");
                            editor.putString("genRef", genRef);
                            editor.putString("cameFromWhichActivity", "forgetPassword");
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
                            startActivity(myIntent);
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            } else Toast.makeText(this, "সঠিক ফোন নাম্বার প্রদান করুন", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "ফোন নাম্বার দিন", Toast.LENGTH_SHORT).show();
        }

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

}
