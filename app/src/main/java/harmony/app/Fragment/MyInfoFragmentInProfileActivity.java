package harmony.app.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import harmony.app.R;
import harmony.app.Helper.Endpoints;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyInfoFragmentInProfileActivity extends Fragment {

    TextView nameTV, birthDateTV;
    RequestQueue queue;
    String phoneNumber;
    String userName;

    public MyInfoFragmentInProfileActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info_fragment_in_profile, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("phoneNumber", MODE_PRIVATE);
        phoneNumber = prefs.getString("phoneNo", "");
        queue = Volley.newRequestQueue(getActivity());
        nameTV = view.findViewById(R.id.nameTVinMyinfoFragment);
        birthDateTV = view.findViewById(R.id.birthDateTVMyInfoFragment);
        loadDataFromVolley();
        return view;
    }

    private void loadDataFromVolley() {
        String url = Endpoints.GET_USER_INFO_URL + phoneNumber;
        Log.d("profileData", url);
        Log.d("phoneNumber",phoneNumber);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responseprofile", response.toString());
                        try {
                            JSONObject postInfo = response.getJSONObject(0);
                            if (postInfo != null) {
                                birthDateTV.setText("   " + postInfo.getString("applicantBirthDate"));
                                userName=postInfo.getString("applicantBirthDate");

                                if (postInfo.has("partnerName") && postInfo.getString("partnerName")!=null)
                                {
                                    nameTV.setText("   " + postInfo.getString("partnerName"));
                                    ActivityCommunicator activityCommunicator=(ActivityCommunicator)getActivity();
                                    activityCommunicator.passDataToActivity("   " + postInfo.getString("partnerName"));
                                }

                                else
                                {
                                    nameTV.setText("   " + postInfo.getString("error"));
                                    ActivityCommunicator activityCommunicator=(ActivityCommunicator)getActivity();
                                    activityCommunicator.passDataToActivity("Error");
                                }

                            } else {
                                Log.d("json", "null");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
              Toast.makeText(getContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }
    public interface ActivityCommunicator{
         void passDataToActivity(String someValue);
    }
}
