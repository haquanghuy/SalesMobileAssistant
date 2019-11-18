package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

public class ConnectionConfig {

    String API_path, username, password, company;

    public ConnectionConfig(String API_path, String username, String password, String company) {
        this.API_path = API_path;
        this.username = username;
        this.password = password;
        this.company = company;
    }

    public ConnectionConfig(Cursor cursor){
        API_path = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CONNECTIONCONFIG_API));
        username = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CONNECTIONCONFIG_USER));
        password = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CONNECTIONCONFIG_PASS));
        company = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_CONNECTIONCONFIG_COMPANY));

    }

    public String getAPI_path() {
        return API_path;
    }

    public void setAPI_path(String API_path) {
        this.API_path = API_path;
    }

    public String getUsername() {
        return username;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
