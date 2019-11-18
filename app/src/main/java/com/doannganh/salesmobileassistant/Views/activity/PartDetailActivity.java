package com.doannganh.salesmobileassistant.Views.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.doannganh.salesmobileassistant.Presenter.ProductPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.model.Product;
import com.doannganh.salesmobileassistant.model.ProductInSite;

public class PartDetailActivity extends AppCompatActivity {

    Product product;
    ProductInSite productInSite;
    TextView edtCOmpany, edtGroup, edtID, edtName, edtUnitPrice, edtUOM, edtDate, txtTitle, txtQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_detail);

        LoadActionBar();
        AnhXa();
        GetIntent();
        LoadProduct();
    }

    private void AnhXa() {
        edtCOmpany = findViewById(R.id.txtPartDetailCompany);
        edtGroup = findViewById(R.id.txtPartDetailProductGroup);
        edtID = findViewById(R.id.txtPartDetailProductID);
        edtName = findViewById(R.id.txtPartDetailProductName);
        edtUnitPrice = findViewById(R.id.txtPartDetailUnitPrice);
        edtUOM = findViewById(R.id.txtPartDetailUOM);
        edtDate = findViewById(R.id.txtPartDetailDateUpdate);
        txtTitle = findViewById(R.id.txtPartDetailTitle);
        txtQuantity = findViewById(R.id.txtPartDetailQuantity);
    }

    private void LoadProduct() {
        // set view
        txtTitle.setText(getString(R.string.productinfo_title) + product.getProdName() +
                " - " + product.getProdID());
        edtCOmpany.setText(product.getCompID());
        edtGroup.setText(product.getPTypeID());
        edtID.setText(product.getProdID());
        edtName.setText(product.getProdName());
        edtUnitPrice.setText(product.getUnitPrice() + "");
        edtUOM.setText(product.getUOM());
        edtDate.setText(product.getDateUpdate() + "");

        txtQuantity.setText(productInSite.getQuantity() + "");
    }

    private void GetIntent() {
        Bundle bundle = getIntent().getBundleExtra("myBundle");
        product = (Product) bundle.getSerializable("productclick");
        productInSite = (ProductInSite) bundle.getSerializable("productISclick");
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.productinfo_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
/*
    class GetQuantityProduct extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PartDetailActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer q) {
            super.onPostExecute(q);
            progressDialog.dismiss();


            txtQuantity.setText(q + "");
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            ProductPresenter p = ProductPresenter.Instance(getApplicationContext());
            return p.getQuantityCurent(product.getProdID());
        }
    }
 */
}
