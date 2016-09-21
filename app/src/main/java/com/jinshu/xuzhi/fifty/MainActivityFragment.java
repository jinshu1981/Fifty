package com.jinshu.xuzhi.fifty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    View rootView;
    AdapterPresentation presentationAdapter;
    AutoHeightViewPager presentation;
    TextView viewAll;
    public final static int[]  presentationImages = new int[] {
            R.drawable.big11, R.drawable.big22, R.drawable.big33
    };
    private ImageView[] indicationPoint;/*指示点控件*/
    private int[] points = {R.id.point1,R.id.point2,R.id.point3};
    private int mImageGroupSize;
    public String[]values= {"1","2","3"};;

    ListView topCategoryListView,FeaturedGigsListView;
    private AdapterTopCategory mTopCategoryListAdapter,mGigIntroductionAdapter;

    class GigsInfo{
        int id;
        String title = "";
        String author = "";
        int price = 0;
        int score = 0;
    }
    GigsInfo[] mGigs;
    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        presentation = (AutoHeightViewPager)rootView.findViewById(R.id.presentation);
        topCategoryListView = (ListView)rootView.findViewById(R.id.TopCategories);
        FeaturedGigsListView = (ListView)rootView.findViewById(R.id.FeaturedGigs);
        viewAll =(TextView)rootView.findViewById(R.id.viewAll);

        Bundle bundle = this.getArguments();
        String receiveMessage = bundle.getString("receiveMessage");
        getGigsFromArguments(receiveMessage);

        mTopCategoryListAdapter = new AdapterTopCategory(getActivity(),R.layout.top_categories_list_view,mGigs);
        topCategoryListView.setAdapter(mTopCategoryListAdapter);
        Utility.setListViewHeightBasedOnChildren(topCategoryListView);

        mGigIntroductionAdapter =  new AdapterTopCategory(getActivity(),R.layout.recommanded_gigs_list_view,mGigs);
        FeaturedGigsListView.setAdapter(mGigIntroductionAdapter);
        Utility.setListViewHeightBasedOnChildren(FeaturedGigsListView);

        presentationAdapter = new AdapterPresentation(getActivity().getSupportFragmentManager(), presentationImages.length);
        presentation.setAdapter(presentationAdapter);
        presentation.setOffscreenPageLimit(presentationImages.length);
        presentation.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < points.length; i++) {
                    if (position == i) {
                        indicationPoint[i].setImageResource(R.drawable.point_white);
                    } else {
                        indicationPoint[i].setImageResource(R.drawable.point_gray);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mImageGroupSize = presentationImages.length;
        indicationPoint = new ImageView[mImageGroupSize];

        for (int i=0; i<mImageGroupSize; i++) {
            indicationPoint[i] = (ImageView) rootView.findViewById(points[i]);
        }

        return rootView;
    }

    public void getGigsFromArguments(String jsonString)
    {
         /*parse json string*/
        String OutputData = "";
        JSONObject jsonResponse;
        try {

            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
            jsonResponse = new JSONObject(jsonString);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*******  Returns null otherwise.  *******/
            JSONArray jsonMainNode = jsonResponse.optJSONArray("gigs");

            /*********** Process each JSON Node ************/

            int lengthJsonArr = jsonMainNode.length();
            mGigs = new GigsInfo[lengthJsonArr];
            for(int i=0; i < lengthJsonArr; i++)
            {
                mGigs[i] = new GigsInfo();
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                /******* Fetch node values **********/
                mGigs[i].id        = jsonChildNode.optInt("id");
                mGigs[i].title   = jsonChildNode.optString("title");
                mGigs[i].author  = jsonChildNode.optString("author");
                mGigs[i].price   = jsonChildNode.optInt("price");
                mGigs[i].score   = jsonChildNode.optInt("score");

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }finally {
            //System.out.println(OutputData);
        }
    }
}
