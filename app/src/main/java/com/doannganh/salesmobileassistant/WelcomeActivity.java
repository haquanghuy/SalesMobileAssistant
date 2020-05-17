package com.doannganh.salesmobileassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.Presenter.ConnectionConfigPresenter;
import com.doannganh.salesmobileassistant.Views.activity.MainActivity;
import com.doannganh.salesmobileassistant.model.ConnectionConfig;

public class WelcomeActivity extends AppCompatActivity {

    EditText edtAPI, edtUs, edtPa, edtCompa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);

        AnhXa();
        AddFragmentViewWelcome();
    }

    private void AddFragmentViewWelcome() {
    }

    private void AnhXa() {
        edtAPI = findViewById(R.id.edtWelcomAPI);
        edtUs = findViewById(R.id.edtWelcomUs);
        edtPa = findViewById(R.id.edtWelcomPa);
        edtCompa = findViewById(R.id.edtWelcomCompany);
    }

    public void ClickOK(View view) {
        ConnectionConfigPresenter configPresenter = ConnectionConfigPresenter.Instance(getApplicationContext());
        ConnectionConfig c = new ConnectionConfig(edtAPI.getText().toString(), edtUs.getText().toString(),
                edtPa.getText().toString(),edtCompa.getText().toString());
        //Server.API_path = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/";
        configPresenter.saveOrderToDB(c);

        ChangeActi();
    }

    public void ClickDefault(View view) {
        ConnectionConfigPresenter configPresenter = ConnectionConfigPresenter.Instance(getApplicationContext());
        //"https://portal.3ssoft.com.vn:2443/SalesMobileAPI/"
        ConnectionConfig c = new ConnectionConfig("http://www.sma-service.somee.com/",
                "","","EPIC06");
        configPresenter.saveOrderToDB(c);

        Server.API_path = c.getAPI_path();
        Server.us = c.getUsername();
        Server.pa = c.getPassword();
        Server.company = c.getCompany();

        ChangeActi();
    }

    void ChangeActi(){
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
