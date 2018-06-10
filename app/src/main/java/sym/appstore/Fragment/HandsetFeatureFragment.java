package sym.appstore.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sym.appstore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandsetFeatureFragment extends Fragment {
    public HandsetFeatureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_handset_fiture, container, false);
    }
}
