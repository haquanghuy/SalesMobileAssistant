package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeDAO {
    private static EmployeeDAO instance;
    private  String custURL = Server.API_path + "api/Employee";
    Context context;

    String getTAG = "getEmployee";

    private EmployeeDAO(){}

    public static EmployeeDAO Instance(Context context)
    {
        if (instance == null){
            instance = new EmployeeDAO();
        }

        instance.context = context;
        return EmployeeDAO.instance;
    }

    public Employee getEmployee(String id){
        String url = custURL + "/" + id;
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(url);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLCustomerDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray);
    }

    private Employee executeJSONArray(JSONArray jsonArray){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return new Employee(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("LLLCustDAO-executeJSON", e.getMessage());
                //return null;
            }
        return null;
    }
}
