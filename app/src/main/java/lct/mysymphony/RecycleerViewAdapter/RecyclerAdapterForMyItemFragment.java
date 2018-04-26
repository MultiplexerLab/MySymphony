package lct.mysymphony.RecycleerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import lct.mysymphony.Activity.NewsDescriptionActivity;
import lct.mysymphony.ModelClass.GoromKhobor;
import lct.mysymphony.R;
import lct.mysymphony.helper.DataHelper;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForMyItemFragment extends RecyclerView.Adapter<RecyclerAdapterForMyItemFragment.RecyclerViewHolder> {


   /// private int[] images = {R.drawable.japito_jibon_1, R.drawable.japito_jibon_2};

    Activity activity;
   DataHelper dataHelper;
   Bitmap bitmap;
   int rowCount=0;


    public RecyclerAdapterForMyItemFragment(Activity activity) {

        this.activity = activity;
        dataHelper=new DataHelper(activity);
        rowCount =(int) dataHelper.getRowCount();

        if (bitmap==null)
            Log.d("bit","null");


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_my_item_in_user_profile, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        ///holder.japitoJebonNewsTV.setText(japitoJibonMCArrayList.get(position).getContentTitle());
        bitmap=dataHelper.getBitmap(position+1);

        Glide.with(activity)
                .load(bitmap)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return rowCount;
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;
        Activity activity;
//        TextView goromKhoborTitle,publishedAt;
//        ArrayList<GoromKhobor> goromKhoborArrayList;

        public RecyclerViewHolder(View view, final Activity activity) {
            super(view);

            this.activity=activity;
//            this.goromKhoborArrayList=goromKhoborArrayList;

            imageView = view.findViewById(R.id.imgMyitemFragment);
//            goromKhoborTitle=view.findViewById(R.id.titleGoromKhobor);
//            publishedAt=view.findViewById(R.id.publishedAtGoromKhobor);
             cardView=view.findViewById(R.id.myItemFragmentCardView);
            ///japitoJebonNewsTV = view.findViewById(R.id.japitoJibonNewsTV);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent myIntent = new Intent(activity, NewsDescriptionActivity.class);
//                    myIntent.putExtra("Data", (Serializable) goromKhoborArrayList.get(getAdapterPosition()));
//                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    activity.startActivity(myIntent);
//                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                    activity.finish();
                    Toast.makeText(activity, "এখন ডাউনলোড করার সুযোগটি নেই\n" +
                            "পেমেন্ট যাচাই করা হলে ডাউললোড করার সুযোগ পাবেন", Toast.LENGTH_SHORT).show();


                }
            });

        }
    }


}
