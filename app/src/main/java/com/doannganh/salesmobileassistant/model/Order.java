package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Order implements Serializable {
    String CompID;
    int OrdeID;
    String MyOrderID;
    int CustID;
    String EmplID;
    int OrderStatus;

    String OrderDate;
    String NeedByDate;
    String RequestDate;

    double DocTotalCharges;
    double TrueDiscountPercent;
    double DocExtPriceDtl;
    double DocTotalTax;
    double DocOrderAmt;



    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

    public static final String sampleDate = "2019-09-11T00:00:00";

    public static final String COMPID = "CompID";
    public static final String EMPLID = "EmplID";
    public static final String CUSTID = "CustID";
    public static final String MYORDERID = "MyOrderID";
    public static final String ORDEID = "OrdeID";
    public static final String ORDERDATE = "OrderDate";
    public static final String NEEDBYDATE = "NeedByDate";
    public static final String REQUESTDATE = "RequestDate";
    public static final String ORDERSTATUS = "OrderStatus";

    public Order(String compID, String myID) {
        CompID = compID;
        MyOrderID = myID;
    }


    public Order(HashMap hashMap){
        CompID = (String) hashMap.get(COMPID);
        EmplID = (String) hashMap.get(EMPLID);
        CustID = (int) hashMap.get(CUSTID);
        MyOrderID = (String) hashMap.get(MYORDERID);
        OrdeID = (int) hashMap.get(ORDEID);
        OrderDate = (String) hashMap.get(ORDERDATE);
        NeedByDate = (String) hashMap.get(NEEDBYDATE);
        RequestDate = (String) hashMap.get(REQUESTDATE);
        OrderStatus = (int) hashMap.get(ORDERSTATUS);
    }

    public Order(int ordeID, int custID,String myOrderID, String emplID, String orderDate, String needByDate,
                 String requestDate, int orderStatus, double docTotalCharges, double trueDiscountPercent,
                 double docExtPriceDtl, double docTotalTax, double docOrderAmt) {
        OrdeID = ordeID;
        CustID = custID;
        MyOrderID = myOrderID;
        EmplID = emplID;
        OrderDate = orderDate;
        NeedByDate = needByDate;
        RequestDate = requestDate;
        OrderStatus = orderStatus;
        DocTotalCharges = docTotalCharges;
        TrueDiscountPercent = trueDiscountPercent;
        DocExtPriceDtl = docExtPriceDtl;
        DocTotalTax = docTotalTax;
        DocOrderAmt = docOrderAmt;
    }

    public Order(JSONObject jsonObject){
        try {
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            CompID = jsonObject.getString(COMPID);
            EmplID = jsonObject.getString(EMPLID);
            CustID = jsonObject.getInt(CUSTID);
            MyOrderID = jsonObject.getString(MYORDERID);
            OrdeID = Integer.parseInt(jsonObject.getString(ORDEID));

                OrderDate = jsonObject.getString(ORDERDATE);
                NeedByDate = jsonObject.getString(NEEDBYDATE);
                RequestDate = jsonObject.getString(REQUESTDATE);
            OrderStatus = Integer.parseInt(jsonObject.getString(ORDERSTATUS));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("LLLpublicOrderJson", e.getMessage());
        }

    }

    public Order(Cursor cursor){
        CompID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_COMPANY));
        MyOrderID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_MYORDERID));
        OrdeID = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_ORDERID));
        CustID = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_CUSTOMERID));
        EmplID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_EMPLOYEEID));
        OrderDate = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_ORDERDATE));
        NeedByDate = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_NEEDBYDATE));
        RequestDate = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_REQUESTDATE));
        OrderStatus = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_ORDERSTATUS));
    }

    public void setOrderHead(int custID, String emplID, String orderDate,
                             String needByDate, int orderStatus){
        CustID = custID;
        EmplID = emplID;
        OrderDate = orderDate;
        NeedByDate = needByDate;
        OrderStatus = orderStatus;
    }

    public void setOrderTotalAmt(double docTotalCharges, double trueDiscountPercent,
                                 double docExtPriceDtl, double docTotalTax, double docOrderAmt){
        DocTotalCharges = docTotalCharges;
        TrueDiscountPercent = trueDiscountPercent;
        DocExtPriceDtl = docExtPriceDtl;
        DocTotalTax = docTotalTax;
        DocOrderAmt = docOrderAmt;
    }

    public String getCompID() {
        return CompID;
    }

    public void setCompID(String compID) {
        CompID = compID;
    }

    public int getOrdeID() {
        return OrdeID;
    }

    public void setOrdeID(int ordeID) {
        OrdeID = ordeID;
    }

    public String getMyOrderID() {
        return MyOrderID;
    }

    public void setMyOrderID(String myOrderID) {
        MyOrderID = myOrderID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getEmplID() {
        return EmplID;
    }

    public void setEmplID(String emplID) {
        EmplID = emplID;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getNeedByDate() {
        return NeedByDate;
    }

    public void setNeedByDate(String needByDate) {
        NeedByDate = needByDate;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }
}
