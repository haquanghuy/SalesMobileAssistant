package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OrderDetail {
    String CompID;
    String MyOrderID;
    String SiteID = "MfgSys";
    int OrderLine;
    int OrdeID;
    String ProdID;
    double SellingQuantity;
    double UnitPrice;


    public static final String COMPID = "CompID";
    public static final String SITEID = "SiteID";
    public static final String MYORDERID = "MyOrderID";
    public static final String ORDERLINE = "OrderLine";
    public static final String ORDERID = "OrderNum";
    public static final String UNITPRICE = "UnitPrice";
    public static final String SELLINGQUANTITY = "SellingQuantity";
    public static final String PRODID = "ProdID";

    public OrderDetail(String compID, String myOrderID){
        CompID = compID;
        MyOrderID = myOrderID;
    }

    public OrderDetail(HashMap hashMap){
        CompID = (String) hashMap.get(COMPID);
        SiteID = (String) hashMap.get(SITEID);
        MyOrderID = (String) hashMap.get(MYORDERID);
        OrderLine = (int) hashMap.get(ORDERLINE);
        OrdeID = (int) hashMap.get(ORDERID);
        UnitPrice = (double) hashMap.get(UNITPRICE);
        SellingQuantity = (double) hashMap.get(SELLINGQUANTITY);
        ProdID = (String) hashMap.get(PRODID);
    }

    public OrderDetail(JSONObject jsonObject){
        try {
            CompID = jsonObject.getString(COMPID);
            SiteID = jsonObject.getString(SITEID);
            MyOrderID = jsonObject.getString(MYORDERID);
            ProdID = jsonObject.getString(PRODID);
            SellingQuantity = jsonObject.getInt(SELLINGQUANTITY);
            UnitPrice = jsonObject.getDouble(UNITPRICE);
            OrdeID = jsonObject.getInt(ORDERID);
            OrderLine = jsonObject.getInt(ORDERLINE);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("LLLpublicOrderJson", e.getMessage());
        }
    }

    public OrderDetail(Cursor cursor){
        CompID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDER_COMPANY));
        SiteID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDERDETAIL_SITEID));
        MyOrderID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDERDETAIL_MYORDERID));
        ProdID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDERDETAIL_PRODUCTID));
        SellingQuantity = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDERDETAIL_QUANTITY));
        UnitPrice = cursor.getDouble(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDERDETAIL_UNITPRICE));
        OrdeID = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDERDETAIL_ORDERNUM));
        OrderLine = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ORDERDETAIL_ORDERLINE));
    }

    public void setOrderDetail(int orderLine, String prodID, double sellingQuantity, double unitPrice){
        OrderLine = orderLine;
        ProdID = prodID;
        SellingQuantity = sellingQuantity;
        UnitPrice = unitPrice;
    }

    public String getCompID() {
        return CompID;
    }

    public void setCompID(String compID) {
        CompID = compID;
    }

    public String getMyOrderID() {
        return MyOrderID;
    }

    public void setMyOrderID(String myOrderID) {
        MyOrderID = myOrderID;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public int getOrderLine() {
        return OrderLine;
    }

    public void setOrderLine(int orderLine) {
        OrderLine = orderLine;
    }

    public int getOrdeID() {
        return OrdeID;
    }

    public void setOrdeID(int ordeID) {
        OrdeID = ordeID;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public double getSellingQuantity() {
        return SellingQuantity;
    }

    public void setSellingQuantity(double sellingQuantity) {
        SellingQuantity = sellingQuantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }
}
