package harmony.app.Fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import harmony.app.Activity.HomePage;
import harmony.app.R;
import harmony.app.Helper.Endpoints;
import harmony.app.Helper.ProgressDialog;

public class PaymentListFragmentInProfileActivity extends Fragment {

    public PaymentListFragmentInProfileActivity() {

    }
    ListView listViewPayment;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.fragment_payment_list_fragment_in_profile, container, false);
        listViewPayment = customView.findViewById(R.id.listViewPayment);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.payment_list_item, arrayList);
        listViewPayment.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        if(internetConnected()) {
            getPaymentData();
        }
        return customView;
    }

    private boolean internetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    private void getPaymentData() {
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        progressDialog.showProgressDialog();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_PAYMENT_INFO+"&key=userId&val="+deviceId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String transactinDate = jsonObject.getString("transactionDate");
                        String arr[] = transactinDate.split("T");

                        arrayList.add("Amount: "+jsonObject.getString("amount")+"\n"+
                                "Payment Method: "+jsonObject.getString("paymentMethod")+"\n"+
                                "Payment Date: "+arr[0]);
                        adapter.notifyDataSetChanged();
                    }
                    progressDialog.hideProgressDialog();
                } catch (Exception e) {
                    Log.d("ExceptionPaymentGet", e.toString());
                    progressDialog.hideProgressDialog();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ExceptionPaymentGet", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(getActivity(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }
}
