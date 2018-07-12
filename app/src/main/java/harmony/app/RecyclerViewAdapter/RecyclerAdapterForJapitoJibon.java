package harmony.app.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;
import java.util.ArrayList;

import harmony.app.Activity.ContentDescriptionActivity.CommonDescriptionActivity;
import harmony.app.ModelClass.CategoryContent;
import harmony.app.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForJapitoJibon extends RecyclerView.Adapter<RecyclerAdapterForJapitoJibon.RecyclerViewHolder> {
    Activity activity;
    private ArrayList<CategoryContent> japitoJibonArrayList;
    public RecyclerAdapterForJapitoJibon(Activity activity, ArrayList<CategoryContent> japitoJibonArrayLis) {

        this.activity = activity;
        this.japitoJibonArrayList = japitoJibonArrayLis;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_japito_jibon, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, japitoJibonArrayList);
        return recyclerViewHolder;
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.japitoJebonNewsTV.setText(japitoJibonArrayList.get(position).getContentTitle());
        Glide.with(activity)
                .load(japitoJibonArrayList.get(position).getThumbnailImgUrl())
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

        if (japitoJibonArrayList.get(position).getContentType().equals("video")) {
            holder.videoPreviewPlayButton.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        if(japitoJibonArrayList.size()>2){
            return 2;
        }else{
            return japitoJibonArrayList.size();
        }
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, videoPreviewPlayButton;
        TextView japitoJebonNewsTV;
        ProgressBar progressBar;
        Activity activity;
        ArrayList<CategoryContent> japitoJibonArrayList;
        CardView cardView;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<CategoryContent> japitoJibonArrayLis) {
            super(view);
            imageView = view.findViewById(R.id.imgJapitoJibon);
            japitoJebonNewsTV = view.findViewById(R.id.japitoJibonNewsTV);
            videoPreviewPlayButton = view.findViewById(R.id.videoPreviewPlayButton);
            cardView = view.findViewById(R.id.cardviewJapitojibon);
            progressBar=view.findViewById(R.id.progressBarInJapitoJibon);

            this.activity = activity;
            this.japitoJibonArrayList = japitoJibonArrayLis;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(activity, CommonDescriptionActivity.class);
                    myIntent.putExtra("source", "home");
                    myIntent.putExtra("Data", (Serializable) japitoJibonArrayLis.get(getAdapterPosition()));
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
