package com.jinshu.xuzhi.fifty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.GigDetailInfo;
import com.example.GigObject;
import com.example.JsonDefination;
import com.example.ReviewObject;
import com.example.SellerObject;
import com.example.SocketCommProtocol;
import com.example.SqlDatabaseItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentGigDetail extends Fragment implements TaskListener {
    private final String LOG_TAG = this.getClass().getSimpleName();
    View rootView;
    AdapterGigExtraExpandableListView mAdapterGigExtras;
    AdapterReviewListView mAdapterReviewList;
    ExpandableListView extrasList;
    ListView reviewList;
    static GigDetailInfo mGigInfo;
    LinearLayout sellerInfo,gigTitleInfo;
    ImageView sellerExpandArrow;
    FragmentGigDetail mThis;
    TextView moreReview;
    AdapterPresentation presentationAdapter;
    AutoHeightViewPager presentation;
    TextView presentationIndicator;
    int i = 0;
    public FragmentGigDetail() {
        mThis = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gig_detail, container, false);

        /*TaskForCommunication中传入数据*/
        if (getActivity().getIntent().hasExtra(Intent.EXTRA_TEXT)) {
            String categoryListJson = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            System.out.println("FragmentGigDetail getIntent");
            getGigsDetailFromArguments(categoryListJson);
        }
        /*gig info*/
        TextView gigTitle = (TextView)rootView.findViewById(R.id.gigsTitle);
        TextView gigSubcategory = (TextView)rootView.findViewById(R.id.subcategory);
        TextView gigIntroduction = (TextView)rootView.findViewById(R.id.gigsIntroduction);
        TextView totalReviewNumber = (TextView)rootView.findViewById(R.id.reviewsNumber);
        Button order = (Button)rootView.findViewById(R.id.buttonOrder);
        Button orderExtras = (Button)rootView.findViewById(R.id.buttonOrderExtras);
        presentationIndicator = (TextView)rootView.findViewById(R.id.indicator);
        moreReview = (TextView)rootView.findViewById(R.id.moreReview);
        presentation = (AutoHeightViewPager)rootView.findViewById(R.id.presentation);

        presentationAdapter = new AdapterPresentation(getActivity().getSupportFragmentManager(), mGigInfo.gig.picture);
        presentation.setAdapter(presentationAdapter);
        presentationIndicator.setText("1 of " + mGigInfo.gig.picture.length);
        presentation.setOffscreenPageLimit(mGigInfo.gig.picture.length);
        //System.out.println("presentationAdapter4");
        presentation.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //System.out.println("onPageSelected length = " + points.length);
                presentationIndicator.setText(String.valueOf(position + 1) + " of " + mGigInfo.gig.picture.length);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*所有review显示后，隐藏更新标识*/
        if ((mGigInfo.review.size() == mGigInfo.totalReviewNumber)||(mGigInfo.review.size() == JsonDefination.TOTAL_REVIEW_LIMIT))
            moreReview.setVisibility(View.GONE);

        extrasList = (ExpandableListView)rootView.findViewById(R.id.gigsExtras);
        mAdapterGigExtras = new AdapterGigExtraExpandableListView(getActivity(),mGigInfo);
        extrasList.setAdapter(mAdapterGigExtras);
        Utility.setListViewHeightBasedOnChildren(extrasList);

        mAdapterReviewList =  new AdapterReviewListView(getActivity(),mGigInfo);
        reviewList = (ListView)rootView.findViewById(R.id.reviews);
        reviewList.setAdapter(mAdapterReviewList);
        Utility.setListViewHeightBasedOnChildren(reviewList);

        gigTitle.setText(mGigInfo.gig.title);
        gigSubcategory.setText(mGigInfo.gig.subcategory);
        gigIntroduction.setText(mGigInfo.gig.introduction);
        totalReviewNumber.setText(String.valueOf(mGigInfo.totalReviewNumber) + " Reviews");
        /*seller info*/
        ImageView sellerImageIcon = (ImageView)rootView.findViewById(R.id.sellerIcon);
        sellerExpandArrow = (ImageView)rootView.findViewById(R.id.sellerexpandArrow);
        sellerExpandArrow.setTag(R.drawable.arrow_down_float);

        sellerInfo  = (LinearLayout)rootView.findViewById(R.id.sellerInfo);
        sellerInfo.setVisibility(View.GONE);
        gigTitleInfo = (LinearLayout)rootView.findViewById(R.id.gigTitleInfo);

        TextView sellerName = (TextView)rootView.findViewById(R.id.sellerName);
        TextView sellerIntroduction = (TextView)rootView.findViewById(R.id.sellerIntroduction);
        Picasso.with(getActivity()).load(mGigInfo.seller.imageIcon).into(sellerImageIcon);
        sellerName.setText(mGigInfo.gig.seller);
        sellerIntroduction.setText(mGigInfo.seller.introduction);

        gigTitleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sellerExpandArrow.getTag().equals(R.drawable.arrow_down_float)) {
                    sellerInfo.setVisibility(View.VISIBLE);
                    sellerExpandArrow.setTag(R.drawable.arrow_up_float);
                    sellerExpandArrow.setImageResource(R.drawable.arrow_up_float);
                } else {
                    sellerInfo.setVisibility(View.GONE);
                    sellerExpandArrow.setTag(R.drawable.arrow_down_float);
                    sellerExpandArrow.setImageResource(R.drawable.arrow_down_float);
                }
            }
        });

        extrasList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Utility.setExpandableListViewHeight(extrasList);

            }
        });
        extrasList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Utility.setExpandableListViewHeight(extrasList);
            }
        });

        moreReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*最多显示25条记录*/
                if ((mGigInfo.review.size() < mGigInfo.totalReviewNumber) || mGigInfo.review.size() >= JsonDefination.TOTAL_REVIEW_LIMIT) {
                    TaskForCommunication task = new TaskForCommunication(getActivity(), mThis);
                    task.execute(SocketCommProtocol.GIG_REVIEW_REQUEST + "/" + mGigInfo.gig.id + "/" + mGigInfo.review.get(mGigInfo.review.size() - 1).id);
                } else {

                    //Log.e(LOG_TAG,"error! review size = "+mGigInfo.review.size()+" total = " + mGigInfo.totalReviewNumber);
                }
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.jinshu.xuzhi.fifty.ActivityPayment.class);
                //intent.putExtra(Intent.EXTRA_TEXT,receiveMessage);
                startActivity(intent);
            }
        });
        return rootView;
    }
    @Override
    public void onReviewTaskCompleted(String receiveMessage) {
        System.out.println("onReviewTaskCompleted");
        getReviewFromArguments(receiveMessage);
        //reviewList.setAdapter(mAdapterReviewList);
        //reviewList.invalidateViews();
        mAdapterReviewList.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(reviewList);
        if ((mGigInfo.review.size() == mGigInfo.totalReviewNumber)||(mGigInfo.review.size() == JsonDefination.TOTAL_REVIEW_LIMIT))
            moreReview.setVisibility(View.GONE);

        //mAdapterReviewList.notifyDataSetChanged();
    }
    public void getGigsDetailFromArguments(String jsonString)
    {
         /*parse json string*/
        JSONObject jsonResponse;
        try {
            /*Creates a new JSONObject with name/value mappings from the JSON string.*/
            jsonResponse = new JSONObject(jsonString);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*Returns null otherwise.*/
            JSONObject jsonObjectNode = jsonResponse.getJSONObject(SocketCommProtocol.GIG_DETAIL_REQUEST);

            JSONObject jsonGigNode = jsonObjectNode.getJSONObject(SqlDatabaseItem.GigTable.TABLE_NAME);
            JSONObject jsonSellerNode = jsonObjectNode.getJSONObject(SqlDatabaseItem.SellerTable.TABLE_NAME);
            System.out.println("node 1");
            JSONObject jsonTotalReviewNumberNode = jsonObjectNode.getJSONObject(JsonDefination.TOTAL_REVIEW_NUMBER);
            System.out.println("node 2");
            JSONArray jsonReviewArrayNode = jsonObjectNode.optJSONArray(JsonDefination.REVIEW_ARRAY);
            System.out.println("node 3");
            /*********** Process each Gigs Node ************/
            int lengthReviewArray = jsonReviewArrayNode.length();
            System.out.println("node 4");
            /*********** Process gig Node *************/
            mGigInfo = new GigDetailInfo();
            mGigInfo.gig = new GigObject();
            mGigInfo.seller = new SellerObject();
            System.out.println("node 5");
            mGigInfo.gig.id   = jsonGigNode.getInt(SqlDatabaseItem.GigTable.COLUMN_ID);
            mGigInfo.gig.title   = jsonGigNode.optString(SqlDatabaseItem.GigTable.COLUMN_TITLE);
            mGigInfo.gig.seller   = jsonGigNode.optString(SqlDatabaseItem.GigTable.COLUMN_SELLER);
            mGigInfo.gig.price   = jsonGigNode.optInt(SqlDatabaseItem.GigTable.COLUMN_PRICE);
            mGigInfo.gig.score   = jsonGigNode.optInt(SqlDatabaseItem.GigTable.COLUMN_SCORE);
            mGigInfo.gig.subcategory   = jsonGigNode.optString(SqlDatabaseItem.GigTable.COLUMN_SUBCATEGORY);
            mGigInfo.gig.createdTime   = jsonGigNode.optString(SqlDatabaseItem.GigTable.COLUMN_CREATEDTIME);
            mGigInfo.gig.introduction   = jsonGigNode.optString(SqlDatabaseItem.GigTable.COLUMN_INTRODUCTION);
            String allPictures = jsonGigNode.optString(SqlDatabaseItem.GigTable.COLUMN_PICTURES);;
            mGigInfo.gig.picture = allPictures.split(";");
            System.out.println("node 6");
            String allExtras = jsonGigNode.optString(SqlDatabaseItem.GigTable.COLUMN_EXTRAS);
            String []extrasArray = allExtras.split(";");
            System.out.println("node 7");
            GigObject.Extra []extras = new GigObject.Extra[extrasArray.length];
            System.out.println("node 8");
            for (int i = 0;i <extrasArray.length;i++)
            {
                String eachExtra = extrasArray[i];
                String[] extra = eachExtra.split(":");
                extras[i] = new GigObject().new Extra(extra[0],extra[1]);
            }
            System.out.println("node 9");
            mGigInfo.gig.extras = extras;
            mGigInfo.seller.name   = jsonSellerNode.optString(SqlDatabaseItem.SellerTable.COLUMN_NAME);
            mGigInfo.seller.level   = jsonSellerNode.optInt(SqlDatabaseItem.SellerTable.COLUMN_LEVEL);
            mGigInfo.seller.avgRespTime   = jsonSellerNode.optInt(SqlDatabaseItem.SellerTable.COLUMN_AVGRESPTIME);
            mGigInfo.seller.imageIcon   = jsonSellerNode.optString(SqlDatabaseItem.SellerTable.COLUMN_IMAGEICON);
            mGigInfo.seller.introduction   = jsonSellerNode.optString(SqlDatabaseItem.SellerTable.COLUMN_INTRODUCTION);
            mGigInfo.seller.land   = jsonSellerNode.optString(SqlDatabaseItem.SellerTable.COLUMN_LAND);
            mGigInfo.seller.lastActiveTime   = jsonSellerNode.optString(SqlDatabaseItem.SellerTable.COLUMN_LASTACTVIETIME);
            /*total review number*/
            System.out.println("total review number");
            mGigInfo.totalReviewNumber = jsonTotalReviewNumberNode.optInt(JsonDefination.TOTAL_REVIEW_NUMBER);
            System.out.println("total review number1");
            /*review array*/

            //mGigInfo.review = new ReviewObject[lengthReviewArray];
            System.out.println("review array");
            mGigInfo.review = new ArrayList<ReviewObject>();

            for(int i=0; i < lengthReviewArray; i++)
            {

                ReviewObject review = new ReviewObject();
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonReviewArrayNode.getJSONObject(i);
                /******* Fetch node values **********/
                review.id          = jsonChildNode.optInt(SqlDatabaseItem.ReviewTable.COLUMN_ID);
                review.author      = jsonChildNode.optString(SqlDatabaseItem.ReviewTable.COLUMN_AUTHOR);
                review.content   = jsonChildNode.optString(SqlDatabaseItem.ReviewTable.COLUMN_CONTENT);
                review.feedback  = jsonChildNode.optString(SqlDatabaseItem.ReviewTable.COLUMN_FEEDBACK);
                review.score   = jsonChildNode.optInt(SqlDatabaseItem.ReviewTable.COLUMN_VOTESCORE);

                mGigInfo.review.add(review);
            }
            System.out.println("review array1");
            /*print gig info*/
            Utility.printGigInfo(mGigInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            //System.out.println(OutputData);
        }
    }

    public void getReviewFromArguments(String jsonString)
    {
         /*parse json string*/
        JSONObject jsonResponse;
        try {

            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
            jsonResponse = new JSONObject(jsonString);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*******  Returns null otherwise.  *******/
            JSONArray jsonReviewArrayNode = jsonResponse.optJSONArray(JsonDefination.REVIEW_ARRAY);
            /*********** Process each Gigs Node ************/
            int lengthReviewArray = jsonReviewArrayNode.length();
            for(int i=0; i < lengthReviewArray; i++)
            {

                ReviewObject review = new ReviewObject();
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonReviewArrayNode.getJSONObject(i);
                /******* Fetch node values **********/
                review.id          = jsonChildNode.optInt(SqlDatabaseItem.ReviewTable.COLUMN_ID);
                review.author      = jsonChildNode.optString(SqlDatabaseItem.ReviewTable.COLUMN_AUTHOR);
                review.content   = jsonChildNode.optString(SqlDatabaseItem.ReviewTable.COLUMN_CONTENT);
                review.feedback  = jsonChildNode.optString(SqlDatabaseItem.ReviewTable.COLUMN_FEEDBACK);
                review.score   = jsonChildNode.optInt(SqlDatabaseItem.ReviewTable.COLUMN_VOTESCORE);

                mGigInfo.review.add(review);
            }
            System.out.println("mGigInfo.review.size = " + mGigInfo.review.size());


        } catch (JSONException e) {

            e.printStackTrace();
        }finally {
            //System.out.println(OutputData);
        }
    }
}
