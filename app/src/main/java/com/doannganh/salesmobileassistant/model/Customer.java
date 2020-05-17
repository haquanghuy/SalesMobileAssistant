package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Customer implements Serializable {
    String CompID;
    String EmplID;
    int CustID;
    String CustName;
    String Address1;
    String Address2;
    String Address3;
    String City;
    String Country;
    String PhoneNum;
    double TrueDiscountPercent;

    public static final String COMPID = "CompID";
    public static final String EMPLID = "EmplID";
    public static final String CUSTID = "CustID";
    public static final String CUSTNAME = "CustName";
    public static final String ADDRESS1 = "Address1";
    public static final String ADDRESS2 = "Address2";
    public static final String ADDRESS3 = "Address3";
    public static final String CITY = "City";
    public static final String COUNTRY = "Country";
    public static final String PHONENUM = "PhoneNum";
    public static final String DISCOUNT = "Discount";

    public Customer(String compID, String emplID, int custID, String custName,
                    String address1, String address2, String address3, String city,
                    String country, String phoneNum, double trueDiscountPercent) {
        CompID = compID;
        EmplID = emplID;
        CustID = custID;
        CustName = custName;
        Address1 = address1;
        Address2 = address2;
        Address3 = address3;
        City = city;
        Country = country;
        PhoneNum = phoneNum;
        TrueDiscountPercent = trueDiscountPercent;
    }

    public Customer(JSONObject jsonObject){
        try {
            CompID = jsonObject.getString(COMPID);
            EmplID = jsonObject.getString(EMPLID);
            CustID = jsonObject.getInt(CUSTID);
            CustName = jsonObject.getString(CUSTNAME);
            Address1 = jsonObject.getString(ADDRESS1);
            Address2 = jsonObject.getString(ADDRESS2);
            Address3 = jsonObject.getString(ADDRESS3);
            City = jsonObject.getString(CITY);
            Country = jsonObject.getString(COUNTRY);
            PhoneNum = jsonObject.getString(PHONENUM);
            TrueDiscountPercent = jsonObject.getDouble(DISCOUNT);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("LLLpublicCustomerJson", e.getMessage());
        }
    }

    public Customer(Cursor cursor){
        try {
            CompID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_COMPANY_));
            EmplID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_EMPLOYEEID_));
            CustID = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_ID_));
            CustName = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_NAME));
            Address1 = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_ADDRESS1));
            Address2 = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_ADDRESS2));
            Address3 = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_ADDRESS3));
            City = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_CITY));
            Country = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_COUNTRY));
            PhoneNum = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_PHONENUM));
            TrueDiscountPercent = cursor.getDouble(cursor.getColumnIndex(SalesMobileAssistant.TB_CUSTOMER_DISCOUNT));
        }catch (Exception ex){
            Log.d("LLLCustomerCursor", ex.getMessage());
        }
    }

    public String getCompID() {
        return CompID;
    }

    public void setCompID(String compID) {
        CompID = compID;
    }

    public String getEmplID() {
        return EmplID;
    }

    public void setEmplID(String emplID) {
        EmplID = emplID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public double getTrueDiscountPercent() {
        return TrueDiscountPercent;
    }

    public void setTrueDiscountPercent(double trueDiscountPercent) {
        TrueDiscountPercent = trueDiscountPercent;
    }

    public String getAddress3() {
        return Address3;
    }

    public void setAddress3(String address3) {
        Address3 = address3;
    }
}
