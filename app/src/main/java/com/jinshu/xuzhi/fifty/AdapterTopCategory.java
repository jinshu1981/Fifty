package com.jinshu.xuzhi.fifty;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by xuzhi on 2016/9/8.
 */
public class AdapterTopCategory extends ArrayAdapter{
    private final String LOG_TAG = this.getClass().getSimpleName();
    private MainActivityFragment.GigsInfo[] values;
    private int resourceId;
    //private LayoutInflater inflater;
    private Context context;

    public AdapterTopCategory( Context ctx, int resourceId, MainActivityFragment.GigsInfo[] values) {
    super( ctx, resourceId);
    this.values = values;
    this.resourceId = resourceId;
    //inflater = LayoutInflater.from(ctx);
    context=ctx;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        Log.e(LOG_TAG, "position = " + position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resourceId, parent, false);
        if (resourceId == R.layout.recommanded_gigs_list_view)
        {
            TextView title = (TextView)view.findViewById(R.id.gigName);
            TextView author = (TextView)view.findViewById(R.id.author);
            TextView price = (TextView)view.findViewById(R.id.price);

            title.setText(values[position].title);
            author.setText("by " + values[position].author);
            price.setText("$"+String.valueOf(values[position].price) );
        }
        return view;
    }
    @Override
    public int getCount(){return this.values.length;}
}
