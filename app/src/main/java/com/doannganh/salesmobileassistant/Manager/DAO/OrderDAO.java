package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDAO implements Serializable {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static OrderDAO instance;

    private  String urlGet = Server.API_path + "api/Order/orders";
    private  String urlPost = Server.API_path + "api/Order";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getTAG = "getOrder";

    private OrderDAO(){}

    public static OrderDAO Instance(Context context)
    {
        if (instance == null){
            instance = new OrderDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return OrderDAO.instance;
    }

    public List<Order> getListOrderFromAPI(String emplID){
        String url = urlPost + "/" + emplID;
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(url);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLOrderDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray);
    }

    public boolean postNewOrderToAPI(HashMap hashMap){
        return ExecMethodHTTP.PostJSONToServer(urlPost,hashMap);
    }

    public boolean deleteOrderFromAPI(String myID){
        return ExecMethodHTTP.DeleteJSONToServer(urlPost,myID);
    }

    public boolean deleteOrderFromDB(Order order){
        db = salesMobileAssistant.getWritableDatabase();
        db.beginTransaction();
        int num = 0;

        try {
            //String sql = "DELETE FROM " + quanLyDatabase.TB_PHONGBAN + " WHERE id = " + department.getId();
            num = db.delete(salesMobileAssistant.TB_ORDERS, salesMobileAssistant.TB_ORDER_COMPANY +
                    " = '" + order.getCompID() + "' AND " + salesMobileAssistant.TB_ORDER_MYORDERID
                    + " = '" + order.getMyOrderID() + "'", null);
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

    public long saveOrderToDB(Order order){
        db = salesMobileAssistant.getWritableDatabase();
        long numberOfRows = 0;
        db.beginTransaction();

        // kiem tra ton tai
        String kt = "SELECT* FROM " + salesMobileAssistant.TB_ORDERS + " WHERE " + salesMobileAssistant.TB_ORDER_COMPANY
                + " = '" + order.getCompID() + "' AND " + salesMobileAssistant.TB_ORDER_MYORDERID
                + " = '" + order.getMyOrderID() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> chuyen thanh update
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_ORDER_CUSTOMERID, order.getCustID());
            values.put(salesMobileAssistant.TB_ORDER_EMPLOYEEID, order.getEmplID());
            values.put(salesMobileAssistant.TB_ORDER_ORDERDATE, String.valueOf(order.getOrderDate()));
            values.put(salesMobileAssistant.TB_ORDER_NEEDBYDATE, String.valueOf(order.getNeedByDate()));
            values.put(salesMobileAssistant.TB_ORDER_REQUESTDATE, String.valueOf(order.getRequestDate()));
            values.put(salesMobileAssistant.TB_ORDER_ORDERSTATUS, order.getOrderStatus());

            if (cKT.getCount() != 0)
                numberOfRows = db.update(salesMobileAssistant.TB_ORDERS, values, salesMobileAssistant.TB_ORDER_COMPANY +
                        " = '" + order.getCompID() + "' AND " + salesMobileAssistant.TB_ORDER_MYORDERID
                        + " = '" + order.getMyOrderID() + "'", null);
            else {
                values.put(salesMobileAssistant.TB_ORDER_COMPANY, order.getCompID());
                values.put(salesMobileAssistant.TB_ORDER_MYORDERID, order.getMyOrderID());
                values.put(salesMobileAssistant.TB_ORDER_ORDERID, 0);

                numberOfRows = db.insert(salesMobileAssistant.TB_ORDERS, null, values);
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


    public String getOrderIDCurrent(String emplpyID){
        String id1 = "Show id current";
        // load order cuoi cung cua nhan vien nay tu DB
        id1 = getLastIDFromDB(emplpyID);
        String id2 = getLastIDFromAPI(emplpyID);
        String id = FindIDLarge(id1, id2);

        if (id == null) return emplpyID + "00001";
        // tach 4 so cuoi thanh int
        String numStr = id.substring(id.length()-5);
        int num = Integer.parseInt(numStr);
        // cong them 1 cho int
        int numPlus = ++num;
        String idPlus = numPlus + "";
        // thay 4 so int cho 4 so cuoi
        while (numStr.length() != idPlus.length()){
            idPlus = "0" + idPlus;
        }
        return id.replace(numStr, idPlus);
    }

    private String FindIDLarge(String id1, String id2) {
        try {
            if(id1 == null && id2 != null) return id2;
            if(id1 != null && id2 == null) return id1;

            int num1, num2;
            try {
                //if(id1 != null)
                    num1 = Integer.parseInt(id1.substring(id1.length()-5));
            } catch (Exception e) {
                num1 = 0;
            }
            try {
                num2 = Integer.parseInt(id2.substring(id2.length()-5));
            } catch (Exception e) {
                num2 = 0;
            }

            if(num1 ==0 && num2==0) return null;
            if(num1 > num2) return id1;
            return  id2;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getLastIDFromAPI(String emplpyID) {
        String url = urlPost +  "/" + emplpyID;
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(url);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length()-1);
            Order order = new Order(jsonObject);
            return order.getMyOrderID();
        } catch (JSONException e) {
            Log.d("LLLOrderDAO-json", e.getMessage());
        }
        return null;
    }

    private String getLastIDFromDB(String emplpyID){
        db = salesMobileAssistant.getReadableDatabase();
        String sql = "SELECT * " + " FROM " + salesMobileAssistant.TB_ORDERS
                + " WHERE " + salesMobileAssistant.TB_ORDER_EMPLOYEEID + " = '" + emplpyID + "'"
                + " ORDER BY " + salesMobileAssistant.TB_ORDER_MYORDERID + " DESC LIMIT 1";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0){
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()){
                    return cursor.getString(cursor.getColumnIndexOrThrow(salesMobileAssistant.TB_ORDER_MYORDERID));
                }
            }
        }catch (Exception e){
            Log.d("LLLUtilGetLastID", e.getMessage());
        }
        return null;
    }

    public List<Order> getListOrderFromDB(){
        List<Order> list = new ArrayList<>();
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_ORDERS + " WHERE " +
                salesMobileAssistant.TB_ORDER_ORDERSTATUS + " = 1";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                list.add(new Order(cursor));
            }
        }catch (Exception e){
            Log.d("LLLOrderDAOGetListDB", e.getMessage());
        }
        return list;
    }

    public Order getOrderFromDBJob(String dateRoute){
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_ORDERS + " WHERE " +
                salesMobileAssistant.TB_ORDER_ORDERSTATUS + " = 1 AND " +
                salesMobileAssistant.TB_ORDER_REQUESTDATE + " = '" + dateRoute + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                Order o = new Order(cursor);
                if(o != null)
                    return o;
            }
        }catch (Exception e){
            Log.d("LLLOrderDAOGetListDB", e.getMessage());
        }
        return null;
    }

    public boolean setSyncedToCenter(Order order){
        db = salesMobileAssistant.getWritableDatabase();
        db.beginTransaction();

        // kiem tra
        String kt = "SELECT* FROM " + salesMobileAssistant.TB_ORDERS + " WHERE " + salesMobileAssistant.TB_ORDER_COMPANY
                + " = '" + order.getCompID() + "' AND " + salesMobileAssistant.TB_ORDER_MYORDERID
                + " = '" + order.getMyOrderID() + "'";
        Cursor cKT = db.rawQuery(kt, null);
        if (cKT.getCount() == 0)
            return false;

        if(order.getOrderStatus() > 1) return false;

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(salesMobileAssistant.TB_ORDER_ORDERSTATUS, 2);

            int numberOfRows = db.update(salesMobileAssistant.TB_ORDERS, contentValues, salesMobileAssistant.TB_ORDER_COMPANY +
                    " = '" + order.getCompID() + "' AND " + salesMobileAssistant.TB_ORDER_MYORDERID
                    + " = '" + order.getMyOrderID() + "'", null);
            db.setTransactionSuccessful();
            if(numberOfRows > 0) return true;
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            db.endTransaction();
        }
        return false;
    }

    private List<Order> executeJSONArray(JSONArray jsonArray){
        List<Order> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order order = new Order(jsonObject);
                list.add(order);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("OrderDAO-executeJSON", e.getMessage());
                //return null;
            }
        }
        return list;
    }
}
