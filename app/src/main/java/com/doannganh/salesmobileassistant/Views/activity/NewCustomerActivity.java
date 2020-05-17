package com.doannganh.salesmobileassistant.Views.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.CustomerPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.util.PermissionUtil;

import java.util.HashMap;

public class NewCustomerActivity extends AppCompatActivity {

    EditText edtCustID, edtCustName, edtAddress1, edtAddress2, edtAddress3,
            edtCity, edtCountry, edtPhoneNum;
    ImageView imgCMND;

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        LoadActionBar();

        AnhXa();

        GetBundle();

        SetView();
    }

    private void SetView() {
        // set img

        // set info
        edtCustID.setText(String.valueOf(customer.getCustID()));
        edtCustName.setText(customer.getCustName());
        edtAddress1.setText(customer.getAddress1());
        edtAddress2.setText(customer.getAddress2());
        edtAddress3.setText(customer.getAddress3());
        edtCity.setText(customer.getCity());
        edtCountry.setText(customer.getCountry());
        edtPhoneNum.setText(customer.getPhoneNum());
    }

    private void GetBundle() {
        Bundle bundle = getIntent().getExtras();
        try {
            byte[] bytes = bundle.getByteArray(MyCameraActivity.ACTION.RETURN_VALUE_IMAGE_DETECT.toString());
            if(bytes != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                imgCMND.setImageBitmap(b);
            }
            customer = (Customer) bundle.getSerializable("customer-info");
            if(customer == null){
                customer = new Customer("EPIC06", "CHUCK", 113, "Ha Quang Huy"
                        , "371 Nguyen Kiem", "P3 Quan Go Vap", "TpHCM"
                        , "TpHCM", "Vietnam", "19001560", 10);
            }

        } catch (Exception e){
            // for test
            if(customer == null){
                customer = new Customer("EPIC06", "CHUCK", 113, "Ha Quang Huy"
                        , "371 Nguyen Kiem", "P3 Quan Go Vap", "TpHCM"
                        , "TpHCM", "Vietnam", "19001560", 10);
            }
        }
    }

    private void AnhXa() {
        edtCustID = findViewById(R.id.edtCustID);
        edtCustName = findViewById(R.id.edtCustName);
        edtAddress1 = findViewById(R.id.edtCustAddress1);
        edtAddress2 = findViewById(R.id.edtCustAddress2);
        edtAddress3 = findViewById(R.id.edtCustAddress3);
        edtCity = findViewById(R.id.edtCustCity);
        edtCountry = findViewById(R.id.edtCustCountry);
        edtPhoneNum = findViewById(R.id.edtCustPhoneNum);
        imgCMND = findViewById(R.id.imgCustomerCMND);
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.customer_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_submit_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuButtonSubmit){
            Toast.makeText(NewCustomerActivity.this, "Da tao thanh cong", Toast.LENGTH_SHORT).show();
            //new AsyncTaskPostJSON().execute();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class AsyncTaskPostJSON extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog progressDialog;
        String mess = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NewCustomerActivity.this);
            progressDialog.setMessage(getString(R.string.config_sync));
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (PermissionUtil.haveNetworkConnection(getApplicationContext())) {
                HashMap hashMap = new HashMap();

                hashMap.put(Customer.COMPID, MainActivity.account.getCompany());
                hashMap.put(Customer.EMPLID, MainActivity.account.getEmplID());
                hashMap.put(Customer.CUSTID, edtCustID.getText());
                hashMap.put(Customer.CUSTNAME, edtCustName.getText());
                hashMap.put(Customer.ADDRESS1, edtAddress1.getText());
                hashMap.put(Customer.ADDRESS2, edtAddress2.getText());
                hashMap.put(Customer.ADDRESS3, edtAddress3.getText());
                hashMap.put(Customer.CITY, edtCity.getText());
                hashMap.put(Customer.COUNTRY, edtCountry.getText());
                hashMap.put(Customer.PHONENUM, edtPhoneNum.getText());
                hashMap.put(Customer.DISCOUNT, 0);

                return CustomerPresenter.Instance(getApplicationContext()).postNewCustomerToAPI(hashMap);

            } else mess = getString(R.string.checkconnect_error);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();

            if(aBoolean == null) {
                Toast.makeText(NewCustomerActivity.this, mess, Toast.LENGTH_LONG).show();
                return;
            }

            if(aBoolean){
                Toast.makeText(NewCustomerActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(NewCustomerActivity.this, "ERROR ! TRY AGAIN", Toast.LENGTH_SHORT).show();
        }
    }

}
