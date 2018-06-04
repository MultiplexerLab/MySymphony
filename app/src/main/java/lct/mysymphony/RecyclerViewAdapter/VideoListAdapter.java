package lct.mysymphony.RecyclerViewAdapter;

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

import lct.mysymphony.ModelClass.JapitoJibon;
import lct.mysymphony.ModelClass.Porashuna;
import lct.mysymphony.R;
import lct.mysymphony.helper.ProgressDialog;

public class VideoListAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<Porashuna> videoList;

    public VideoListAdapter(Context context, ArrayList<Porashuna> appData) {
        this.context = context;
        this.videoList = appData;
        progressDialog = new lct.mysymphony.helper.ProgressDialog(context);
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

        } else {
            Glide.with(context).load(videoList.get(position).getThumbnailImgUrl()).into(videoThumbnail);
        }
        apptitle.setText(videoList.get(position).getContentTitle());
        type.setText(videoList.get(position).getContentType());

        return customView;
    }
}
