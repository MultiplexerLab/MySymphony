package sym.appstore.RecyclerViewAdapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sym.appstore.R;

/**
 * Created by USER on 01-Feb-17.
 */

public class RecyclerAdapterForCustomerCareFragment extends RecyclerView.Adapter<RecyclerAdapterForCustomerCareFragment.RecyclerViewHolder> {

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
        }
    }
}
