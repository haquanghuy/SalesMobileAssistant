package com.doannganh.salesmobileassistant.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Account implements Serializable {
    public String getUsername() {
        return username;
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
