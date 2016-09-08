package com.jinshu.xuzhi.fifty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    View rootView;
    AdapterPresentation presentationAdapter;
    AutoHeightViewPager presentation;
    public final static int[]  presentationImages = new int[] {
            R.drawable.big11, R.drawable.big22, R.drawable.big33
    };
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        presentation = (AutoHeightViewPager)rootView.findViewById(R.id.presentation);
        presentationAdapter = new AdapterPresentation(getActivity().getSupportFragmentManager(), presentationImages.length);
        presentation.setAdapter(presentationAdapter);

        return rootView;
    }
}
