package com.doannganh.salesmobileassistant.Views.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.util.LanguageChange;

import java.util.ArrayList;
import java.util.List;

public class LanguageActivity extends AppCompatActivity {

    ListView lvLang;
    List<String> listFunc;
    ArrayAdapter arrayAdapter;

    LanguageChange languageChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        LoadListFunc();

        EventClickList();

    }

    private void EventClickList() {
        lvLang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setLanguage(LanguageChange.ENGLISH);
                        break;
                    case 1:
                        setLanguage(LanguageChange.VIETNAMESE);
                        break;
                }
            }
        });
    }

    private void LoadListFunc() {
        languageChange = new LanguageChange(LanguageActivity.this);

        // load language present
        String langNow = languageChange.getLanguagePresent();
        langNow = langNow.equals(LanguageChange.ENGLISH)
                ?getString(R.string.setting_lang_en)
                :getString(R.string.setting_lang_vi);

        lvLang = findViewById(R.id.lvLanguage);
        listFunc = new ArrayList<>();
        listFunc.add(getString(R.string.setting_lang_en));
        listFunc.add(getString(R.string.setting_lang_vi));

        arrayAdapter = new ArrayAdapter(LanguageActivity.this, android.R.layout.simple_list_item_single_choice, listFunc);
        lvLang.setAdapter(arrayAdapter);
        lvLang.setChoiceMode(lvLang.CHOICE_MODE_SINGLE);
        lvLang.setItemChecked(listFunc.indexOf(langNow),true);
    }

    public void setLanguage(String language){
        languageChange.saveLocale(language);

        Intent intent = new Intent(LanguageActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
