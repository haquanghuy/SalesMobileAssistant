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
import com.doannganh.salesmobileassistant.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static CustomerDAO instance;
    //private String custURL = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/api/Customer/customers";
    //private String custURL = "http://192.168.1.136/SalesMobileAssistants/api/Customer/customers";
    private  String getURL = Server.API_path + "api/Customer/customers";
    private  String postURL = Server.API_path + "api/Customer";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getTAG = "getCustomers";

    private CustomerDAO(){}

    public static CustomerDAO Instance(Context context)
    {
        if (instance == null){
            instance = new CustomerDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return CustomerDAO.instance;
    }

    public List<Customer> getListCustomersFromDB(String employID){
        List<Customer> list = new ArrayList<>();
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_CUSTOMER
                + " WHERE " + salesMobileAssistant.TB_CUSTOMER_EMPLOYEEID_
                + " = '" + employID + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                list.add(new Customer(cursor));
            }
            cursor.close();
        }catch (Exception e){
            Log.d("LLLCustDAOGetListDB", e.getMessage());
        }
        finally {
            db.close();
            return list;
        }
    }

    public List<Customer> getListCustomers(String employID){
        String url = postURL + "/" + employID;
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(url);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLCustomerDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray);
    }

    private List<Customer> executeJSONArray(JSONArray jsonArray){
        List<Customer> list = new ArrayList<>();

        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Customer customer = new Customer(jsonObject);
                list.add(customer);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("LLLCustDAO-executeJSON", e.getMessage());
                //return null;
            }
        }
        return list;
    }

    public long saveCustomerToDB(Customer product){
        db = salesMobileAssistant.getWritableDatabase();
        long num = ConstantUtil.DB_CRUD_RESPONSE_EMPTY;
        db.beginTransaction();

        // kiem tra ton tai

        String kt = "SELECT* FROM " + salesMobileAssistant.TB_CUSTOMER + " WHERE "
                + salesMobileAssistant.TB_CUSTOMER_COMPANY_ + " = '" + product.getCompID()
                + "' AND " + salesMobileAssistant.TB_CUSTOMER_ID_
                + " = '" + product.getCustID() + "' AND " + salesMobileAssistant.TB_CUSTOMER_EMPLOYEEID_
                + " = '" + product.getEmplID() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> return routeplan tu db
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_CUSTOMER_NAME, product.getCustName());
            values.put(salesMobileAssistant.TB_CUSTOMER_ADDRESS1, product.getAddress1());
            values.put(salesMobileAssistant.TB_CUSTOMER_ADDRESS2, product.getAddress2());
            values.put(salesMobileAssistant.TB_CUSTOMER_ADDRESS3, product.getAddress3());
            values.put(salesMobileAssistant.TB_CUSTOMER_CITY, product.getCity());
            values.put(salesMobileAssistant.TB_CUSTOMER_COUNTRY, product.getCountry());
            values.put(salesMobileAssistant.TB_CUSTOMER_PHONENUM, product.getPhoneNum());
            values.put(salesMobileAssistant.TB_CUSTOMER_DISCOUNT, product.getTrueDiscountPercent());

            if (cKT.getCount() != 0) {
                num = db.update(salesMobileAssistant.TB_CUSTOMER, values, salesMobileAssistant.TB_CUSTOMER_COMPANY_ +
                        " = '" + product.getCompID() + "' AND " + salesMobileAssistant.TB_CUSTOMER_EMPLOYEEID_
                        + " = '" + product.getEmplID() + "' AND " + salesMobileAssistant.TB_CUSTOMER_ID_
                        + " = " + product.getCustID(), null);
            }else {
                values.put(salesMobileAssistant.TB_CUSTOMER_COMPANY_, product.getCompID());
                values.put(salesMobileAssistant.TB_CUSTOMER_EMPLOYEEID_, product.getEmplID());
                values.put(salesMobileAssistant.TB_CUSTOMER_ID_, product.getCustID());
                num = db.insert(salesMobileAssistant.TB_CUSTOMER, null, values);
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
}
