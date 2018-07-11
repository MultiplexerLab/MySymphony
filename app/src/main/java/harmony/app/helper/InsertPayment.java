package harmony.app.helper;

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
import java.util.HashMap;
import java.util.Map;


public class InsertPayment {

    public static void insertPayment(final Context context, final int contentId, final float contentPrice, final String paymentID, final String paymentMethod, final String referenceCode, final String userId, final String contentTitle) {
        final RequestQueue queue;
        queue = Volley.newRequestQueue(context);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.PAYMENT_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("PaymentData", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("errorRespose", error.toString());
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                String deviceId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Map<String, String> params = new HashMap<>();
                params.put("userId", deviceId);
                params.put("refCode", referenceCode);
                params.put("transactionId", paymentID);
                params.put("amount", contentPrice+"");
                params.put("paymentMethod", paymentMethod);
                params.put("contentId", contentId+"");
                params.put("deviceId", deviceId);
                params.put("transactionStatus", "Completed");
                params.put("customerId", userId);
                if(contentTitle==null || contentTitle.isEmpty()){
                }else{
                    params.put("contentName", contentTitle);
                }

                Log.i("PaymentParams", params.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
