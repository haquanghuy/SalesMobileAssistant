package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Product;
import com.doannganh.salesmobileassistant.model.ProductInSite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductInSiteDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static ProductInSiteDAO instance;
    private String urlGet = Server.API_path + "api/Prodcutinsite/prodcutinsites";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getTAG = "getProductInSite";

    private ProductInSiteDAO(){}

    public static ProductInSiteDAO Instance(Context context)
    {
        if (instance == null){
            instance = new ProductInSiteDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return ProductInSiteDAO.instance;
    }

    public List<ProductInSite> getListProductInSite(){
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(urlGet);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLProductISDAO-json", e.getMessage());
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
            Log.d("LLLProdISDAOGetQuanDB", e.getMessage());
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
            Log.d("LLLProductISDAO-json", e.getMessage());
        }
        int inAPI = executeJSONArrayPartWhse(jsonArray, productID);
        return inAPI - inDB;
    }

    private List<ProductInSite> executeJSONArray(JSONArray jsonArray){
        List<ProductInSite> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProductInSite product = new ProductInSite(jsonObject);
                list.add(product);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("LLLProdISDAO-execJSON", e.getMessage());
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
                Log.d("LLLProdISDAO-execJSON", e.getMessage());
                //return null;
            }
        }
        return -1;
    }
}
