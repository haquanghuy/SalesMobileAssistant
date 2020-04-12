package com.doannganh.salesmobileassistant.Views.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.doannganh.salesmobileassistant.model.Custom_list_item;
import com.doannganh.salesmobileassistant.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapterListView extends BaseAdapter {
    Activity activity;
    int layout;
    List<Custom_list_item> list;
    List<Custom_list_item> listTemp;

    public CustomAdapterListView(@NonNull Activity activity, @NonNull List<Custom_list_item> objects) {
        this.activity = activity;
        this.layout = R.layout.custom_list_item;
        this.list = objects;
        this.listTemp = new ArrayList<>();
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
        View view = activity.getLayoutInflater().inflate(layout, null);

        TextView title = view.findViewById(R.id.txtCustomListItemTitle);
        TextView subtitle = view.findViewById(R.id.txtCustomListItemSub);
        TextView text = view.findViewById(R.id.txtCustomListItemText);

        Custom_list_item c = list.get(position);
        title.setText(c.getTitle());
        subtitle.setText(c.getSubTitle());
        text.setText(c.getText());

        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(listTemp);
        } else {
            for (Custom_list_item wp : listTemp) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void changeListSourceOnce(List<Custom_list_item> li) {
        list.clear();
        if (li == null || li.size() < 1)
            return;
        list.addAll(li);

        notifyDataSetChanged();
    }

    public void resetListSource() {
        list.clear();
        list.addAll(listTemp);

        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setMyListTemp(){
        this.listTemp = new ArrayList<>(list);
    }
}
