package lct.mysymphony.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lct.mysymphony.Activity.ContentDescriptionActivity.JapitoJibonDescriptionActivity;
import lct.mysymphony.ModelClass.AppData;
import lct.mysymphony.ModelClass.JapitoJibon;
import lct.mysymphony.ModelClass.Porashuna;
import lct.mysymphony.R;
import lct.mysymphony.RecyclerViewAdapter.AppListAdapter;
import lct.mysymphony.RecyclerViewAdapter.VideoListAdapter;
import lct.mysymphony.helper.AppLogger;

public class Music_Video extends AppCompatActivity {

    ListView listView;
    VideoListAdapter adapter;
    ArrayList<Porashuna> videoList;
    Date currenTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__video);

        listView = findViewById(R.id.listViewApps);

        String jsonData = getIntent().getStringExtra("videoList");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Porashuna>>() {
        }.getType();
        videoList = gson.fromJson(jsonData, type);
        adapter = new VideoListAdapter(Music_Video.this, videoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String contentType = videoList.get(i).getContentType();
                if (contentType.equals("audio")) {
                    Intent myIntent = new Intent(Music_Video.this, PlayAudioActivity.class);
                    myIntent.putExtra("cameFromWhichActivity", "music_video");
                    myIntent.putExtra("data", (Serializable) videoList.get(i));
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Music_Video.this, JapitoJibonDescriptionActivity.class);
                    myIntent.putExtra("Data", (Serializable) videoList.get(i));
                    myIntent.putExtra("cameFromWhichActivity", "music_video");
                    startActivity(myIntent);
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(Music_Video.this, dateFormat.format(currenTime), "N", "MusicVideo",
                "IN", "Entrance");
    }
    @Override
    protected void onStop(){
        super.onStop();
        AppLogger.insertLogs(Music_Video.this, dateFormat.format(currenTime), "Y", "MusicVideo",
                "OUT", "Leave");
    }
}
