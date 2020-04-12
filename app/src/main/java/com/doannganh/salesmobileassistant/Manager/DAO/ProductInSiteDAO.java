package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.Product;
import com.doannganh.salesmobileassistant.model.ProductInSite;
import com.doannganh.salesmobileassistant.util.ConstantUtil;

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

    public List<ProductInSite> getListProductInSiteFromDB(){
        List<ProductInSite> list = new ArrayList<>();
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_PRODUCTINSITE;
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                list.add(new ProductInSite(cursor));
            }
            cursor.close();
        }catch (Exception e){
            Log.d("LLLProISiteDAOGetListDB", e.getMessage());
        }
        finally {
            db.close();
            return list;
        }
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

    public long saveProductInSiteToDB(ProductInSite product){
        db = salesMobileAssistant.getWritableDatabase();
        long num = ConstantUtil.DB_CRUD_RESPONSE_EMPTY;
        db.beginTransaction();

        // kiem tra ton tai

        String kt = "SELECT* FROM " + salesMobileAssistant.TB_PRODUCTINSITE + " WHERE "
                + salesMobileAssistant.TB_PRODUCTINSITE_COMPANY + " = '" + product.getCompany()
                + "' AND " + salesMobileAssistant.TB_PRODUCTINSITE_SITEID
                + " = '" + product.getSiteID() + "' AND " + salesMobileAssistant.TB_PRODUCTINSITE_PRODUCTID
                + " = '" + product.getProdID() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> return routeplan tu db
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_PRODUCTINSITE_QUANTITY, product.getQuantity());

            if (cKT.getCount() != 0) {
                cKT.moveToFirst();
                num = db.update(salesMobileAssistant.TB_PRODUCT, values, salesMobileAssistant.TB_PRODUCTINSITE_COMPANY +
                        " = '" + product.getCompany() + "' AND " + salesMobileAssistant.TB_PRODUCTINSITE_SITEID
                        + " = '" + product.getSiteID() + "' AND " + salesMobileAssistant.TB_PRODUCTINSITE_PRODUCTID
                        + " = '" + product.getProdID() + "'", null);
            }else {
                values.put(salesMobileAssistant.TB_PRODUCTINSITE_COMPANY, product.getCompany());
                values.put(salesMobileAssistant.TB_PRODUCTINSITE_SITEID, product.getSiteID());
                values.put(salesMobileAssistant.TB_PRODUCTINSITE_PRODUCTID, product.getProdID());
                num = db.insert(salesMobileAssistant.TB_PRODUCTINSITE, null, values);
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
