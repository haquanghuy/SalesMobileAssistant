package com.doannganh.salesmobileassistant.Views.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doannganh.salesmobileassistant.Presenter.ConnectionConfigPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.util.LanguageChange;
import com.doannganh.salesmobileassistant.WelcomeActivity;
import com.doannganh.salesmobileassistant.model.ConnectionConfig;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageChange languageChange;
        languageChange = new LanguageChange(SplashScreenActivity.this);
        languageChange.loadLocale();

        setContentView(R.layout.activity_splash_screen);

        ConnectionConfigPresenter configPresenter = ConnectionConfigPresenter.Instance(getApplicationContext());
        ConnectionConfig c = configPresenter.getConfigFromDB();
        if(c != null){
            Server.API_path = c.getAPI_path();
            Server.us = c.getUsername();
            Server.pa = c.getPassword();
            Server.company = c.getCompany();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds
                ConnectionConfigPresenter configPresenter = ConnectionConfigPresenter.Instance(getApplicationContext());
                ConnectionConfig c = configPresenter.getConfigFromDB();
                if(c == null){

                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);


    }

}
