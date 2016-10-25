package com.jinshu.xuzhi.fifty;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGigPresentation extends Fragment {
    private static final String IMAGE_DATA_EXTRA = "resId";
    private static final String IMAGE_POSITION = "position";
    private String mPresentationUrl;
    private Context mContext;
    private ImageView mImageView;
    //private LinearLayout layout;

    static FragmentGigPresentation newInstance(Context context,String presentationUrl) {
        final FragmentGigPresentation f = new FragmentGigPresentation();
        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, presentationUrl);
        f.setArguments(args);
        return f;
    }

    // Empty constructor, required as per Fragment docs
    public FragmentGigPresentation(){

    }
    public FragmentGigPresentation(Context context,String presentationUrl){
        this.mPresentationUrl = presentationUrl;
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() == null)return;
        //mPresentationUrl = getArguments().getString(IMAGE_DATA_EXTRA) ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // image_detail_fragment.xml contains just an ImageView
        final View v = inflater.inflate(R.layout.fragment_gig_presentation, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mImageView.setImageResource(resId); // Load image into ImageView
        //layout.setBackgroundResource(resId);
        Picasso.with(mContext).load(mPresentationUrl).fit().into(mImageView);

    }


}
