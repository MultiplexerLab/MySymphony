package sym.appstore.Activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import sym.appstore.CustomSwipeAdapter;
import sym.appstore.ModelClass.Icon;
import sym.appstore.ModelClass.SliderImage;
import sym.appstore.R;
import sym.appstore.ViewpagerAdapter.CustomSwipeAdapterIntro;
import sym.appstore.helper.AppLogger;
import sym.appstore.helper.Endpoints;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class WelComeActivity extends AppCompatActivity {

    sym.appstore.helper.ProgressDialog progressDialog;
    RequestQueue queue;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_PHONE_NUMBERS,
    };
    String emailId = "default", devicePhoneNumber = "default", deviceName = "default";
    String osVersion = "default", carrierName = "default", firebaseToken = "default", deviceId = "default";
    Date currenTime;
    DateFormat dateFormat;
    ArrayList<SliderImage> sliderImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();

        Intent intentDeep = getIntent();
        String action = intentDeep.getAction();
        Uri data = intentDeep.getData();

        if (data != null) {
            finish();
            startActivity(getIntent());
        }

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        int loginStatus = prefs.getInt("loginStatus", 0);
        if (loginStatus == 1) {
            String cameFromWhichActivity = getIntent().getStringExtra("cameFromWhichActivity");
            if (cameFromWhichActivity != null) {
                if (cameFromWhichActivity.contains("HomePage")) {
                    ActivityCompat.finishAffinity(this);
                }
            } else {
                Intent intent = new Intent(WelComeActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        }
        queue = Volley.newRequestQueue(WelComeActivity.this);

        getIntroInfo();
        initializeData();
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

    private void initializeData() {
        emailId = "default";
        devicePhoneNumber = "default";
        deviceName = "default";
        osVersion = "default";
        carrierName = "default";
        firebaseToken = "default";
        deviceId = "default";

        sliderImages = new ArrayList<>();
        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        firebaseToken = prefs.getString("firebaseToken", "nothing");
        Log.i("FirebaseTokenMain", firebaseToken);
        progressDialog = new sym.appstore.helper.ProgressDialog(WelComeActivity.this);
        checkPermissions();

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();
        try {
            if (ActivityCompat.checkSelfPermission(WelComeActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            devicePhoneNumber = manager.getLine1Number();
            Log.i("PhoneNo", devicePhoneNumber);
        } catch (Exception e) {
            Log.e("Excep", e.toString());
        }
        Log.i("carrierName", carrierName);
        deviceName = android.os.Build.MODEL;
        Log.i("ModelName", deviceName);
        osVersion = Build.VERSION.RELEASE;
        Log.i("osVersion", osVersion);
        //getInstanceInfo();
    }

    public void getIntroInfo() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_INTRO_INFO, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Respponse", response.toString());
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<SliderImage>>() {
                    }.getType();
                    sliderImages = gson.fromJson(response.toString(), type);
                    Log.i("SliderImages", sliderImages.toString());
                    initialCustomSwipeAdapter();
                } catch (Exception e) {
                    AppLogger.insertLogs(WelComeActivity.this, dateFormat.format(currenTime), "Y", "Intro",
                            "FAILED", response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLogger.insertLogs(WelComeActivity.this, dateFormat.format(currenTime), "Y", "Intro",
                        "FAILED", error.toString());
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void initialCustomSwipeAdapter() {
        ViewPager viewPager = findViewById(R.id.viewPagerIntro);
        CustomSwipeAdapterIntro customSwipeAdapter = new CustomSwipeAdapterIntro(this, sliderImages);
        viewPager.setAdapter(customSwipeAdapter);
        CirclePageIndicator indicator = findViewById(R.id.indicatorIntro);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
    }

    public void getAccountsName() {
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            emailId = accountName;
            Log.i("Email", accountName);
            initializeData();
            insertToServerDB();
        }
    }

    public void enableRuntimePermission() {
        ActivityCompat.requestPermissions(WelComeActivity.this, new String[]
                {
                        GET_ACCOUNTS,
                        READ_PHONE_STATE
                }, 1);

    }

    public void signUp(View view) {
        if (internetConnected()) {
            insertUserToDB();
        } else {
            Toast.makeText(WelComeActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertUserToDB() {
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                emailId = account.name;
            }
        }
        Log.i("EmailId", emailId);
        if (emailId.isEmpty() || emailId == null) {
            enableRuntimePermission();
            getAccountsName();
        } else {
            initializeData();
            insertToServerDB();
        }
    }

    private void insertToServerDB() {
        progressDialog.showProgressDialog();
        String url = "http://bot.sharedtoday.com:9500/ws/mysymphony/commonInsertIntoSymUsers?tbl=symUsers";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseSignUp", response);
                if (response.contains("SUCCESS")) {
                    AppLogger.insertLogs(WelComeActivity.this, dateFormat.format(currenTime), "Y", "Registration",
                            "SUCCESS", "");
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("username", "Guest");
                    editor.putString("deviceId", deviceId);
                    editor.putInt("loginStatus", 1);
                    editor.putString("emailId", emailId);
                    editor.apply();
                    Intent intent = new Intent(WelComeActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else if (response.contains("exists")) {
                    AppLogger.insertLogs(WelComeActivity.this, dateFormat.format(currenTime), "Y", "Login",
                            "SUCCESS", "");
                    progressDialog.hideProgressDialog();
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("username", "Guest");
                    editor.putString("deviceId", deviceId);
                    editor.putInt("loginStatus", 1);
                    editor.putString("emailId", emailId);
                    editor.apply();
                    Intent intent = new Intent(WelComeActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    AppLogger.insertLogs(WelComeActivity.this, dateFormat.format(currenTime), "Y", "Login/Registration",
                            "FAILED", response);
                    progressDialog.hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorInSignUp", error.toString());
                progressDialog.hideProgressDialog();
                AppLogger.insertLogs(WelComeActivity.this, dateFormat.format(currenTime), "Y", "Login/Registration",
                        "FAILED", error.toString());
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();
                Map<String, String> params = new HashMap<String, String>();
                params.put("defaultPassword", "default");

                if (deviceId == null || deviceId.equals("null")) {
                    params.put("deviceId", "default");
                    params.put("userId", "default");
                } else {
                    params.put("deviceId", deviceId);
                    params.put("userId", deviceId);
                }
                params.put("registrationDate", dateFormat.format(date));
                if (devicePhoneNumber == null || devicePhoneNumber.equals("null")) {

                } else {
                    params.put("contactNum", devicePhoneNumber);
                }
                if (firebaseToken == null || firebaseToken.equals("null")) {

                } else {
                    params.put("firebaseToken", firebaseToken);
                }
                if (emailId == null || emailId.equals("null")) {

                } else {
                    params.put("email", emailId);
                }

                if (carrierName == null || carrierName.equals("null")) {
                } else {
                    params.put("operatorName", carrierName);
                }
                if (deviceName == null || deviceName.equals("null")) {

                } else {
                    params.put("mobileModel", deviceName);
                }
                if (osVersion == null || osVersion.equals("null")) {

                } else {
                    params.put("osVersion", osVersion);
                }
                Log.i("DataSet", params.toString());

                currenTime = new Date();
                AppLogger.insertLogs(WelComeActivity.this, dateFormat.format(currenTime), "N", "login",
                        "Login/Registration", "Data from user:" + params.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
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