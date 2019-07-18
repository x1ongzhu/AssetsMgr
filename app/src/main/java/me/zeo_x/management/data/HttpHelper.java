package me.zeo_x.management.data;

import android.content.Context;
import android.text.TextUtils;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import me.zeo_x.management.MyApplication;

public class HttpHelper {
    private Context context;
    private static Gson gson;
    private static RequestQueue mQueue;
    private static AsyncHttpClient client;
    private static HttpHelper ourInstance;
    private static String TAG = "HttpHelper";

    public static HttpHelper getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new HttpHelper(context);
        return ourInstance;
    }

    private HttpHelper(Context context) {
        gson = new Gson();
        mQueue = Volley.newRequestQueue(context);
        client = new AsyncHttpClient();
        this.context = context.getApplicationContext();
    }

    public void check_cz(String equid, String project, final SimpleHttpListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equid);
        params.add("check_project", project);
        params.add("check_operator", Cookie.username);
        params.add("check_date", time);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/check_cz.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void get_sb_equ(String username, final HttpListener<List<Map<String, String>>> listener) {
        RequestParams params = new RequestParams();
        params.add("username", username);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/check_sb.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        JSONArray a = response.getJSONArray("equ");
                        List<Map<String, String>> list = new ArrayList<>();
                        for (int i = 0; i < a.length(); i++) {
                            Map<String, String> m = new HashMap<>();
                            m.put("equ_id", a.getJSONObject(i).getString("equ_id"));
                            list.add(m);
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

        });
    }

    public void get_check_content(String equ_id, final HttpListener<List<Map<String, String>>> listener) {
        RequestParams params = new RequestParams();
        params.add("equ_id", equ_id);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/check_sb_view.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        JSONArray a = response.getJSONArray("data");
                        List<Map<String, String>> list = new ArrayList<>();
                        for (int i = 0; i < a.length(); i++) {
                            Map<String, String> m = new HashMap<>();
                            m.put("content_id", a.getJSONObject(i).getString("content_id"));
                            m.put("content_name", a.getJSONObject(i).getString("content_name"));
                            m.put("type", a.getJSONObject(i).getString("type"));
                            list.add(m);
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

        });
    }

    public void check_sb(String equid, String path, String check_desc, String sure, String content_id, final HttpUploadListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equid);
        if (!TextUtils.isEmpty(path)) {
            File myFile = new File(path);
            try {
                params.put("picture", myFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                listener.onFailure(e.getMessage());
                return;
            }
        }
        try {
            params.add("check_desc", URLEncoder.encode(check_desc, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            listener.onFailure(e.getMessage());
            return;
        }
        params.add("check_man", Cookie.username);
        params.add("check_date", time);
        params.add("sure", sure);
        params.add("content_id", content_id);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/check_sb.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                listener.onProgress((int) (bytesWritten * 100 / totalSize));
            }
        });
    }

    public void reort_repair(String equ_id, String picture, String baoxiu_desc, final HttpUploadListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equ_id);
        if (!TextUtils.isEmpty(picture)) {
            File myFile = new File(picture);
            try {
                params.put("picture", myFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                listener.onFailure(e.getMessage());
                return;
            }
        }
        try {
            params.add("baoxiu_desc", URLEncoder.encode(baoxiu_desc, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            listener.onFailure(e.getMessage());
            return;
        }
        params.add("baoxiu_man", Cookie.username);
        params.add("baoxiu_time", time);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/baoxiu.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                listener.onProgress((int) (bytesWritten * 100 / totalSize));
            }
        });
    }

    public void reort_repair_done(String equ_id, String baoxiu_id, String picture, String weixiu_desc, final HttpUploadListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equ_id);
        params.add("baoxiu_id", baoxiu_id);
        if (!TextUtils.isEmpty(picture)) {
            File myFile = new File(picture);
            try {
                params.put("picture", myFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                listener.onFailure(e.getMessage());
                return;
            }
        }
        try {
            params.add("weixiu_desc", URLEncoder.encode(weixiu_desc, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            listener.onFailure(e.getMessage());
            return;
        }
        params.add("weixiu_man", Cookie.username);
        params.add("weixiu_time", time);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/xiuwan.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                listener.onProgress((int) (bytesWritten * 100 / totalSize));
            }
        });
    }

    public void get_equ_list(final HttpListener<List<Map<String, String>>> listener) {
        RequestParams params = new RequestParams();
        params.add("equ_id", "");
        params.add("equ_name", "");
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_equ.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        JSONArray a = response.getJSONArray("data");
                        List<Map<String, String>> list = new ArrayList<>();
                        for (int i = 0; i < a.length(); i++) {
                            JSONObject o = a.getJSONObject(i);
                            Map<String, String> m = new HashMap<>();
                            m.put("equ_id", o.getString("equ_id"));
                            m.put("equ_name", o.getString("equ_name"));
                            list.add(m);
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void get_equ_state(final HttpListener<Map<String, List<String>>> listener) {
        RequestParams params = new RequestParams();
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_equ.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        JSONObject guzhang = response.getJSONObject("guzhang");
                        JSONObject unfin_db = response.getJSONObject("unfin_db");
                        JSONObject unfin_xx = response.getJSONObject("unfin_xx");
                        JSONObject unfin_check = response.getJSONObject("unfin_check");
                        Map<String, List<String>> state = new HashMap<>();

                        if (guzhang.getInt("num") > 0) {
                            JSONArray guzhangArray = guzhang.getJSONArray("equ_id");
                            for (int i = 0; i < guzhangArray.length(); i++) {
                                if (state.containsKey(guzhangArray.getJSONObject(i).getString("id"))) {
                                    state.get(guzhangArray.getJSONObject(i).getString("id")).add("故障");
                                } else {
                                    List<String> l = new ArrayList<>();
                                    l.add("故障");
                                    state.put(guzhangArray.getJSONObject(i).getString("id"), l);
                                }
                            }
                        }
                        if (unfin_db.getInt("num") > 0) {
                            JSONArray unfin_dbArray = unfin_db.getJSONArray("equ_id");
                            for (int i = 0; i < unfin_dbArray.length(); i++) {
                                if (state.containsKey(unfin_dbArray.getJSONObject(i).getString("id"))) {
                                    state.get(unfin_dbArray.getJSONObject(i).getString("id")).add("未完成定保");
                                } else {
                                    List<String> l = new ArrayList<>();
                                    l.add("未完成定保");
                                    state.put(unfin_dbArray.getJSONObject(i).getString("id"), l);
                                }
                            }
                        }
                        if (unfin_xx.getInt("num") > 0) {
                            JSONArray unfin_xxArray = unfin_xx.getJSONArray("equ_id");
                            for (int i = 0; i < unfin_xxArray.length(); i++) {
                                if (state.containsKey(unfin_xxArray.getJSONObject(i).getString("id"))) {
                                    state.get(unfin_xxArray.getJSONObject(i).getString("id")).add("未完成小修");
                                } else {
                                    List<String> l = new ArrayList<>();
                                    l.add("未完成小修");
                                    state.put(unfin_xxArray.getJSONObject(i).getString("id"), l);
                                }
                            }
                        }
                        if (unfin_xx.getInt("num") > 0) {
                            JSONArray unfin_checkArray = unfin_check.getJSONArray("equ_id");
                            for (int i = 0; i < unfin_checkArray.length(); i++) {
                                if (state.containsKey(unfin_checkArray.getJSONObject(i).getString("id"))) {
                                    state.get(unfin_checkArray.getJSONObject(i).getString("id")).add("未完成点检");
                                } else {
                                    List<String> l = new ArrayList<>();
                                    l.add("未完成点检");
                                    state.put(unfin_checkArray.getJSONObject(i).getString("id"), l);
                                }
                            }
                        }
                        listener.onSuccess(state);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }


    public void get_equ_detail(String equ_id, final HttpListener<List<String>> listener) {
        RequestParams params = new RequestParams();
        params.add("equ_id", equ_id);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_equ_detail.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        List<String> list = new ArrayList<>();
                        Iterator iterator = response.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            if (!TextUtils.equals(key, "success"))
                                list.add(response.getString(key));
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void get_repair_content(String equ_id, final HttpListener<List<Map<String, String>>> listener) {
        RequestParams params = new RequestParams();
        params.add("equ_id", equ_id);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/baoxiu_view.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        List<Map<String, String>> list = new ArrayList<>();
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, String> m = new HashMap<>();
                            m.put("baoxiu_id", array.getJSONObject(i).getString("baoxiu_id"));
                            m.put("baoxiu_desc", array.getJSONObject(i).getString("baoxiu_desc"));
                            list.add(m);
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void get_repair_done_list(final HttpListener<List<String>> listener) {
        RequestParams params = new RequestParams();
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_shenhe.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure("暂无需要审核的设备");
                    } else {
                        List<String> list = new ArrayList<>();
                        JSONArray array = response.getJSONArray("equ");
                        for (int i = 0; i < array.length(); i++) {
                            list.add(array.getJSONObject(i).getString("equ_id"));
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void repair_check(String equ_id, final SimpleHttpListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equ_id);
        params.add("shenhe_man", Cookie.username);
        params.add("shenhe_time", time);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/shenhe.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void get_db_xx_content(String equ_id, final String type, final HttpListener<List<Map<String, String>>> listener) {
        RequestParams params = new RequestParams();
        params.add("equ_id", equ_id);
        params.add("type", type);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_content.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        List<Map<String, String>> list = new ArrayList<>();
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            if (TextUtils.equals(type, "1")) {
                                Map<String, String> m = new HashMap<>();
                                m.put("db_id", array.getJSONObject(i).getString("db_id"));
                                m.put("db_content", array.getJSONObject(i).getString("db_content"));
                                list.add(m);
                            } else {
                                Map<String, String> m = new HashMap<>();
                                m.put("xx_id", array.getJSONObject(i).getString("xx_id"));
                                m.put("xx_content", array.getJSONObject(i).getString("xx_content"));
                                list.add(m);
                            }
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void upload_db(String equ_id, String picture, String content, String upload_desc, final SimpleHttpListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equ_id);
        params.add("content", content);
        if (!TextUtils.isEmpty(picture)) {
            File myFile = new File(picture);
            try {
                params.put("picture", myFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                listener.onFailure(e.getMessage());
                return;
            }
        }
        params.add("upload_man", Cookie.username);
        params.add("upload_date", time);
        params.add("upload_desc", upload_desc);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/db_record.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void upload_xx(String equ_id, String picture, String content, String upload_desc, final SimpleHttpListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equ_id);
        if (!TextUtils.isEmpty(picture)) {
            File myFile = new File(picture);
            try {
                params.put("picture", myFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                listener.onFailure(e.getMessage());
                return;
            }
        }
        params.add("content", content);
        params.add("upload_man", Cookie.username);
        params.add("upload_date", time);
        params.add("upload_desc", upload_desc);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/xx_record.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void db_xx_check_list(final String type, final HttpListener<List<Map<String, String>>> listener) {
        RequestParams params = new RequestParams();
        params.add("username", Cookie.username);
        params.add("type", type);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_record.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure("暂无需要审核的设备");
                    } else {
                        List<Map<String, String>> list = new ArrayList<>();
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, String> m = new HashMap<>();
                            m.put("equ_id", array.getJSONObject(i).getString("equ_id"));
                            m.put("equ_name", array.getJSONObject(i).getString("equ_name"));
                            list.add(m);

                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void db_xx_check(String equ_id, String type, final SimpleHttpListener listener) {
        RequestParams params = new RequestParams();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        params.add("equ_id", equ_id);
        params.add("type", type);
        params.add("shenhe_man", Cookie.username);
        params.add("shenhe_time", time);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/shenhe_record.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void query_contract(String hetong_id, String zichan_name, String hetong_start, String hetong_end, final HttpListener<List<Map<String, String>>> listener) {
        RequestParams params = new RequestParams();
        params.add("hetong_id", hetong_id);
        params.add("zichan_name", zichan_name);
        params.add("hetong_start", hetong_start);
        params.add("hetong_end", hetong_end);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_hetong.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        List<Map<String, String>> list = new ArrayList<>();
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, String> map = new HashMap<>();
                            JSONObject object = array.getJSONObject(i);
                            map.put("hetong_id", object.getString("hetong_id"));
                            map.put("zichan_name", object.getString("zichan_name"));
                            map.put("hetong_date", object.getString("hetong_date"));
                            list.add(map);
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void get_contract_detail(String hetong_id, final HttpListener<List<String>> listener) {
        RequestParams params = new RequestParams();
        params.add("hetong_id", hetong_id);
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/view_hetong_detail.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        List<String> list = new ArrayList<>();
                        Iterator iterator = response.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            if (!TextUtils.equals(key, "success"))
                                list.add(response.getString(key));
                        }
                        listener.onSuccess(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public void upload_contract_picture(String hetong_id, String hetong_num, String picture, final SimpleHttpListener listener) {
        RequestParams params = new RequestParams();
        params.add("hetong_id", hetong_id);
        params.add("hetong_num", hetong_num);
        if (!TextUtils.isEmpty(picture)) {
            File myFile = new File(picture);
            try {
                params.put("picture", myFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                listener.onFailure(e.getMessage());
                return;
            }
        }
        Log.d(TAG, params.toString());
        client.post(context, MyApplication.domain + "pu_android/hetong_upload.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("success") == 0) {
                        listener.onFailure(response.getString("message"));
                    } else {
                        listener.onSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                listener.onFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                listener.onFailure(throwable.getMessage());
            }
        });
    }

    public interface HttpListener<T> {
        void onSuccess(T data);

        void onFailure(String error);
    }

    public interface SimpleHttpListener {
        void onSuccess();

        void onFailure(String error);
    }

    public interface HttpUploadListener {
        void onSuccess();

        void onProgress(int progress);

        void onFailure(String error);
    }

}
