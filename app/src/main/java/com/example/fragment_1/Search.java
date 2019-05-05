package com.example.fragment_1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import static com.example.fragment_1.R.drawable.beer1;

public class Search extends AppCompatActivity {
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        ListViewAdapter adapter;

        adapter = new ListViewAdapter();

        listView = (ListView) findViewById(R.id.listview1);
        listView.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(this, beer1),"맥주","heaven.\r\n I do it for you.");

        EditText editTextFilter = (EditText)findViewById(R.id.editTextFilter);
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
    }
}
