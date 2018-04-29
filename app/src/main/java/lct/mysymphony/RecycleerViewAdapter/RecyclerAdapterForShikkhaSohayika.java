package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import lct.mysymphony.Activity.ContentDescriptionActivity.ShikhaDetails;
import lct.mysymphony.ModelClass.ShikkhaSohaYika;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForShikkhaSohayika extends RecyclerView.Adapter<RecyclerAdapterForShikkhaSohayika.RecyclerViewHolder> {


    ArrayList<ShikkhaSohaYika> shikkhaSohaYikaArrayList;
    Activity activity;

    public RecyclerAdapterForShikkhaSohayika(Activity activity,ArrayList<ShikkhaSohaYika> shikkhaSohaYikaArrayList)
    {

        this.activity = activity;
        this.shikkhaSohaYikaArrayList=shikkhaSohaYikaArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_shikkha_sohayika,parent,false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, shikkhaSohaYikaArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        holder.news.setText(shikkhaSohaYikaArrayList.get(position).getContentTitle());

        Glide.with(activity)
                .load(shikkhaSohaYikaArrayList.get(position).getImageURL()).into(holder.imageView);

        if(shikkhaSohaYikaArrayList.get(position).getContentType().equals("video")){
            holder.videoPreviewPlayButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return shikkhaSohaYikaArrayList.size();
    }


    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView, videoPreviewPlayButton;
        TextView news;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<ShikkhaSohaYika> shikkhaSohaYikaArrayList)
        {
            super(view);

            news=view.findViewById(R.id.newsTV);
            imageView =  view.findViewById(R.id.imgShikkhaSohayika);
            videoPreviewPlayButton = view.findViewById(R.id.videoPreviewPlayButton);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, ShikhaDetails.class);
                    myIntent.putExtra("shikkha", (Serializable) shikkhaSohaYikaArrayList.get(getAdapterPosition()));
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });

        }
    }

}
