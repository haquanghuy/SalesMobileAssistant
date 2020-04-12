package com.doannganh.salesmobileassistant.Views.customView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.util.MyDate;
import com.doannganh.salesmobileassistant.model.MyJob;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class CustomDialogWithRecyclerView extends Dialog {
    Activity activity;
    List<MyJob> list;
    ReturnValueListerner returnValueListerner;

    public interface ReturnValueListerner{
        void OnTouchNegativeButton ();
        void OnTouchPositiveButton ();
    }
    public CustomDialogWithRecyclerView(Activity activity, List<MyJob> list, ReturnValueListerner returnValueListerner) {
        super(activity);
        this.list = list;
        this.activity = activity;
        this.returnValueListerner = returnValueListerner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_recyclerview);

        EditText id = findViewById(R.id.edtMyJobCustID);
        EditText name = findViewById(R.id.edtMyJobCustName);
        EditText address = findViewById(R.id.edtMyJobAddress);
        EditText date = findViewById(R.id.edtMyJobCustDate);
        EditText note = findViewById(R.id.edtMyJobNote);

        TextView cancel = findViewById(R.id.txtCustomDialogNegativeButton);
        TextView delete = findViewById(R.id.txtCustomDialogPositiveButton);

        MyJob m = list.get(0);
        // set
        id.setText(m.getRoutePlan().getCustID() + "");

        if(m.getCustName().length() > 13)
            name.setTextSize(12);
        name.setText(m.getCustName());

        if(m.getAddress().length() > 13)
            address.setTextSize(12);
        address.setText(m.getAddress());
        DateFormat sp = MyDate.df;
        Date d = null;
        try {
            d = sp.parse(m.getRoutePlan().getDatePlan());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setText(sp.format(d));

        if(m.getRoutePlan().getNote().length() > 13)
            note.setTextSize(12);
        note.setText(m.getRoutePlan().getNote());

        // event
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnValueListerner.OnTouchNegativeButton();
                dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnValueListerner.OnTouchPositiveButton();
                dismiss();
            }
        });
    }

}
