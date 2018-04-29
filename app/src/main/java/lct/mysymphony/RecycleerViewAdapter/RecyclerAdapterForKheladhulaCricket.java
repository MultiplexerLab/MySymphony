package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lct.mysymphony.Activity.WallpaperBundleActivity;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForKheladhulaCricket extends RecyclerView.Adapter<RecyclerAdapterForKheladhulaCricket.RecyclerViewHolder> {


    private int[] images = {R.drawable.kheladhula_cricket_1, R.drawable.kheladhula_cricket_2, R.drawable.kheladhula_cricket_3};
    private ArrayList<String> data;


    Activity activity;


    public RecyclerAdapterForKheladhulaCricket(Activity activity, ArrayList<String> data) {

        this.activity = activity;
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_for_wallpaper_in_crickrt_fragment_in_sports_activity, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity);
        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {


        holder.strikrthroughTextView.setPaintFlags(holder.strikrthroughTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.kheladhulaCricketTV.setText(data.get(position));

        Glide.with(activity)
                .load(images[position])
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return images.length;
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView strikrthroughTextView;
        TextView kheladhulaCricketTV;
        LinearLayout linearLayout;

        public RecyclerViewHolder(View view, final Activity activity) {
            super(view);

            kheladhulaCricketTV = view.findViewById(R.id.kheladhulaCricketTV);
            strikrthroughTextView = view.findViewById(R.id.strikeThroughText);
            imageView = view.findViewById(R.id.imgSportsCricket);
            linearLayout = view.findViewById(R.id.LinearLayout_RV_cricket_fragment);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (position == 0) {
                        Intent myIntent = new Intent(activity, WallpaperBundleActivity.class);
                        
                        activity.startActivity(myIntent);
                        activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        activity.finish();
                    }
                }
            });

        }
    }


}
