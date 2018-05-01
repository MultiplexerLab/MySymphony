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

import lct.mysymphony.Activity.ContentDescriptionActivity.NewsDescriptionActivity;
import lct.mysymphony.ModelClass.GoromKhobor;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForGoromKhobor extends RecyclerView.Adapter<RecyclerAdapterForGoromKhobor.RecyclerViewHolder> {


   /// private int[] images = {R.drawable.japito_jibon_1, R.drawable.japito_jibon_2};

    Activity activity;
    private ArrayList<GoromKhobor> goromKhoborArrayList;


    public RecyclerAdapterForGoromKhobor(Activity activity,ArrayList<GoromKhobor> goromKhoborArrayList) {

        this.activity = activity;
        this.goromKhoborArrayList=goromKhoborArrayList;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_gorom_khobor, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity, goromKhoborArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        ///holder.japitoJebonNewsTV.setText(japitoJibonMCArrayList.get(position).getContentTitle());
        holder.goromKhoborTitle.setText(goromKhoborArrayList.get(position).getContentTitle());
        holder.publishedAt.setText(goromKhoborArrayList.get(position).getPublishedAt());
        Glide.with(activity)
                .load(goromKhoborArrayList.get(position).getImageUrl())
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
    }

    @Override
    public int getItemCount() {
        return goromKhoborArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;
        Activity activity;
        TextView goromKhoborTitle,publishedAt;
        ArrayList<GoromKhobor> goromKhoborArrayList;
        ProgressBar progressBar;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<GoromKhobor> goromKhoborArrayList) {
            super(view);

            this.activity=activity;
            this.goromKhoborArrayList=goromKhoborArrayList;
            progressBar=view.findViewById(R.id.progressBarInGoromKhobor);

            imageView = view.findViewById(R.id.imgGoromKhobor);
            goromKhoborTitle=view.findViewById(R.id.titleGoromKhobor);
            publishedAt=view.findViewById(R.id.publishedAtGoromKhobor);
            cardView=view.findViewById(R.id.goromKhoborCardView);
            ///japitoJebonNewsTV = view.findViewById(R.id.japitoJibonNewsTV);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, NewsDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) goromKhoborArrayList.get(getAdapterPosition()));
                    
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();


                }
            });

        }
    }


}
