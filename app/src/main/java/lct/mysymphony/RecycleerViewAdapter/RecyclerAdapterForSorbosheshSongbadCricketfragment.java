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
import lct.mysymphony.ModelClass.Kheladhula_Cricket_sorboshesh_sobgbad;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForSorbosheshSongbadCricketfragment extends RecyclerView.Adapter<RecyclerAdapterForSorbosheshSongbadCricketfragment.RecyclerViewHolder> {


    ArrayList<Kheladhula_Cricket_sorboshesh_sobgbad> kheladhulaCricketArrayList;

    Activity activity;


    public RecyclerAdapterForSorbosheshSongbadCricketfragment(Activity activity, ArrayList<Kheladhula_Cricket_sorboshesh_sobgbad> kheladhulaCricketArrayList) {

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
        ArrayList<Kheladhula_Cricket_sorboshesh_sobgbad> kheladhula_cricket_sorboshesh_sobgbadArrayList;


        public RecyclerViewHolder(View view, final ArrayList<Kheladhula_Cricket_sorboshesh_sobgbad> kheladhula_cricket_sorboshesh_sobgbadArrayList, final Activity activity) {
            super(view);

            imageView = view.findViewById(R.id.imgSorbosheshShongbad);
            sorbosheshSongbadTV = view.findViewById(R.id.sorbosheshSongbadTV);
            cardView=view.findViewById(R.id.cardViewCricketSorbosheshSongBad);
            this.kheladhula_cricket_sorboshesh_sobgbadArrayList = kheladhula_cricket_sorboshesh_sobgbadArrayList;

            this.activity = activity;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent myIntent = new Intent(activity, CricketSorbosheshSongbadDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) kheladhula_cricket_sorboshesh_sobgbadArrayList.get(getAdapterPosition()));
                    
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });



        }
    }

}
