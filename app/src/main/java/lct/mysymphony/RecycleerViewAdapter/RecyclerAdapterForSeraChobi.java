package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForSeraChobi extends RecyclerView.Adapter<RecyclerAdapterForSeraChobi.RecyclerViewHolder> {

    ///private int[] images = {R.drawable.sera_chobi_1, R.drawable.sera_chobi_2, R.drawable.sera_chobi_3, R.drawable.sera_chobi_4};

    ArrayList<String> images;
    Activity activity;


    public RecyclerAdapterForSeraChobi(Activity activity, ArrayList<String> images) {

        this.activity = activity;
        this.images = images;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_sera_chobi, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, images);
        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        Glide.with(activity)
                .load(images.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(images.size()>5) {
            return 5;
        }else{
            return images.size();
        }
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<String> images) {
            super(view);
            imageView = view.findViewById(R.id.img);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, ImageViewActivity.class);
                    myIntent.putExtra("wallpaper", (Serializable) images.get(getAdapterPosition()));
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });
        }
    }


}
