package com.jinshu.xuzhi.fifty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.GigDetailInfo;

import java.util.ArrayList;

/**
 * Created by xuzhi on 2016/9/30.
 */
public class AdapterGigExtraExpandableListView extends BaseExpandableListAdapter {

    private GigDetailInfo gigDetailInfo;
    private ArrayList<String> parentItems, child;
    Context context;
    LayoutInflater inflater ;
    public AdapterGigExtraExpandableListView(Context ctx, GigDetailInfo gigDetailInfo) {
        this.gigDetailInfo = gigDetailInfo;
        this.context = ctx;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gigs_extras_child_list_view, null);
        }

        return convertView;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gigs_extras_group_list_view, null);
        }

        TextView extraContent = (TextView) convertView.findViewById(R.id.checkbox_extraContent);
        TextView price = (TextView)convertView.findViewById(R.id.price);
        extraContent.setText(gigDetailInfo.gig.extras[groupPosition].content);
        price.setText("$" + gigDetailInfo.gig.extras[groupPosition].price);

        int lineNumber = gigDetailInfo.gig.extras[groupPosition].content.length() /35 + 1;//每行35个字母
        extraContent.setLines(lineNumber);
        return convertView;
    }
    @Override

    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }
    @Override
    public int getGroupCount() {
        return gigDetailInfo.gig.extras.length;
    }
    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
