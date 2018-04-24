package tanvir.multiplexer.RecycleerViewAdapter;

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

import tanvir.multiplexer.Activity.JibonJaponDescriptionActivity;
import tanvir.multiplexer.ModelClass.JibonJapon;
import tanvir.multiplexer.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForJibonJapon extends RecyclerView.Adapter<RecyclerAdapterForJibonJapon.RecyclerViewHolder> {


    ///private int[] images = {R.drawable.shikkhya_sohayika_1, R.drawable.shikkhya_sohayika_2};

    Activity activity;
    private ArrayList<JibonJapon> jibonJaponArrayList;


    public RecyclerAdapterForJibonJapon(Activity activity, ArrayList<JibonJapon> jibonJaponArrayList)
    {

        this.activity = activity;
        this.jibonJaponArrayList=jibonJaponArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_jibon_japon,parent,false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity,jibonJaponArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        holder.JibonJaponNewsTV.setText(jibonJaponArrayList.get(position).getContentTitle());

        Glide.with(activity)
                .load(jibonJaponArrayList.get(position).getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return jibonJaponArrayList.size();
    }


    public  static  class  RecyclerViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;
        TextView JibonJaponNewsTV;
        CardView cardView;
        Activity activity;
        ArrayList<JibonJapon> jibonJaponArrayList;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<JibonJapon> jibonJaponArrayList)
        {
            super(view);
            imageView =  view.findViewById(R.id.imgJibonJapon);
            JibonJaponNewsTV=view.findViewById(R.id.JibonJaponNewsTV);
            this.activity=activity;
            this.jibonJaponArrayList=jibonJaponArrayList;

            cardView=view.findViewById(R.id.cardViewJibonJapon);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, JibonJaponDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) jibonJaponArrayList.get(getAdapterPosition()));
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(myIntent);
                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            });

        }
    }


}
