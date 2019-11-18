package com.doannganh.salesmobileassistant.Views.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doannganh.salesmobileassistant.model.Custom_grid_item;
import com.doannganh.salesmobileassistant.R;

import java.util.ArrayList;

public class CustomAdapterGridView extends ArrayAdapter {
    Activity activity;
    int layout;
    ArrayList<Custom_grid_item> list;

    public CustomAdapterGridView(Activity activity, int resource, ArrayList<Custom_grid_item> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.layout = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(layout, null);

        ImageView img = view.findViewById(R.id.imgGridItem);
        TextView txt = view.findViewById(R.id.txtGridItem);

        Custom_grid_item c = list.get(position);
        img.setImageResource(c.getPicture());
        txt.setText(c.getName());

        return view;
    }
}
