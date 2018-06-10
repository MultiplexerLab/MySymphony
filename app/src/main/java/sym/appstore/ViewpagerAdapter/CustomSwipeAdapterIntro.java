package sym.appstore.ViewpagerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import sym.appstore.ModelClass.SliderImage;
import sym.appstore.R;

public class CustomSwipeAdapterIntro extends PagerAdapter {
    ArrayList<SliderImage> sliderImages;
    private Context context;
    LayoutInflater inflater;

    public CustomSwipeAdapterIntro(Context context, ArrayList<SliderImage> images) {
        this.context = context;
        this.sliderImages = images;
    }

    @Override
    public int getCount() {
        return sliderImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.i("Data", sliderImages.get(position).getThumbNail_image());
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = inflater.inflate(R.layout.swipelayout_intro, null, false);
        assert item_view != null;
        final ImageView imageView = item_view
                .findViewById(R.id.imageIntro);
        TextView descriptionIntro = item_view.findViewById(R.id.descriptionIntro);
        TextView titleIntro = item_view.findViewById(R.id.titleIntro);
        titleIntro.setText(sliderImages.get(position).getContentTitle());
        descriptionIntro.setText(sliderImages.get(position).getContentDescription());
        final ProgressBar progressBar = item_view.findViewById(R.id.progressBarInSliderContent);
        Log.i("DataIntro", sliderImages.get(position).getThumbNail_image());
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

        Button button = item_view.findViewById(R.id.btn_signup);
        Log.i("ContextData", context.toString());
        if(context.toString().contains("AboutUs")){
            button.setVisibility(View.INVISIBLE);
        }else{
            if (position == 3) {
                button.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }

        container.addView(item_view);
        return item_view;
    }
}
