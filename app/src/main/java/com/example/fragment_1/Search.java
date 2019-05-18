package com.example.fragment_1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Search extends Fragment {
    private ListViewAdapter adapter;
    ListView listView = null;
    public static String foodName;
    public static JSONArray list;

    MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public static Search newnstance(){
        return new Search();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.search, container, false);
        // Inflate the layout for this fragment

        adapter = new ListViewAdapter();

        listView = (ListView) v.findViewById(R.id.listview1);
        new Server("yamukzza/search_recipe/food.php"){
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getContext(),
                        "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                try {
                    JSONObject data = new JSONObject(result);
                    list = data.getJSONArray("foodnamelist");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        ListViewItem dto = new ListViewItem();
                        dto.setTitle(item.getString("요리이름"));
                        dto.setTitle(item.getString("재료이름"));
                        dto.setContent(null);
                        adapter.addItem(item.getString("요리이름"), item.getString("재료이름"));
                    }

                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();

        setData();

        EditText editTextFilter = (EditText)v.findViewById(R.id.editTextFilter);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged (Editable edit){
                String filterText = edit.toString();
                if (filterText.length() > 0) {
                    listView.setFilterText(filterText);
                } else {
                    listView.clearTextFilter();
                }
                ((ListViewAdapter)listView.getAdapter()).getFilter().filter(filterText);
            }
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        //onItemClickListener를 추가
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject item = list.getJSONObject(position);
                    foodName = item.getString("요리이름");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                activity.replaceFragment(Recipe.newnstance(foodName));
            }
        });
        return v;
    }
    private void setData () {
        TypedArray arrResld = getResources().obtainTypedArray(R.array.resId);
        String[] titles = getResources().getStringArray(R.array.title);
        String[] contents = getResources().getStringArray(R.array.content);

//        for (int i = 0; i < arrResld.length(); i++) {
//            Custom_Weekly dto = new Custom_Weekly();
//            //dto.setResId(arrResld.getResourceId(i, 0));
//            dto.setTitle(titles[i]);
//            dto.setContent(contents[i]);
//
//            adapter.addItem(dto);
//        }
    }
}
