package com.jinshu.xuzhi.fifty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.CategoryObject;
import com.example.GigObject;
import com.example.JsonDefination;
import com.example.SocketCommProtocol;
import com.example.SqlDatabaseItem;

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
    public final static String[]  presentationImages = new String[] {
            "https://s22.postimg.org/cylwjowoh/presentation1.png",
            "https://s13.postimg.org/gh6jizid3/presentation2.png",
            "https://s10.postimg.org/morktrwvd/presentation3.png"
    };
    private ImageView[] indicationPoint;/*指示点控件*/
    private int[] points = {R.id.point1,R.id.point2,R.id.point3};
    private int mImageGroupSize;
    //public String[]values= {"1","2","3"};;

    ListView topCategoryListView,FeaturedGigsListView;
    private AdapterTopCategory mTopCategoryListAdapter,mGigIntroductionAdapter;

    GigObject[] mGigs;
    CategoryObject[] mCategoryInfo;

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

        mTopCategoryListAdapter = new AdapterTopCategory(getActivity(),R.layout.top_categories_list_view, mCategoryInfo);
        topCategoryListView.setAdapter(mTopCategoryListAdapter);
        Utility.setListViewHeightBasedOnChildren(topCategoryListView);

        mGigIntroductionAdapter =  new AdapterTopCategory(getActivity(),R.layout.recommanded_gigs_list_view,mGigs);
        FeaturedGigsListView.setAdapter(mGigIntroductionAdapter);
        Utility.setListViewHeightBasedOnChildren(FeaturedGigsListView);
        //System.out.println("presentationAdapter1");
        presentationAdapter = new AdapterPresentation(getActivity().getSupportFragmentManager(), presentationImages);
        //System.out.println("presentationAdapter2");
        presentation.setAdapter(presentationAdapter);
        //System.out.println("presentationAdapter3");
        presentation.setOffscreenPageLimit(presentationImages.length);
        //System.out.println("presentationAdapter4");
        presentation.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //System.out.println("onPageSelected length = " + points.length);
                for (int i = 0; i < points.length; i++) {
                    if (position == i) {
                        //System.out.println("onPageSelected white position = " + position);
                        indicationPoint[i].setImageResource(R.drawable.point_white);
                    } else {
                        //System.out.println("onPageSelected gray position = " + position);
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

        /*View all*/
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskForCommunication task = new TaskForCommunication(getActivity());
                task.execute(SocketCommProtocol.CATEGORY_REQUEST);
            }
        });

        /*topCategoryListView item click */
        topCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView categoryName = (TextView)view.findViewById(R.id.categoryName);
                TaskForCommunication task = new TaskForCommunication(getActivity());
                task.execute(SocketCommProtocol.SUBCATEGORY_REQUEST + "/" + categoryName.getText().toString());
            }
        });
        FeaturedGigsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView gigId = (TextView)view.findViewById(R.id.gigId);
                TaskForCommunication task = new TaskForCommunication(getActivity());
                task.execute(SocketCommProtocol.GIG_DETAIL_REQUEST + "/" + gigId.getText().toString());
            }
        });
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
            JSONArray jsonGigsNode = jsonResponse.optJSONArray(JsonDefination.FEATURED_GIG_ARRAY);
            JSONArray jsonTopCategoriesNode = jsonResponse.optJSONArray(JsonDefination.TOP_CATEGORY_ARRAY);
            /*********** Process each Gigs Node ************/
            int lengthJsonArr = jsonGigsNode.length();
            mGigs = new GigObject[lengthJsonArr];
            for(int i=0; i < lengthJsonArr; i++)
            {

                mGigs[i] = new GigObject();
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonGigsNode.getJSONObject(i);
                /******* Fetch node values **********/
                mGigs[i].id      = jsonChildNode.optInt(SqlDatabaseItem.GigTable.COLUMN_ID);
                mGigs[i].title   = jsonChildNode.optString(SqlDatabaseItem.GigTable.COLUMN_TITLE);
                mGigs[i].seller  = jsonChildNode.optString(SqlDatabaseItem.GigTable.COLUMN_SELLER);
                mGigs[i].price   = jsonChildNode.optInt(SqlDatabaseItem.GigTable.COLUMN_PRICE);
                mGigs[i].score   = jsonChildNode.optInt(SqlDatabaseItem.GigTable.COLUMN_SCORE);
            }

            /*********** Process each top Category Node ************/
            lengthJsonArr = jsonTopCategoriesNode.length();
            mCategoryInfo = new CategoryObject[lengthJsonArr];
            for(int i=0; i < lengthJsonArr; i++)
            {
                mCategoryInfo[i] = new CategoryObject();
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonTopCategoriesNode.getJSONObject(i);
                /******* Fetch node values **********/
                mCategoryInfo[i].name   = jsonChildNode.optString(SqlDatabaseItem.CategoryTable.COLUMN_NAME);
                mCategoryInfo[i].introduction   = jsonChildNode.optString(SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION);
                mCategoryInfo[i].url   = jsonChildNode.optString(SqlDatabaseItem.CategoryTable.COLUMN_URL);
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }finally {
            //System.out.println(OutputData);
        }
    }
}
