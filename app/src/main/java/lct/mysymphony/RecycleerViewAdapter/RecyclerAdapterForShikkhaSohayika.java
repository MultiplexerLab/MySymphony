package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;
import java.util.ArrayList;

import lct.mysymphony.Activity.ContentDescriptionActivity.ShikkhaSohaYikaDescriptionActivity;
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
                .load(shikkhaSohaYikaArrayList.get(position).getImageURL())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);

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
        ProgressBar progressBar;
        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<ShikkhaSohaYika> shikkhaSohaYikaArrayList)
        {
            super(view);
            news=view.findViewById(R.id.newsTV);
            imageView =  view.findViewById(R.id.imgShikkhaSohayika);
            videoPreviewPlayButton = view.findViewById(R.id.videoPreviewPlayButton);
            progressBar=view.findViewById(R.id.progressBarInShikkhaSohayika);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, ShikkhaSohaYikaDescriptionActivity.class);
                    myIntent.putExtra("shikkha", (Serializable) shikkhaSohaYikaArrayList.get(getAdapterPosition()));
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
