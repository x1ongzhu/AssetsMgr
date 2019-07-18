package me.zeo_x.management.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;
import me.zeo_x.management.model.bean.UserBean;
import me.zeo_x.management.MyApplication;

/**
 * Created by xiong on 2016/1/2.
 */
public class LoginHelper {
    private static Gson gson;
    private static RequestQueue mQueue;
    private static LoginHelper ourInstance;
    private Context context;

    private static String TAG = "LoginHelper";

    public static LoginHelper getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new LoginHelper(context);
        return ourInstance;
    }

    private LoginHelper(Context context) {
        gson = new Gson();
        this.context = context;
        mQueue = Volley.newRequestQueue(context);
    }

    public void Login(final String username, String password, final LoginListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(10000);
        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);
        client.post(context, MyApplication.domain + "pu_android/login.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure("登录失败:" + response.getString("message"));
                    } else {
                        UserBean user = gson.fromJson(response.toString(), UserBean.class);
                        Cookie.user = user;
                        Cookie.username = username;
                        listener.onSuccess(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure("登录失败:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFailure("登录失败:" + throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFailure("登录失败:" + throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                listener.onFailure("登录失败:" + throwable.getMessage());
            }
        });
    }

    public interface LoginListener {
        void onSuccess(UserBean user);

        void onFailure(String error);
    }
}
