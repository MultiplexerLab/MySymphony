package sym.appstore.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import sym.appstore.Activity.ImageViewActivity;
import sym.appstore.ModelClass.GamesZone;
import sym.appstore.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForGamesZone extends RecyclerView.Adapter<RecyclerAdapterForGamesZone.RecyclerViewHolder> {

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
        holder.mullocharTV.setText(gamesZoneArrayList.get(position).getContentTile());
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
            imageView = view.findViewById(R.id.imgGamesZone);
            newPriceTV = view.findViewById(R.id.newPriceTVInGamesZone);
            progressBar=view.findViewById(R.id.progressBarInGamesZone);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (gamesZoneArrayList.get(getAdapterPosition()).getThumbNailImgUrl().length()==0)
                        Log.d("lengto","lengto");
                    Log.d("thumbnailImageUrlINrv", gamesZoneArrayList.get(getAdapterPosition()).getThumbNailImgUrl());
                    Intent myIntent = new Intent(activity, ImageViewActivity.class);
                    myIntent.putExtra("wallpaper", gamesZoneArrayList.get(getAdapterPosition()));
                    myIntent.putExtra("cameFromWhichActivity","GameZone");
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
