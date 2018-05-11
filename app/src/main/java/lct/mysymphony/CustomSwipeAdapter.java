package lct.mysymphony;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.Serializable;
import java.util.ArrayList;
import lct.mysymphony.Activity.ContentDescriptionActivity.SliderContentDescriptionActivity;
import lct.mysymphony.ModelClass.SliderImage;

/**
 * Created by USER on 20-Nov-17.
 */

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

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = inflater.inflate(R.layout.swipelayout, view, false);
        assert item_view != null;
        final ImageView imageView = item_view
                .findViewById(R.id.image);
        final ProgressBar progressBar=item_view.findViewById(R.id.progressBarInSliderContent);
        Glide.with(context)
                .load(sliderImages.get(position).getImage_url())
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
                Activity activity= (Activity) context;
                Intent intent = new Intent(context, SliderContentDescriptionActivity.class);
                intent.putExtra("wallpaper", (Serializable) sliderImages.get(position));
                context.startActivity(intent);
            }
        });

        view.addView(item_view, 0);
        return item_view;
    }
}
