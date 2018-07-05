package harmony.app.Notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import harmony.app.Activity.ContactActivity;
import harmony.app.Activity.HomePage;
import harmony.app.helper.Endpoints;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //sendTokenToServer(refreshedToken);
        Log.i(TAG, "onTokenRefresh: " + refreshedToken);
    }

    public void sendTokenToServer(final String refreshedToken) {
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.FIREBASE_TOKEN_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseFirebase", response);
                if (response.contains("SUCCESS")) {
                    Log.i("ResponseFirebase", "SUCCESS");
                } else {
                    Log.i("ResponseFirebase", "FAILED");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorVolley", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                String deviceId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Map<String, String> params = new HashMap<>();
                params.put("userId", deviceId);
                params.put("refresh_token", refreshedToken);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}