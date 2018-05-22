package lct.mysymphony.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;

import lct.mysymphony.Activity.ImageViewActivity;
import lct.mysymphony.ModelClass.MulloChar;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForMulloChar extends RecyclerView.Adapter<RecyclerAdapterForMulloChar.RecyclerViewHolder> {

    Activity activity;
    ArrayList<MulloChar> mulloCharArrayList;

    public RecyclerAdapterForMulloChar(Activity activity, ArrayList<MulloChar> mulloCharArrayList) {
        this.activity = activity;
        this.mulloCharArrayList = mulloCharArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_mullo_char, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, mulloCharArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.strikrthroughTextView.setPaintFlags(holder.strikrthroughTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.mullocharTV.setText(mulloCharArrayList.get(position).getContentTile());
        holder.strikrthroughTextView.setText(Integer.toString(mulloCharArrayList.get(position).getPreviousPrice()));
        holder.newPriceTV.setText(Integer.toString((mulloCharArrayList.get(position).getNewPrice())));
        Glide.with(activity).load(mulloCharArrayList.get(position).getImageUrl()).listener(new RequestListener<Drawable>() {
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

        if (mulloCharArrayList.get(position).getContentType().equals("video")) {
            holder.videoPreviewPlayButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mulloCharArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, videoPreviewPlayButton;
        TextView strikrthroughTextView;
        TextView mullocharTV, newPriceTV;
        ArrayList<MulloChar> mulloCharArrayList;
        ProgressBar progressBar;
        Activity activity;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<MulloChar> mulloCharArrayList) {
            super(view);
            mullocharTV = view.findViewById(R.id.mulloCharTV);
            strikrthroughTextView = view.findViewById(R.id.strikeThroughTextMulloChar);
            imageView = view.findViewById(R.id.imgMulloChar);
            newPriceTV = view.findViewById(R.id.newPriceTV);
            videoPreviewPlayButton = view.findViewById(R.id.videoPreviewPlayButton);
            this.mulloCharArrayList = mulloCharArrayList;
            progressBar = view.findViewById(R.id.progressBarInMulloChar);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent myIntent = new Intent(activity, ImageViewActivity.class);
                    myIntent.putExtra("mullochar", mulloCharArrayList.get(getAdapterPosition()));
                    myIntent.putExtra("cameFromWhichActivity", "MulloChar");
                    activity.startActivity(myIntent);
                }
            });
        }
    }
}
