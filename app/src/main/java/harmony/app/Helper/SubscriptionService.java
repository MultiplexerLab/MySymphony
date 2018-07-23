package harmony.app.Helper;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import harmony.app.ModelClass.MusicVideo;
import harmony.app.ModelClass.Subscription;
import harmony.app.ModelClass.SubscriptionConfig;

public class SubscriptionService {

    RequestQueue queue;
    ArrayList<Subscription> subscriptionList;

    public void insertSubscription(final Context context, final Subscription subscription){

        queue = Volley.newRequestQueue(context);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.SUBSCRIPTION_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("insertSubscription", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("insertSubscription", error.toString());
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                String deviceId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                Calendar startDate, endDate;
                DateFormat dateFormat;
                dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                startDate = Calendar.getInstance();
                endDate = Calendar.getInstance();

                startDate.setTime(new Date());
                endDate.setTime(new Date());

                if(subscription.getSubscriptionType().equals("weekly")) {
                    endDate.add(Calendar.DATE, 7);
                }
                else if(subscription.getSubscriptionType().equals("monthly")) {
                    endDate.add(Calendar.MONTH, 1);
                }
                else if(subscription.getSubscriptionType().equals("yearly")) {
                    endDate.add(Calendar.MONTH, 12);
                }

                String sDate = dateFormat.format(startDate.getTime());
                String eDate = dateFormat.format(endDate.getTime());

                Map<String, String> params = new HashMap<>();
                params.put("userId", deviceId);
                params.put("deviceid", deviceId);
                params.put("subscribedFor", subscription.getSubscribedFor());
                params.put("itemType", subscription.getItemType());
                params.put("itemName", subscription.getItemName());
                params.put("itemCategory", subscription.getItemCategory());
                params.put("itemSubCategory", subscription.getItemSubCategory());
                params.put("itemPrice", subscription.getItemPrice());
                params.put("subscriptionType", subscription.getSubscriptionType());
                params.put("startDate", sDate);
                params.put("endDate", eDate);
                params.put("paymentID", subscription.getPaymentID());
                params.put("paymentMethod", subscription.getPaymentMethod());

                Log.i("insertSubscription", params.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public boolean isSubscribed(Map<String, Subscription> subscription, String key){

        Calendar currentDate, startDate, endDate;
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        endDate.set(Calendar.MILLISECOND, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.HOUR, 0);
        currentDate.setTime(new Date());

        if(subscription.get(key) != null) {

            String endArr[] = subscription.get(key).getEndDate().split("T");

            try {
                endDate.setTime(dateFormat.parse(endArr[0]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(subscription.get(key) != null && endDate.getTimeInMillis() > currentDate.getTimeInMillis())
            return true;
        else
            return false;
    }
}
