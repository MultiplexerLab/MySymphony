package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public RecyclerViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imgNewWallpaper);

        }
    }


}
