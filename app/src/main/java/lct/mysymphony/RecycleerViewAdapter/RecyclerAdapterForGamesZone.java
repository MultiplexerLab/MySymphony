package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
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

import java.util.ArrayList;

import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.ModelClass.GamesZone;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForGamesZone extends RecyclerView.Adapter<RecyclerAdapterForGamesZone.RecyclerViewHolder> {


    ///private int[] images = {R.drawable.mullochar_1, R.drawable.mullochar_2, R.drawable.mullochar_3};


    Activity activity;
    ArrayList<GamesZone> gamesZoneArrayList;


    public RecyclerAdapterForGamesZone(Activity activity, ArrayList<GamesZone> gamesZoneArrayList) {

        this.activity = activity;
        this.gamesZoneArrayList = gamesZoneArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_games_zone, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity,gamesZoneArrayList);
        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {


        ///holder.strikrthroughTextView.setPaintFlags(holder.strikrthroughTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.strikrthroughTextView.setVisibility(View.GONE);

        holder.mullocharTV.setText(gamesZoneArrayList.get(position).getContentTile());
//        holder.strikrthroughTextView.setText(Integer.toString(gamesZoneArrayList.get(position).getPreviousPrice()));
        holder.newPriceTV.setText(Integer.toString(gamesZoneArrayList.get(position).getNewPrice()));

        Glide.with(activity)
                .load(gamesZoneArrayList.get(position).getContentUrl())
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
        return gamesZoneArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView strikrthroughTextView;
        TextView mullocharTV, newPriceTV;
        Activity activity;
        ArrayList<GamesZone> gamesZoneArrayList;
        ProgressBar progressBar;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<GamesZone> gamesZoneArrayList) {
            super(view);

            this.activity = activity;
            this.gamesZoneArrayList=gamesZoneArrayList;

            mullocharTV = view.findViewById(R.id.gamesZoneTV);
//            strikrthroughTextView = view.findViewById(R.id.strikeThroughTextInGamesZone);
            imageView = view.findViewById(R.id.imgGamesZone);
            newPriceTV = view.findViewById(R.id.newPriceTVInGamesZone);

            progressBar=view.findViewById(R.id.progressBarInGamesZone);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent myIntent = new Intent(activity, ImageViewActivity.class);
                    myIntent.putExtra("wallpaper", gamesZoneArrayList.get(getAdapterPosition()));
                    myIntent.putExtra("cameFromWhichActivity","GameZone");
                    ///myIntent.putExtra("price",gamesZoneArrayList.get(getAdapterPosition()).getNewPrice());
                    
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();

                }
            });

        }
    }


}
