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
public class PopularWallPaperFragment extends Fragment {

    public PopularWallPaperFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_wall_paper, container, false);
    }

}
