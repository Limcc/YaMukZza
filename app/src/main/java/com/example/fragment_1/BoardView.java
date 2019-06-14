package com.example.fragment_1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoardView extends Fragment {
    public static String food;
    public static BoardView newnstance(){
        return new BoardView();
    }
    static BoardView newnstance(String foodName){
        food = foodName;
        return new BoardView();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.recipe_view,container,false);
        final TextView board_name = (TextView) rootview.findViewById(R.id.board_name);
        final TextView board_title = (TextView) rootview.findViewById(R.id.board_title);
        final TextView board_content = (TextView) rootview.findViewById(R.id.board_content);

        new Server("yamukzza/board/view.php"){
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
                    JSONArray list = data.getJSONArray("boardlist");
                    Log.i("food", food);
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        if (food.equals(item.getString("글 제목"))){
                            board_name.setText(item.getString("글 제목"));
                            board_title.setText(item.getString("재료"));
                            board_content.setText(item.getString("만드는법"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
        return rootview;
    }
}