package harmony.app.RecyclerViewAdapter;

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

import harmony.app.Activity.PlayAudioActivity;
import harmony.app.ModelClass.JapitoJibon;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.ModelClass.Porashuna;
import harmony.app.R;
import harmony.app.helper.DataHelper;
import harmony.app.helper.ProgressDialog;

public class VideoListAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<MusicVideo> videoList;

    public VideoListAdapter(Context context, ArrayList<MusicVideo> videoList) {
        this.context = context;
        this.videoList = videoList;
        progressDialog = new harmony.app.helper.ProgressDialog(context);
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
        DataHelper dataHelper = new DataHelper(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.video_list_item, viewGroup, false);
        ImageView videoThumbnail = customView.findViewById(R.id.videoThumbnail);
        TextView apptitle = customView.findViewById(R.id.videoTitle);
        TextView type = customView.findViewById(R.id.contentType);
        TextView priceTag = customView.findViewById(R.id.priceTag);
        if (videoList.get(position).getThumbnailImgUrl().isEmpty() || videoList.get(position).getThumbnailImgUrl().equals("null")) {
            if (videoList.get(position).getContentType().equals("video")) {
                videoThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.video_thumbnail));
            }
        } else {
            Glide.with(context).load(videoList.get(position).getThumbnailImgUrl()).into(videoThumbnail);
        }
        int price = videoList.get(position).getContentPrice();
        if(price>0) {
            if (dataHelper.checkDownLoadedOrNot(videoList.get(position).getContentCat(), videoList.get(position).getContentId()))
            {
                priceTag.setVisibility(View.INVISIBLE);
            }else{
                priceTag.setText("৳" + price);
            }
        }
        apptitle.setText(videoList.get(position).getContentTitle());
        type.setText(videoList.get(position).getContentType());

        return customView;
    }
}
