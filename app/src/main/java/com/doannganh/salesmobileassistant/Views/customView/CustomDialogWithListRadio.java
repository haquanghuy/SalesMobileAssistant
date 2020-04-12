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
import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListWithRadioButton;

import java.util.List;


public class CustomDialogWithListRadio extends Dialog {
    Activity activity;
    List<String> list;
    int id;
    int itemChecked = 0;
    
    private InterfaceReturnEventListerner onFooEventListener;

    public CustomDialogWithListRadio(Activity activity, List<String> list, int id){
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

        CustomAdapterListWithRadioButton clad = new CustomAdapterListWithRadioButton(
               activity, list);

        //CustomAdapterListViewWithTextView clad = new CustomAdapterListViewWithTextView(
        //        activity, R.layout.custom_list_item_with_textview, list);
        lv.setAdapter(clad);
        // no work
        lv.setChoiceMode(lv.CHOICE_MODE_SINGLE);
        lv.setItemChecked(itemChecked,true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onFooEventListener.returnEvent(id, i);
                dismiss();
            }
        });
    }


    public void setItemChecked(int position){
        itemChecked = position;
    }
}
