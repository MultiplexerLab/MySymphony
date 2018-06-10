package sym.appstore.Activity;

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
import java.util.ArrayList;
import java.util.List;

import sym.appstore.ModelClass.SliderImage;
import sym.appstore.R;
import sym.appstore.ViewpagerAdapter.CustomSwipeAdapterIntro;
import sym.appstore.helper.Endpoints;

public class AboutUs extends AppCompatActivity {

    sym.appstore.helper.ProgressDialog progressDialog;
    RequestQueue queue;
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
}
