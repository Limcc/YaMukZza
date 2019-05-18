package com.example.fragment_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recommend extends Fragment {

    private Custom_Adapter adapter;
    private ListView listView;
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
    public static Recommend newnstance(){
        return new Recommend();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.recommend,container,false);
        // Inflate the layout for this fragment
        adapter = new Custom_Adapter();
        listView = (ListView) rootview.findViewById(R.id.listView);

        new Server("yamukzza/cart/recommend.php"){
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
                    list = data.getJSONArray("cart recommend list");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        Custom_Weekly dto = new Custom_Weekly();
                        dto.setResId(item.getString("이미지"));
                        dto.setTitle(item.getString("요리이름"));
                        dto.setContent(null);
                        adapter.addItem(dto);
                    }

                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();

        setData();

        listView.setAdapter(adapter);
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
        return rootview;
    }

    // 보통 ListView는 통신을 통해 가져온 데이터를 보여줍니다.
    // arrResId, titles, contents를 서버에서 가져온 데이터라고 생각하시면 됩니다.
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
