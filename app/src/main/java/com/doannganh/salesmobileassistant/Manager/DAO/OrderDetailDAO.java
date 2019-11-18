package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.OrderDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDetailDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static OrderDetailDAO instance;

    private  String urlGet = Server.API_path + "api/Orderdetail/orderdetails";
    private  String urlPost = Server.API_path + "api/OrderDetail";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getTAG = "getOrder";

    private OrderDetailDAO(){}

    public static OrderDetailDAO Instance(Context context)
    {
        if (instance == null){
            instance = new OrderDetailDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return OrderDetailDAO.instance;
    }

    public List<OrderDetail> getListOrder(){
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(urlGet);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLOrderDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray);
    }

    public List<OrderDetail> getListOrder(String myID){
        String url = urlPost + "/" + myID;
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(url);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLOrderDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray);
    }

    public boolean postNewOrderDetail(List<HashMap> hashMap){
        for (HashMap h : hashMap){
            boolean b = ExecMethodHTTP.PostJSONToServer(urlPost,h);
            if(!b) return false;
        }
        return true;
    }

    public int saveListOrderDetailToDB(List<OrderDetail> order){
        int kq = 0;
        for (OrderDetail o : order){
            saveOrderDetailToDB(o);
            kq++;
        }
        return kq;
    }

    public long saveOrderDetailToDB(OrderDetail order){
        db = salesMobileAssistant.getWritableDatabase();
        long numberOfRows = 0;
        db.beginTransaction();

        // kiem tra ton tai
        String kt = "SELECT* FROM " + salesMobileAssistant.TB_ORDERDETAIL + " WHERE " + salesMobileAssistant.TB_ORDERDETAIL_COMPANY
                + " = '" + order.getCompID() + "' AND " + salesMobileAssistant.TB_ORDERDETAIL_MYORDERID
                + " = '" + order.getMyOrderID() + "' AND " + salesMobileAssistant.TB_ORDERDETAIL_PRODUCTID
                + " = '" + order.getProdID() + "'";
        Cursor cKT = db.rawQuery(kt, null);
        if (cKT.getCount() != 0)
            return 0;

        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_ORDERDETAIL_COMPANY, order.getCompID());
            values.put(salesMobileAssistant.TB_ORDERDETAIL_MYORDERID, order.getMyOrderID());
            values.put(salesMobileAssistant.TB_ORDERDETAIL_SITEID, order.getSiteID());
            values.put(salesMobileAssistant.TB_ORDERDETAIL_ORDERNUM, order.getOrdeID());
            values.put(salesMobileAssistant.TB_ORDERDETAIL_ORDERLINE, order.getOrderLine());
            values.put(salesMobileAssistant.TB_ORDERDETAIL_PRODUCTID, order.getProdID());
            values.put(salesMobileAssistant.TB_ORDERDETAIL_QUANTITY, order.getSellingQuantity());
            values.put(salesMobileAssistant.TB_ORDERDETAIL_UNITPRICE, String.valueOf(order.getUnitPrice()));

            numberOfRows = db.insert(salesMobileAssistant.TB_ORDERDETAIL, null, values);
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

    public List<OrderDetail> getListOrderDetailFromDB(String company, String myOrderID){
        List<OrderDetail> list = new ArrayList<>();
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_ORDERDETAIL + " WHERE " +
                salesMobileAssistant.TB_ORDERDETAIL_COMPANY + " = '" + company + "' AND " +
                salesMobileAssistant.TB_ORDERDETAIL_MYORDERID + " = '" + myOrderID + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                list.add(new OrderDetail(cursor));
            }
        }catch (Exception e){
            Log.d("LLLOrderDAOGetListDB", e.getMessage());
        }
        return list;
    }

    private List<OrderDetail> executeJSONArray(JSONArray jsonArray){
        List<OrderDetail> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                OrderDetail order = new OrderDetail(jsonObject);
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
