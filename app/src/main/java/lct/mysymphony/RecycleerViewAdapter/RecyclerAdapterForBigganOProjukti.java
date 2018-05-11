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

import lct.mysymphony.Activity.ContentDescriptionActivity.BigganOProjuktiDescriptionActivity;
import lct.mysymphony.ModelClass.BigganOProjukti;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForBigganOProjukti extends RecyclerView.Adapter<RecyclerAdapterForBigganOProjukti.RecyclerViewHolder> {

    Activity activity;
    private ArrayList<BigganOProjukti> bigganOProjuktiArrayList;
    public RecyclerAdapterForBigganOProjukti(Activity activity,  ArrayList<BigganOProjukti> bigganOProjuktiArrayList)
    {
        this.activity = activity;
        this.bigganOProjuktiArrayList=bigganOProjuktiArrayList;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_biggan_o_projukti,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity,bigganOProjuktiArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.bigganOProjuktiNewsTV.setText(bigganOProjuktiArrayList.get(position).getContentTitle());
        Glide.with(activity)
                .load(bigganOProjuktiArrayList.get(position).getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bigganOProjuktiArrayList.size();
    }
    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView bigganOProjuktiNewsTV;
        ArrayList<BigganOProjukti> bigganOProjuktiArrayList;
        Activity activity;
        CardView cardView;
        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<BigganOProjukti> bigganOProjuktiArrayList)
        {
            super(view);
            imageView =  view.findViewById(R.id.imgbiggan_o_projukti);
            bigganOProjuktiNewsTV=view.findViewById(R.id.Biggan_o_projuktiNewsTV);
            cardView=view.findViewById(R.id.carddviewBigganOProjukti);
            this.activity=activity;
            this.bigganOProjuktiArrayList=bigganOProjuktiArrayList;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, BigganOProjuktiDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) bigganOProjuktiArrayList.get(getAdapterPosition()));
                    myIntent.putExtra("imageUrl",bigganOProjuktiArrayList.get(getAdapterPosition()).getImageUrl());
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
