package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import lct.mysymphony.ModelClass.SeraChobi;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForNewWallPaperFragment extends RecyclerView.Adapter<RecyclerAdapterForNewWallPaperFragment.RecyclerViewHolder> {

    ArrayList<SeraChobi> images;
    Activity activity;

    public RecyclerAdapterForNewWallPaperFragment(Activity activity, ArrayList<SeraChobi> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_new_wallpaper_fragment, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        Glide.with(activity)
                .load(images.get(position).getImage_url())
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
        return images.size();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        public RecyclerViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imgNewWallpaper);
            progressBar=view.findViewById(R.id.progressBarInNewWallpaper);
        }
    }
}
