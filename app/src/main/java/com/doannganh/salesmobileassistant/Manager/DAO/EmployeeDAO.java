package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Account;
import com.doannganh.salesmobileassistant.model.Employee;
import com.doannganh.salesmobileassistant.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
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
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
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

    public Employee getEmployeeFromDB(String id){
        Employee re = null;
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT * " + " FROM " + salesMobileAssistant.TB_EMPLOYEE
                + " WHERE " + salesMobileAssistant.TB_EMPLOYEE_ID + " = '" + id + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0){
                cursor.moveToFirst();
                re = new Employee(cursor);
            }
            cursor.close();
        }catch (Exception e){
            Log.d("LLLEmployeeDAO", e.getMessage());
        }
        finally {
            db.close();
            return re;
        }
    }

    public long saveEmployeeToDB(Employee employee){
        db = salesMobileAssistant.getWritableDatabase();
        long num = ConstantUtil.DB_CRUD_RESPONSE_EMPTY;
        db.beginTransaction();

        // kiem tra ton tai

        String kt = "SELECT* FROM " + salesMobileAssistant.TB_EMPLOYEE + " WHERE "
                + salesMobileAssistant.TB_EMPLOYEE_ID + " = '" + employee.getId()
                + "' AND " + salesMobileAssistant.TB_EMPLOYEE_NAME
                + " = '" + employee.getName() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> return routeplan tu db
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_EMPLOYEE_ID, employee.getId());
            values.put(salesMobileAssistant.TB_EMPLOYEE_NAME, employee.getName());

            if (cKT.getCount() != 0) {
                num = ConstantUtil.DB_CRUD_RESPONSE_EMPTY;
            }else {
                num = db.insert(salesMobileAssistant.TB_EMPLOYEE, null, values);
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
