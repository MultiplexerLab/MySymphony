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

import lct.mysymphony.Activity.PorashunaDescriptionActivity;
import lct.mysymphony.ModelClass.Porashuna;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForPorashuna extends RecyclerView.Adapter<RecyclerAdapterForPorashuna.RecyclerViewHolder> {




    Activity activity;
    private ArrayList<Porashuna> porashunaArrayList;


    public RecyclerAdapterForPorashuna(Activity activity, ArrayList<Porashuna> porashunaArrayList)
    {

        this.activity = activity;
        this.porashunaArrayList=porashunaArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_porashuna,parent,false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity,porashunaArrayList);
        return recyclerViewHolder;
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {


        holder.porashunaNewsTV.setText(porashunaArrayList.get(position).getContentTitle());

        Glide.with(activity)
                .load(porashunaArrayList.get(position).getImageUrl())
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return porashunaArrayList.size();
    }


    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;
        TextView porashunaNewsTV;
        CardView cardView;
        Activity activity;
        ArrayList<Porashuna> porashunaArrayList;

        public RecyclerViewHolder(View view, final Activity activity , final ArrayList<Porashuna> porashunaArrayList)
        {
            super(view);

            this.activity=activity;
            this.porashunaArrayList=porashunaArrayList;

            cardView=view.findViewById(R.id.cardViewPorashuna);
            imageView =  view.findViewById(R.id.imgPorashuna);
            porashunaNewsTV=view.findViewById(R.id.porashunNewsTV);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, PorashunaDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) porashunaArrayList.get(getAdapterPosition()));
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });

        }
    }


}
