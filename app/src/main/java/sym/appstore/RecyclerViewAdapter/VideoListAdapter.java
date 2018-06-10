package sym.appstore.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sym.appstore.ModelClass.JapitoJibon;
import sym.appstore.ModelClass.Porashuna;
import sym.appstore.R;
import sym.appstore.helper.ProgressDialog;

public class VideoListAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<Porashuna> videoList;

    public VideoListAdapter(Context context, ArrayList<Porashuna> appData) {
        this.context = context;
        this.videoList = appData;
        progressDialog = new sym.appstore.helper.ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.video_list_item, viewGroup, false);
        ImageView videoThumbnail = customView.findViewById(R.id.videoThumbnail);
        TextView apptitle = customView.findViewById(R.id.videoTitle);
        TextView type = customView.findViewById(R.id.contentType);
        if (videoList.get(position).getThumbnailImgUrl().isEmpty() || videoList.get(position).getThumbnailImgUrl().equals("null")) {
            if (videoList.get(position).getContentType().equals("video")) {
                videoThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.video_thumbnail));
            }
        } else {
            Glide.with(context).load(videoList.get(position).getThumbnailImgUrl()).into(videoThumbnail);
        }
        apptitle.setText(videoList.get(position).getContentTitle());
        type.setText(videoList.get(position).getContentType());

        return customView;
    }
}
