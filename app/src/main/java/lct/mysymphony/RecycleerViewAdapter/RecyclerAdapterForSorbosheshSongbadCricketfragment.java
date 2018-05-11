package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import lct.mysymphony.Activity.ContentDescriptionActivity.CricketSorbosheshSongbadDescriptionActivity;
import lct.mysymphony.ModelClass.KheladhulaCricketSorbosheshSongbad;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForSorbosheshSongbadCricketfragment extends RecyclerView.Adapter<RecyclerAdapterForSorbosheshSongbadCricketfragment.RecyclerViewHolder> {

    ArrayList<KheladhulaCricketSorbosheshSongbad> kheladhulaCricketArrayList;
    Activity activity;
    public RecyclerAdapterForSorbosheshSongbadCricketfragment(Activity activity, ArrayList<KheladhulaCricketSorbosheshSongbad> kheladhulaCricketArrayList) {
        this.activity = activity;
        this.kheladhulaCricketArrayList = kheladhulaCricketArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_sorboshesh_songbad_in_cricket_fragment, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, kheladhulaCricketArrayList, activity);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.sorbosheshSongbadTV.setText(kheladhulaCricketArrayList.get(position).getContentTitle());
        Glide.with(activity)
                .load(kheladhulaCricketArrayList.get(position).getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return kheladhulaCricketArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView sorbosheshSongbadTV;
        Activity activity;
        CardView cardView;
        ArrayList<KheladhulaCricketSorbosheshSongbad> kheladhula_cricket_sorbosheshSongbadArrayList;
        public RecyclerViewHolder(View view, final ArrayList<KheladhulaCricketSorbosheshSongbad> kheladhula_cricket_sorbosheshSongbadArrayList, final Activity activity) {
            super(view);
            imageView = view.findViewById(R.id.imgSorbosheshShongbad);
            sorbosheshSongbadTV = view.findViewById(R.id.sorbosheshSongbadTV);
            cardView=view.findViewById(R.id.cardViewCricketSorbosheshSongBad);
            this.kheladhula_cricket_sorbosheshSongbadArrayList = kheladhula_cricket_sorbosheshSongbadArrayList;
            this.activity = activity;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, CricketSorbosheshSongbadDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) kheladhula_cricket_sorbosheshSongbadArrayList.get(getAdapterPosition()));
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
