package harmony.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import harmony.app.Activity.ContentDescriptionActivity.JapitoJibonDescriptionActivity;
import harmony.app.ModelClass.MusicVideo;
import harmony.app.R;
import harmony.app.RecyclerViewAdapter.VideoListAdapter;

public class VideoFragment extends Fragment {

    ListView listView;
    View view;
    ArrayList<MusicVideo> videoList;
    VideoListAdapter adapter;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_videolist, container, false);

        String value = getArguments().getString("videoListStr");
        Gson gson = new Gson();
        Type type = new TypeToken<List<MusicVideo>>() {
        }.getType();
        videoList = gson.fromJson(value, type);

        listView = view.findViewById(R.id.listViewVideo);

        adapter = new VideoListAdapter(getActivity(), videoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(getActivity(), JapitoJibonDescriptionActivity.class);
                myIntent.putExtra("Data", (Serializable) videoList.get(i));
                myIntent.putExtra("cameFromWhichActivity", "music_video");
                startActivity(myIntent);
            }
        });

        return view;
    }
}
