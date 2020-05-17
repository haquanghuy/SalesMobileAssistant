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

import com.doannganh.salesmobileassistant.Presenter.OrderDetailPresenter;
import com.doannganh.salesmobileassistant.Presenter.OrderPresenter;
import com.doannganh.salesmobileassistant.Presenter.RoutePlanPresenter;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.Interface.InterfaceReturnEventListerner;
import com.doannganh.salesmobileassistant.Views.customView.CustomDialogWithListRadio;
import com.doannganh.salesmobileassistant.Views.customView.CustomDialogWithRecyclerView;
import com.doannganh.salesmobileassistant.util.PermissionUtil;
import com.doannganh.salesmobileassistant.util.MyDate;
import com.doannganh.salesmobileassistant.util.UtilFilter;
import com.doannganh.salesmobileassistant.util.UtilMethod;
import com.doannganh.salesmobileassistant.model.Custom_list_item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.doannganh.salesmobileassistant.Views.adapter.CustomAdapterListView;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.MyJob;
import com.doannganh.salesmobileassistant.model.Order;
import com.doannganh.salesmobileassistant.model.RoutePlan;
import com.doannganh.salesmobileassistant.util.ConstantUtil;
import com.doannganh.salesmobileassistant.util.StringUtil;

public class ListOrdersActivity extends AppCompatActivity implements InterfaceReturnEventListerner {

    ListView listView;
    Button btnMonth, btnOK;
    EditText edtMonth;
    EditText edtYear;
    TextView txtSyncNum, txtTotalNum;

    List<Custom_list_item> list;
    List<String> listMonth;
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

        EventClickBtnOK();

        registerForContextMenu(this.listView);
    }

    private void EventClickBtnOK() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Custom_list_item> l = filterOrderByCustom(list, isSync?listOrderDB:listOrderSource);

                if(l == null || l.size() < 1){
                    listView.setVisibility(View.GONE);
                    findViewById(R.id.txtEmplty).setVisibility(View.VISIBLE);
                    txtTotalNum.setText(getString(R.string.listorder_total) + " " + 0);
                    txtSyncNum.setText(getString(R.string.listorder_total) + " " + 0);
                }
                else {
                    listView.setVisibility(View.VISIBLE);
                    findViewById(R.id.txtEmplty).setVisibility(View.GONE);
                    customAdapterListView.changeListSourceOnce(l);
                    CheckListViewEmpty();
                }
            }
        });
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
        int count = 0;
        if(list == null || list.size() < 1) {
            txtTotalNum.setText(getString(R.string.listorder_total) + " " + count);
        } else {
            txtTotalNum.setText(getString(R.string.listorder_total) + " " + list.size());

            for (Custom_list_item c : list) {
                if (c.getText().equals("Pending"))
                    count++;
            }
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
            btnOK.setVisibility(View.VISIBLE);
        else btnOK.setVisibility(View.INVISIBLE);
    }

    private void LoadSpinner() {
        listMonth = new ArrayList<>();
        listMonth.add(getString(R.string.listorder_list_thisweek));
        listMonth.add(getString(R.string.listorder_list_thismonth));
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
        btnOK = findViewById(R.id.btnOrderListOK);
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
                            deleteOrder(info);
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

    private void deleteOrder(final AdapterView.AdapterContextMenuInfo info) {
        Order o = listOrderDB.get(info.position);
        if(!StringUtil.isNullOrEmpty(o.getRequestDate())){
            final List<MyJob> l = RoutePlanPresenter.Instance(ListOrdersActivity.this).getListMyJob(
                    MainActivity.listCustomer, o.getEmplID(), o.getCustID(), o.getRequestDate());
            CustomDialogWithRecyclerView custom = new CustomDialogWithRecyclerView(
                    this, l, new CustomDialogWithRecyclerView.ReturnValueListerner() {
                @Override
                public void OnTouchNegativeButton() {

                }

                @Override
                public void OnTouchPositiveButton() {
                    execDeleteOrder(info, l);
                    CheckListViewEmpty();
                }
            });
            custom.show();
        } else {
            execDeleteOrder(info, null);
            CheckListViewEmpty();
        }

    }

    private void execDeleteOrder(final AdapterView.AdapterContextMenuInfo info, @Nullable List<MyJob> l){
        boolean b = OrderDetailPresenter.Instance(getApplicationContext()).deleteOrderDetailFromDB(
                listOrderDB.get(info.position).getCompID(),
                listOrderDB.get(info.position).getMyOrderID()
        );
        if (b)
            b = orderPresenter.deleteOrderFromDB(listOrderDB.get(info.position));
        else
            Toast.makeText(ListOrdersActivity.this, getString(R.string.listorder_delete_error), Toast.LENGTH_SHORT).show();
        if(b) {
            list.remove(info.position);
            customAdapterListView.notifyDataSetChanged();
            LoadValueCountHeader();

            // set status job
            if(l != null) {
                RoutePlan routePlan = l.get(0).getRoutePlan();
                RoutePlanPresenter routePlanPresenter = RoutePlanPresenter.Instance(getApplicationContext());
                // save to db
                routePlan.setVisited(RoutePlan.UNVISIT);
                long num = routePlanPresenter.setRoutePlanToDB(routePlan);
            }

            Toast.makeText(ListOrdersActivity.this, R.string.listorder_delete_success
                    , Toast.LENGTH_SHORT).show();
        }
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

    private void CheckListViewEmpty(){
        if(list == null || list.size() < 1){
            listView.setVisibility(View.GONE);
            findViewById(R.id.txtEmplty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.txtEmplty).setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        LoadInfoHeader();
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
            startActivityForResult(new Intent(ListOrdersActivity.this, NewOrderActivity.class), 0);
            return true;
        } else if (item.getItemId() == R.id.menuOrderListOrderRefresh) {
            if (!PermissionUtil.haveNetworkConnection(getApplicationContext()) && !isSync)
                PermissionUtil.showToastNetworkError(getApplicationContext());
            LoadListOrder();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void returnEvent(@Nullable int id, int positionSelect) {
        if(id == btnMonth.getId()){
            if(list != null)
                customAdapterListView.resetListSource();
            List<Custom_list_item> l = new ArrayList<>();
            btnMonth.setText(listMonth.get(positionSelect) + "");

            Calendar calendar = Calendar.getInstance();
            int thisMonth = calendar.get(Calendar.MONTH) + 1;
            int thisYear = calendar.get(Calendar.YEAR);

            switch (positionSelect){
                case 0:
                    // this week
                    edtMonth.setText(thisMonth + "");
                    edtYear.setText(thisYear + "");

                    ChangeStatusMonthYear(false);

                    l = filterOrderInCurrent(list, isSync?listOrderDB:listOrderSource, 0);

                    break;

                case 1:
                    // this month
                    edtMonth.setText(thisMonth + "");
                    edtYear.setText(thisYear + "");

                    ChangeStatusMonthYear(false);
                    l = filterOrderInCurrent(list, isSync?listOrderDB:listOrderSource, 1);
                    break;

                case 2:
                    ChangeStatusMonthYear(true);
                    break;
                default: l = null;

            }

            if(l == null || l.size() < 1){
                listView.setVisibility(View.GONE);
                findViewById(R.id.txtEmplty).setVisibility(View.VISIBLE);
                txtTotalNum.setText(getString(R.string.listorder_total) + " " + 0);
                txtSyncNum.setText(getString(R.string.listorder_total) + " " + 0);
            }
            else {
                listView.setVisibility(View.VISIBLE);
                findViewById(R.id.txtEmplty).setVisibility(View.GONE);
                customAdapterListView.changeListSourceOnce(l);
                CheckListViewEmpty();
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
                    listOrderDB = orderPresenter.getListOrderFromDB(MainActivity.account.getEmplID()
                        , new int[]{ConstantUtil.DB_ORDER_STATUS_PENDING});
                    return listOrderDB;
                }

                if (PermissionUtil.haveNetworkConnection(getApplicationContext())) {
                    listOrderSource = orderPresenter.getListOrderFromAPI(MainActivity.account.getEmplID());
                    // save db
                    if(listOrderSource != null) {
                        for (Order o : listOrderSource)
                            orderPresenter.saveOrderToDB(o);
                    }
                }
                else listOrderSource = orderPresenter.getListOrderFromDB(MainActivity.account.getEmplID()
                        , new int[]{ConstantUtil.DB_ORDER_STATUS_VERIFYING, ConstantUtil.DB_ORDER_STATUS_COMPLETE});
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

                    customAdapterListView = new CustomAdapterListView(ListOrdersActivity.this, list);
                    customAdapterListView.setMyListTemp();
                    listView.setAdapter(customAdapterListView);
                    listView.setTextFilterEnabled(true);

                    EventClickList();
                }

                CheckListViewEmpty();
            }
            progressDialog.dismiss();

            UtilMethod.hideKeyboardFrom(ListOrdersActivity.this);
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


    /**
     * Filter list by week, month
     * @param listUI: list root UI show
     * @param list: list source
     * @param weekOrMonth: 0:week, 1:month
     * @return: list filtered
     */
    private List<Custom_list_item> filterOrderInCurrent(List<Custom_list_item> listUI, List<Order> list, int weekOrMonth){
        List<Custom_list_item> re = new ArrayList<>();
        MyDate myDate = new MyDate();
        // filter by date
        try {
            String[] date;
            if(weekOrMonth == 0)
                date = myDate.getCurrentWeekDate(MyDate.WEEK_NOW);
            else date = myDate.getCurrentMonthDate();
            for (int i=0; i<list.size(); i++) {
                boolean b = myDate.isIn(date, list.get(i).getOrderDate());

                if(b){
                    re.add(listUI.get(i));
                }
            }

        } catch (Exception e) {
            Log.d("LLLFilterOrderWeek", e.getMessage());
        }

        return re;
    }

    private List<Custom_list_item> filterOrderByCustom(List<Custom_list_item> listUI, List<Order> list){
        int moth = 0;
        int year = 0;
        try {
            moth = Integer.parseInt(edtMonth.getText().toString());
            year = Integer.parseInt(edtYear.getText().toString());
        } catch (Exception e){
            Toast.makeText(this, "Month or year invalid", Toast.LENGTH_LONG).show();
            return null;
        }

        List<Custom_list_item> re = new ArrayList<>();
        MyDate myDate = new MyDate();
        // filter by date
        try {
            String[] date = myDate.getCurrentMonthDate(moth, year);
            for (int i=0; i<list.size(); i++) {
                boolean b = myDate.isIn(date, list.get(i).getOrderDate());

                if(b){
                    re.add(listUI.get(i));
                }
            }

        } catch (Exception e) {
            Log.d("LLLFilterOrderWeek", e.getMessage());
        }

        return re;
    }
}
