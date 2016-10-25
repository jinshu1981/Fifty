package com.jinshu.xuzhi.fifty;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.GigDetailInfo;
import com.example.GigObject;

/**
 * Created by xuzhi on 2016/9/9.
 */
public class Utility {

    private final String LOG_TAG = this.getClass().getSimpleName();

    static public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
            //Log.e("eachHeight = ",Integer.toString(listItem.getMeasuredHeight()));
            System.out.println("i  ="  + i + ",height = " + Integer.toString(listItem.getMeasuredHeight()));
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    // calculate the height of expandable listview dynamically
    static  void setExpandableListViewHeight(ExpandableListView expListView/*, int group*/) {

        ExpandableListAdapter listAdapter = expListView
                .getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(expListView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null,
                    expListView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();
            //System.out.println("group " + i + ", height:" + groupItem.getMeasuredHeight());

            if (expListView.isGroupExpanded(i)) {
                System.out.println("group " + i + "expanded ");

                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {

                    View listItem = listAdapter.getChildView(i, j, false, null,
                            expListView);

                    listItem.setLayoutParams(new ViewGroup.LayoutParams(
                            desiredWidth, View.MeasureSpec.UNSPECIFIED));
                    // listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                    listItem.measure(View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                            .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();
                   // System.out.println("group " + i + "child " + j + ", height:" + listItem.getMeasuredHeight());
                }
            }

        }
        //System.out.println("totalHeight" + totalHeight);
        ViewGroup.LayoutParams params = expListView.getLayoutParams();
        int height = totalHeight
                + (expListView.getDividerHeight() * (listAdapter
                .getGroupCount() - 1));

        if (height < 10) {
            height = 100;
        }
        params.height = height;
        expListView.setLayoutParams(params);
        expListView.requestLayout();

    }
    static public void printGigInfo(GigDetailInfo gigInfo)
    {
                 /*    public int id;
            public String title = "";
            public String seller = "";
            public int price = 0;
            public int score = 0;
            public String subcategory = "";
            public String[] gigspicture ;
            public String createdTime = "";
            public String introduction = "";
            public Extra[] extras;*/
        System.out.println("/******Gig: "+ gigInfo.gig.title +" ********/");
        System.out.println("/****** id = "+ gigInfo.gig.id +
                                    "\n seller = " + gigInfo.gig.seller +
                                    "\n price = " + gigInfo.gig.price +
                                    "\n score = " + gigInfo.gig.score +
                                    "\n subcategory = " + gigInfo.gig.subcategory +
                                    "\n createdTime = " + gigInfo.gig.createdTime +
                                    "\n introduction = " + gigInfo.gig.introduction);
        System.out.println(" pictures["+ gigInfo.gig.picture.length+"] = ");
        for (String eachPicture:gigInfo.gig.picture)
        {
            System.out.println("\n***"+ eachPicture +" ***");
        }

        System.out.println(" extras[" +gigInfo.gig.extras.length +"] = ");
        for (GigObject.Extra eachExtra:gigInfo.gig.extras)
        {
            System.out.println("\n*** content = "+ eachExtra.content +"," + "price = " + eachExtra.price + " ***/");
        }


/*    public String name = "";
    public int level = 0;
    public int avgRespTime = 0;
    public String imageIcon = "";
    public String introduction = "";
    public String land = "";
    public String lastActiveTime
    = "";*/
        System.out.println("/******Gig: "+ gigInfo.seller.name +" ********/");
        System.out.println("/****** level = "+ gigInfo.seller.level +
                "\n avgRespTime = " + gigInfo.seller.avgRespTime +
                "\n imageIcon = " + gigInfo.seller.imageIcon +
                "\n introduction = " + gigInfo.seller.introduction +
                "\n land = " + gigInfo.seller.land +
                "\n lastActiveTime = " + gigInfo.seller.lastActiveTime +"********/");
    }
}
