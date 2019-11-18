package com.doannganh.salesmobileassistant.Views.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.OrderPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.Interface.InterfaceReturnEventListerner;
import com.doannganh.salesmobileassistant.Views.customView.CustomDialogWithListRadio;
import com.doannganh.salesmobileassistant.Views.util.UtilFilter;
import com.doannganh.salesmobileassistant.Views.util.UtilMethod;
import com.doannganh.salesmobileassistant.model.Custom_list_item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListView;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.Order;

public class ListOrdersActivity extends AppCompatActivity implements InterfaceReturnEventListerner {

    ListView listView;
    Button btnMonth;
    EditText edtMonth;
    EditText edtYear;
    TextView txtSyncNum, txtTotalNum;

    ArrayList<Custom_list_item> list;
    ArrayList<String> listMonth;
    CustomAdapterListView customAdapterListView;
    boolean isSync;
    List<Order> listOrderSource, listOrderDB;
    //List<OrderDetail> listOrderDetailDB;
    List<Customer> listCustomer;
    OrderPresenter orderPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        MyGetActivity();

        AnhXa();
        LoadActionBar();
        LoadListOrder();

        EventClickSelectMonth();

        registerForContextMenu(this.listView);
    }

    private void LoadListOrder() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AsyncTaskGetJSONArray asyncTaskGetJSONArray = new AsyncTaskGetJSONArray();
                asyncTaskGetJSONArray.execute();
            }
        });
    }

    private void MyGetActivity() {
        Intent intent = getIntent();
        isSync = intent.getBooleanExtra("isSync", false);
    }

    private void LoadInfoHeader() {

        // info
        LoadValueCountHeader();
    }

    private void LoadValueCountHeader() {
        txtTotalNum.setText(getString(R.string.listorder_total)+": " + list.size());

        int count = 0;
        for (Custom_list_item c : list){
            if(c.getText().equals("Pending"))
                count++;
        }
        txtSyncNum.setText(getString(R.string.listorder_notsynced) + " " + count);
    }

    private void EventClickSelectMonth() {
        LoadSpinner();

        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogWithListRadio customDialogWithListRadio = new CustomDialogWithListRadio(
                        ListOrdersActivity.this, listMonth, btnMonth.getId());
                customDialogWithListRadio.show();
            }
        });
    }

    private void ChangeStatusMonthYear(boolean b) {
        edtMonth.setEnabled(b);
        edtYear.setEnabled(b);
        if(b)
            findViewById(R.id.btnOrderListOK).setVisibility(View.VISIBLE);
        else findViewById(R.id.btnOrderListOK).setVisibility(View.INVISIBLE);
    }

    private void LoadSpinner() {
        listMonth = new ArrayList<>();
        listMonth.add(getString(R.string.listorder_list_thismonth));
        listMonth.add(getString(R.string.listorder_list_lastmonth));
        listMonth.add(getString(R.string.listorder_list_other));

        returnEvent(btnMonth.getId(),0);
    }

    private void LoadActionBar() {
        if(isSync) getSupportActionBar().setTitle(getString(R.string.syncorder_tag));
        else getSupportActionBar().setTitle(getString(R.string.listorder_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void AnhXa()
    {
        listView = findViewById(R.id.lvListOrders);
        btnMonth = findViewById(R.id.btnOrderListSelectMonth);
        edtMonth = (EditText) findViewById(R.id.edtSpinnerMonth);
        edtYear = (EditText) findViewById(R.id.edtSpinnerYear);
        txtSyncNum = findViewById(R.id.txtOrdersLisetOrderSyncNum);
        txtTotalNum = findViewById(R.id.txtOrdersLisetOrderTotalNum);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.list_order_menu_context, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list.get(info.position).getTitle());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //Department department = list.get(info.position);

        switch (item.getItemId()){
            case R.id.menuListOrderOpen:
                OpenOrder(info.position);
                break;
            case R.id.menuListOrderDelete:
                if(list.get(info.position).getText().equals("Pending")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete");
                    builder.setMessage(getString(R.string.listorder_delete_message));
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean b = orderPresenter.deleteOrderFromDB(listOrderDB.get(info.position));
                            if(b) {
                                list.remove(info.position);
                                customAdapterListView.notifyDataSetChanged();
                                LoadValueCountHeader();
                                Toast.makeText(ListOrdersActivity.this, R.string.listorder_delete_success
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();


                }else Toast.makeText(this, getString(R.string.listorder_delete_error), Toast.LENGTH_SHORT).show();
                break;
                default: return false;
        }
        return true;
    }

    private void OpenOrder(int position) {
        Intent intent = new Intent(ListOrdersActivity.this, NewOrderActivity.class);

        Bundle bundle = new Bundle();
        if(isSync) {
            bundle.putSerializable("OrderShow", listOrderDB.get(position));

        }
        else bundle.putSerializable("OrderShow", listOrderSource.get(position));

        bundle.putBoolean("isSync", isSync);
        intent.putExtra("listOrderBundle", bundle);
        startActivityForResult(intent, 0);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_order_menu_option, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menuOrderListOrderSearch));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setQueryHint(getString(R.string.search_title));
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(ListOrdersActivity.this, "Filter by customer name "+s, Toast.LENGTH_SHORT).show();
                //ListOrdersActivity.this.customAdapterListView.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    listView.clearTextFilter();
                    customAdapterListView.filter("");
                }else{
                    String texto = s;
                    customAdapterListView.filter(s);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuListOrderAdd){
            startActivityForResult(new Intent(ListOrdersActivity.this, NewOrderActivity.class), 0);
            return true;
        }else if(item.getItemId() ==  R.id.menuOrderListOrderRefresh){
            LoadListOrder();
                //Toast.makeText(this, "list size "+list.size(), Toast.LENGTH_SHORT).show();
                //customAdapterListView
        }
            return super.onOptionsItemSelected(item);
    }

    public void ClickButtonOK(View view) {

        Toast.makeText(ListOrdersActivity.this, "Show order "+
                edtMonth.getText().toString() + " " + edtYear.getText().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void returnEvent(@Nullable int id, int positionSelect) {
        if(id == btnMonth.getId()){
            btnMonth.setText(listMonth.get(positionSelect) + "");

            Calendar calendar = Calendar.getInstance();
            int thisMonth = calendar.get(Calendar.MONTH) + 1;
            int thisYear = calendar.get(Calendar.YEAR);

            switch (positionSelect){
                case 0:
                    edtMonth.setText(thisMonth + "");
                    edtYear.setText(thisYear + "");

                    ChangeStatusMonthYear(false);
                    break;
                case 1:
                    edtMonth.setText(thisMonth-1 + "");
                    edtYear.setText(thisYear + "");

                    ChangeStatusMonthYear(false);
                    break;
                case 2:
                    ChangeStatusMonthYear(true);
                    break;
                default: return;
            }
        }

    }

    public class AsyncTaskGetJSONArray extends AsyncTask<Void, Void, List<Order>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ListOrdersActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List<Order> doInBackground(Void... voids) {
            try {
                orderPresenter = OrderPresenter.Instance(getApplicationContext());
                listCustomer = MainActivity.listCustomer;
                if (listCustomer == null){
                    //CustomerPresenter customerPresenter = CustomerPresenter.Instance(getApplicationContext());
                    //listCustomer = customerPresenter.GetListCustomer();
                }

                if(isSync){
                    listOrderDB = orderPresenter.getListOrderFromDB();
                    return listOrderDB;
                }

                listOrderSource = orderPresenter.getListOrderFromAPI(MainActivity.account.getEmplID());
                return listOrderSource;


            } catch (Exception e) {
                Toast.makeText(ListOrdersActivity.this, "Error when get order", Toast.LENGTH_LONG).show();
                Log.d("LLLListOrdActGetInit", e.getMessage());
                onBackPressed();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Order> listRe) {
            if(listRe != null)
            {
                if(listRe.size() > 0){
                    list = new ArrayList<>();

                    for(Order o : listRe){
                        //get name customer
                        String name = UtilFilter.getCustomerNameByID(listCustomer, o.getCustID());
                        if (name == null) continue;
                        list.add(new Custom_list_item(name, o.getMyOrderID(), UtilMethod.convertIdToStatus(o.getOrderStatus())));
                    }

                    customAdapterListView = new CustomAdapterListView(ListOrdersActivity.this
                            , R.layout.custom_list_item, list);
                    customAdapterListView.setMyListTemp();
                    listView.setAdapter(customAdapterListView);
                    listView.setTextFilterEnabled(true);
                    LoadInfoHeader();

                    EventClickList();
                }

            }
            progressDialog.dismiss();
        }
    }

    private void EventClickList() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenOrder(position);
            }
        });
    }
}
