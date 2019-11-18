package com.doannganh.salesmobileassistant.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Employee {
    String id, Name;

    public Employee(String name) {
        Name = name;
    }

    public Employee(JSONObject jsonObject){
        try {
            id = jsonObject.getString("EmplID");
            Name = jsonObject.getString("EmplName");
        } catch (JSONException e) {
            Log.d("LLLEmployeeJSON", e.getMessage());
        }
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
