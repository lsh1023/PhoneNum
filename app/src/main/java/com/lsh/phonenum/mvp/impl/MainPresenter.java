package com.lsh.phonenum.mvp.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lsh.phonenum.MainActivity;
import com.lsh.phonenum.http.HttpUtils;
import com.lsh.phonenum.model.Phone;
import com.lsh.phonenum.mvp.MvpMainView;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by LSH on 2017/8/2.
 */

public class MainPresenter extends BasePresenter {

    String httpUrl = "http://apis.baidu.com/netpopo/shouji/query";

    MvpMainView mvpMainView;
    Phone mPhone;

    public Phone getPhoneInfo() {
        return mPhone;
    }

    public MainPresenter(MainActivity mainView) {
        mvpMainView = mainView;
    }

    public void seachPhoneInfo(String phone) {
        if (phone.length() != 11) {
            mvpMainView.showToast("请输入正确的手机号");
            return;
        }

        mvpMainView.showLoading();
        //写上http请求的的处理逻辑
        sendPhone(phone);
    }

    private void sendPhone(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("shouji", phone);
        HttpUtils httpUtil = new HttpUtils(new HttpUtils.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                mPhone=parseModelWithGson(json);
                mvpMainView.hidenLoading();
                mvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hidenLoading();
            }
        });
        httpUtil.sendGetHttp(httpUrl, map);

    }

    private Phone parseModelWithGson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Phone.class);
    }

    private Phone parseModelWidtFasrJson(String json)
    {
        Phone phone= JSONObject.parseObject(json,Phone.class);
        return phone;
    }


}
