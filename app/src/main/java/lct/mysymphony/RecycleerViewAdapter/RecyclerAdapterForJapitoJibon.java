package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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

import lct.mysymphony.Activity.ContentDescriptionActivity.JapitiJibonDescriptionActivity;
import lct.mysymphony.R;
import lct.mysymphony.ModelClass.JapitoJibonMC;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForJapitoJibon extends RecyclerView.Adapter<RecyclerAdapterForJapitoJibon.RecyclerViewHolder> {


    ///private int[] images = {R.drawable.japito_jibon_1, R.drawable.japito_jibon_2};

    Activity activity;
    private ArrayList<JapitoJibonMC> japitoJibonMCArrayList;


    public RecyclerAdapterForJapitoJibon(Activity activity, ArrayList<JapitoJibonMC> japitoJibonMCArrayLis) {

        this.activity = activity;
        this.japitoJibonMCArrayList = japitoJibonMCArrayLis;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_japito_jibon, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, japitoJibonMCArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        holder.japitoJebonNewsTV.setText(japitoJibonMCArrayList.get(position).getContentTitle());

        Glide.with(activity)
                .load(japitoJibonMCArrayList.get(position).getImageUrl())
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

        if (japitoJibonMCArrayList.get(position).getContentType().equals("video")) {
            holder.videoPreviewPlayButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(japitoJibonMCArrayList.size()>3){
            return 3;
        }else{
            return japitoJibonMCArrayList.size();
        }
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, videoPreviewPlayButton;
        TextView japitoJebonNewsTV;
        ProgressBar progressBar;

        Activity activity;
        ArrayList<JapitoJibonMC> japitoJibonMCArrayList;
        CardView cardView;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<JapitoJibonMC> japitoJibonMCArrayLis) {
            super(view);

            imageView = view.findViewById(R.id.imgJapitoJibon);
            japitoJebonNewsTV = view.findViewById(R.id.japitoJibonNewsTV);
            videoPreviewPlayButton = view.findViewById(R.id.videoPreviewPlayButton);
            cardView = view.findViewById(R.id.cardviewJapitojibon);
            progressBar=view.findViewById(R.id.progressBarInJapitoJibon);

            this.activity = activity;
            this.japitoJibonMCArrayList = japitoJibonMCArrayLis;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, JapitiJibonDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) japitoJibonMCArrayLis.get(getAdapterPosition()));
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });
        }
    }

}
