package harmony.app.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import harmony.app.Activity.SubscriptionActivity;
import harmony.app.Helper.SubscriptionService;
import harmony.app.ModelClass.Icon;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.ModelClass.Subscription;
import harmony.app.ModelClass.SubscriptionConfig;
import harmony.app.R;

public class VerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Map<String, ArrayList<MusicVideo>> dataMap;
    int globalPos = -1;
    Map<String, Icon> categoryMap;
    String contextType = null;
    String itemSubcat = null;
    Context mContext;
    RequestQueue queue;
    ArrayList<SubscriptionConfig> subscriptionConfigList;
    Map<String, SubscriptionConfig> subscriptionConfigMap;
    Map<String, Subscription> subscriptionMap;

    public VerticalAdapter() {
    }

    public void setData(Map<String, ArrayList<MusicVideo>> data, Map<String, Icon> map, Map<String, SubscriptionConfig> configMap, Map<String, Subscription> subsMap, String type) {
        dataMap = data;
        categoryMap = map;
        contextType = type;
        subscriptionConfigMap = configMap;
        subscriptionMap = subsMap;
        notifyDataSetChanged();
    }

    private class HorizontalListViewHolder extends RecyclerView.ViewHolder {

        private TextView title, isSubscribed;
        private RecyclerView horizontalList;
        private HorizontalAdapter horizontalAdapter;
        private Button subscribeBtn;

        public HorizontalListViewHolder(View itemView, String contextType) {
            super(itemView);
            Context context = itemView.getContext();
            title = itemView.findViewById(R.id.subCategoryName);
            isSubscribed = itemView.findViewById(R.id.isSubscribeText);
            horizontalList = (RecyclerView) itemView.findViewById(R.id.horizontalListView);
            subscribeBtn = itemView.findViewById(R.id.subscribeBtn);

            horizontalList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new HorizontalAdapter(contextType);
            horizontalList.setAdapter(horizontalAdapter);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_videolist, parent, false);
        HorizontalListViewHolder holder = new HorizontalListViewHolder(itemView, contextType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        HorizontalListViewHolder holder = (HorizontalListViewHolder) rawHolder;
        Set<String> keys = dataMap.keySet();
        globalPos = -1;
        SubscriptionService service = new SubscriptionService();
        for (String key : keys) {
            globalPos++;
            itemSubcat = key;
            final String keyVal = key;
            if(globalPos == position) {
                String title = categoryMap.get(key).getCategoryTitle();
                holder.title.append(title);
                final SubscriptionConfig config = subscriptionConfigMap.get(key);
                boolean isSubscribed = service.isSubscribed(subscriptionMap, key);
                holder.horizontalAdapter.setData(dataMap.get(key), isSubscribed);
                if(config != null && contextType.equals(config.getItemType()) && !isSubscribed) {
                    holder.subscribeBtn.setVisibility(View.VISIBLE);
                    holder.subscribeBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent myIntent = null;
                            myIntent = new Intent(mContext, SubscriptionActivity.class);
                            myIntent.putExtra("itemType", contextType);
                            myIntent.putExtra("itemSubCat", keyVal);
                            myIntent.putExtra("config", (Serializable) config);
                            mContext.startActivity(myIntent);
                        }
                    });
                }
                else {
                    if(isSubscribed) {
                        holder.isSubscribed.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataMap.size();
    }
}
