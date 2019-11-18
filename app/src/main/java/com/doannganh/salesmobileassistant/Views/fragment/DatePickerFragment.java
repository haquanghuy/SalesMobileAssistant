package com.doannganh.salesmobileassistant.Views.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;


import com.doannganh.salesmobileassistant.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    EditText editText;

    public DatePickerFragment() {
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DAY = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, YEAR, MONTH, DAY);
        // Add 3 days to Calendar
        calendar.add(Calendar.DATE, 30);
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Subtract 6 days from Calendar updated date
        calendar.add(Calendar.DATE, -6);

        // Set the Calendar new date as minimum date of date picker
        dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText edt = getActivity().findViewById(R.id.edtOrdersNewNeedByDate);
        edt.setText(year + "-" + (month+1) + "-" + dayOfMonth);
    }
}
