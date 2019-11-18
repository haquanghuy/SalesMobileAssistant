package com.doannganh.salesmobileassistant.Views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.ConnectionConfigPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.model.ConnectionConfig;

public class ConfigActivity extends AppCompatActivity {

    EditText edtPath, edtUs, edtPa, edtComp;
    LinearLayout onlyEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        AnhXa();
        LoadActionBar();
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.config_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void AnhXa() {
        edtPath = findViewById(R.id.edtConfigPath);
        edtUs = findViewById(R.id.edtConfigUser);
        edtPa = findViewById(R.id.edtConfigPass);
        edtComp = findViewById(R.id.edtConfigCompany);
        onlyEdit = findViewById(R.id.tabRowConfigButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuConfigEdit){
            edtPath.setEnabled(true);
            edtUs.setEnabled(true);
            edtPa.setEnabled(true);
            edtComp.setEnabled(true);

            onlyEdit.setVisibility(View.VISIBLE);
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    public void SaveConfig(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change");
        builder.setMessage(getString(R.string.config_mess));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(ConfigActivity.this, "Xu ly thay doi", Toast.LENGTH_SHORT).show();
                ChangeConfig();
                Intent i = new Intent(ConfigActivity.this, MainActivity.class);
                i.putExtra("isRestart", true);
                ConfigActivity.this.startActivity(i);
                finish();
                return;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(ConfigActivity.this, SettingActivity.class));
            }
        });
        builder.show();
    }

    private void ChangeConfig() {
        String path = edtPath.getText().toString();
        String us = edtUs.getText().toString();
        String pa = edtPa.getText().toString();
        String comp = edtComp.getText().toString();

        ConnectionConfig c = new ConnectionConfig(path,us, pa, comp);
        ConnectionConfigPresenter configPresenter = ConnectionConfigPresenter.Instance(getApplicationContext());
        if(configPresenter.saveOrderToDB(c) != 0)
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
    }
}
