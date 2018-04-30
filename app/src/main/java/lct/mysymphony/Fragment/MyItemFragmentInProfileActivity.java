package lct.mysymphony.Fragment;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import lct.mysymphony.ModelClass.DataBaseData;
import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForMyItemFragment;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForNewWallPaperFragment;
import lct.mysymphony.helper.DataHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyItemFragmentInProfileActivity extends Fragment {

    private RecyclerView recyclerViewForMyItem;
    private RecyclerView.Adapter adapterForMyItem;
    RecyclerView.LayoutManager mLayoutManager;
    DataHelper dataHelper;
    int rowCount;
    private ArrayList<Bitmap> bitmapArrayList;
    private ArrayList<String> contentTypeArrayList;
    private ArrayList<DataBaseData> dataBaseDataArrayList;

    View view;


    public MyItemFragmentInProfileActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_item_fragment_in_profile, container, false);

        dataHelper=new DataHelper(getActivity());
        bitmapArrayList=new ArrayList<>();
        contentTypeArrayList=new ArrayList<>();
        dataBaseDataArrayList=new ArrayList<>() ;
        new RetriveBitMapFromDatabase().execute();

        return view;
    }

    private class RetriveBitMapFromDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            rowCount =(int) dataHelper.getRowCount();
            for (int i=0;i<rowCount;i++)
            {
                bitmapArrayList.add(dataHelper.getBitmap(i+1));
                dataBaseDataArrayList.add(dataHelper.getAllData(i+1));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateRecyclerView();
        }
    }
    public void updateRecyclerView()
    {
        recyclerViewForMyItem = view.findViewById(R.id.RV_MyItemFragMent);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForMyItem.setLayoutManager(mLayoutManager);
        recyclerViewForMyItem.setHasFixedSize(true);
        adapterForMyItem = new RecyclerAdapterForMyItemFragment(getActivity(),bitmapArrayList,dataBaseDataArrayList);
        recyclerViewForMyItem.setAdapter(adapterForMyItem);
    }
}
