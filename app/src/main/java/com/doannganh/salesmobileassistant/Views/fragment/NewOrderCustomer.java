package com.doannganh.salesmobileassistant.Views.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.activity.MainActivity;
import com.doannganh.salesmobileassistant.Views.activity.NewOrderActivity;
import com.doannganh.salesmobileassistant.Views.customView.CustomDialogWithListViewCustom;
import com.doannganh.salesmobileassistant.util.UtilFilter;
import com.doannganh.salesmobileassistant.model.Custom_list_item;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewOrderCustomer extends Fragment {
    Context context;

    LinearLayout linearLayout;
    Button btnCustNameID;
    EditText edtOrderID, edtEmplID;
    EditText edtCustAddress1, edtCustAddress2, edtNeedByDate;

    String TAG;
    boolean isReady = false;
    List<Custom_list_item> listComboBox;
    public static Customer customer; // customer click chon
    Order orderS; // show order co san

    public String getTAG() {
        return TAG;
    }

    public static NewOrderCustomer myInstance(String tag) {
        NewOrderCustomer newOrderCustomer = new NewOrderCustomer();
        newOrderCustomer.TAG = tag;
        return newOrderCustomer;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init orderhead
        String myID = NewOrderActivity.myOrderID;

        if (myID != null) {
            isReady = true;
            NewOrderActivity.orderHead = new Order(MainActivity.account.getCompany(), myID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();

        if(NewOrderActivity.orderShow != null){
            orderS = NewOrderActivity.orderShow;
        }

        if (isReady) {
            // tham chieu
            linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_new_order_customer, null);
            AnhXa();

            SetOrderIDCurent();
            GetCustomer();


            EventClickNeedByDate();
            EventClickCustomer();

            return linearLayout;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Too dangerous");
            builder.setMessage(R.string.neworder_getIDCurrent);
            builder.create().show();
            return null;
        }
    }

    private void GetCustomer() {
        if(customer==null) {
            //RoutePlan
            if (orderS == null) {
                Bundle bundle = this.getArguments();
                if (customer == null) /// chi get lan 1
                    customer = (Customer) bundle.getSerializable("F1Customer");

                if (customer != null) {
                    btnCustNameID.setText(customer.getCustName() + " - " + customer.getCustID());
                    btnCustNameID.setEnabled(false);
                    edtCustAddress1.setText(customer.getAddress1());
                    edtCustAddress2.setText(customer.getAddress2());
                }
            }
        } else {
            btnCustNameID.setText(customer.getCustName() + " - " + customer.getCustID());
            edtCustAddress1.setText(customer.getAddress1());
            edtCustAddress2.setText(customer.getAddress2());
        }

    }

    private void EventClickCustomer() {
        if (orderS != null) {
            //customer = MainActivity.listCustomer.get(positionSelect);
            customer = UtilFilter.getCustomerByID(MainActivity.listCustomer, orderS.getCustID());

            if (customer == null) {
                Toast.makeText(context, R.string.neworder_invalid_customer, Toast.LENGTH_SHORT).show();
                return;
            }
            btnCustNameID.setText(customer.getCustName() + " - " + customer.getCustID());
            edtCustAddress1.setText(customer.getAddress1());
            edtCustAddress2.setText(customer.getAddress2());
            return;
        }
        // tao list combobox
        listComboBox = new ArrayList<>();

        for (Customer c : MainActivity.listCustomer){
            listComboBox.add(new Custom_list_item(c.getCustName(), c.getCustID()+"", ""));
        }

        btnCustNameID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogWithListViewCustom customDialog = new CustomDialogWithListViewCustom(
                        getActivity(), listComboBox, btnCustNameID.getId());
                customDialog.show();
/*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (customer==null){}
                            edtCustAddress1.setText(customer.getAddress1());
                            edtCustAddress2.setText(customer.getAddress2());
                        } catch (Exception e) {
                            Log.d("LLLSleepWaitForChoose", e.getMessage());
                        }
                    }
                });*/
            }
        });
    }

    private void EventClickNeedByDate() {/*
        edtNeedByDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new DatePickerFragment() {
                        @Override
                        public EditText SetClickView() {
                            return edtNeedByDate;
                        }
                    };
                    newFragment.show(getFragmentManager(),"timePicker");
                }

            }
        });*/

        edtNeedByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"timePicker");
            }
        });
    }

    private void SetOrderIDCurent() {
        if(orderS == null){
            edtOrderID.setText(NewOrderActivity.orderHead.getMyOrderID());

            edtEmplID.setText(MainActivity.account.getEmplID());
        }
        else {
            edtOrderID.setText(orderS.getMyOrderID());
            edtEmplID.setText(orderS.getEmplID());

            // set needbydate
            if (orderS.getNeedByDate() != null){
                SimpleDateFormat sp = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    edtNeedByDate.setText(sp.format(sp.parse(orderS.getNeedByDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(orderS.getOrderStatus() > 1)
                    edtNeedByDate.setEnabled(false);
            }
        }

        edtOrderID.setEnabled(false);
        edtEmplID.setEnabled(false);
    }

    private void AnhXa() {

        edtOrderID = linearLayout.findViewById(R.id.edtOrdersNewOrderID);
        edtEmplID = linearLayout.findViewById(R.id.edtOrdersNewEmployeeID);
        btnCustNameID = linearLayout.findViewById(R.id.btnOrdersNewCustomerNameID);
        edtCustAddress1 = linearLayout.findViewById(R.id.edtOrdersNewCustomerAddress1);
        edtCustAddress2 = linearLayout.findViewById(R.id.edtOrdersNewCustomerAddress2);
        edtNeedByDate = linearLayout.findViewById(R.id.edtOrdersNewNeedByDate);

    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            String dateEnd = "T00:00:00";
            String needByDate="";

            // save
            String emplID = edtEmplID.getText()+"";
            int custID = customer.getCustID();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String orderDate = df.format(Calendar.getInstance().getTime()) + dateEnd;
            if (!edtNeedByDate.getText().toString().equals(""))
                needByDate = edtNeedByDate.getText().toString() + dateEnd;
            int orderStatus = 1;

            NewOrderActivity.orderHead.setOrderHead(custID, emplID, orderDate, needByDate, orderStatus);
        } catch (Exception e) {
            Log.d("LLLFragmentCustSaeOHead", e.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customer = null;
    }
}

