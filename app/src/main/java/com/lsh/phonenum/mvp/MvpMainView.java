package com.lsh.phonenum.mvp;


/**
 * Created by LSH on 2017/8/2.
 */

public interface MvpMainView extends MvpLoadingView {

    void showToast(String msg);

    void updateView();

}
