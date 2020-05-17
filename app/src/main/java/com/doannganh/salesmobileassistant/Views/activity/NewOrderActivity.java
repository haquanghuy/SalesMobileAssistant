package com.doannganh.salesmobileassistant.Views.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.Presenter.OrderDetailPresenter;
import com.doannganh.salesmobileassistant.Presenter.OrderPresenter;
import com.doannganh.salesmobileassistant.Presenter.ProductPresenter;
import com.doannganh.salesmobileassistant.Manager.OrderTemp;
import com.doannganh.salesmobileassistant.Views.Interface.InterfaceReturnEventListerner;
import com.doannganh.salesmobileassistant.Views.fragment.NewOrderCustomer;
import com.doannganh.salesmobileassistant.Views.fragment.NewOrderProduct;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.fragment.NewOrderTotalAmt;
import com.doannganh.salesmobileassistant.util.PermissionUtil;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.Order;
import com.doannganh.salesmobileassistant.model.OrderDetail;
import com.doannganh.salesmobileassistant.model.ProductInSite;
import com.doannganh.salesmobileassistant.model.RoutePlan;
import com.doannganh.salesmobileassistant.util.ConstantUtil;
import com.doannganh.salesmobileassistant.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity implements InterfaceReturnEventListerner {
    //Toolbar toolbar;
    Button btnNext, btnPrev, btnSave;

    public Customer customer; // from job
    String datePlan; // from job
    public static OrderTemp orderTemp;
    public static Order orderHead;
    public static List<OrderDetail> orderDetail;
    public static Order orderShow; // from list order
    public static List<OrderDetail> orderDetailShow; // from list order

    FragmentManager fragmentManager = getFragmentManager();
    int indexScreen = 1;
    boolean isJob = false;
    boolean isSync;

    public static String myOrderID;
    OrderDetailPresenter orderDetailPresenter;
    OrderPresenter orderPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        Load();
        MyGetActivity();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 1 seconds
                LoadMyOrderID();
            }
        }, 700);

        orderTemp = new OrderTemp();
        orderPresenter = OrderPresenter.Instance(getApplicationContext());


        AnhXa();
        LoadActionBar();

        SetButtonBackgroundDefault(btnNext);
        SetButtonBackgroundDefault(btnPrev);
        CheckEnableButtonPrev();


        EventNext();
        EventPrev();
    }

    private void Load() {
    }

    private void LoadMyOrderID() {
        AsyncTaskLoadActi asyncTaskLoadActi = new AsyncTaskLoadActi();
        asyncTaskLoadActi.execute();
    }


    private void MyGetActivity() {
        Intent intent = getIntent();

        if (getCallingActivity().getClassName().equals(OrdersActivity.class.getName())) {

            /* here the main activity is calling activity*/

        }else if (getCallingActivity().getClassName().equals(ListOrdersActivity.class.getName())) {

            /* here the main activity is calling activity*/
            // start from view order
            Bundle bun = intent.getBundleExtra("listOrderBundle");
            if(bun != null) {
                orderShow = (Order) bun.getSerializable("OrderShow");
                isSync = (boolean) bun.getSerializable("isSync");

            }

        }else if (getCallingActivity().getClassName().equals(RoutePlanActivity.class.getName())) {

            /* here the main activity is calling activity*/
            // start from job
            // nhan customer
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                customer = (Customer) bundle.getSerializable("Customer");
                if(customer == null) {
                    orderShow = (Order) bundle.getSerializable("JobOrder");
                    isSync = true;
                }

                datePlan = bundle.getString("DatePlan");
                isJob = true;
            }

        }


    }

    private void LoadActionBar() {
        if(orderShow != null){
            String title = getString(R.string.neworder_view_tag);
            if(orderShow.getOrderStatus() > 2)
                title += getString(R.string.neworder_view_tag_posted);
            else if (orderShow.getOrderStatus() == 2)
                title += getString(R.string.neworder_view_tag_posted);
            else title += getString(R.string.neworder_view_tag_pending);
            getSupportActionBar().setTitle(title);
        }
        else getSupportActionBar().setTitle(getString(R.string.neworder_tag));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void SetButtonBackgroundDefault(Button btn) {
        btn.setBackgroundResource(android.R.drawable.btn_default);
        btn.setTextColor(Color.BLACK);
        //SetButtonBackgroundDefault(btnNext);
        /*
        btn.setTextColor(Color.BLACK);
        btn.getBackground().setColorFilter(getResources().getColor(R.color.public_button_bacground),
                PorterDuff.Mode.MULTIPLY);*/
    }

    private void CheckEnableButtonPrev() {
        if (indexScreen == 1) {
            btnPrev.setEnabled(false);
            btnPrev.setTextColor(Color.GRAY);
        } else {
            btnPrev.setEnabled(true);
            btnPrev.setTextColor(Color.BLACK);
        }
    }

    private void ChangeFragment(int indexScreen) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (indexScreen) {
            case 1:
                NewOrderCustomer fragmentOne;
                try {
                    fragmentOne = (NewOrderCustomer) getFragmentManager().findFragmentByTag("fragment-1");
                    if (fragmentOne != null) {
                        //fragmentTransaction.add(R.id.frameLayourNewOrder, fragmentOne, fragmentOne.getTAG());
                        fragmentTransaction.replace(R.id.frameLayourNewOrder, fragmentOne, fragmentOne.getTAG());

                    } else {
                        fragmentOne = NewOrderCustomer.myInstance("fragment-1");
                        //fragmentTransaction.add(R.id.frameLayourNewOrder, fragmentOne, fragmentOne.getTAG());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("F1Customer", customer);
                        fragmentOne.setArguments(bundle);
                        fragmentTransaction.replace(R.id.frameLayourNewOrder, fragmentOne, fragmentOne.getTAG());
                        fragmentTransaction.addToBackStack(fragmentOne.getTAG());
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Page transfer error 1", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                NewOrderProduct fragmentTwo;
                try {
                    fragmentTwo = (NewOrderProduct) getFragmentManager().findFragmentByTag("fragment-2");
                    if (fragmentTwo != null) {
                        //fragmentTransaction.add(R.id.frameLayourNewOrder, fragmentTwo, fragmentTwo.getTAG());
                        fragmentTransaction.replace(R.id.frameLayourNewOrder, fragmentTwo, fragmentTwo.getTAG());
                    } else {
                        fragmentTwo = NewOrderProduct.myInstance("fragment-2");
                        //fragmentTransaction.add(R.id.frameLayourNewOrder, fragmentTwo, fragmentTwo.getTAG());
                        fragmentTransaction.replace(R.id.frameLayourNewOrder, fragmentTwo, fragmentTwo.getTAG());
                        fragmentTransaction.addToBackStack(fragmentTwo.getTAG());
                    }
                    ChangeButtonFunction(false);
                    btnSave.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    Toast.makeText(this, "Page transfer error 2", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                NewOrderTotalAmt fragmentThree;
                try {
                    fragmentThree = (NewOrderTotalAmt) getFragmentManager().findFragmentByTag("fragment-3");
                    if (fragmentThree != null) {
                        //fragmentTransaction.add(R.id.frameLayourNewOrder, fragmentThree, fragmentThree.getTAG());
                        fragmentTransaction.replace(R.id.frameLayourNewOrder, fragmentThree, fragmentThree.getTAG());
                    } else {
                        fragmentThree = NewOrderTotalAmt.myInstance("fragment-3");
                        //fragmentTransaction.add(R.id.frameLayourNewOrder, fragmentThree, fragmentThree.getTAG());
                        customer = NewOrderCustomer.customer;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("F3Customer", customer);
                        fragmentThree.setArguments(bundle);
                        fragmentTransaction.replace(R.id.frameLayourNewOrder, fragmentThree, fragmentThree.getTAG());
                        fragmentTransaction.addToBackStack(fragmentThree.getTAG());
                    }
                    ChangeButtonFunction(true);
                    btnSave.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(this, "Page transfer error 3", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                indexScreen = 1;
                ChangeFragment(indexScreen);
                return;
        }
        fragmentTransaction.commit();
    }

    private void ChangeButtonFunction(boolean bool) {
        // true: change, false: !true
        if (bool) {
            btnNext.setText(getString(R.string.neworder_text_buttonNextChange));
            //btnNext.setBackgroundColor(Color.RED);
            btnNext.setTextColor(Color.WHITE);
            btnNext.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        } else {
            btnNext.setText(getString(R.string.neworder_text_buttonNext));
            //SetButtonBackgroundDefault(btnNext);
            btnNext.setTextColor(Color.BLACK);
            btnNext.getBackground().clearColorFilter();
        }
    }

    private void AnhXa() {
        btnNext = findViewById(R.id.btnNewOrderNext);
        btnPrev = findViewById(R.id.btnNewOrderPrev);
        btnSave = findViewById(R.id.btnNewOrderSave);
    }

    public void saveOffline(View view) {
        if(orderShow != null){
            if(orderShow.getOrderStatus() != ConstantUtil.DB_ORDER_STATUS_PENDING) {
                Toast.makeText(this, R.string.save_order_success, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        orderHead.setOrderStatus(ConstantUtil.DB_ORDER_STATUS_PENDING);
        if(isJob){
            orderHead.setRequestDate(datePlan);
        }
        long num = orderPresenter.saveOrderToDB(orderHead);
        if(num == 0)
            Toast.makeText(this, R.string.save_order_view, Toast.LENGTH_SHORT).show();
        else{
            orderDetailPresenter = OrderDetailPresenter.Instance(getApplicationContext());
            long numDt = orderDetailPresenter.saveListOrderDetailToDB(orderDetail);
            if(numDt == ConstantUtil.DB_CRUD_RESPONSE_ERROR)
                Toast.makeText(this, R.string.save_order_error, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(this, R.string.save_order_success, Toast.LENGTH_SHORT).show();

                if(isJob){
                    Intent returnIntent = new Intent(NewOrderActivity.this, RoutePlanActivity.class);
                    returnIntent.putExtra("isFinish", RoutePlan.VISITOFFLINE);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    return;
                }
                while (fragmentManager.getBackStackEntryCount() != 0){
                    fragmentManager.popBackStackImmediate();
                }
                finish();
            }

        }
    }

    private void EventPrev() {
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPrevEvent();
            }
        });
    }

    void SetPrevEvent(){
        indexScreen--;
        ChangeFragment(indexScreen);
        CheckEnableButtonPrev();
    }

    private void EventNext() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexScreen != 3) {
                    if(indexScreen==2){
                        if(NewOrderProduct.getTotalMoney() < 1){
                            Toast.makeText(NewOrderActivity.this
                                    , R.string.neworder_product_pleaseChooswProduct, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (NewOrderCustomer.customer == null) {
                        Toast.makeText(NewOrderActivity.this, R.string.neworder_customer_nocustomer
                                , Toast.LENGTH_LONG).show();
                        return;
                    }
                    indexScreen++;
                    ChangeFragment(indexScreen);
                    CheckEnableButtonPrev();
                } else if (indexScreen == 3) {
                    AsyncTaskPostJSONArray asyncTaskPostJSONArray = new AsyncTaskPostJSONArray();
                    asyncTaskPostJSONArray.execute();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (indexScreen == 1) {
            if (orderDetail != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit");
                builder.setMessage("Not save ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent intent = new Intent(NewOrderActivity.this, OrdersActivity.class);
                        //startActivity(intent);
                        Exit();
                    }
                });
                builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else {
                //Intent intent = new Intent(NewOrderActivity.this, OrdersActivity.class);
                //startActivity(intent);
                Exit();
            }
        } else {
            indexScreen--;
            ChangeFragment(indexScreen);
            //super.onBackPressed();
        }

    }

    @Override
    public void returnEvent(@Nullable int id, int positionSelect) {
        SetCustomerSelect(id, positionSelect);
    }

    private void SetCustomerSelect(int id, int positionSelect) {
        Button bt = findViewById(id);
        if (id == R.id.btnOrdersNewCustomerNameID) {
            customer = MainActivity.listCustomer.get(positionSelect);
            NewOrderCustomer.customer = customer;

            bt.setText(customer.getCustName() + " - " + customer.getCustID());
            EditText edtAd1 = findViewById(R.id.edtOrdersNewCustomerAddress1);
            EditText edtAd2 = findViewById(R.id.edtOrdersNewCustomerAddress2);
            edtAd1.setText(customer.getAddress1());
            edtAd2.setText(customer.getAddress2());
        } else if (id == R.id.btnNewOrderProductGetProduct) {
            NewOrderProduct.positionProductTemp = positionSelect;

            bt.setText(MainActivity.listProductSource.get(positionSelect).getProdName()
                    + " - " + MainActivity.listProductSource.get(positionSelect).getProdID());

            // set quantity
            ProductInSite ps = null;
            // để đây tạm
            for (ProductInSite p : MainActivity.listProductInSite){
                if(p.getProdID().equals(MainActivity.listProductSource.get(positionSelect).getProdID())){
                    NewOrderProduct.quantityProductChoose = p.getQuantity();
                    NewOrderProduct.textView.setText(getString(R.string.neworder_product_textbeforequantity)
                            + p.getQuantity() + "");
                    break;
                }
            }

        }
        //customerID = MainActivity.listCustomer.get(positionSelect).getCustID();
        //customerName = MainActivity.listCustomer.get(positionSelect).getCustName();

    }

    private class AsyncTaskPostJSONArray extends AsyncTask<Void, Void, List<Boolean>> {

        private ProgressDialog progressDialog;
        String mess = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NewOrderActivity.this);
            progressDialog.setMessage(getString(R.string.config_sync));
            progressDialog.show();
        }

        @Override
        protected List<Boolean> doInBackground(Void... voids) {
            List<Boolean> listRe = new ArrayList<>();

            if(orderShow != null)
                if(orderShow.getOrderStatus() != ConstantUtil.DB_ORDER_STATUS_PENDING){
                    mess = getString(R.string.post_json_exists);
                    return null;
                }

            if(StringUtil.isNullOrEmpty(orderHead.getRequestDate()) && isJob){
                orderHead.setRequestDate(datePlan);
            }

//            if(StringUtil.isNullOrEmpty(orderHead.getRequestDate()))
//                if(StringUtil.isNullOrEmpty(orderShow.getRequestDate()))
//                    orderHead.setRequestDate(orderShow.getRequestDate());

            if (PermissionUtil.haveNetworkConnection(getApplicationContext())) {
                HashMap hashMap = new HashMap();
                List<HashMap> listHashMap = new ArrayList<>();

                // order head
                hashMap.put(Order.COMPID, orderHead.getCompID());
                hashMap.put(Order.EMPLID, orderHead.getEmplID());
                hashMap.put(Order.CUSTID, orderHead.getCustID());
                hashMap.put(Order.MYORDERID, orderHead.getMyOrderID());
                hashMap.put(Order.ORDEID, orderHead.getOrdeID());
                hashMap.put(Order.ORDERDATE, orderHead.getOrderDate());
                hashMap.put(Order.NEEDBYDATE, orderHead.getNeedByDate());
                hashMap.put(Order.REQUESTDATE, orderHead.getRequestDate());
                hashMap.put(Order.ORDERSTATUS, ConstantUtil.DB_ORDER_STATUS_VERIFYING);


                // order detail
                for(OrderDetail detail : orderDetail){
                    HashMap hashMap1 = new HashMap();
                    hashMap1.put(OrderDetail.COMPID, detail.getCompID());
                    hashMap1.put(OrderDetail.SITEID, detail.getSiteID());
                    hashMap1.put(OrderDetail.MYORDERID, detail.getMyOrderID());
                    hashMap1.put(OrderDetail.PRODID, detail.getProdID());
                    hashMap1.put(OrderDetail.SELLINGQUANTITY, detail.getSellingQuantity());
                    hashMap1.put(OrderDetail.UNITPRICE, detail.getUnitPrice());
                    hashMap1.put(OrderDetail.ORDERID, detail.getOrdeID());
                    hashMap1.put(OrderDetail.ORDERLINE, detail.getOrderLine());

                    listHashMap.add(hashMap1);
                }


                boolean isPostOrder = orderPresenter.postNewOrderToAPI(hashMap);

                listRe.add(isPostOrder);
                if (!isPostOrder) return listRe;

                orderDetailPresenter = OrderDetailPresenter.Instance(getApplicationContext());
                boolean isPostOrderDetail = orderDetailPresenter.postNewOrderDetail(listHashMap);

                listRe.add(isPostOrderDetail);
                return listRe;
            } else mess = getString(R.string.checkconnect_error);
            return null;
        }

        @Override
        protected void onPostExecute(List<Boolean> aBoolean) {
            progressDialog.dismiss();

            if(aBoolean == null) {
                Toast.makeText(NewOrderActivity.this, mess, Toast.LENGTH_LONG).show();
                return;
            }
            if(!aBoolean.get(0))
                Toast.makeText(NewOrderActivity.this, R.string.post_json_error_order
                        , Toast.LENGTH_LONG).show();
            else if (aBoolean.get(0) && aBoolean.get(1)) {
                Toast.makeText(NewOrderActivity.this, R.string.post_json_success,
                        Toast.LENGTH_LONG).show();

                if(isJob){
                    Intent returnIntent = new Intent(NewOrderActivity.this, RoutePlanActivity.class);
                    returnIntent.putExtra("isFinish",RoutePlan.VISITED);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    return;
                }

                Exit();
            }
            else if (aBoolean.get(0) && !aBoolean.get(1)) {
                Toast.makeText(NewOrderActivity.this, R.string.post_json_error_detail
                        , Toast.LENGTH_LONG).show();
                // delete order
                orderPresenter.deleteOrderFromAPI(orderHead.getMyOrderID());
                orderPresenter.deleteOrderFromDB(orderHead);
            }


        }
    }

    private class AsyncTaskLoadActi extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NewOrderActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... booleans) {
            // get dtail
            if (orderShow != null) {
                orderDetailPresenter = OrderDetailPresenter.Instance(getApplicationContext());

                if (isSync) {
                    orderDetailShow = orderDetailPresenter.getListOrderDetailFromDB(orderShow.getCompID(),
                            orderShow.getMyOrderID());
                } else {
                    if(PermissionUtil.haveNetworkConnection(getApplicationContext()))
                        orderDetailShow = orderDetailPresenter.getListOrderDetail(orderShow.getMyOrderID());
                    else orderDetailShow = orderDetailPresenter.getListOrderDetailFromDB(orderShow.getCompID()
                            , orderShow.getMyOrderID());
                }
                myOrderID = orderShow.getMyOrderID();
                return myOrderID;
            }
            return orderPresenter.getOrderIDCurrent(MainActivity.account.getEmplID());
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            myOrderID = s;

            if(myOrderID != null){
                ChangeFragment(indexScreen);
            }
            else Toast.makeText(NewOrderActivity.this, R.string.neworder_getIDCurrent
                    , Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncTaskLoadQuantity extends AsyncTask<String, Void, Integer> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NewOrderActivity.this);
            progressDialog.setMessage(getString(R.string.dialog_init));
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            ProductPresenter p = ProductPresenter.Instance(getApplicationContext());
            return p.getQuantityCurent(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progressDialog.dismiss();

            if(integer != null){
                if(integer > 0)
                    NewOrderProduct.textView.setText(R.string.neworder_product_textbeforequantity + ": " + integer);
            }
            else NewOrderProduct.textView.setText(R.string.neworder_product_textbeforequantityError);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Exit();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearStack() { // khong su dung duoc
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    void Exit(){
        clearStack();
        Intent intent = new Intent(NewOrderActivity.this, OrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearStack();
        // set null to NewOrderProduct
        NewOrderProduct.textView = null;
        NewOrderProduct.positionProductTemp = -1;
        NewOrderProduct.quantityProductChoose = 0;

        orderShow = null;
        btnNext.getBackground().clearColorFilter();
        Runtime.getRuntime().gc();      //This is the key
    }
}
