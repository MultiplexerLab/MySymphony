package lct.mysymphony;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import lct.mysymphony.Activity.ContentDescriptionActivity.SliderContentActivity;
import lct.mysymphony.ModelClass.SliderImage;

/**
 * Created by USER on 20-Nov-17.
 */

public class CustomSwipeAdapter extends PagerAdapter {

    ///private int[] images = {R.drawable.japito_jibon_1, R.drawable.japito_jibon_2, R.drawable.shikkhya_sohayika_4};
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
        container.removeView((LinearLayout) object);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = inflater.inflate(R.layout.swipelayout, view, false);

        assert item_view != null;
        final ImageView imageView = item_view
                .findViewById(R.id.image);
        Glide.with(context)
                .load(sliderImages.get(position).getImage_url())
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity= (Activity) context;
                Intent intent = new Intent(context, SliderContentActivity.class);
                intent.putExtra("wallpaper", (Serializable) sliderImages.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        view.addView(item_view, 0);
        return item_view;
    }
}
