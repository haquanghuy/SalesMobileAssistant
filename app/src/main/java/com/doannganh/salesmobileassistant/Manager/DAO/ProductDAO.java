package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static ProductDAO instance;
    //private String custURL = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/api/Product/products";
    //private String custURL = "http://192.168.1.136/SalesMobileAssistants/api/Product/products";
    private String custURL = Server.API_path + "api/Product/products";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getProduct = "getProduct";

    private ProductDAO(){}

    public static ProductDAO Instance(Context context)
    {
        if (instance == null){
            instance = new ProductDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return ProductDAO.instance;
    }

    public List<Product> getListProduct(){
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(custURL);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLProductDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray);
    }

    public int getQuantityDB(String productID){
        String sql = "select sum(d.SellingQuantity) as Quantity" +
            " from OrderDetail d join Orders o on d.MyOrderID = o.MyOrderID" +
            " where ProdID = '" + productID + "' and OrderStatus < 3";
        db = salesMobileAssistant.getReadableDatabase();
        int quan = 0;

        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                quan = cursor.getInt(0);
            }
        }catch (Exception e){
            Log.d("LLLProductDAOGetQuanDB", e.getMessage());
        }
        return quan;
    }

    public int getQuantityCurent(String productID){
        int inDB = getQuantityDB(productID);

        String urlGetQuan = Server.API_path + "api/PartWhse/partWhse";
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(urlGetQuan);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLProductDAO-json", e.getMessage());
        }
        int inAPI = executeJSONArrayPartWhse(jsonArray, productID);
        return inAPI - inDB;
    }

    private List<Product> executeJSONArray(JSONArray jsonArray){
        List<Product> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Product product = new Product(jsonObject);
                list.add(product);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("LLLProductDAO-execJSON", e.getMessage());
                //return null;
            }
        }
        return list;
    }

    private int executeJSONArrayPartWhse(JSONArray jsonArray, String productID){
        List<Product> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String qu = jsonObject.getString("PartWhse_PartNum");
                if(qu.equals(productID))
                    return jsonObject.getInt("Calculated_Available");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("LLLProductDAO-execJSON", e.getMessage());
                //return null;
            }
        }
        return -1;
    }
}
