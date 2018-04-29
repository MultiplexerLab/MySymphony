package lct.mysymphony.RecycleerViewAdapter;

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

import lct.mysymphony.Activity.ContentDescriptionActivity.NewsDescriptionActivity;
import lct.mysymphony.ModelClass.GoromKhobor;
import lct.mysymphony.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForCustomerCareFragment extends RecyclerView.Adapter<RecyclerAdapterForCustomerCareFragment.RecyclerViewHolder> {


   /// private int[] images = {R.drawable.japito_jibon_1, R.drawable.japito_jibon_2};

    Activity activity;
    private ArrayList<String> goromKhoborArrayList;


    public RecyclerAdapterForCustomerCareFragment(Activity activity, ArrayList<String> goromKhoborArrayList) {

        this.activity = activity;
        this.goromKhoborArrayList=goromKhoborArrayList;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_customer_care_fragment, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,activity, goromKhoborArrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        ///holder.japitoJebonNewsTV.setText(japitoJibonMCArrayList.get(position).getContentTitle());
        holder.customerCareTitle.setText(goromKhoborArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return goromKhoborArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;
        Activity activity;
        TextView customerCareTitle;
        ArrayList<String> goromKhoborArrayList;

        public RecyclerViewHolder(View view, final Activity activity, final ArrayList<String> goromKhoborArrayList) {
            super(view);

            this.activity=activity;
            this.goromKhoborArrayList=goromKhoborArrayList;
            customerCareTitle=view.findViewById(R.id.titleCustomerCare);


//            imageView = view.findViewById(R.id.imgGoromKhobor);
//            goromKhoborTitle=view.findViewById(R.id.titleGoromKhobor);
//            publishedAt=view.findViewById(R.id.publishedAtGoromKhobor);
//            cardView=view.findViewById(R.id.goromKhoborCardView);
//            ///japitoJebonNewsTV = view.findViewById(R.id.japitoJibonNewsTV);
//
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent myIntent = new Intent(activity, NewsDescriptionActivity.class);
//                    myIntent.putExtra("Data", (Serializable) goromKhoborArrayList.get(getAdapterPosition()));
//                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    activity.startActivity(myIntent);
//                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                    activity.finish();
//
//
//                }
//            });

        }
    }


}
