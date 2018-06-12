package sym.appstore.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sym.appstore.ModelClass.AppData;
import sym.appstore.R;
import sym.appstore.RecyclerViewAdapter.AppListAdapter;
import sym.appstore.helper.AppLogger;
import sym.appstore.helper.DownloadApk;
import sym.appstore.helper.ProgressDialog;

public class AppList extends AppCompatActivity implements DownloadApk.AsyncResponse{

    ListView listView;
    AppListAdapter adapter;
    ArrayList<AppData> appList;
    Date startTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        listView = findViewById(R.id.listViewApps);

        String jsonData = getIntent().getStringExtra("appList");
        Gson gson = new Gson();
        Type type = new TypeToken<List<AppData>>(){}.getType();
        appList = gson.fromJson(jsonData, type);

        adapter = new AppListAdapter(AppList.this, appList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLogger.insertLogs(this, dateFormat.format(startTime), "Y", "AppList",
                "LEAVE", "Leave from Applist");
    }

    @Override
    protected void onResume(){
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        startTime = new Date();
        AppLogger.insertLogs(this, dateFormat.format(startTime), "N", "AppList",
                "IN", "Entrance to Applist");
    }

    @Override
    public void processFinish(String output) {

    }
}
