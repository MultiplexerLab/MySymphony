package lct.mysymphony.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lct.mysymphony.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class other_sports_fragment extends Fragment {


    public other_sports_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_sports_fragment, container, false);
    }

}
