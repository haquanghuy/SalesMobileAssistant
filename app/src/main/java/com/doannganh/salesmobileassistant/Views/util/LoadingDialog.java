package com.doannganh.salesmobileassistant.Views.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.activity.LoginActivity;

public class LoadingDialog {
    private static LoadingDialog instance;
    Activity activity;
    Dialog dialog;

    private LoadingDialog(){}

    public static LoadingDialog Instance(Activity activity)
    {
        if (instance == null){
            instance = new LoadingDialog();
        }
        instance.activity = activity;
        return LoadingDialog.instance;
    }

    public void ShowDialog(){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_load);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void Dismiss(){
        if (dialog != null)
            dialog.dismiss();
    }
}
