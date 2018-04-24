package tanvir.multiplexer.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tanvir.multiplexer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyItemFragmentInProfileActivity extends Fragment {


    public MyItemFragmentInProfileActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_item_fragment_in_profile, container, false);
    }

}
