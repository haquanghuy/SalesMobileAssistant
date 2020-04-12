package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

import org.json.JSONException;
import org.json.JSONObject;

public class Employee {
    String id, Name;

    public Employee(String name) {
        Name = name;
    }

    public Employee(Cursor cursor){
        try {
            id = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_EMPLOYEE_ID));
            Name = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_EMPLOYEE_NAME));
        }catch (Exception ex){
            Log.d("LLLEmployeeCursor", ex.getMessage());
        }
    }

    public Employee(JSONObject jsonObject){
        try {
            id = jsonObject.getString("EmplID");
            Name = jsonObject.getString("EmplName");
        } catch (JSONException e) {
            Log.d("LLLEmployeeJSON", e.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
