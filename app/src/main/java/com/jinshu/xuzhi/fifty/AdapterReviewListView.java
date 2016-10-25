package com.jinshu.xuzhi.fifty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.GigDetailInfo;

/**
 * Created by xuzhi on 2016/9/27.
 */
public class AdapterReviewListView extends ArrayAdapter {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private GigDetailInfo gigDetailInfo;

    LayoutInflater inflater;

    private Context context;

    public AdapterReviewListView(Context ctx, GigDetailInfo gigDetailInfo) {
        super( ctx,R.layout.review_list_item);
        context=ctx;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.gigDetailInfo = gigDetailInfo;//FragmentGigDetail.mGigInfo;
        System.out.println("AdapterReviewListView constructor");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }
        TextView content = (TextView)convertView.findViewById(R.id.content);
        TextView feedback = (TextView)convertView.findViewById(R.id.feedback);
        LinearLayout feedbackLayout = (LinearLayout)convertView.findViewById(R.id.feedbackLayout);
        LinearLayout reviewLayout = (LinearLayout)convertView.findViewById(R.id.reviewLayout);

        content.setText(gigDetailInfo.review.get(position).content);
        int lineNumber = gigDetailInfo.review.get(position).content.length() /35 + 1;//每行35个字母
        content.setLines(lineNumber);

        if (!gigDetailInfo.review.get(position).feedback.equals("null")){
            feedback.setText(gigDetailInfo.review.get(position).feedback);
        }
        else{
            feedbackLayout.setVisibility(View.GONE);
            reviewLayout.setBackgroundResource(R.drawable.review_list_border);
        }

        return convertView;
    }
    @Override
    public int getCount(){
            System.out.println("getCount = "+ gigDetailInfo.review.size());
            return gigDetailInfo.review.size();

    }

}
