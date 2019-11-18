package com.doannganh.salesmobileassistant.Views.customView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.Interface.InterfaceReturnEventListerner;
import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListView;
import com.doannganh.salesmobileassistant.model.Custom_list_item;

import java.util.List;

public class CustomDialogWithListViewCustom extends Dialog {
    Activity activity;
    List<Custom_list_item> list;
    int id;

    private InterfaceReturnEventListerner onFooEventListener;

    public CustomDialogWithListViewCustom(Activity activity, List<Custom_list_item> list, int id){
        super(activity);
        this.list = list;
        this.activity = activity;
        this.id = id;

        try {
            onFooEventListener = (InterfaceReturnEventListerner)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " you must implement InterfaceReturnEventListerner");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_list_radio);

        ListView lv = (ListView) findViewById(R.id.lvDialogCustomList);

        CustomAdapterListView clad = new CustomAdapterListView(
                activity, R.layout.custom_list_item, list);
        lv.setAdapter(clad);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onFooEventListener.returnEvent(id, i);
                dismiss();
            }
        });
    }

}
