package harmony.app.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import harmony.app.ModelClass.SliderImage;
import harmony.app.R;
import harmony.app.ViewpagerAdapter.CustomSwipeAdapterIntro;
import harmony.app.Helper.AppLogger;
import harmony.app.Helper.Endpoints;

public class AboutUs extends AppCompatActivity {

    harmony.app.Helper.ProgressDialog progressDialog;
    RequestQueue queue;
    Date currenTime;
    DateFormat dateFormat;
    ArrayList<SliderImage> sliderImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        queue = Volley.newRequestQueue(AboutUs.this);
        getIntroInfo();
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
                    Log.d("exceptionLoadData4rmvly", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
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

    @Override
    protected void onResume() {
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "N", "AboutUs",
                "IN", "Entrance", "page");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(this, dateFormat.format(currenTime), "Y", "AboutUs",
                "LEAVE", "Leave", "page");
    }
}
