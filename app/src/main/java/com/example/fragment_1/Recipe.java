package com.example.fragment_1;

import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Recipe extends Fragment {
    public static String food;
    public static Recipe newnstance(){
        return new Recipe();
    }
    static Recipe newnstance(String foodName){
        food = foodName;
        return new Recipe();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.recipe,container,false);
        // Inflate the layout for this fragment
        final TextView name = (TextView) rootview.findViewById(R.id.name);
        final ImageView image = (ImageView) rootview.findViewById(R.id.image);
        final TextView recipe_title = (TextView) rootview.findViewById(R.id.recipe_title);
        final TextView recipe_content = (TextView) rootview.findViewById(R.id.recipe_content);

        new Server("yamukzza/search_recipe/recipeshow.php"){
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
                    JSONArray list = data.getJSONArray("foodnamelist");
                    Log.i("food", food);
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        if (food.equals(item.getString("요리이름"))){
                            name.setText(item.getString("요리이름"));
                            new DownloadImageTask(image).execute(item.getString("이미지"));
                            recipe_title.setText(item.getString("재료"));
                            recipe_content.setText(item.getString("레시피"));

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();

        //onItemClickListener를 추가

        return rootview;
    }

}