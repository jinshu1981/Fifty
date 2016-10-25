package com.jinshu.xuzhi.fifty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by xuzhi on 2016/9/7.
 */
public class ImageDetailFragment extends Fragment {
    private static final String IMAGE_DATA_EXTRA = "resId";
    //private int mImageNum;
    private ImageView mImageView;
    private String imageUrl;

    static ImageDetailFragment newInstance(int imageNum,String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        //args.putInt(IMAGE_DATA_EXTRA, imageNum);
        args.putString(IMAGE_DATA_EXTRA,imageUrl);
        f.setArguments(args);
        System.out.println("ImageDetailFragment:newInstance");
        return f;
    }

    // Empty constructor, required as per Fragment docs
    public ImageDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ImageDetailFragment:onCreate");
        imageUrl = getArguments() != null ?getArguments().getString(IMAGE_DATA_EXTRA) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // image_detail_fragment.xml contains just an ImageView
        final View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        System.out.println("ImageDetailFragment:onCreateView:" + imageUrl);
        Picasso.with(getContext()).load(imageUrl).resize(1000, 500)./*fit().*/into(mImageView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //final int resId = MainActivityFragment.presentationImages [mImageNum];
        //mImageView.setImageResource(resId); // Load image into ImageView
        //layout.setBackgroundResource(resId);
        //Picasso.with(getContext()).load(imageUrl).resize(2000, 1000)./*fit().*/into(mImageView);

    }


}
