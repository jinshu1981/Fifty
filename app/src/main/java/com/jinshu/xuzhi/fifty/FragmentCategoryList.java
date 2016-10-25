package com.jinshu.xuzhi.fifty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.CategoryObject;
import com.example.JsonDefination;
import com.example.SqlDatabaseItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentCategoryList extends Fragment {
    View rootView;
    ListView categoryListView;
    private AdapterTopCategory mCategoryListAdapter;

    CategoryObject[] mCategoriesInfo;
    public FragmentCategoryList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ategory_list, container, false);

        categoryListView = (ListView)rootView.findViewById(R.id.categoryList);
        /*编辑自定义学习内容时，intent中传入数据*/
        if (getActivity().getIntent().hasExtra(Intent.EXTRA_TEXT)) {
            String categoryListJson = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            getCategoryListFromArguments(categoryListJson);
        }

        mCategoryListAdapter = new AdapterTopCategory(getActivity(),R.layout.top_categories_list_view,mCategoriesInfo);
        categoryListView.setAdapter(mCategoryListAdapter);
        Utility.setListViewHeightBasedOnChildren(categoryListView);
        return rootView;
    }

    public void getCategoryListFromArguments(String jsonString)
    {
         /*parse json string*/
        JSONObject jsonResponse;
        try {

            /*Creates a new JSONObject with name/value mappings from the JSON string.*/
            jsonResponse = new JSONObject(jsonString);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*Returns null otherwise.*/
            JSONArray jsonTopCategoriesNode = jsonResponse.optJSONArray(JsonDefination.TOP_CATEGORY_ARRAY);
            int lengthJsonArr;

            /*********** Process each top Category Node ************/
            lengthJsonArr = jsonTopCategoriesNode.length();
            mCategoriesInfo = new CategoryObject[lengthJsonArr];
            for(int i=0; i < lengthJsonArr; i++)
            {
                mCategoriesInfo[i] = new CategoryObject();
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonTopCategoriesNode.getJSONObject(i);
                /******* Fetch node values **********/
                mCategoriesInfo[i].name   = jsonChildNode.optString(SqlDatabaseItem.CategoryTable.COLUMN_NAME);
                mCategoriesInfo[i].introduction   = jsonChildNode.optString(SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION);
                mCategoriesInfo[i].url   = jsonChildNode.optString(SqlDatabaseItem.CategoryTable.COLUMN_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {

        }
    }
}
