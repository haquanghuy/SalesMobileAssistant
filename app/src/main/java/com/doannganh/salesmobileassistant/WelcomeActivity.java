package com.doannganh.salesmobileassistant;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.doannganh.salesmobileassistant.Presenter.ConnectionConfigPresenter;
import com.doannganh.salesmobileassistant.Views.activity.MainActivity;
import com.doannganh.salesmobileassistant.Views.activity.SplashScreenActivity;
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
        ConnectionConfig c = new ConnectionConfig("https://portal.3ssoft.com.vn:2443/SalesMobileAPI/",
                "","","EPIC06");
        //Server.API_path = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/";
        configPresenter.saveOrderToDB(c);

        ChangeActi();
    }

    void ChangeActi(){

        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
