package com.lsh.phonenum.http;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LSH on 2017/8/2.
 */

public class HttpUtils {

    String mUrl;
    Map<String, String> mParam;

    HttpResponse mHttpResponse;

    private static final String APIKEY = "";
    private OkHttpClient client = new OkHttpClient();
    Handler mHandler = new Handler(Looper.getMainLooper());

    public interface HttpResponse {
        void onSuccess(Object object);

        void onFail(String error);
    }

    public HttpUtils(HttpResponse response) {
        mHttpResponse = response;
    }


    public void sendPostHttp(String url, Map<String, String> param) {
        sendHttp(url, param, true);
    }

    public void sendGetHttp(String url, Map<String, String> param) {
        sendHttp(url, param, false);
    }

    public void sendHttp(String url, Map<String, String> param, boolean isPost) {
        mUrl = url;
        mParam = param;
        //编写Http请求
        run(isPost);

    }


    private void run(boolean isPost) {
        //request请求创建
        final Request request = createRequest(isPost);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpResponse != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpResponse.onFail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response == null) {
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.isSuccessful()) {
                            mHttpResponse.onFail("请求失败"+response);
                        }else {
                            try {
                                mHttpResponse.onSuccess(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                mHttpResponse.onFail("结果转化失败");
                            }
                        }
                    }
                });
            }
        });
    }

    private Request createRequest(boolean isPost) {
        Request request;
        if (isPost) {
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            Iterator<Map.Entry<String, String>> iterator = mParam.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
            request = new Request.Builder().url(mUrl)
                    .addHeader("apikey",APIKEY)
                    .post(requestBodyBuilder.build()).build();
        } else {
            String urlStr = mUrl + "?" + MapParamToString(mParam);
            request = new Request.Builder().url(urlStr)
                    .addHeader("apikey", APIKEY)
                    .build();
        }
        return request;
    }

    private String MapParamToString(Map<String, String> param) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = param.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        String str = stringBuilder.toString().substring(0, stringBuilder.length());
        return str;
    }


}
