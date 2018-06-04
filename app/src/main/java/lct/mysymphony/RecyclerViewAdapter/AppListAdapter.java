package lct.mysymphony.RecyclerViewAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import lct.mysymphony.Activity.HomePage;
import lct.mysymphony.ModelClass.AppData;
import lct.mysymphony.R;
import lct.mysymphony.helper.DownloadApk;
import lct.mysymphony.helper.ProgressDialog;

public class AppListAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<AppData> appData;

    public AppListAdapter(Context context, ArrayList<AppData> appData) {
        this.context = context;
        this.appData = appData;
        progressDialog = new lct.mysymphony.helper.ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return appData.size();
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
        View customView = inflater.inflate(R.layout.app_list_item, viewGroup, false);
        ImageView appThumbnail = customView.findViewById(R.id.appCoverImage);
        TextView apptitle = customView.findViewById(R.id.appTitle);
        //TextView appDesc = customView.findViewById(R.id.appDescription);
        Button installButton = customView.findViewById(R.id.buttonInstall);
        Glide.with(context).load(appData.get(position).getThumbNailImage()).into(appThumbnail);
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
                SharedPreferences preferences = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
                int flag = preferences.getInt("unknownSource", 0);
                if (flag == 0) {
                    progressDialog.showProgressDialogAPK();
                    DownloadApk downloadApk = new DownloadApk();
                    downloadApk.downLoadAPK(appData.get(position).getContentUrl(), context);
                } else {
                    progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
                    DownloadApk downloadApk = new DownloadApk();
                    downloadApk.downLoadAPK(appData.get(position).getContentUrl(), context);
                }
            }
        });
        return customView;
    }
}
