package com.lsh.phonenum.mvp.impl;

import android.content.Context;

/**
 * Created by LSH on 2017/8/2.
 */

public class BasePresenter {

    Context mContext;

    public void attach(Context context) {
        mContext = context;
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onDestroy() {
        mContext = null;
    }
}
