package com.example.fragment_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KakaoPlus2 extends Fragment {

    public static KakaoPlus2 newnstance(){
        return new KakaoPlus2();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String url ="http://pf.kakao.com/_iHrUj";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

        return inflater.inflate(R.layout.mypage2, container, false);
    }

}
