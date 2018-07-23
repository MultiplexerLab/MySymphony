package harmony.app.RecyclerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;
import java.util.ArrayList;

import harmony.app.Activity.ContentDescriptionActivity.VideoDescriptionActivity;
import harmony.app.Activity.ContentDescriptionActivity.SliderContentDescriptionActivity;
import harmony.app.Activity.PlayAudioActivity;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.ModelClass.SliderImage;
import harmony.app.R;

public class CustomSwipeAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    ArrayList<SliderImage> sliderImages;
    private Context context;
    public CustomSwipeAdapter(Context context, ArrayList<SliderImage> images) {
        this.context = context;
        this.sliderImages=images;
    }
    @Override
    public int getCount() {
        return sliderImages.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==  object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        Log.i("DataGot", sliderImages.get(position).getThumbNail_image());
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = inflater.inflate(R.layout.swipelayout, null, false);
        final ImageView imageView = item_view
                .findViewById(R.id.image);
        final ProgressBar progressBar=item_view.findViewById(R.id.progressBarInSliderContent);
        Glide.with(context)
                .load(sliderImages.get(position).getThumbNail_image())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sliderImages.get(position).getContentType().equals("audio")){
                    Intent myIntent = new Intent(context, PlayAudioActivity.class);
                    myIntent.putExtra("cameFromWhichActivity", "music_video");
                    SliderImage object = sliderImages.get(position);
                    MusicVideo musicVideo = new MusicVideo(object.getContentTitle(), object.getContentType(), object.getContentDescription(),
                            object.getContentUrl(), object.getContentCat(), "", object.getThumbNail_image(), object.getId(), object.getContentPrice());
                    myIntent.putExtra("data", (Serializable) musicVideo);
                    context.startActivity(myIntent);

                }else if(sliderImages.get(position).getContentType().equals("video")){
                    Intent myIntent = new Intent(context, VideoDescriptionActivity.class);
                    SliderImage object = sliderImages.get(position);
                    MusicVideo musicVideo = new MusicVideo(object.getContentTitle(), object.getContentType(), object.getContentDescription(),
                            object.getContentUrl(), object.getContentCat(), "", object.getThumbNail_image(), object.getId(), object.getContentPrice());
                    myIntent.putExtra("Data", (Serializable) musicVideo);
                    myIntent.putExtra("cameFromWhichActivity", "music_video");
                    context.startActivity(myIntent);
                }else{
                    Intent intent = new Intent(context, SliderContentDescriptionActivity.class);
                    intent.putExtra("sliderImage", (Serializable) sliderImages.get(position));
                    context.startActivity(intent);
                }
            }
        });

        view.addView(item_view, 0);
        return item_view;
    }
}
