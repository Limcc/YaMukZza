package com.example.fragment_1;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    Button bt_frag1, bt_frag2, bt_frag3, bt_frag4, bt_frag5;

    PackageInstaller.SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        //kakao
        Session.getCurrentSession().addCallback((ISessionCallback) callback);

        //키해시
        getHashKey();

        //button
        bt_frag1 = (Button)findViewById(R.id.bt_frag1);
        bt_frag2 = (Button)findViewById(R.id.bt_frag2);
        bt_frag3 = (Button)findViewById(R.id.bt_frag3);
        bt_frag4 = (Button)findViewById(R.id.bt_frag4);
        bt_frag5 = (Button)findViewById(R.id.bt_frag5);

        final Weekly fragment1 = new Weekly();
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, fragment1);
        fragmentTransaction.commit();

        bt_frag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(Weekly.newnstance());
            }
        });

        bt_frag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(Board.newnstance());
            }
        });

        bt_frag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(Search.newnstance());
            }
        });

        bt_frag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(Mypage.newnstance());
            }
        });

        bt_frag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(Recommend.newnstance());
            }
        });


    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container,fragment).commit();
    }
    //키해시 구하기
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

}
