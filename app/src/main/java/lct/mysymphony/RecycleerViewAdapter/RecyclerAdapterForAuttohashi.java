package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import lct.mysymphony.Activity.ContentDescriptionActivity.AuttohasiDescriptionActivity;
import lct.mysymphony.ModelClass.Auttohashi;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForAuttohashi extends RecyclerView.Adapter<RecyclerAdapterForAuttohashi.RecyclerViewHolder> {

    Activity activity;
    private ArrayList<Auttohashi> auttohashiArrayList;

    public RecyclerAdapterForAuttohashi(Activity activity, ArrayList<Auttohashi> auttohashiArrayList) {
        this.activity = activity;
        this.auttohashiArrayList = auttohashiArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_auttohashi, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, auttohashiArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.auttohashiNewsTV.setText(auttohashiArrayList.get(position).getContentTitle());
        Glide.with(activity).load(auttohashiArrayList.get(position).getImageUrl()).into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return auttohashiArrayList.size();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView auttohashiNewsTV;
        ArrayList<Auttohashi> auttohashiArrayList;
        Activity activity;
        CardView cardView;
        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<Auttohashi> auttohashiArrayList) {
            super(view);
            imageView = view.findViewById(R.id.imgauttohashi);
            auttohashiNewsTV = view.findViewById(R.id.auttohashiNewsTV);
            cardView = view.findViewById(R.id.cardViewAuttoHashi);
            this.activity = activity;
            this.auttohashiArrayList = auttohashiArrayList;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, AuttohasiDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) auttohashiArrayList.get(getAdapterPosition()));
                    myIntent.putExtra("imageUrl", auttohashiArrayList.get(getAdapterPosition()).getImageUrl());
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
