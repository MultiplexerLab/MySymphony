package harmony.app.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import harmony.app.Activity.ContentDescriptionActivity.VideoDescriptionActivity;
import harmony.app.Activity.PlayAudioActivity;
import harmony.app.Helper.DataHelper;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.R;

public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MusicVideo> mDataList;
    private int mRowIndex = -1;
    public static Context mContext;
    String contextType = null;
    boolean isSubscribed = false;

    public HorizontalAdapter(String type) {
        contextType = type;
    }

    public void setData(ArrayList<MusicVideo> data, boolean isSubs) {
        if (mDataList != data) {
            mDataList = data;
            isSubscribed = isSubs;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView videoThumbnail;
        private TextView appTitle;
        private TextView type;
        private TextView priceTag;

        public ItemViewHolder(View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            appTitle = itemView.findViewById(R.id.videoTitle);
            type = itemView.findViewById(R.id.contentType);
            priceTag = itemView.findViewById(R.id.priceTag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Intent myIntent = null;
            if(contextType.equals("audio")) {
                myIntent = new Intent(mContext, PlayAudioActivity.class);
                myIntent.putExtra("cameFromWhichActivity", "music_video");
                mDataList.get(pos).setSubscribed(isSubscribed);
                myIntent.putExtra("data", (Serializable) mDataList.get(pos));
            }
            else if(contextType.equals("video")){
                myIntent = new Intent(mContext, VideoDescriptionActivity.class);
                myIntent.putExtra("cameFromWhichActivity", "music_video");
                mDataList.get(pos).setSubscribed(isSubscribed);
                myIntent.putExtra("Data", (Serializable) mDataList.get(pos));
            }
            mContext.startActivity(myIntent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        View itemView = LayoutInflater.from(context).inflate(R.layout.video_list_item_modified, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder) rawHolder;
        DataHelper dataHelper = new DataHelper(mContext);
        if (mDataList.get(position).getThumbnailImgUrl().isEmpty() || mDataList.get(position).getThumbnailImgUrl().equals("null")) {
            if (mDataList.get(position).getContentType().equals("video")) {
                holder.videoThumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video_thumbnail));
            }
        } else {
            Glide.with(mContext).load(mDataList.get(position).getThumbnailImgUrl()).into(holder.videoThumbnail);
        }
        /*int price = mDataList.get(position).getContentPrice();
        if(price>0 && !isSubscribed) {
            if (dataHelper.checkDownLoadedOrNot(mDataList.get(position).getContentCat(), mDataList.get(position).getContentId()))
            {
                holder.priceTag.setVisibility(View.INVISIBLE);
            }else{
                holder.priceTag.setText("৳" + price);
            }
        }*/
        holder.appTitle.setText(mDataList.get(position).getContentTitle());
        holder.type.setText(mDataList.get(position).getContentType());
        this.setRowIndex(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}
