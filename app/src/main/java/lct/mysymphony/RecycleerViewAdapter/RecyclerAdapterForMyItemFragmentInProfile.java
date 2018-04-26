package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lct.mysymphony.ModelClass.Cartoon;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForMyItemFragmentInProfile extends RecyclerView.Adapter<RecyclerAdapterForMyItemFragmentInProfile.RecyclerViewHolder> {




    Activity activity;
    private ArrayList<Cartoon> cartoonArrayList;


    public RecyclerAdapterForMyItemFragmentInProfile(Activity activity, ArrayList<Cartoon> cartoonArrayList)
    {

        this.activity = activity;
        this.cartoonArrayList=cartoonArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_cartoon,parent,false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
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

        public RecyclerViewHolder(View view)
        {
            super(view);


            imageView =  view.findViewById(R.id.imgCartoon);
            cartoonNewsTV=view.findViewById(R.id. CartoonNewsTV);

        }
    }


}
