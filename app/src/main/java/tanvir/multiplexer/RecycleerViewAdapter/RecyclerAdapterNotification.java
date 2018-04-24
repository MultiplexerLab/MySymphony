package tanvir.multiplexer.RecycleerViewAdapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tanvir.multiplexer.ModelClass.BigganOProjukti;
import tanvir.multiplexer.ModelClass.Notifications;
import tanvir.multiplexer.R;

/**
 * Created by Lenovo on 4/24/2018.
 */

public class RecyclerAdapterNotification extends RecyclerView.Adapter<RecyclerAdapterNotification.RecyclerViewHolder> {

    Activity activity;
    private ArrayList<Notifications> notificationsArrayList;

    public RecyclerAdapterNotification(Activity activity, ArrayList<Notifications> notificationsArrayList) {
        this.activity = activity;
        this.notificationsArrayList = notificationsArrayList;
    }

    @Override
    public RecyclerAdapterNotification.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_notification_activity, parent, false);

        RecyclerAdapterNotification.RecyclerViewHolder recyclerViewHolder = new RecyclerAdapterNotification.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerAdapterNotification.RecyclerViewHolder holder, int position) {
        holder.title.setText(notificationsArrayList.get(position).getTitle());
        holder.time.setText(notificationsArrayList.get(position).getTime());
        holder.description.setText(notificationsArrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, time;

        public RecyclerViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.titleNotification);
            time = view.findViewById(R.id.timeNotification);
            description = view.findViewById(R.id.descNotification);
        }
    }

}
