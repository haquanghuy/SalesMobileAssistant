package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {
    private static CustomerDAO instance;
    //private String custURL = "https://portal.3ssoft.com.vn:2443/SalesMobileAPI/api/Customer/customers";
    //private String custURL = "http://192.168.1.136/SalesMobileAssistants/api/Customer/customers";
    private  String getURL = Server.API_path + "api/Customer/customers";
    private  String postURL = Server.API_path + "api/Customer";
    Context context;

    private volatile JSONArray jsonArrayTemp; // luu lai json khi asynx de goi tiep tuc executeJSON

    String getCustomers = "getCustomers";

    private CustomerDAO(){}

    public static CustomerDAO Instance(Context context)
    {
        if (instance == null){
            instance = new CustomerDAO();
        }

        instance.context = context;
        return CustomerDAO.instance;
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
}
