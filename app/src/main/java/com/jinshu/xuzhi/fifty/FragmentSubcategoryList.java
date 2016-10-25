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
public class FragmentSubcategoryList extends Fragment {
    View rootView;
    ListView subcategoryListView;
    AdapterTopCategory msubcategoryListAdapter;
    CategoryObject[] mSubcategoriesInfo;
    private final String LOG_TAG = this.getClass().getSimpleName();
    public FragmentSubcategoryList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_subcategory_list, container, false);

        subcategoryListView = (ListView)rootView.findViewById(R.id.subCategoryList);
        /*TaskForCommunication中传入数据*/
        if (getActivity().getIntent().hasExtra(Intent.EXTRA_TEXT)) {
            String categoryListJson = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            getSubcategoryListFromArguments(categoryListJson);
        }

        msubcategoryListAdapter = new AdapterTopCategory(getActivity(),R.layout.sub_categories_list_view,mSubcategoriesInfo);
        subcategoryListView.setAdapter(msubcategoryListAdapter);
        Utility.setListViewHeightBasedOnChildren(subcategoryListView);
        return rootView;
    }
    public void getSubcategoryListFromArguments(String jsonString)
    {
         /*parse json string*/
        JSONObject jsonResponse;
        try {

            /*Creates a new JSONObject with name/value mappings from the JSON string.*/
            jsonResponse = new JSONObject(jsonString);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*Returns null otherwise.*/
            JSONArray jsonTopCategoriesNode = jsonResponse.optJSONArray(JsonDefination.SUBCATEGORY_ARRAY);
            int lengthJsonArr;

            /*********** Process each top Category Node ************/
            lengthJsonArr = jsonTopCategoriesNode.length();
            mSubcategoriesInfo = new CategoryObject[lengthJsonArr];
            for(int i=0; i < lengthJsonArr; i++)
            {
                mSubcategoriesInfo[i] = new CategoryObject();
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonTopCategoriesNode.getJSONObject(i);
                /******* Fetch node values **********/
                mSubcategoriesInfo[i].name   = jsonChildNode.optString(SqlDatabaseItem.SubcategoryTable.COLUMN_SUBCATEGORY);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            //System.out.println(OutputData);
        }
    }
}
