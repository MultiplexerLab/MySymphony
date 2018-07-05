package harmony.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import harmony.app.R;
import harmony.app.helper.AppLogger;
import harmony.app.helper.Endpoints;

public class TermsActivity extends AppCompatActivity {
    RequestQueue queue;
    Date currenTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        queue = Volley.newRequestQueue(this);
        getInstanceInfo();
    }

    private void getInstanceInfo() {

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_APP_INFO, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0; i<response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.i("Pname", jsonObject.toString());
                        if(jsonObject.getString("pName").equals("appTermsAndCond")){
                            String termsUrl = jsonObject.getString("pValue");
                            WebView webView = findViewById(R.id.webView);
                            webView.loadUrl(termsUrl);
                            webView.setWebViewClient(new WebViewClient());
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
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", "Terms",
                "IN", "Entrance", "page");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "Y", "Terms",
                "LEAVE", "Leave", "page");
    }
}
