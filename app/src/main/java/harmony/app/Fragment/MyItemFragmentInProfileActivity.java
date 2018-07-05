package harmony.app.Fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import harmony.app.ModelClass.DataBaseData;
import harmony.app.R;
import harmony.app.RecyclerViewAdapter.RecyclerAdapterForMyItemFragment;
import harmony.app.helper.DataHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyItemFragmentInProfileActivity extends Fragment {

    private RecyclerView recyclerViewForMyItem;
    private RecyclerView.Adapter adapterForMyItem;
    RecyclerView.LayoutManager mLayoutManager;
    DataHelper dataHelper;
    Activity activity;
    int rowCount;
    /*private ArrayList<Bitmap> bitmapArrayList;*/
    private ArrayList<String> contentSdCardUrlArrayList;
    private ArrayList<DataBaseData> dataBaseDataArrayList;
    private ArrayList<String> contentDownloadTimestampArrayList;
    View view;
    harmony.app.helper.ProgressDialog progressDialog;

    public MyItemFragmentInProfileActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_item_fragment_in_profile, container, false);
        dataHelper = new DataHelper(getActivity());
        /*bitmapArrayList=new ArrayList<>();*/
        dataBaseDataArrayList = new ArrayList<>();
        contentSdCardUrlArrayList = new ArrayList<>();
        contentDownloadTimestampArrayList = new ArrayList<>();
        activity = getActivity();
        progressDialog = new harmony.app.helper.ProgressDialog(getActivity());
        progressDialog.showProgressDialog();
        new RetriveBitMapFromDatabase().execute();
        return view;
    }

    private class RetriveBitMapFromDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Void doInBackground(Void... voids) {
            rowCount = (int) dataHelper.getRowCount();
            Log.i("Irritating", rowCount + "");
            for (int i = 0; i < rowCount; i++) {
                dataBaseDataArrayList.add(dataHelper.getAllData(i + 1));
                contentSdCardUrlArrayList.add(dataHelper.getColContentSdCardUrl(i + 1));
                contentDownloadTimestampArrayList.add(dataHelper.getContentDownloadTimestamp(i + 1));
               /* if (dataBaseDataArrayList.get(i).getContentType().contains("audio") || dataBaseDataArrayList.get(i).getContentType().contains("video"))
                {
                    bitmapArrayList.add(null);
                }
                else
                {
                    bitmapArrayList.add(dataHelper.getBitmap(i+1));
                }*/
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            ///progressDialog.hideProgressDialog();
            updateRecyclerView();
        }
    }

    public void updateRecyclerView() {
        recyclerViewForMyItem = view.findViewById(R.id.RV_MyItemFragMent);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForMyItem.setLayoutManager(mLayoutManager);
        recyclerViewForMyItem.setHasFixedSize(true);
        /*adapterForMyItem = new RecyclerAdapterForMyItemFragment(getActivity(),bitmapArrayList,dataBaseDataArrayList);*/
        adapterForMyItem = new RecyclerAdapterForMyItemFragment(getActivity(), dataBaseDataArrayList, contentSdCardUrlArrayList, contentDownloadTimestampArrayList);
        recyclerViewForMyItem.setAdapter(adapterForMyItem);
        progressDialog.hideProgressDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog.getAlertDialog() != null)
            progressDialog.hideProgressDialog();
    }
}