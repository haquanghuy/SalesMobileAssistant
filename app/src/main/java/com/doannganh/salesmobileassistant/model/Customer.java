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
            CompID = jsonObject.getString("CompID");
            EmplID = jsonObject.getString("EmplID");
            CustID = jsonObject.getInt("CustID");
            CustName = jsonObject.getString("CustName");
            Address1 = jsonObject.getString("Address1");
            Address2 = jsonObject.getString("Address2");
            Address3 = jsonObject.getString("Address3");
            City = jsonObject.getString("City");
            Country = jsonObject.getString("Country");
            PhoneNum = jsonObject.getString("PhoneNum");
            TrueDiscountPercent = jsonObject.getDouble("Discount");
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
