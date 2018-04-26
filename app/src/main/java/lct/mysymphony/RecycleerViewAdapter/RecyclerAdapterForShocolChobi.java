package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.ModelClass.ShocolChobi;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForShocolChobi extends RecyclerView.Adapter<RecyclerAdapterForShocolChobi.RecyclerViewHolder> {


    ///private int[] images = {R.drawable.gif_1, R.drawable.gif_2, R.drawable.gif_1, R.drawable.gif_2};

    Activity activity;
    ArrayList<ShocolChobi> shocolChobiArrayList;


    public RecyclerAdapterForShocolChobi(Activity activity, ArrayList<ShocolChobi> shocolChobiArrayList) {

        this.activity = activity;
        this.shocolChobiArrayList = shocolChobiArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_shocol_chobi, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, shocolChobiArrayList);
        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {


        Glide.with(activity)
                .load(shocolChobiArrayList.get(position).getContentUrl())
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return shocolChobiArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Activity activity;
        ArrayList<ShocolChobi> shocolChobiArrayList;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<ShocolChobi> shocolChobiArrayList) {
            super(view);


            imageView = view.findViewById(R.id.imgShocholChobi);

            this.activity = activity;
            this.shocolChobiArrayList = shocolChobiArrayList;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, ImageViewActivity.class);
                    myIntent.putExtra("wallpaper", shocolChobiArrayList.get(getAdapterPosition()).getContentUrl());
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });
        }

    }
}

