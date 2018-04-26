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

import lct.mysymphony.ModelClass.GoromKhobor;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForPopularWallpaperFragment extends RecyclerView.Adapter<RecyclerAdapterForPopularWallpaperFragment.RecyclerViewHolder> {


   /// private int[] images = {R.drawable.japito_jibon_1, R.drawable.japito_jibon_2};

    Activity activity;
    private ArrayList<GoromKhobor> goromKhoborArrayList;


    public RecyclerAdapterForPopularWallpaperFragment(Activity activity, ArrayList<GoromKhobor> goromKhoborArrayList) {

        this.activity = activity;
        this.goromKhoborArrayList=goromKhoborArrayList;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_gorom_khobor, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        ///holder.japitoJebonNewsTV.setText(japitoJibonMCArrayList.get(position).getContentTitle());
        holder.goromKhoborTitle.setText(goromKhoborArrayList.get(position).getContentTitle());
        holder.publishedAt.setText(goromKhoborArrayList.get(position).getPublishedAt());
        Glide.with(activity)
                .load(goromKhoborArrayList.get(position).getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return goromKhoborArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView goromKhoborTitle,publishedAt;

        public RecyclerViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imgGoromKhobor);
            goromKhoborTitle=view.findViewById(R.id.publishedAtGoromKhobor);
            publishedAt=view.findViewById(R.id.publishedAtGoromKhobor);
            ///japitoJebonNewsTV = view.findViewById(R.id.japitoJibonNewsTV);

        }
    }


}
