package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.HttpsTrustManager;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountDAO {
    private static AccountDAO instance;
    //private String custURL = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/api/AccountDB/accountdb";
    //private String custURL = "http://192.168.1.136/SalesMobileAssistants/api/AccountDB/accountdb";
    private  String custURL = Server.API_path + "api/AccountDB/accountdb";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getAccount = "getAccount";

    private AccountDAO(){}

    public static AccountDAO Instance(Context context)
    {
        if (instance == null){
            instance = new AccountDAO();
        }

        instance.context = context;
        return AccountDAO.instance;
    }

    public Account Login(Account account){/*
        AsyncTaskGetJSONArray as = new AsyncTaskGetJSONArray();
        try {
            jsonArrayTemp = as.execute(custURL).get();

            return executeJSONArray(jsonArrayTemp, account);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;*/
        HttpsTrustManager.allowAllSSL();
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(custURL);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLAccountDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray, account);
    }

    private Account executeJSONArray(JSONArray jsonArray, Account ac){
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Account account = new Account(jsonObject);
                if (ac.getUsername().equals(account.getUsername())
                && ac.getPassword().equals(account.getPassword())) return account;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("LLLAccountDAO-execJSON", e.getMessage());
                //return null;
            }
        }
        return null;
    }
}
