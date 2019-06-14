package com.example.fragment_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Basket extends Fragment {
    private Custom_Adapter adapter;
    private ListView listView;
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

    public static Basket newnstance(){
        return new Basket();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.basket_list,container,false);
        adapter = new Custom_Adapter();
        listView = (ListView) rootview.findViewById(R.id.listView);

        new Server("yamukzza/cart/cartlist.php"){
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
                    JSONArray list = data.getJSONArray("cartlist");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        Custom_Weekly dto = new Custom_Weekly();
                        dto.setResId(item.getString("이미지"));
                        dto.setTitle(item.getString("재료명"));
                        dto.setContent(item.getString("가격")+"원");
                        adapter.addItem(dto);
                    }
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();
        listView.setAdapter(adapter);
        return rootview;
    }
}
