package sym.appstore.RecyclerViewAdapter;

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

import sym.appstore.Activity.ContentDescriptionActivity.PachMishaliDescriptionActivity;
import sym.appstore.ModelClass.PacMishali;
import sym.appstore.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForPachMishali extends RecyclerView.Adapter<RecyclerAdapterForPachMishali.RecyclerViewHolder> {

    Activity activity;
    private ArrayList<PacMishali> pacMishaliArrayList;

    public RecyclerAdapterForPachMishali(Activity activity, ArrayList<PacMishali> pacMishaliArrayList)
    {
        this.activity = activity;
        this.pacMishaliArrayList=pacMishaliArrayList;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_pach_mishali,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity,pacMishaliArrayList);
        return recyclerViewHolder;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.pachMishaliNewsTV.setText(pacMishaliArrayList.get(position).getContentTitle());
        Glide.with(activity)
                .load(pacMishaliArrayList.get(position).getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pacMishaliArrayList.size();
    }

    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView pachMishaliNewsTV;
        CardView cardView;
        Activity activity;
        ArrayList<PacMishali> pacMishaliArrayLis;
        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<PacMishali> pacMishaliArrayList)
        {
            super(view);
            imageView=view.findViewById(R.id.imgPachMishali);
            pachMishaliNewsTV=view.findViewById(R.id.PachMishaliNewsTV);
            cardView=view.findViewById(R.id.cardViewPachMishali);
            this.activity=activity;
            this.pacMishaliArrayLis=pacMishaliArrayList;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, PachMishaliDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) pacMishaliArrayList.get(getAdapterPosition()));
                    activity.startActivity(myIntent);;
                }
            });
        }
    }
}
