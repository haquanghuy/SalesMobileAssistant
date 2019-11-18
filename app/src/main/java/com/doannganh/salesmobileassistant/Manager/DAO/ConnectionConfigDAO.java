package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.doannganh.salesmobileassistant.model.ConnectionConfig;

public class ConnectionConfigDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static ConnectionConfigDAO instance;
    Context context;

    String getTAG = "getConfig";

    private ConnectionConfigDAO(){}

    public static ConnectionConfigDAO Instance(Context context)
    {
        if (instance == null){
            instance = new ConnectionConfigDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return ConnectionConfigDAO.instance;
    }

    public ConnectionConfig getConfigFromDB(){
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_CONNECTIONCONFIG;
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                ConnectionConfig o = new ConnectionConfig(cursor);
                if(o != null)
                    return o;
            }
        }catch (Exception e){
            Log.d("LLLOrderDAOGetListDB", e.getMessage());
        }
        return null;
    }

    public long saveOrderToDB(ConnectionConfig config){
        db = salesMobileAssistant.getWritableDatabase();
        long numberOfRows = 0;
        db.beginTransaction();

        // kiem tra ton tai
        String kt = "SELECT* FROM " + salesMobileAssistant.TB_CONNECTIONCONFIG + " WHERE " + salesMobileAssistant.TB_CONNECTIONCONFIG_API
                + " = '" + config.getAPI_path() + "'";
                /*"' AND " + salesMobileAssistant.TB_CONNECTIONCONFIG_USER
                + " = '" + config.getUsername() + "' AND " + salesMobileAssistant.TB_CONNECTIONCONFIG_PASS
                + " = '" + config.getPassword() + "' AND " + salesMobileAssistant.TB_CONNECTIONCONFIG_COMPANY
                + " = '" + config.getCompany() + "'";*/
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> chuyen thanh update
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_CONNECTIONCONFIG_API, config.getAPI_path());
            values.put(salesMobileAssistant.TB_CONNECTIONCONFIG_USER, config.getUsername());
            values.put(salesMobileAssistant.TB_CONNECTIONCONFIG_PASS, config.getPassword());
            values.put(salesMobileAssistant.TB_CONNECTIONCONFIG_COMPANY, config.getCompany());

            if (cKT.getCount() != 0)
                numberOfRows = db.update(salesMobileAssistant.TB_CONNECTIONCONFIG, values, salesMobileAssistant.TB_CONNECTIONCONFIG_API +
                        " = '" + config.getAPI_path() + "'", null);
            else {

                deleteAllConfigDB();
                numberOfRows = db.insert(salesMobileAssistant.TB_CONNECTIONCONFIG, null, values);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex){
            Log.d("LLL"+getTAG, ex.getMessage());
        }
        finally {
            db.endTransaction();
            return numberOfRows;
        }
    }

    public boolean deleteAllConfigDB(){
        db = salesMobileAssistant.getWritableDatabase();
        db.beginTransaction();
        int num = 0;

        try {
            //String sql = "DELETE FROM " + quanLyDatabase.TB_PHONGBAN + " WHERE id = " + department.getId();
            num = db.delete(salesMobileAssistant.TB_CONNECTIONCONFIG, "", null);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            db.endTransaction();
            return (num>0);
        }
    }
}
