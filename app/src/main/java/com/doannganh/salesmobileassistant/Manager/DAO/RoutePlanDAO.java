package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.RoutePlan;
import com.doannganh.salesmobileassistant.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RoutePlanDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;

    private static RoutePlanDAO instance;
    private String urlGet = Server.API_path + "api/RoutePlan/routeplans";
    private String urlPost = Server.API_path + "api/RoutePlan";
    Context context;


    String getTAG = "getRoutePlan";

    private RoutePlanDAO(){}

    public static RoutePlanDAO Instance(Context context)
    {
        if (instance == null){
            instance = new RoutePlanDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return RoutePlanDAO.instance;
    }

    public List<RoutePlan> getListRoutePlan(String employID){
        String url = urlPost + "/" + employID;
        String content = ExecMethodHTTP.docNoiDung_Tu_URL(url);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
        } catch (JSONException e) {
            Log.d("LLLRPlanDAO-json", e.getMessage());
        }
        return executeJSONArray(jsonArray);
    }

    public List<RoutePlan> getListRoutePlanFromDB(String employID, int cusID, String datePlan){
        List<RoutePlan> list = new ArrayList<>();
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_ROUTEPLAN
                + " WHERE " + salesMobileAssistant.TB_ROUTEPLAN_EMPLOYEEID
                + " = '" + employID + "' AND " + salesMobileAssistant.TB_ROUTEPLAN_CUSTOMERID
                + " = " + cusID + " AND " + salesMobileAssistant.TB_ROUTEPLAN_DATEPLAN
                + " = '" + datePlan + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                list.add(new RoutePlan(cursor));
            }
            cursor.close();
        }catch (Exception e){
            Log.d("LLLRPlanDAOGetListDB", e.getMessage());
        }
        finally {
            db.close();
            return list;
        }
    }

    public List<RoutePlan> getListRoutePlanFromDB(String employID){
        List<RoutePlan> list = new ArrayList<>();
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT* FROM " + salesMobileAssistant.TB_ROUTEPLAN
                + " WHERE " + salesMobileAssistant.TB_ROUTEPLAN_EMPLOYEEID
                + " = '" + employID + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                list.add(new RoutePlan(cursor));
            }
            cursor.close();
        }catch (Exception e){
            Log.d("LLLRPlanDAOGetListDB", e.getMessage());
        }
        finally {
            db.close();
            return list;
        }
    }

    public RoutePlan saveRoutePlanToDB(RoutePlan routePlan){
        db = salesMobileAssistant.getWritableDatabase();
        // neu da ton tai thi uu tien DB
        RoutePlan routeReturn = null;
        db.beginTransaction();

        // kiem tra ton tai

        String kt = "SELECT* FROM " + salesMobileAssistant.TB_ROUTEPLAN + " WHERE " + salesMobileAssistant.TB_ROUTEPLAN_COMPANY
                + " = '" + routePlan.getCompID() + "' AND " + salesMobileAssistant.TB_ROUTEPLAN_EMPLOYEEID
                + " = '" + routePlan.getEmplID() + "' AND " + salesMobileAssistant.TB_ROUTEPLAN_CUSTOMERID
                + " = " + routePlan.getCustID() + " AND " + salesMobileAssistant.TB_ROUTEPLAN_DATEPLAN
                + " = '" + routePlan.getDatePlan() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> return routeplan tu db
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_ROUTEPLAN_VISITED, routePlan.isVisited());
            values.put(salesMobileAssistant.TB_ROUTEPLAN_PRIORITIZE, routePlan.getPrioritize());
            values.put(salesMobileAssistant.TB_ROUTEPLAN_NOTE, routePlan.getNote());

            if (cKT.getCount() != 0) {
                cKT.moveToFirst();
                routeReturn = new RoutePlan(cKT);
            }else {
                values.put(salesMobileAssistant.TB_ROUTEPLAN_COMPANY, routePlan.getCompID());
                values.put(salesMobileAssistant.TB_ROUTEPLAN_EMPLOYEEID, routePlan.getEmplID());
                values.put(salesMobileAssistant.TB_ROUTEPLAN_CUSTOMERID, routePlan.getCustID());
                values.put(salesMobileAssistant.TB_ROUTEPLAN_DATEPLAN, routePlan.getDatePlan());
                db.insert(salesMobileAssistant.TB_ROUTEPLAN, null, values);
                routeReturn = routePlan;
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex){
            Log.d("LLL"+getTAG, ex.getMessage());
        }
        finally {
            cKT.close();
            db.endTransaction();
            return routeReturn;
        }
    }


    public long setRoutePlanToDB(RoutePlan routePlan){
        db = salesMobileAssistant.getWritableDatabase();
        long numberOfRows = ConstantUtil.DB_CRUD_RESPONSE_EMPTY;
        db.beginTransaction();

        // kiem tra ton tai
        String kt = "SELECT* FROM " + salesMobileAssistant.TB_ROUTEPLAN + " WHERE " + salesMobileAssistant.TB_ROUTEPLAN_COMPANY
                + " = '" + routePlan.getCompID() + "' AND " + salesMobileAssistant.TB_ROUTEPLAN_EMPLOYEEID
                + " = '" + routePlan.getEmplID() + "' AND " + salesMobileAssistant.TB_ROUTEPLAN_CUSTOMERID
                + " = " + routePlan.getCustID() + " AND " + salesMobileAssistant.TB_ROUTEPLAN_DATEPLAN
                + " = '" + routePlan.getDatePlan() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> update else return -1
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_ROUTEPLAN_VISITED, routePlan.isVisited());
            values.put(salesMobileAssistant.TB_ROUTEPLAN_PRIORITIZE, routePlan.getPrioritize());
            values.put(salesMobileAssistant.TB_ROUTEPLAN_NOTE, routePlan.getNote());

            if (cKT.getCount() != 0)
                numberOfRows = db.update(salesMobileAssistant.TB_ROUTEPLAN, values
                        , salesMobileAssistant.TB_ROUTEPLAN_COMPANY +
                                " = '" + routePlan.getCompID() + "' AND " + salesMobileAssistant.TB_ROUTEPLAN_EMPLOYEEID
                                + " = '" + routePlan.getEmplID() + "' AND " + salesMobileAssistant.TB_ROUTEPLAN_CUSTOMERID
                                + " = " + routePlan.getCustID() + " AND " + salesMobileAssistant.TB_ROUTEPLAN_DATEPLAN
                                + " = '" + routePlan.getDatePlan() + "'", null);
            else {
                numberOfRows = -1;
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex){
            Log.d("LLL"+getTAG, ex.getMessage());
            numberOfRows = ConstantUtil.DB_CRUD_RESPONSE_ERROR;
        }
        finally {
            db.endTransaction();
            return numberOfRows;
        }
    }

    public boolean setMadeOrder(RoutePlan routePlan){HashMap hashMap = new HashMap();
        hashMap.put(RoutePlan.COMPANY, routePlan.getCompID());
        hashMap.put(RoutePlan.EMPLID, routePlan.getEmplID());
        hashMap.put(RoutePlan.CUSTID, routePlan.getCustID());
        hashMap.put(RoutePlan.DATEPLAN, routePlan.getDatePlan());
        hashMap.put(RoutePlan.PRIORITIZE, routePlan.getPrioritize());
        hashMap.put(RoutePlan.ISVISITED, true);
        hashMap.put(RoutePlan.NOTE, routePlan.getNote());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = df.parse( routePlan.getDatePlan());
            String url = urlPost + "/" + routePlan.getCompID() + "," + routePlan.getEmplID()
                    + "," + routePlan.getCustID() + "," + df.format(d);
            return ExecMethodHTTP.putJSONToServer(url, hashMap);
        } catch (ParseException e) {
            Log.d("LLLRoutePlanPutDate", e.getMessage());
        }
        return false;
    }

    private List<RoutePlan> executeJSONArray(JSONArray jsonArray){
        List<RoutePlan> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RoutePlan routePlan = new RoutePlan(jsonObject);
                list.add(routePlan);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("LLLRoPlanDAO-execJSON", e.getMessage());
                //return null;
            }
        }
        return list;
    }
}
