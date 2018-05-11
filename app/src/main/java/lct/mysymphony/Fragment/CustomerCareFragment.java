package lct.mysymphony.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lct.mysymphony.R;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForCustomerCareFragment;
import lct.mysymphony.RecycleerViewAdapter.RecyclerAdapterForMyItemFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCareFragment extends Fragment {

    private RecyclerView recyclerViewForMyItem;
    private RecyclerView.Adapter adapterForMyItem;
    RecyclerView.LayoutManager mLayoutManager;

    public CustomerCareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> customerCareTitleArraylist = new ArrayList<>();

        customerCareTitleArraylist.add("Dhaka Mirpur");
        customerCareTitleArraylist.add("Dhaka Mirpur");
        customerCareTitleArraylist.add("Dhaka Mirpur");
        customerCareTitleArraylist.add("Dhaka Mirpur");
        customerCareTitleArraylist.add("Dhaka Mirpur");
        customerCareTitleArraylist.add("Dhaka Mirpur");
        customerCareTitleArraylist.add("Dhaka Mirpur");

        View view=inflater.inflate(R.layout.fragment_customer_care, container, false);
        recyclerViewForMyItem = view.findViewById(R.id.RV_customerCareFragment);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForMyItem.setLayoutManager(mLayoutManager);
        recyclerViewForMyItem.setHasFixedSize(true);
        adapterForMyItem = new RecyclerAdapterForCustomerCareFragment(getActivity(),customerCareTitleArraylist);
        recyclerViewForMyItem.setAdapter(adapterForMyItem);

        return view;
    }
}
