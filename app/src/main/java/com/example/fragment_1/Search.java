package com.example.fragment_1;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import static com.example.fragment_1.R.drawable.beer1;

public class Search extends Fragment {
    ListView listView = null;

    public static Search newnstance(){
        return new Search();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.search, container, false);
        // Inflate the layout for this fragment

        ListViewAdapter adapter;
        adapter = new ListViewAdapter();

        listView = (ListView) v.findViewById(R.id.listview1);
        listView.setAdapter(adapter);
        adapter.addItem(ContextCompat.getDrawable(getActivity(), beer1),"맥주","heaven.\r\n I do it for you.");
        EditText editTextFilter = (EditText)v.findViewById(R.id.editTextFilter);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged (Editable edit){
                String filterText = edit.toString();
//                if (filterText.length() > 0) {
//                    listView.setFilterText(filterText);
//                } else {
//                    listView.clearTextFilter();
//                }
                ((ListViewAdapter)listView.getAdapter()).getFilter().filter(filterText);
            }
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        return v;
    }
}
