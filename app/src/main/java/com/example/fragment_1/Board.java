package com.example.fragment_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Board extends Fragment {
    public static Board newnstance(){
        return new Board();
    }
    MainActivity activity;
    private Custom_Adapter_Board adapter;
    public static String foodName;
    private ListView listView;
//    public static String foodName;
    public static JSONArray list;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.board,container,false);
        // Inflate the layout for this fragment

        adapter = new Custom_Adapter_Board();
        listView = (ListView) rootview.findViewById(R.id.listView3);
        new Server("yamukzza/board/list.php"){
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
                    list = data.getJSONArray("boardlist");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        Custom_Board dto2 = new Custom_Board();
                        dto2.setResId(item.getString("boardnum"));
                        dto2.setTitle(item.getString("글 제목"));
                        adapter.addItem(dto2);
                    }

                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();

        setData();

        listView.setAdapter(adapter);



        Button button1 = (Button) rootview.findViewById(R.id.write);
        button1.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                activity.replaceFragment(RecipeWrite.newnstance());
            }
        });
        //onItemClickListener를 추가
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject item = list.getJSONObject(position);
                    foodName = item.getString("글 제목");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                activity.replaceFragment(BoardView.newnstance(foodName));
            }
        });
        return rootview;
    }
    private void setData () {
        TypedArray arrResld = getResources().obtainTypedArray(R.array.resId);
        String[] titles = getResources().getStringArray(R.array.title);
        String[] contents = getResources().getStringArray(R.array.content);

/*        for (int i = 0; i < arrResld.length(); i++) {
            Custom_Weekly dto = new Custom_Weekly();
            dto.setResId(arrResld.getResourceId(i, 0));
            dto.setTitle(titles[i]);
            dto.setContent(contents[i]);

            adapter.addItem(dto);
        }*/
    }
}