package com.doannganh.salesmobileassistant.Views.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListViewWithTextView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    CustomAdapterListViewWithTextView customAdapterListViewWithTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LoadActionBar();

        listView = findViewById(R.id.lvSetting);
        list = new ArrayList<>();
        list.add(getString(R.string.setting_confign));
        list.add(getString(R.string.setting_lang));
        list.add(getString(R.string.setting_theme));
        list.add(getString(R.string.setting_update));
        list.add(getString(R.string.setting_about));
        customAdapterListViewWithTextView = new CustomAdapterListViewWithTextView(this,
                R.layout.custom_list_item_with_textview, list);
        listView.setAdapter(customAdapterListViewWithTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(SettingActivity.this, ConfigActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(SettingActivity.this, LanguageActivity.class));
                        break;
                    case 2:
                        Toast.makeText(SettingActivity.this, "Developing", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(SettingActivity.this, R.string.setting_update_no, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        builder.setMessage(getString(R.string.setting_about_content));
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        break;
                }

            }
        });
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.setting_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
