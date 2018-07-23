package harmony.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import harmony.app.Helper.SubscriptionService;
import harmony.app.ModelClass.Icon;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.ModelClass.Subscription;
import harmony.app.ModelClass.SubscriptionConfig;
import harmony.app.R;
import harmony.app.RecyclerViewAdapter.VerticalAdapter;
import harmony.app.RecyclerViewAdapter.VideoListAdapter;

public class VideoFragment extends Fragment {

    ListView listView;
    View view;
    ArrayList<MusicVideo> videoList;
    VideoListAdapter adapter;
    private RecyclerView verticalListView;
    ArrayList<MusicVideo> dataList;
    Map<String, ArrayList<MusicVideo>> dataMap;
    ArrayList<Icon> categoryDefList;
    Map<String, Icon> categoryMap;
    View rootView = null;
    String contextType = "video";
    ArrayList<SubscriptionConfig> subscriptionConfigList;
    Map<String, SubscriptionConfig> subscriptionConfigMap;
    ArrayList<Subscription> subscriptionList;
    Map<String, Subscription> subscriptionMap;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String value = getArguments().getString("videoListStr");
        Gson gson = new Gson();
        Type type = new TypeToken<List<MusicVideo>>() {
        }.getType();
        videoList = gson.fromJson(value, type);

        value = getArguments().getString("categoryDefStr");
        gson = new Gson();
        type = new TypeToken<List<Icon>>() {
        }.getType();
        categoryDefList = gson.fromJson(value, type);


        dataMap = new HashMap<String, ArrayList<MusicVideo>>();
        for(int i=0;i<videoList.size();i++){
            String key = videoList.get(i).getContentSubCat();
            if(dataMap.get(key) == null) {
                dataList = new ArrayList<MusicVideo>();
                dataList.add(videoList.get(i));
                dataMap.put(key, dataList);
            }
            else {
                dataList = dataMap.get(key);
                dataList.add(videoList.get(i));
                dataMap.put(key, dataList);
            }
        }

        categoryMap = new HashMap<String, Icon>();
        for(int i=0;i<categoryDefList.size();i++){
            String key = categoryDefList.get(i).getCategory();
            Icon icon = categoryDefList.get(i);
            categoryMap.put(key, icon);
        }

        value = getArguments().getString("subscriptionConfigStr");
        gson = new Gson();
        type = new TypeToken<List<SubscriptionConfig>>() {
        }.getType();
        subscriptionConfigList = gson.fromJson(value, type);

        subscriptionConfigMap = new HashMap<String, SubscriptionConfig>();
        for(int i=0;i<subscriptionConfigList.size();i++){
            String itemType = subscriptionConfigList.get(i).getItemType();
            String itemSubCat = subscriptionConfigList.get(i).getItemSubcategory();
            if(itemType.equals("video") && subscriptionConfigList.get(i).getStatus().equals("active")) {
                SubscriptionConfig config = subscriptionConfigList.get(i);
                subscriptionConfigMap.put(itemSubCat, config);
            }
        }

        value = getArguments().getString("subscriptionStr");
        gson = new Gson();
        type = new TypeToken<List<Subscription>>() {
        }.getType();
        subscriptionList = gson.fromJson(value, type);

        subscriptionMap = new HashMap<String, Subscription>();
        for(int i=0;i<subscriptionList.size();i++){
            String itemType = subscriptionList.get(i).getItemType();
            String itemSubCat = subscriptionList.get(i).getItemSubCategory();
            if(itemType.equals("video")) {
                Subscription subscription = subscriptionList.get(i);
                subscriptionMap.put(itemSubCat, subscription);
            }
        }


        rootView = inflater.inflate(R.layout.common_linner_layout, null, false);
        verticalListView = (RecyclerView) rootView.findViewById(R.id.veryicalListView);
        verticalListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        VerticalAdapter verticalAdapter = new VerticalAdapter();
        verticalAdapter.setData(dataMap, categoryMap, subscriptionConfigMap, subscriptionMap, contextType);
        verticalListView.setAdapter(verticalAdapter);

        /*listView = view.findViewById(R.id.listViewVideo);

        adapter = new VideoListAdapter(getActivity(), videoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(getActivity(), VideoDescriptionActivity.class);
                myIntent.putExtra("Data", (Serializable) videoList.get(i));
                myIntent.putExtra("cameFromWhichActivity", "music_video");
                startActivity(myIntent);
            }
        });*/

        return rootView;
    }
}
