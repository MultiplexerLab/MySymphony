package sym.appstore.RecyclerViewAdapter;

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
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sym.appstore.ModelClass.AppData;
import sym.appstore.R;
import sym.appstore.helper.AppLogger;
import sym.appstore.helper.DownloadApk;
import sym.appstore.helper.ProgressDialog;

public class AppListAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<AppData> appData;

    public AppListAdapter(Context context, ArrayList<AppData> appData) {
        this.context = context;
        this.appData = appData;
        progressDialog = new sym.appstore.helper.ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return appData.size();
    }

    @Override
    public Object getItem(int i) {
        return appData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.app_list_item, viewGroup, false);
        ImageView appThumbnail = customView.findViewById(R.id.appCoverImage);
        Log.i("backgroundUrl", appData.get(position).getThumbNailImage());
        final TextView apptitle = customView.findViewById(R.id.appTitle);
        //TextView appDesc = customView.findViewById(R.id.appDescription);
        Button installButton = customView.findViewById(R.id.buttonInstall);
        String imageUrl = appData.get(position).getThumbNailImage();
        if(imageUrl==null || imageUrl.isEmpty() || imageUrl.equals("null") ) {

        }else{
            Glide.with(context).load(appData.get(position).getThumbNailImage()).into(appThumbnail);
        }

        apptitle.setText(appData.get(position).getTitle());

        ExpandableTextView expTv1 = (ExpandableTextView) customView.findViewById(R.id.cardViewAppList)
                .findViewById(R.id.expand_text_view);
        if(appData.get(position).getDescription().isEmpty()){
            expTv1.setText("No Description");
        }else{
            expTv1.setText(appData.get(position).getDescription());
        }

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date startTime;
                DateFormat dateFormat;
                dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                startTime = new Date();
                AppLogger.insertLogs(context, dateFormat.format(startTime), "Y", "Start Downloading",
                        "INSTALL", "Install Button Clicked for "+ apptitle);

                String apkUrl = appData.get(position).getContentUrl();
                String appTitle = appData.get(position).getTitle();
                Log.i("DownloadAPK", apkUrl);
                DownloadApk downloadApk = new DownloadApk();
                downloadApk.downLoadAPK(appTitle, apkUrl, context);
            }
        });
        return customView;
    }
}
