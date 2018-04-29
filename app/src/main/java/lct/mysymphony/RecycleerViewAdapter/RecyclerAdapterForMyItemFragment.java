package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import lct.mysymphony.Activity.ShowOnlyImage;
import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForMyItemFragment extends RecyclerView.Adapter<RecyclerAdapterForMyItemFragment.RecyclerViewHolder> {


    /// private int[] images = {R.drawable.japito_jibon_1, R.drawable.japito_jibon_2};

    Activity activity;
    ArrayList<Bitmap> bitmapArrayList;
    ArrayList<DataBaseData> dataHelperArrayList;


    public RecyclerAdapterForMyItemFragment(Activity activity, ArrayList<Bitmap> bitmapArrayList, ArrayList<DataBaseData> dataHelperArrayList) {

        this.activity = activity;
        this.bitmapArrayList = bitmapArrayList;
        this.dataHelperArrayList = dataHelperArrayList;


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_my_item_in_user_profile, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, activity, dataHelperArrayList, bitmapArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        ///holder.japitoJebonNewsTV.setText(japitoJibonMCArrayList.get(position).getContentTitle());
        String contentType = dataHelperArrayList.get(position).getContentType();
        if (dataHelperArrayList.get(position).getContentStatus().contains("free"))
            holder.downloadIcon.setVisibility(View.GONE);
        String text;
        if (contentType.contains("image"))
            text = "ছবি";
        else if (contentType.contains("apk")) {
            text = "গেম";
        } else if (contentType.contains("video")) {
            text = "ভিডিও";
        } else {
            text = "ছবি";
        }
        holder.textView.setText(text);

        Glide.with(activity)
                .load(bitmapArrayList.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bitmapArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;
        Activity activity;
        TextView textView;
        ArrayList<DataBaseData> dataHelperArrayList;
        ArrayList<Bitmap> bitmapArrayList;
        ImageView downloadIcon;


        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<DataBaseData> dataHelperArrayList, final ArrayList<Bitmap> bitmapArrayList) {
            super(view);

            this.activity = activity;
            this.dataHelperArrayList = dataHelperArrayList;
            this.bitmapArrayList = bitmapArrayList;
            imageView = view.findViewById(R.id.imgMyitemFragment);
            textView = view.findViewById(R.id.titleMyItemFragment);
            cardView = view.findViewById(R.id.myItemFragmentCardView);
            downloadIcon = view.findViewById(R.id.downLoadIcon);
            view.setDrawingCacheEnabled(false);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setDrawingCacheEnabled(false);
//                    Intent myIntent = new Intent(activity, NewsDescriptionActivity.class);
//                    myIntent.putExtra("Data", (Serializable) goromKhoborArrayList.get(getAdapterPosition()));
//                    
//                    activity.startActivity(myIntent);
//                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                    activity.finish();

                    if (dataHelperArrayList.get(getAdapterPosition()).getContentStatus().contains("free")) {
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                        bitmapArrayList.get(getAdapterPosition()).compress(Bitmap.CompressFormat.PNG, 100, stream);
////                        byte[] byteArray = stream.toByteArray();
////
////
                        Intent myIntent = new Intent(activity, ShowOnlyImage.class);
                        myIntent.putExtra("id", getAdapterPosition());
                        
                        activity.startActivity(myIntent);
                        activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "এখন ডাউনলোড করার সুযোগটি নেই\n" +
                                "পেমেন্ট যাচাই করা হলে ডাউললোড করার সুযোগ পাবেন", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }
    }


}
