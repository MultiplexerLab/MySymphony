package sym.appstore.helper;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sym.appstore.Activity.WelComeActivity;

public class AppLogger {

    public static void insertLogs(final Context context, final String startTime, final String tagEndTime, final String procName, final String event,
                                  final String message){
        final RequestQueue queue;
        queue = Volley.newRequestQueue(context);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.APPLOGS_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LogResponse", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("errorRespose", error.toString());
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();
                String deviceId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Map<String, String> params = new HashMap<>();
                params.put("startTime", startTime);
                if(tagEndTime.equals("Y")) {
                    params.put("endTime", dateFormat.format(date));
                }else{
                    params.put("endTime", "");
                }
                params.put("procName", procName);
                params.put("event", event);
                params.put("deviceId", deviceId);
                params.put("userId", deviceId);
                params.put("type", "test");
                params.put("msg", message);

                Log.i("ParamsNew", params.toString());
                return params;
            }
        };
        queue.add(stringRequest);

    }
}
