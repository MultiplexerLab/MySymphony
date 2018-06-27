package sym.appstore.Activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
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
import sym.appstore.ModelClass.AppData;
import sym.appstore.ModelClass.DataBaseData;
import sym.appstore.ModelClass.GamesZone;
import sym.appstore.ModelClass.Icon;
import sym.appstore.ModelClass.MulloChar;
import sym.appstore.ModelClass.Porashuna;
import sym.appstore.ModelClass.SeraChobi;
import sym.appstore.ModelClass.ShocolChobi;
import sym.appstore.R;
import sym.appstore.RecyclerViewAdapter.RecyclerAdapterForJapitoJibon;
import sym.appstore.helper.AppLogger;
import sym.appstore.helper.DownloadApk;
import sym.appstore.helper.Endpoints;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class HomePage extends AppCompatActivity implements DownloadApk.AsyncResponse {
    Context context;
    Date currenTime;
    DateFormat dateFormat;

    ArrayList<Porashuna> sliderImages;
    ArrayList<SeraChobi> seraChobiArrayList;
    ArrayList<Porashuna> japitoJibonArrayList, musicVideoList, audioList, hotNewsArrayList, sportsArralyList, auttoHashiArrList, mixedArrList, scienceArrList, kidsArrList, educationList;
    ArrayList<MulloChar> mulloCharArrayList;
    ArrayList<AppData> appList;
    ArrayList<ShocolChobi> shocolChobiArrayList;
    ArrayList<GamesZone> gamesZoneArrayList;
    private android.support.v7.widget.Toolbar toolbar;
    CustomSwipeAdapter customSwipeAdapter;
    ViewPager viewPager;
    private RecyclerView recyclerViewForJapitoJibon;
    public RecyclerAdapterForJapitoJibon adapterForJapitoJibon;
    ArrayList<Icon> iconImageUrls;

    RequestQueue queue;
    sym.appstore.helper.ProgressDialog progressDialog;
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
    String emailId = "", devicePhoneNumber = "", deviceName = "";
    String osVersion = "", carrierName = "", firebaseToken = "", deviceId = "";
    LinearLayout rootLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        FirebaseMessaging.getInstance().subscribeToTopic("All");
        rootLayout = findViewById(R.id.rootLayout);

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (internetConnected()) {
                            newloadDataFromVolley();
                            if (snackbar != null) {
                                if (snackbar.isShown()) {
                                    snackbar.dismiss();
                                }
                            }
                            mySwipeRefreshLayout.setRefreshing(false);
                        } else {
                            showSnackBar();
                        }
                    }
                }
        );

        progressDialog = new sym.appstore.helper.ProgressDialog(this);
        context = HomePage.this;
        queue = Volley.newRequestQueue(HomePage.this);
        sliderImages = new ArrayList<>();
        seraChobiArrayList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbarlayoutinhome_page);
        setSupportActionBar(toolbar);
        japitoJibonArrayList = new ArrayList<>();
        mulloCharArrayList = new ArrayList<>();
        gamesZoneArrayList = new ArrayList<>();
        shocolChobiArrayList = new ArrayList<>();
        educationList = new ArrayList<>();
        hotNewsArrayList = new ArrayList<>();
        sportsArralyList = new ArrayList<>();
        auttoHashiArrList = new ArrayList<>();
        mixedArrList = new ArrayList<>();
        scienceArrList = new ArrayList<>();
        kidsArrList = new ArrayList<>();
        appList = new ArrayList<>();
        musicVideoList = new ArrayList<>();
        audioList = new ArrayList<>();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        SharedPreferences.Editor editor = getSharedPreferences("tracker", MODE_PRIVATE).edit();
        editor.putString("startTime", dateFormat.format(currenTime));
        editor.commit();
        Log.i("TrackerstartTime", dateFormat.format(currenTime));

        initDataFromServer();
        String appContentId = getIntent().getStringExtra("appContentId");
        String apkTitle = getIntent().getStringExtra("notificationTitle");
        if (appContentId != null) {
            String url = "http://bot.sharedtoday.com:9500/ws/mysymphony/getSpecificContent?id=" + appContentId;
            getDataFromContentId(url);

            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("jsonDataObject", "");
            JSONObject jsonDataObject = gson.fromJson(json, JSONObject.class);
            if (jsonDataObject == null) {
                getDataFromContentId(url);
            }
            if (jsonDataObject != null) {
                try {
                    String packageName = jsonDataObject.getString("reference1");
                    String versionName = jsonDataObject.getString("reference2");
                    String versionCode = jsonDataObject.getString("reference3");
                    Log.i("dataVersionCode", packageName + "  " + versionCode);
                    if (isPackageExisted(packageName)) {
                        PackageInfo pinfo = null;
                        try {
                            pinfo = getPackageManager().getPackageInfo(packageName, 0);
                            int versionNumber = pinfo.versionCode;

                            if (versionCode == null || versionCode.equals("null")) {

                            } else {
                                if (versionNumber == Integer.parseInt(versionCode)) {
                                    Toast.makeText(context, jsonDataObject.getString("contentTitle") + " অ্যাপ আপনার মোবাইলে ইন্সটলড আছে!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(context, jsonDataObject.getString("contentTitle") + " অ্যাপ আপনার মোবাইলে ইন্সটলড আছে, কিন্তু নতুন ভার্সন এসেছে!", Toast.LENGTH_SHORT).show();
                                    DownloadApk downloadApk = new DownloadApk();
                                    DataBaseData dataBaseData = new DataBaseData(jsonDataObject.getString("contentTitle"), "mobile_app", "apk", jsonDataObject.getString("contentDescription"), Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("thumbNail_image"), "free",
                                            Integer.parseInt(appContentId));
                                    downloadApk.downLoadAPK(Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("contentUrl"), this, dataBaseData);
                                }
                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e("unknown", e.toString());
                        }
                    } else {
                        DownloadApk downloadApk = new DownloadApk();
                        DataBaseData dataBaseData = new DataBaseData(jsonDataObject.getString("contentTitle"), "mobile_app", "apk", jsonDataObject.getString("contentDescription"), Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("thumbNail_image"), "free",
                                Integer.parseInt(appContentId));
                        downloadApk.downLoadAPK(Endpoints.DOMAIN_PREFIX + jsonDataObject.getString("contentUrl"), this, dataBaseData);
                    }
                } catch (JSONException e) {
                    Log.e("Hudai", e.toString());
                }
            }
        }
    }

    public boolean isPackageExisted(String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            Log.i("PackageIstalled", packageInfo.packageName.toString());
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    public void getDataFromContentId(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonDataObject = response.getJSONObject(0);
                    Log.i("jsonDataObject", jsonDataObject.toString());
                    SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(jsonDataObject);
                    prefsEditor.putString("jsonDataObject", json);
                    prefsEditor.commit();

                } catch (JSONException e) {
                    Log.e("JSONError", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void initDataFromServer() {
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        int loginStatus = prefs.getInt("loginStatus", 0);
        Log.i("LoginTag", loginStatus + "");
        if (loginStatus == 1) {
            if (internetConnected()) {
                loadIconsFromServer();
                getInstanceInfo();
                newloadDataFromVolley();
            } else {
                showSnackBar();
            }
        } else {
            if (internetConnected()) {
                initializeData();
                insertUserToDB();
                loadIconsFromServer();
                getInstanceInfo();
            } else {
                showSnackBar();
            }
        }
    }

    public void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, "ইন্টারনেটের সাথে সংযুক্ত নেই!", Snackbar.LENGTH_INDEFINITE)
                .setAction("সংযুক্ত করুন", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(settingsIntent, 9003);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
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

    private void initializeData() {
        sliderImages = new ArrayList<>();
        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        firebaseToken = prefs.getString("firebaseToken", "nothing");
        Log.i("FirebaseTokenMain", firebaseToken);
        progressDialog = new sym.appstore.helper.ProgressDialog(HomePage.this);
        checkPermissions();

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();
        try {
            if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            devicePhoneNumber = manager.getLine1Number();
            Log.i("PhoneNo", devicePhoneNumber);
        } catch (Exception e) {
            Log.e("ExcepPhoneNo", e.toString());
        }
        Log.i("carrierName", carrierName);
        deviceName = android.os.Build.MODEL;
        Log.i("ModelName", deviceName);
        osVersion = Build.VERSION.RELEASE;
        Log.i("osVersion", osVersion);
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
        if (requestCode == 9003) {
            if (internetConnected()) {
                initializeData();
                insertUserToDB();
                loadIconsFromServer();
                getInstanceInfo();
            } else {
                showSnackBar();
            }
        }
    }

    public void enableRuntimePermission() {
        ActivityCompat.requestPermissions(HomePage.this, new String[]
                {
                        GET_ACCOUNTS,
                        READ_PHONE_STATE
                }, 1);

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

    private boolean internetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
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
                    progressDialog.hideProgressDialog();
                    AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "Y", "Registration",
                            "SUCCESS", "Succesful Registration", "registration");
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("username", "Guest");
                    editor.putString("deviceId", deviceId);
                    editor.putInt("loginStatus", 1);
                    editor.putString("emailId", emailId);
                    editor.apply();
                    loadIconsFromServer();
                    newloadDataFromVolley();
                } else if (response.contains("exists")) {
                    AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "Y", "Login",
                            "SUCCESS", "Succesful Login", "login");
                    progressDialog.hideProgressDialog();
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("username", "Guest");
                    editor.putString("deviceId", deviceId);
                    editor.putInt("loginStatus", 1);
                    editor.putString("emailId", emailId);
                    editor.apply();
                    loadIconsFromServer();
                    newloadDataFromVolley();
                } else {
                    AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "Y", "Registration",
                            "FAILED", response, "login/registration");
                    progressDialog.hideProgressDialog();
                    Toast.makeText(HomePage.this, "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorInSignUp", error.toString());
                progressDialog.hideProgressDialog();
                AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "Y", "Login/Registration",
                        "FAILED", error.toString(), "login/registration");

                Toast.makeText(HomePage.this, "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();
                Map<String, String> params = new HashMap<String, String>();
                params.put("defaultPassword", "default");

                if (deviceId == null || deviceId.equals("")) {
                    params.put("deviceId", "default");
                    params.put("userId", "default");
                } else {
                    params.put("deviceId", deviceId);
                    params.put("userId", deviceId);
                }
                params.put("registrationDate", dateFormat.format(date));
                if (devicePhoneNumber == null || devicePhoneNumber.equals("")) {
                    params.put("contactNum", "01717");
                } else {
                    params.put("contactNum", devicePhoneNumber);
                }
                if (firebaseToken == null || firebaseToken.equals("")) {

                } else {
                    params.put("firebaseToken", firebaseToken);
                }
                if (emailId == null || emailId.equals("")) {

                } else {
                    params.put("email", emailId);
                }

                if (carrierName == null || carrierName.equals("")) {
                } else {
                    params.put("operatorName", carrierName);
                }
                if (deviceName == null || deviceName.equals("")) {

                } else {
                    params.put("mobileModel", deviceName);
                }
                if (osVersion == null || osVersion.equals("")) {

                } else {
                    params.put("osVersion", osVersion);
                }
                Log.i("DataSet", params.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void setIcons() {
        Integer[] imageViews = {R.id.mainButton1, R.id.mainButton2, R.id.mainButton3, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8, R.id.bottom1, R.id.bottom2, R.id.bottom3, R.id.bottom4};
        Integer[] textViews = {R.id.textViewMain1, R.id.textViewMain2, R.id.textViewMain3, R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textBottom1, R.id.textBottom2, R.id.textBottom3, R.id.textBottom4};
        Integer[] textViewsCategories = {R.id.hotNewsTV, R.id.sportsTV, R.id.ShikkhaSohayikaTV, R.id.auttoHashiTV, R.id.japitoJibonTV, R.id.mixedTV, R.id.scienceTV, R.id.kidsTV};

        for (int i = 0; i < iconImageUrls.size(); i++) {
            Glide.with(HomePage.this).load(iconImageUrls.get(i).getImage()).into((ImageView) findViewById(imageViews[i]));
            TextView temp = findViewById(textViews[i]);
            temp.setText(iconImageUrls.get(i).getCategoryTitle());
            if (iconImageUrls.get(i).getType().equals("topic")) {
                TextView temp2 = findViewById(textViewsCategories[i - 3]);
                temp2.setText(iconImageUrls.get(i).getCategoryTitle());

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "N", "HomePage",
                "IN", "Entrance", "page");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "Y", "HomePage",
                "LEAVE", "Leave", "page");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getSharedPreferences("tracker", MODE_PRIVATE);
        AppLogger.insertLogs(HomePage.this, pref.getString("startTime", dateFormat.format(currenTime)), "Y", "AppStore APP",
                "DURATION", "App is finishing", "page");
    }

    private void loadIconsFromServer() {
        ImageView doanloadedIcon = findViewById(R.id.doanloadedIcon);
        doanloadedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent downloadedContent = new Intent(HomePage.this, ProfileActivity.class);
                startActivity(downloadedContent);
            }
        });
        iconImageUrls = new ArrayList<Icon>();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.ICON_GET_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Icon>>() {
                    }.getType();
                    iconImageUrls = gson.fromJson(response.toString(), type);
                    Log.i("IconImages", iconImageUrls.toString());
                    setIcons();
                    progressDialog.hideProgressDialog();
                } catch (Exception e) {
                    Log.d("exceptionLoadData4rmvly", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(HomePage.this, "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void getInstanceInfo() {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_APP_INFO, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.i("Pname", jsonObject.toString());
                        if (jsonObject.getString("pName").equals("appMetaName")) {
                            String appTitleExtra = jsonObject.getString("pValue");
                            TextView extraParam = findViewById(R.id.extraParam);
                            extraParam.setText(appTitleExtra);
                        }
                        if (jsonObject.getString("pName").equals("appLogo")) {
                            ImageView imageLogo = toolbar.findViewById(R.id.imageLogo);
                            Glide.with(HomePage.this).load(jsonObject.getString("pValue")).into(imageLogo);
                        }
                    }
                } catch (Exception e) {
                    Log.d("exceptionLoadData4rmvly", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(HomePage.this, "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void newloadDataFromVolley() {
        queue = Volley.newRequestQueue(HomePage.this);
        progressDialog.showProgressDialog();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.NEW_HOME_GET_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    setSliderContent(response);
                    setDailyLife(response);
                    progressDialog.hideProgressDialog();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exceptionLoadData4rmvly", e.toString());
                    progressDialog.hideProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                if (progressDialog != null) {
                    progressDialog.hideProgressDialog();
                }
            }
        });
        queue.add(jsonArrayRequest);
    }


    private void setDailyLife(JSONArray japito_jibon_content_arr) {
        if (japito_jibon_content_arr.length() > 0) {
            for (int i = 0; i < japito_jibon_content_arr.length(); i++) {
                int contentPrice = 0;
                try {
                    String contentTitle = japito_jibon_content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = japito_jibon_content_arr.getJSONObject(i).getString("contentType");
                    String contentDescription = japito_jibon_content_arr.getJSONObject(i).getString("contentDescription");
                    String contentUrl = japito_jibon_content_arr.getJSONObject(i).getString("contentUrl");
                    String contentCat = japito_jibon_content_arr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image = japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image");
                    int contentid = japito_jibon_content_arr.getJSONObject(i).getInt("id");
                    if (japito_jibon_content_arr.getJSONObject(i).has("contentPrice")) {
                        contentPrice = japito_jibon_content_arr.getJSONObject(i).getInt("contentPrice");
                    }
                    if (contentCat.equals("daily_life")) {
                        japitoJibonArrayList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeJapitoJibonRecyclerView();
                    } else if (contentCat.equals("news")) {
                        hotNewsArrayList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeHotNewsRecylerView();
                    } else if (contentCat.equals("sports")) {
                        sportsArralyList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeSportsRecylerView();
                    } else if (contentCat.equals("autto_hashi")) {
                        auttoHashiArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeAuttoHashiRecylerView();
                    } else if (contentCat.equals("science")) {
                        scienceArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeScienceRecylerView();
                    } else if (contentCat.equals("mixed")) {
                        mixedArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeMixedRecylerView();
                    } else if (contentCat.equals("kk_mela")) {
                        kidsArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeKidsRecylerView();
                    } else if (contentCat.equals("mobile_app")) {
                        String packageName = japito_jibon_content_arr.getJSONObject(i).getString("reference1");
                        String versionName = japito_jibon_content_arr.getJSONObject(i).getString("reference2");
                        String versionCode = japito_jibon_content_arr.getJSONObject(i).getString("reference3");
                        appList.add(new AppData(contentid + "", contentTitle, contentDescription, japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image"), contentUrl, packageName, versionCode));
                    } else if (contentCat.equals("music_video")) {
                        if (contentType.equals("video")) {
                            musicVideoList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        } else {
                            audioList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        }
                    } else if (contentCat.equals("education")) {
                        educationList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeEducationRecyclerView();
                    }
                } catch (JSONException e) {
                    Log.d("japito_jibon_exception", e.toString());
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSliderContent(JSONArray jsonSliderContentArr) {
        sliderImages.clear();
        if (jsonSliderContentArr.length() > 0) {
            int contentPrice = 0;
            String contentType;
            for (int i = 0; i < jsonSliderContentArr.length(); i++) {
                try {
                    String contentCat = jsonSliderContentArr.getJSONObject(i).getString("contentCat");
                    if (contentCat.equals("slider")) {
                        String contentUrl = jsonSliderContentArr.getJSONObject(i).getString("contentUrl");
                        String description = jsonSliderContentArr.getJSONObject(i).getString("contentDescription");
                        String contentTitle = jsonSliderContentArr.getJSONObject(i).getString("contentTitle");
                        contentType = jsonSliderContentArr.getJSONObject(i).getString("contentType");
                        int contentId = jsonSliderContentArr.getJSONObject(i).getInt("id");
                        String contentDescription = jsonSliderContentArr.getJSONObject(i).getString("contentDescription");
                        String thumbNailImg = jsonSliderContentArr.getJSONObject(i).getString("thumbNail_image");
                        if (jsonSliderContentArr.getJSONObject(i).has("contentPrice")) {
                            contentPrice = jsonSliderContentArr.getJSONObject(i).getInt("contentPrice");
                        }
                        sliderImages.add(new Porashuna(contentTitle, contentType, description, contentUrl, thumbNailImg, contentCat, contentId));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SldrCntntExcptn", e.toString());
                }
            }
            initialCustomSwipeAdapter();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void initialCustomSwipeAdapter() {
        viewPager = findViewById(R.id.viewPager);
        customSwipeAdapter = new CustomSwipeAdapter(this, sliderImages);
        viewPager.setAdapter(customSwipeAdapter);
        CirclePageIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = MotionEventCompat.getActionMasked(motionEvent);
                switch(action) {
                    case (MotionEvent.ACTION_UP) :
                        Log.d("SWIPE","Action UP");
                        return true;
                    default :
                        return HomePage.super.onTouchEvent(motionEvent);
                }
            }
        });*/
    }

    public void initializeJapitoJibonRecyclerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RV_japitoJibon);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, japitoJibonArrayList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeHotNewsRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVhotnews);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, hotNewsArrayList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeEducationRecyclerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RV_ShikkhaSohayika);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, educationList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeAuttoHashiRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVAuttoahshi);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, auttoHashiArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeSportsRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVsports);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, sportsArralyList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeScienceRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVScience);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, scienceArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeMixedRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVMixed);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, mixedArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeKidsRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVKids);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, kidsArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void startGoromKhoborPage(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "news");
        startActivity(myIntent);
    }

    public void subscribe(View view) {
        Intent intentDailyLife = new Intent(HomePage.this, PaymentMethod.class);
        startActivity(intentDailyLife);
    }

    public void moreJapitoJibon(View view) {
        Intent intentDailyLife = new Intent(HomePage.this, PorashunaActivity.class);
        intentDailyLife.putExtra("tag", "daily_life");
        startActivity(intentDailyLife);
    }

    public void moreShikha(View view) {
        Intent intentDailyLife = new Intent(HomePage.this, PorashunaActivity.class);
        intentDailyLife.putExtra("tag", "education");
        startActivity(intentDailyLife);
    }

    public void startSportActivity(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "sports");
        startActivity(myIntent);
    }

    public void startPorashunaActivity(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "education");
        startActivity(myIntent);
    }

    public void startAuttoHashiActivity(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "autto_hashi");
        startActivity(myIntent);
    }

    public void startJibonJaponActivity(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "daily_life");
        startActivity(myIntent);
    }

    public void startPachMishaliActivity(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "mixed");
        startActivity(myIntent);
    }

    public void startBigganOProjuktiActivity(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "science");
        startActivity(myIntent);
    }

    public void startCartoonActivity(View view) {
        Intent myIntent = new Intent(HomePage.this, PorashunaActivity.class);
        myIntent.putExtra("tag", "kk_mela");
        startActivity(myIntent);
    }


    public void jumpToAppList(View view) {
        Intent intent = new Intent(HomePage.this, AppList.class);
        Gson gson = new Gson();
        String appListJson = gson.toJson(appList);
        intent.putExtra("appList", appListJson);
        startActivity(intent);
    }

    public void jumpToVideoList(View view) {
        Intent intent = new Intent(HomePage.this, Music_Video.class);
        Gson gson = new Gson();
        String videoJson = gson.toJson(musicVideoList);
        String audioJson = gson.toJson(audioList);
        intent.putExtra("videoList", videoJson);
        intent.putExtra("audioList", audioJson);
        startActivity(intent);
    }

    public void jumpToContact(View view) {
        //Toast.makeText(HomePage.this, "Emoticons will available soon!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomePage.this, ContactActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void processFinish(String output) {
        progressDialog.hideProgressDialog();
        if (output.contains("complete")) {
            Log.d("onProcessFinishedComplt", "onProcessFinishedComplt");
        }
    }

    public void jumpToAboutUs(View view) {
        Intent intent = new Intent(HomePage.this, AboutUs.class);
        startActivity(intent);
    }

    public void jumpToTerms(View view) {
        Intent intent = new Intent(HomePage.this, TermsActivity.class);
        startActivity(intent);
    }

    public void jumpToEmoticon(View view) {
        Intent intent = new Intent(HomePage.this, Emoticons.class);
        startActivity(intent);
    }
}
