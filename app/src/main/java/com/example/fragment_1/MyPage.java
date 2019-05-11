package com.example.fragment_1;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Mypage extends Fragment {

    public static Mypage newnstance(){
        return new Mypage();
    }

    // fragment_main.xml 파일과 인플레이션으로 연결해주는것을 메모리 객체화를 시켜주어야한다
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.mypage,container,false);

        Button button1 = (Button) rootview.findViewById(R.id.basket);
        button1.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                activity.replaceFragment(Basket.newnstance());
            }
        });

        Button button2 = (Button) rootview.findViewById(R.id.kakao_plus);
        button2.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                activity.replaceFragment(KakaoPlus.newnstance());
            }
        });
        //getActivity();      // 액티비티위에 올라가게 한다.
        return rootview;

    }


}