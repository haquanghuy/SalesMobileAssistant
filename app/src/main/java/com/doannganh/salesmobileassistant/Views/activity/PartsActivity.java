package com.doannganh.salesmobileassistant.Views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.ProductPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListView;
import com.doannganh.salesmobileassistant.model.Custom_grid_item;

import java.util.ArrayList;
import java.util.List;

import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterGridView;
import com.doannganh.salesmobileassistant.model.Custom_list_item;
import com.doannganh.salesmobileassistant.model.Product;
import com.doannganh.salesmobileassistant.model.ProductInSite;

public class PartsActivity extends AppCompatActivity {

    ListView listView;
    List<Custom_list_item> list;
    CustomAdapterListView customAdapterListView;

    public static List<Product> listProduct;
    ProductPresenter productPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);

        AnhXa();
        LoadActionBar();
        LoadListProduct();
    }

    private void LoadListProduct() {
        AsyncTaskLoadActi asyncTaskLoadActi = new AsyncTaskLoadActi();
        asyncTaskLoadActi.execute();
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.product_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void EvenClickGridView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PartsActivity.this, PartDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productclick", listProduct.get(position));

                ProductInSite ps = null;
                // để đây tạm
                for (ProductInSite p : MainActivity.listProductInSite){
                    if(p.getProdID().equals(listProduct.get(position).getProdID())){
                        ps = p;
                        break;
                    }
                }

                bundle.putSerializable("productISclick", ps);
                intent.putExtra("myBundle", bundle);
                // truyen tap tin
                startActivity(intent);
            }
        });
    }

    private void LoadGridView() {
        list = new ArrayList<>();
        for(Product p : listProduct){
            list.add(new Custom_list_item(p.getProdName(), p.getProdID(), ""));
        }

        customAdapterListView = new CustomAdapterListView(this, R.layout.custom_list_item, list);
        listView.setAdapter(customAdapterListView);
    }

    private void AnhXa() {
        listView = findViewById(R.id.lvParts);

    }

    public class AsyncTaskLoadActi extends AsyncTask<Void, Void, List<Product>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PartsActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.show();
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            try {
                productPresenter = ProductPresenter.Instance(getApplicationContext());
                listProduct = productPresenter.GetListProduct();
                return listProduct;
            } catch (Exception e) {
                Toast.makeText(PartsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Product> l) {
            progressDialog.dismiss();

            if(l != null){
                LoadGridView();

                EvenClickGridView();
            }
        }
    }
}
