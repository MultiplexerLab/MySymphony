package sym.appstore.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sym.appstore.ModelClass.KheladhulaCricketSorbosheshSongbad;
import sym.appstore.R;
import sym.appstore.RecyclerViewAdapter.RecyclerAdapterForKheladhulaCricket;
import sym.appstore.RecyclerViewAdapter.RecyclerAdapterForSorbosheshSongbadCricketfragment;
import sym.appstore.helper.Endpoints;

/**
 * A simple {@link Fragment} subclass.
 */
public class CricketFragment extends Fragment {

    private RecyclerView recyclerViewForWallPaper;
    private RecyclerView.Adapter adapterForWallPaper;

    private RecyclerView recyclerViewForSorbosheshSongbad;
    private RecyclerView.Adapter adapterForSorbosheshSongbad;

    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<KheladhulaCricketSorbosheshSongbad> kheladhulaCricketArrayList;
    RequestQueue queue;

    public CricketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cricket_fragment, container, false);
        kheladhulaCricketArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(getActivity());
        loadDataFromVolley();
        recyclerViewForSorbosheshSongbad = view.findViewById(R.id.RV_sorboshesh_songbad_cricket_fragment);

        ArrayList<String> wallpaperData = new ArrayList<>();
        wallpaperData.add("সওয়ালপেপার বান্ডিল");
        wallpaperData.add("এক্সক্লুসিভ গান ");
        wallpaperData.add("এক্সক্লুসিভ মুস্তাফিজ");

        recyclerViewForWallPaper = view.findViewById(R.id.RV_wallpaper_cricket_fragment);
        recyclerViewForWallPaper.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStartMulloChar = new GravitySnapHelper(Gravity.START);
        snapHelperStartMulloChar.attachToRecyclerView(recyclerViewForWallPaper);
        recyclerViewForWallPaper.setHasFixedSize(true);
        adapterForWallPaper = new RecyclerAdapterForKheladhulaCricket(getActivity(), wallpaperData);
        recyclerViewForWallPaper.setAdapter(adapterForWallPaper);
        return view;
    }

    private void loadDataFromVolley() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.KHELADHULA_CRICKET_GET_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray pachMishaliContentArr = response.getJSONArray("contents");
                            setporashunaiContent(pachMishaliContentArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void setporashunaiContent(JSONArray jsonHotNewsContentArr) {

        if (jsonHotNewsContentArr.length() > 0) {
            for (int i = 0; i < jsonHotNewsContentArr.length(); i++) {
                try {
                    String imageUrl = jsonHotNewsContentArr.getJSONObject(i).getString("imageUrl");
                    String contentTitle = jsonHotNewsContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = jsonHotNewsContentArr.getJSONObject(i).getString("contentType");
                    String contentDescription = jsonHotNewsContentArr.getJSONObject(i).getString("contentDescription");
                    String publishedAt = jsonHotNewsContentArr.getJSONObject(i).getString("publishedAt");
                    int contentId=jsonHotNewsContentArr.getJSONObject(i).getInt("contentId");
                    String thumbNail_image;
                    if (jsonHotNewsContentArr.getJSONObject(i).has("thumbNail_image"))
                        thumbNail_image=jsonHotNewsContentArr.getJSONObject(i).getString("thumbNail_image");
                    else
                        thumbNail_image="empty";

                    kheladhulaCricketArrayList.add(new KheladhulaCricketSorbosheshSongbad(contentTitle, contentType, publishedAt, contentDescription, thumbNail_image, imageUrl,"Cricket_sorboshesh_songbad",contentId));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exceptionIncricket",e.toString());
                }
            }
            initializerRecyclerView();

        } else {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializerRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForSorbosheshSongbad.setLayoutManager(mLayoutManager);
        recyclerViewForSorbosheshSongbad.setHasFixedSize(true);
        adapterForSorbosheshSongbad = new RecyclerAdapterForSorbosheshSongbadCricketfragment(getActivity(), kheladhulaCricketArrayList);
        recyclerViewForSorbosheshSongbad.setAdapter(adapterForSorbosheshSongbad);
    }
}
