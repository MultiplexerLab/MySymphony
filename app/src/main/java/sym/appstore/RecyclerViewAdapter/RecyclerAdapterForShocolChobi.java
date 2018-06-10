package sym.appstore.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
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

import sym.appstore.Activity.ImageViewActivity;
import sym.appstore.ModelClass.ShocolChobi;
import sym.appstore.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForShocolChobi extends RecyclerView.Adapter<RecyclerAdapterForShocolChobi.RecyclerViewHolder> {
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
        Glide.with(activity).load(shocolChobiArrayList.get(position).getContentUrl()).listener(new RequestListener<Drawable>() {
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
        }).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return shocolChobiArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Activity activity;
        ArrayList<ShocolChobi> shocolChobiArrayList;
        ProgressBar progressBar;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<ShocolChobi> shocolChobiArrayList) {
            super(view);
            imageView = view.findViewById(R.id.imgShocholChobi);
            this.activity = activity;
            this.shocolChobiArrayList = shocolChobiArrayList;
            progressBar = view.findViewById(R.id.progressBarInShocholChobi);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, ImageViewActivity.class);
                    myIntent.putExtra("wallpaper", shocolChobiArrayList.get(getAdapterPosition()));
                    myIntent.putExtra("cameFromWhichActivity", "ShocolChobi");
                    activity.startActivity(myIntent);
                    ;
                }
            });
        }
    }
}

