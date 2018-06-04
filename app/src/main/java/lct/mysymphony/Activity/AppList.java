package lct.mysymphony.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lct.mysymphony.ModelClass.AppData;
import lct.mysymphony.R;
import lct.mysymphony.RecyclerViewAdapter.AppListAdapter;
import lct.mysymphony.helper.DownloadApk;
import lct.mysymphony.helper.ProgressDialog;

public class AppList extends AppCompatActivity implements DownloadApk.AsyncResponse{

    ListView listView;
    AppListAdapter adapter;
    ArrayList<AppData> appList;

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
    public void processFinish(String output) {

    }
}
