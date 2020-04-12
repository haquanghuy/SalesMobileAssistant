package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.HttpsTrustManager;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Account;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static AccountDAO instance;
    //private String custURL = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/api/AccountDB/accountdb";
    //private String custURL = "http://192.168.1.136/SalesMobileAssistants/api/AccountDB/accountdb";
    private  String custURL = Server.API_path + "api/AccountDB/accountdb";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getTAG = "getAccount";

    private AccountDAO(){}

    public static AccountDAO Instance(Context context)
    {
        if (instance == null){
            instance = new AccountDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return AccountDAO.instance;
    }

    public Account loginFromDB(Account account) {
        Account re = null;
        db = salesMobileAssistant.getReadableDatabase();
        String reString = null;

        String sql = "SELECT * " + " FROM " + salesMobileAssistant.TB_ACCOUNT
                + " WHERE " + salesMobileAssistant.TB_ACCOUNT_USERNAME + " = '" + account.getUsername() + "'"
                + " AND " + salesMobileAssistant.TB_ACCOUNT_PASSWORD + " = '" + account.getPassword() + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0){
                cursor.moveToFirst();
                re = new Account(cursor);
            }
            cursor.close();
        }catch (Exception e){
            Log.d("LLLAccountDAO", e.getMessage());
        }
        finally {
            db.close();
            return re;
        }
    }

    public Account loginFromAPI(Account account){
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

    public long saveAccountToDB(Account account){
        db = salesMobileAssistant.getWritableDatabase();
        long num = ConstantUtil.DB_CRUD_RESPONSE_EMPTY;
        db.beginTransaction();

        // kiem tra ton tai

        String kt = "SELECT* FROM " + salesMobileAssistant.TB_ACCOUNT + " WHERE "
                + salesMobileAssistant.TB_ACCOUNT_COMPANYID + " = '" + account.getCompany()
                + "' AND " + salesMobileAssistant.TB_ACCOUNT_ID
                + " = " + account.getId() + " AND " + salesMobileAssistant.TB_CUSTOMER_EMPLOYEEID_
                + " = '" + account.getEmplID() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> return routeplan tu db
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_ACCOUNT_USERNAME, account.getUsername());
            values.put(salesMobileAssistant.TB_ACCOUNT_PASSWORD, account.getPassword());
            values.put(salesMobileAssistant.TB_ACCOUNT_TYPE, account.isType());

            if (cKT.getCount() != 0) {
                num = db.update(salesMobileAssistant.TB_ACCOUNT, values, salesMobileAssistant.TB_ACCOUNT_COMPANYID +
                        " = '" + account.getCompany() + "' AND " + salesMobileAssistant.TB_ACCOUNT_ID
                        + " = " + account.getId() + " AND " + salesMobileAssistant.TB_ACCOUNT_EMPLOYEEID
                        + " = " + account.getEmplID(), null);
            }else {
                values.put(salesMobileAssistant.TB_ACCOUNT_COMPANYID, account.getCompany());
                values.put(salesMobileAssistant.TB_ACCOUNT_ID, account.getId());
                values.put(salesMobileAssistant.TB_ACCOUNT_EMPLOYEEID, account.getEmplID());
                num = db.insert(salesMobileAssistant.TB_ACCOUNT, null, values);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex){
            Log.d("LLL"+getTAG, ex.getMessage());
            num = ConstantUtil.DB_CRUD_RESPONSE_ERROR;
        }
        finally {
            cKT.close();
            db.endTransaction();
            return num;
        }
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
