package harmony.app.RecyclerViewAdapter;

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

import harmony.app.Activity.ContentDescriptionActivity.CommonDescriptionActivity;
import harmony.app.ModelClass.CategoryContent;
import harmony.app.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForCategory extends RecyclerView.Adapter<RecyclerAdapterForCategory.RecyclerViewHolder> {

    Activity activity;
    private ArrayList<CategoryContent> porashunaArrayList;

    public RecyclerAdapterForCategory(Activity activity, ArrayList<CategoryContent> porashunaArrayList)
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
        holder.categoryTV.setText(porashunaArrayList.get(position).getContentTitle());
        Glide.with(activity)
                .load(porashunaArrayList.get(position).getThumbnailImgUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return porashunaArrayList.size();
    }
    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView categoryTV;
        CardView cardView;
        Activity activity;
        ArrayList<CategoryContent> porashunaArrayList;

        public RecyclerViewHolder(View view, final Activity activity , final ArrayList<CategoryContent> porashunaArrayList)
        {
            super(view);
            this.activity=activity;
            this.porashunaArrayList=porashunaArrayList;
            cardView=view.findViewById(R.id.cardViewPorashuna);
            imageView =  view.findViewById(R.id.imgPorashuna);
            categoryTV=view.findViewById(R.id.porashunNewsTV);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, CommonDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) porashunaArrayList.get(getAdapterPosition()));
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
