package com.example.fragment_1;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.basket_list,container,false);
        adapter = new Custom_Adapter();
        listView = (ListView) rootview.findViewById(R.id.listView);

        setData();

        listView.setAdapter(adapter);
        //onItemClickListener를 추가
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.replaceFragment(Recipe.newnstance());
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

        for (int i = 0; i < arrResld.length(); i++) {
            Custom_Weekly dto = new Custom_Weekly();
            dto.setResId(arrResld.getResourceId(i, 0));
            dto.setTitle(titles[i]);
            dto.setContent(contents[i]);

            adapter.addItem(dto);
        }
    }

}
