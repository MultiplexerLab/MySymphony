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

import lct.mysymphony.Activity.CartoonDescriptionActivity;
import lct.mysymphony.ModelClass.Cartoon;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForCartoon extends RecyclerView.Adapter<RecyclerAdapterForCartoon.RecyclerViewHolder> {




    Activity activity;
    private ArrayList<Cartoon> cartoonArrayList;


    public RecyclerAdapterForCartoon(Activity activity, ArrayList<Cartoon> cartoonArrayList)
    {

        this.activity = activity;
        this.cartoonArrayList=cartoonArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_cartoon,parent,false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity,cartoonArrayList);
        return recyclerViewHolder;
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {


        holder. cartoonNewsTV.setText(cartoonArrayList.get(position).getContentTitle());

        Glide.with(activity)
                .load(cartoonArrayList.get(position).getImageUrl())
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return cartoonArrayList.size();
    }


    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;
        TextView cartoonNewsTV;
        CardView cardView;
        Activity activity;
        ArrayList<Cartoon> cartoonArrayList;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<Cartoon> cartoonArrayList)
        {
            super(view);


            imageView =  view.findViewById(R.id.imgCartoon);
            cartoonNewsTV=view.findViewById(R.id. CartoonNewsTV);
            cardView=view.findViewById(R.id.cardViewCartoon);

            this.activity=activity;
            this.cartoonArrayList=cartoonArrayList;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, CartoonDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) cartoonArrayList.get(getAdapterPosition()));
                    myIntent.putExtra("imageUrl",cartoonArrayList.get(getAdapterPosition()).getImageUrl());
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });

        }
    }


}
