package lct.mysymphony.Fragment;


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

import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForMyItemFragment;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForNewWallPaperFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyItemFragmentInProfileActivity extends Fragment {

    private RecyclerView recyclerViewForMyItem;
    private RecyclerView.Adapter adapterForMyItem;
    RecyclerView.LayoutManager mLayoutManager;



    public MyItemFragmentInProfileActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_my_item_fragment_in_profile, container, false);;

        recyclerViewForMyItem = view.findViewById(R.id.RV_MyItemFragMent);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForMyItem.setLayoutManager(mLayoutManager);
        recyclerViewForMyItem.setHasFixedSize(true);
        adapterForMyItem = new RecyclerAdapterForMyItemFragment(getActivity());
        recyclerViewForMyItem.setAdapter(adapterForMyItem);
        return view;
    }
}
