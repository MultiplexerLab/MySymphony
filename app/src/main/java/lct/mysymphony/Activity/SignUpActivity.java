package lct.mysymphony.Activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import lct.mysymphony.R;
import lct.mysymphony.helper.Endpoints;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private android.support.v7.widget.Toolbar toolbar;
    RequestQueue queue;
    EditText DOB;
    String DOBTV = "";
    String phoneNumber = "";
    private EditText txtpassword, txtUserName;
    String[] permissions = new String[]{Manifest.permission.ACCESS_NETWORK_STATE,};
    lct.mysymphony.helper.ProgressDialog progressDialog;
    String emailId, devicePhoneNumber, deviceName, osVersion, carrierName;
    Pattern pattern;
    Account[] account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = findViewById(R.id.toolbarlayoutinmainactivity);
        setSupportActionBar(toolbar);
        DOB = findViewById(R.id.txtDOB);
        txtpassword = findViewById(R.id.txtpassword);
        txtUserName = findViewById(R.id.txtUserName);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        queue = Volley.newRequestQueue(SignUpActivity.this);
        checkPermissions();
        progressDialog = new lct.mysymphony.helper.ProgressDialog(this);

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                emailId = account.name;
                Log.i("GmailId", emailId);
            }
        }

        pattern = Patterns.EMAIL_ADDRESS;
        enableRuntimePermission();
        getAccountsName();
    }

    public void getAccountsName() {
        try {
            account = AccountManager.get(SignUpActivity.this).getAccounts();
        } catch (SecurityException e) {
            Log.e("ExecptionAcc", e.toString());

        }

        for (Account TempAccount : account) {
            if (pattern.matcher(TempAccount.name).matches()) {
                Log.i("Emails", TempAccount.name);
            }
        }
    }

    public void enableRuntimePermission() {

        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]
                {
                        GET_ACCOUNTS,
                        READ_PHONE_STATE
                }, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    boolean GetAccountPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (GetAccountPermission && ReadPhoneStatePermission) {
                        Toast.makeText(SignUpActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(myIntent);
        ;
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

    public void signUpRequest(View view) {
        if (internetConnected()) {
            progressDialog.showProgressDialog();
            String name = txtUserName.getText().toString();
            String password = txtpassword.getText().toString();
            if (name.isEmpty() || password.isEmpty() || DOBTV.isEmpty()) {
                if (progressDialog.getAlertDialog() != null)
                    progressDialog.hideProgressDialog();
                Toast.makeText(this, "সবগুলো তথ্য প্রদান করুন", Toast.LENGTH_SHORT).show();
            } else {
                signInRequest(name, password, phoneNumber);
            }
        } else {
            Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DOBTV = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        DOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }

    public void showDatePickerDialog(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(SignUpActivity.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void signUpRequest(final String name, final String password, final String phoneNumber) {
        String url = "http://bot.sharedtoday.com:9500/ws/commonUpdateForArrayJSON?tbl=Partner&keyname=partnerId&id=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseSignUp", response);
                if (response.contains("SUCCESS")) {
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("username", txtUserName.getText().toString());
                    editor.apply();
                    sendUpdatedPasswordToserver();
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
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();
                Map<String, String> params = new HashMap<>();
                params.put("partnerType", "Retailer");
                params.put("defaultPassword", password);
                params.put("partnerStatus", "Signup complete");
                params.put("applicantBirthDate", DOBTV);
                params.put("partnerId", phoneNumber);
                params.put("partnerName", name);
                params.put("contactNum", devicePhoneNumber);
                params.put("email", emailId);
                params.put("registrationDate", dateFormat.format(date));
                params.put("accountName", carrierName);
                params.put("partnerIdentificationInfo", osVersion);
                params.put("channel", deviceName);
                return params;
            }
        };
        queue.add(stringRequest);

    }

    public void signInRequest(final String name, final String password, final String phoneNumber) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.USER_SIGN_IN_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseInSignIn", response);

                if (response.contains("SUCCESS")) {
                    if (internetConnected()) {
                        signUpRequest(name, password, phoneNumber);
                    } else {
                        progressDialog.hideProgressDialog();
                        Toast.makeText(SignUpActivity.this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.hideProgressDialog();
                    Toast.makeText(SignUpActivity.this, "আপনার তথ্য সঠিক নয় ", Toast.LENGTH_SHORT).show();
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
                params.put("userid", phoneNumber);
                params.put("password", "01717");
                return params;
            }
        };
        queue.add(stringRequest);

    }

    public void sendUpdatedPasswordToserver() {
        String url = Endpoints.UPDATE_DEFAULT_PASSWORD_POST_URL + txtpassword.getText().toString() + "&userid=" + phoneNumber;
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
                    Toast.makeText(SignUpActivity.this, "আপনার সাইন আপ সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, HomePage.class);
                    startActivity(intent);
                } else {
                    progressDialog.hideProgressDialog();
                    Toast.makeText(SignUpActivity.this, "আপনার সাইন আপ সম্পন্ন হয়নি\n" + "আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
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

    public void guestLogin(View view) {
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        String userName = prefs.getString("username", "");
        if (userName.equals("Guest")) {
            Intent intent = new Intent(SignUpActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        } else {
            if (internetConnected()) {
                progressDialog.showProgressDialog();
                signInRequest("Guest", "default", phoneNumber);
            } else
                Toast.makeText(this, "ইন্টারনেট সংযোগ করে চেষ্টা করুন", Toast.LENGTH_SHORT).show();

        }
    }

}
