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

public class MusicFragment extends Fragment {

    ListView listView;
    View view;
    ArrayList<MusicVideo> audioList;
    VideoListAdapter adapter;
    private RecyclerView horizontalListView;
    private RecyclerView verticalListView;
    ArrayList<MusicVideo> dataList;
    Map<String, ArrayList<MusicVideo>> dataMap;
    ArrayList<Icon> categoryDefList;
    Map<String, Icon> categoryMap;
    String contextType = "audio";
    ArrayList<SubscriptionConfig> subscriptionConfigList;
    Map<String, SubscriptionConfig> subscriptionConfigMap;
    ArrayList<Subscription> subscriptionList;
    Map<String, Subscription> subscriptionMap;

    public MusicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //view = inflater.inflate(R.layout.fragment_videolist, container, false);

        View rootView = null;

        audioList = new ArrayList<>();
        String value = getArguments().getString("audioListStr");
        Gson gson = new Gson();
        Type type = new TypeToken<List<MusicVideo>>() {
        }.getType();
        audioList = gson.fromJson(value, type);

        categoryDefList = new ArrayList<>();
        value = getArguments().getString("categoryDefStr");
        gson = new Gson();
        type = new TypeToken<List<Icon>>() {
        }.getType();
        categoryDefList = gson.fromJson(value, type);


        dataMap = new HashMap<String, ArrayList<MusicVideo>>();
        for(int i=0;i<audioList.size();i++){
            String key = audioList.get(i).getContentSubCat();
            if(dataMap.get(key) == null) {
                dataList = new ArrayList<MusicVideo>();
                dataList.add(audioList.get(i));
                dataMap.put(key, dataList);
            }
            else {
                dataList = dataMap.get(key);
                dataList.add(audioList.get(i));
                dataMap.put(key, dataList);
            }
        }

        categoryMap = new HashMap<String, Icon>();
        for(int i=0;i<categoryDefList.size();i++){
            String key = categoryDefList.get(i).getCategory();
            Icon icon = categoryDefList.get(i);
            categoryMap.put(key, icon);
        }

        subscriptionConfigList = new ArrayList<>();
        value = getArguments().getString("subscriptionConfigStr");
        gson = new Gson();
        type = new TypeToken<List<SubscriptionConfig>>() {
        }.getType();
        subscriptionConfigList = gson.fromJson(value, type);

        subscriptionConfigMap = new HashMap<String, SubscriptionConfig>();
        for(int i=0;i<subscriptionConfigList.size();i++){
            String itemType = subscriptionConfigList.get(i).getItemType();
            String itemSubCat = subscriptionConfigList.get(i).getItemSubcategory();
            if(itemType.equals("audio") && subscriptionConfigList.get(i).getStatus().equals("active")) {
                SubscriptionConfig config = subscriptionConfigList.get(i);
                subscriptionConfigMap.put(itemSubCat, config);
            }
        }

        subscriptionList = new ArrayList<>();
        value = getArguments().getString("subscriptionStr");
        gson = new Gson();
        type = new TypeToken<List<Subscription>>() {
        }.getType();
        subscriptionList = gson.fromJson(value, type);

        subscriptionMap = new HashMap<String, Subscription>();
        for(int i=0;i<subscriptionList.size();i++){
            String itemType = subscriptionList.get(i).getItemType();
            String itemSubCat = subscriptionList.get(i).getItemSubCategory();
            if(itemType.equals("audio")) {
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

        return rootView;
    }
}
