package com.example.fragment_1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadingActivity extends Activity {

    private SessionCallback callback;
    private static String id;
    private static String TAG = "phptest";

    static boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Button button2 = (Button) findViewById(R.id.btn_kakao_login);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback = new SessionCallback();
                Session session = Session.getCurrentSession();
                session.addCallback(callback);
                session.open(AuthType.KAKAO_LOGIN_ALL, LoadingActivity.this);
                session.checkAndImplicitOpen();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().requestMe(new MeResponseCallback() {
                // 세션 오픈 실패. 세션이 삭제된 경우,
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                    // TODO 이새기 로그아웃함
                }
                // 회원이 아닌 경우,
                @Override
                public void onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp");
                    // TODO 회원 아직 아님
                }
                // 사용자정보 요청에 성공한 경우,
                @Override
                public void onSuccess(UserProfile userProfile) {
                    // 로그인 이미 했으면 무시함
                    if (isLoggedIn) return;
                    isLoggedIn = true;

                    // TODO 사용자 아이디 여깄음.
                    id = String.valueOf(userProfile.getId());

                    Log.i("yamukzza", "User ID : " + id);

                    // Request login to server.
                    InsertData task = new InsertData();
                    task.execute("http://yamukzza.iptime.org/yamukzza/kakao.php", id);

//                  // TODO 사용자 정보 훔쳐보기
//                  String nickname = userProfile.getNickname();
//                  String email = userProfile.getEmail();
//                  String profileImagePath = userProfile.getProfileImagePath();
//                  String thumnailPath = userProfile.getThumbnailImagePath();
//                  String UUID = userProfile.getUUID();
//                  long id = userProfile.getId();


                }
                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
                    // TODO 로그인 실패했을 때
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    protected void redirectSignupActivity() {
        Log.i("yamukzza", "Succeed login");
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoadingActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response 2 - " + result);

            // TODO
            redirectSignupActivity(); // 로그인 성공한 경우

        }

        @Override
        protected String doInBackground(String... params) {
            id = (String)params[1];
            String serverURL = (String)params[0];
            String postParameters = "id=" + id;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code 1 - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
}