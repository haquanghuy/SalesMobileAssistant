package com.doannganh.salesmobileassistant.Views.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doannganh.salesmobileassistant.R;

import java.util.List;

public class CustomAdapterListWithRadioButton extends BaseAdapter {
    Activity activity;
    List<String> list;

    public CustomAdapterListWithRadioButton(@NonNull Activity activity, @NonNull List<String> objects) {
        this.activity = activity;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.custom_list_with_radiobutton, null);

        CheckedTextView title = view.findViewById(R.id.txtCustomListWithRadioBT);

        String t = list.get(position);
        title.setText(t);
        return view;
    }
}
