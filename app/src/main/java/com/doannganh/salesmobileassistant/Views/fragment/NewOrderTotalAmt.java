package com.doannganh.salesmobileassistant.Views.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.Views.activity.NewOrderActivity;
import com.doannganh.salesmobileassistant.model.Customer;

import java.text.NumberFormat;

public class NewOrderTotalAmt extends Fragment {
    Context context;

    String TAG;
    Customer customer;
    double totalMoneyProduct = 0;
    int tax = 20;
    double total = 0;

    TextView txtTaxPer, txtTaxMon, txtDisPer, txtDisMon, txtTotal;

    public String getTAG() {
        return TAG;
    }

    public static NewOrderTotalAmt myInstance(String tag) {
        NewOrderTotalAmt newOrderTotalAmt = new NewOrderTotalAmt();
        newOrderTotalAmt.TAG = tag;
        return newOrderTotalAmt;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        // tham chieu
        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.fragment_new_order_totalamt, null);
        txtTaxPer = view.findViewById(R.id.txtOrderNewTotalTaxPer);
        txtTaxMon = view.findViewById(R.id.txtOrderNewTotalTaxMoney);
        txtDisPer = view.findViewById(R.id.txtOrderNewTotalDiscountPer);
        txtDisMon = view.findViewById(R.id.txtOrderNewTotalDiscountMoney);
        txtTotal = view.findViewById(R.id.txtOrderNewTotalMoneyTotal);

        GetBundle();
        LoadInfoMoney();


        return view;
    }

    private void GetBundle() {
        Bundle bundle = getArguments();
        if(bundle!=null) {
            customer = (Customer) bundle.getSerializable("F3Customer");
            totalMoneyProduct = bundle.getDouble("TotalMoneyProduct");
        }else Toast.makeText(context, "khong load duoc du lieu", Toast.LENGTH_SHORT).show();

    }

    private void LoadInfoMoney() {
        txtDisPer.setText(customer.getTrueDiscountPercent() + "");
        txtDisMon.setText((totalMoneyProduct * customer.getTrueDiscountPercent() / 100) + "");

        txtTaxPer.setText( tax + "%");
        txtTaxMon.setText((totalMoneyProduct * tax / 100) + "");

        total = totalMoneyProduct - (totalMoneyProduct * customer.getTrueDiscountPercent() / 100);
        total = total + (totalMoneyProduct * tax / 100);
        // total
        NumberFormat format = NumberFormat.getCurrencyInstance();
        txtTotal.setText(format.format(total) + "");
    }
}
