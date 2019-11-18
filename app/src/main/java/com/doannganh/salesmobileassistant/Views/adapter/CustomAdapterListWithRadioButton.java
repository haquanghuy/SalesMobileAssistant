package com.doannganh.salesmobileassistant.Views.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doannganh.salesmobileassistant.R;

import java.util.List;

public class CustomAdapterListWithRadioButton extends ArrayAdapter {
    Activity activity;
    int layout;
    List<String> list;

    public CustomAdapterListWithRadioButton(@NonNull Activity activity, int resource, @NonNull List<String> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.layout = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(layout, null);

        TextView title = view.findViewById(R.id.txtCustomListWithRadioBT);

        String t = list.get(position);
        title.setText(t);
        return view;
    }
}
