package harmony.app.RecyclerViewAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import harmony.app.Activity.Emoticons;
import harmony.app.ModelClass.DataBaseData;
import harmony.app.ModelClass.SliderImage;
import harmony.app.R;
import harmony.app.helper.AppLogger;
import harmony.app.helper.DownloadApk;
import harmony.app.helper.DownloadImage;
import harmony.app.helper.ProgressDialog;

public class EmoticonsAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<SliderImage> emoticonList;

    public EmoticonsAdapter(Context context, ArrayList<SliderImage> emoticonList) {
        this.context = context;
        this.emoticonList = emoticonList;
        progressDialog = new harmony.app.helper.ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return emoticonList.size();
    }

    @Override
    public Object getItem(int i) {
        return emoticonList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.emoticon_item, viewGroup, false);
        ImageView emoticon = customView.findViewById(R.id.imageEmoticon);
        final TextView emoticonTitle = customView.findViewById(R.id.textEmoticon);
        ImageView downloadButton = customView.findViewById(R.id.downloadIcon);

        final String emoticonUrl = emoticonList.get(position).getContentUrl();
        final String title = emoticonList.get(position).getContentTitle();
        Glide.with(context).load(emoticonUrl).into(emoticon);
        emoticonTitle.setText(emoticonList.get(position).getContentTitle());

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("DownloadAPK", emoticonUrl);

                SharedPreferences.Editor mPrefs = context.getSharedPreferences("tempEmoticon", Context.MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String json = gson.toJson(emoticonList.get(position));
                mPrefs.putString("emoticonObj", json);
                mPrefs.putString("emoticonUrl", emoticonUrl);
                mPrefs.commit();

                SliderImage emoticon = emoticonList.get(position);
                DataBaseData dataBaseData = new DataBaseData(emoticon.getContentTitle(), emoticon.getContentCat(), emoticon.getContentType(),
                        emoticon.getContentDescription(), emoticon.getContentUrl(), "free", emoticon.getId());

                DownloadImage downloadImage = new DownloadImage();
                downloadImage.downloadImage(emoticonUrl, context, dataBaseData);
            }
        });
        return customView;
    }
}
