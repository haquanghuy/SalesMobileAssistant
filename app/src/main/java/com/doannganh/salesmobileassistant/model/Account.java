package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Account implements Serializable {
    public String getUsername() {
        return username;
    }

    public Account(Cursor cursor){
        try {
            company = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ACCOUNT_COMPANYID));
            id = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ACCOUNT_ID));
            username = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ACCOUNT_USERNAME));
            password = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ACCOUNT_PASSWORD));
            type = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ACCOUNT_TYPE))==1?true:false;
            emplID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ACCOUNT_EMPLOYEEID));
        }catch (Exception ex){
            Log.d("LLLRoutePlanCursor", ex.getMessage());
        }
    }

    public Account(JSONObject jsonObject){
        try {
            company = jsonObject.getString("CompID");
            id = Integer.parseInt(jsonObject.getString("ID"));
            username = jsonObject.getString("Username");
            password = jsonObject.getString("Password");
            type = Boolean.parseBoolean(jsonObject.getString("Type"));
            emplID = jsonObject.getString("EmplID");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("publicCustomerJson", e.getMessage());
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public int getId() {
        return id;
    }

    public boolean isType() {
        return type;
    }

    public String getEmplID() {
        return emplID;
    }

    String company;
    int id;
    String username;
    String password;
    boolean type;
    String emplID;
}
