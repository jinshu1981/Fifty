package com.jinshu.xuzhi.fifty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.CategoryObject;
import com.example.GigObject;
import com.squareup.picasso.Picasso;

/**
 * Created by xuzhi on 2016/9/8.
 */
public class AdapterTopCategory extends ArrayAdapter{
    private final String LOG_TAG = this.getClass().getSimpleName();
    private GigObject[] values;
    private CategoryObject[] cValues;
    private int resourceId;
    //private LayoutInflater inflater;
    private Context context;

    public AdapterTopCategory( Context ctx, int resourceId, GigObject[] values) {
    super( ctx, resourceId);
    this.values = values;
    this.resourceId = resourceId;
    context=ctx;
}
    public AdapterTopCategory( Context ctx, int resourceId, CategoryObject[] cValues) {
        super( ctx, resourceId);
        this.cValues = cValues;
        this.resourceId = resourceId;
        context=ctx;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        //Log.e(LOG_TAG, "position = " + position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resourceId, parent, false);
        if (resourceId == R.layout.recommanded_gigs_list_view)
        {
            TextView id = (TextView)view.findViewById(R.id.gigId);
            TextView title = (TextView)view.findViewById(R.id.gigTitle);
            TextView author = (TextView)view.findViewById(R.id.author);
            TextView price = (TextView)view.findViewById(R.id.price);

            id.setText(Integer.toString(values[position].id));
            title.setText(values[position].title);
            author.setText("by " + values[position].seller);
            price.setText("$"+String.valueOf(values[position].price) );
        }
        if (resourceId ==  R.layout.top_categories_list_view)
        {
            TextView name = (TextView)view.findViewById(R.id.categoryName);
            TextView introduction = (TextView)view.findViewById(R.id.categoryIntroduction);
            ImageView image = (ImageView)view.findViewById(R.id.topCategoryImage);

            name.setText(cValues[position].name);
            introduction.setText(cValues[position].introduction);
            //Log.e(LOG_TAG, "url=" + cValues[position].url);

            Picasso.with(context).load(cValues[position].url).fit().into(image);
        }
        if (resourceId == R.layout.sub_categories_list_view)
        {
            TextView name = (TextView)view.findViewById(R.id.subcategoryName);
            name.setText(cValues[position].name);
        }
        return view;
    }
    @Override
    public int getCount(){
        if (resourceId == R.layout.recommanded_gigs_list_view){
        return this.values.length;}
        if (resourceId ==  R.layout.top_categories_list_view){
            return this.cValues.length;
        }
        if (resourceId ==  R.layout.sub_categories_list_view){
            return this.cValues.length;
        }
        return 1;

    }
}
