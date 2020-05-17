package com.doannganh.salesmobileassistant.Views.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.CustomerPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListView;

import java.util.ArrayList;
import java.util.List;

import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.util.PermissionUtil;
import com.doannganh.salesmobileassistant.model.Custom_list_item;


public class CustomerActivity extends AppCompatActivity {

    ListView listView;
    List<Custom_list_item> list;
    CustomAdapterListView customAdapterListView;

    public List<Customer> listCustomer;
    CustomerPresenter customerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);

        AnhXa();
        LoadActionBar();
        LoadListProduct();
        EvenClickGridView();
    }

    private void LoadListProduct() {
        AsyncTaskLoadActi asyncTaskLoadActi = new AsyncTaskLoadActi();
        asyncTaskLoadActi.execute();
    }

    private void LoadActionBar() {
        getSupportActionBar().setTitle(getString(R.string.customer_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void EvenClickGridView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomerActivity.this, NewCustomerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("customer-info", listCustomer.get(position));
                intent.putExtras(bundle);
                // truyen tap tin
                startActivity(intent);
            }
        });
    }


    private void LoadGridView() {
        list = new ArrayList<>();
        for(Customer c : listCustomer){
            list.add(new Custom_list_item(c.getCustName(), c.getAddress1() + ", " + c.getAddress2(), ""));
        }

        customAdapterListView = new CustomAdapterListView(this, list);
        customAdapterListView.setMyListTemp();
        listView.setAdapter(customAdapterListView);
        listView.setTextFilterEnabled(true);
    }

    private void AnhXa() {
        listView = findViewById(R.id.lvParts);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_order_menu_option, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menuOrderListOrderSearch));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setQueryHint(getString(R.string.search_title));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    listView.clearTextFilter();
                    customAdapterListView.filter("");
                }else{
                    customAdapterListView.filter(s);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuListOrderAdd) {
            String title = getString(R.string.customers_scan_cmnd);
            Intent intent = new Intent(this, MyCameraActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(MyCameraActivity.INTENT_FLAP, MyCameraActivity.ACTION.FLAP_DETECT_TEXT.toString());
            bundle.putString(MyCameraActivity.SHORT_TEXT_TITLE, title);
            bundle.putBoolean(MyCameraActivity.IS_AUTOSCAN, false);
            //bundle.putBoolean(MyCameraActivity.IS_AUTORETURN, true);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
            return true;
        } else if (item.getItemId() == R.id.menuOrderListOrderRefresh) {
            if (!PermissionUtil.haveNetworkConnection(getApplicationContext()))
                PermissionUtil.showToastNetworkError(getApplicationContext());
            LoadListProduct();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0){
            // Add new customer after scan camera
            if(resultCode == Activity.RESULT_OK){
                Intent intent = new Intent(getApplicationContext(), NewCustomerActivity.class);

                intent.putExtras(data.getExtras());

                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class AsyncTaskLoadActi extends AsyncTask<Void, Void, Long> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CustomerActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.show();
        }

        @Override
        protected Long doInBackground(Void... voids) {
            customerPresenter = CustomerPresenter.Instance(getApplicationContext());
            if(PermissionUtil.haveNetworkConnection(CustomerActivity.this)) {
                try {
                    long num = 0;
                    listCustomer = customerPresenter.getListCustomer(MainActivity.account.getEmplID());
                    if(listCustomer != null)
                        num = customerPresenter.saveCustomerToDB(listCustomer);
                    return num;
                } catch (Exception e) {
                    Log.d("LLLCustomerActiInit", e.getMessage());
                }
            } else {
                // load from db
                listCustomer = customerPresenter.getListCustomerFromDB(MainActivity.account.getEmplID());
                return (long) listCustomer.size();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long l) {
            progressDialog.dismiss();

            if(l != 0){
                LoadGridView();

                //EvenClickGridView();
            } else {
                PermissionUtil.showToastNetworkError(getApplicationContext());
            }
        }
    }
}

