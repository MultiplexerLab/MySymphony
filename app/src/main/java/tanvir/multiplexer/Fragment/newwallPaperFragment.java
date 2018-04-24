package tanvir.multiplexer.Fragment;


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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import tanvir.multiplexer.R;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForNewWallPaperFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class newwallPaperFragment extends Fragment {

    ArrayList<String> seraChobiArrayList;

    private RecyclerView recyclerViewForNewWallPaper;
    private RecyclerView.Adapter adapterForNewWallPaper;




    public newwallPaperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_wall_paper, container, false);


        if (getActivity().getIntent()!=null)
        {
            seraChobiArrayList = getActivity().getIntent().getStringArrayListExtra("galleryImageData");
        }
        else
            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();




        recyclerViewForNewWallPaper= view.findViewById(R.id.RV_newWallPaperFragment);
        int numberOfColumns = 3;

        recyclerViewForNewWallPaper.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns,
                LinearLayoutManager.VERTICAL, false));

        SnapHelper snapHelperStartMulloChar = new GravitySnapHelper(Gravity.START);
        snapHelperStartMulloChar.attachToRecyclerView(recyclerViewForNewWallPaper);



        recyclerViewForNewWallPaper.setHasFixedSize(true);
        adapterForNewWallPaper = new RecyclerAdapterForNewWallPaperFragment(getActivity(),seraChobiArrayList);

        recyclerViewForNewWallPaper.setAdapter(adapterForNewWallPaper);




        // Inflate the layout for this fragment
        return view;
    }

}
