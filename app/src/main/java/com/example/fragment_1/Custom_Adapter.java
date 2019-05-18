package com.example.fragment_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class Custom_Adapter extends BaseAdapter{
    private ArrayList<Custom_Weekly> listCustom = new ArrayList<>();

    // ListView에 보여질 Item 수
    @Override
    public int getCount() {
        return listCustom.size();
    }

    // 하나의 Item(ImageView 1, TextView 2)
    @Override
    public Object getItem(int position) {
        return listCustom.get(position);
    }

    // Item의 id : Item을 구별하기 위한 것으로 position 사용
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실제로 Item이 보여지는 부분
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_item, null, false);

            holder = new CustomViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textTitle = (TextView) convertView.findViewById(R.id.text_title);
            holder.textContent = (TextView) convertView.findViewById(R.id.text_content);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        Custom_Weekly dto = listCustom.get(position);

        new DownloadImageTask(holder.imageView)
                .execute(dto.getResId());
        //holder.imageView.setImageResource(dto.getResId());
        holder.textTitle.setText(dto.getTitle());
        holder.textContent.setText(dto.getContent());

        return convertView;
    }

    class CustomViewHolder {
        ImageView imageView;
        TextView textTitle;
        TextView textContent;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(Custom_Weekly dto) {
        listCustom.add(dto);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
