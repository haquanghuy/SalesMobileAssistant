package com.doannganh.salesmobileassistant.Views.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.AccountPresenter;
import com.doannganh.salesmobileassistant.Presenter.ConnectionConfigPresenter;
import com.doannganh.salesmobileassistant.Presenter.EmployeePresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.util.CheckConnection;
import com.doannganh.salesmobileassistant.WelcomeActivity;
import com.doannganh.salesmobileassistant.model.Account;
import com.doannganh.salesmobileassistant.model.ConnectionConfig;
import com.doannganh.salesmobileassistant.model.Employee;

public class LoginActivity extends AppCompatActivity {

    public static Account account;
            EditText edtUser, edtPass;

    CheckBox checkBox;
    Button btnLogin;

    AccountPresenter accountPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoadActionBar();
        CheckConfig();
        accountPresenter = AccountPresenter.Instance(getApplicationContext());

        AnhXa();
        LoadPreferences();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked())
                    accountPresenter.saveAccountPreferences(getApplicationContext(),
                            edtUser.getText()+"", edtPass.getText()+"");
                else if(checkBox.isChecked()==false){
                    accountPresenter.saveAccountPreferences(getApplicationContext(),
                            "", "");
                }
                Login();
            }
        });
    }

    private void LoadPreferences() {
        Intent i = getIntent();
        boolean b = i.getBooleanExtra("isLogout", false);

        String a[] = accountPresenter.loadAccountPreferences(getApplicationContext());
        edtUser.setText(a[0]);
        edtPass.setText(a[1]);
        if(a[0].equals("") || a[1].equals("") || b) return;
        Login();
    }

    private void LoadActionBar() {
        getSupportActionBar().hide();
    }

    private void CheckConfig() {

    }

    private void AnhXa() {
        edtUser = findViewById(R.id.edtUSername);
        edtPass = findViewById(R.id.edtUPassword);
        btnLogin = findViewById(R.id.btnLogin);
        checkBox = findViewById(R.id.chbLoginRemember);
    }

    private void Login(){
        account = new Account(edtUser.getText()+"", edtPass.getText()+"");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
                    AsyncTaskGetJSONArray asyncTaskGetJSONArray = new AsyncTaskGetJSONArray();
                    asyncTaskGetJSONArray.execute(account);
                }else CheckConnection.ShowToastError(getApplicationContext());
            }
        });
    }

    public void ResetAPI(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Reset API default");
        builder.setMessage(getString(R.string.login_reset_api_message));
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ConnectionConfigPresenter configPresenter = ConnectionConfigPresenter.Instance(getApplicationContext());
                ConnectionConfig c = new ConnectionConfig("https://portal.3ssoft.com.vn:2443/SalesMobileAPI/",
                        "","","EPIC06");
                //Server.API_path = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/";
                configPresenter.saveOrderToDB(c);

                Toast.makeText(LoginActivity.this, "OK", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNeutralButton("API other", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public class AsyncTaskGetJSONArray extends AsyncTask<Account, Void, Account> {

        Employee employee;
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_checkaccount));
            progressDialog.show();
        }

        @Override
        protected Account doInBackground(Account... accounts) {
            Account check;
            check = accountPresenter.Login(accounts[0]);

            if(check != null){
                EmployeePresenter employeePresenter = EmployeePresenter.Instance(getApplicationContext());
                employee = employeePresenter.getEmployee(check.getEmplID());
            }
            return check;
        }

        @Override
        protected void onPostExecute(Account account) {
            if (account != null)
            {
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("account", account);
                bundle.putString("EmployeeName", employee.getName());
                intent.putExtra("bundle", bundle);
                setResult(Activity.RESULT_OK, intent);
                progressDialog.dismiss();
                finish();
                //startActivity(intent);
            }
            else {
                progressDialog.dismiss();
                edtPass.setText("");
                if (checkBox.isChecked())
                    accountPresenter.saveAccountPreferences(getApplicationContext(),"", "");
                Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CheckBox checkBox = findViewById(R.id.chbLoginRemember);
        if(checkBox.isChecked())
            accountPresenter.saveAccountPreferences(getApplicationContext(),
                    edtUser.getText()+"", edtPass.getText()+"");

    }
}
