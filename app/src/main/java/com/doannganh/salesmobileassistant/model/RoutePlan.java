package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class RoutePlan {
    public static final int VISITED = 1;
    public static final int VISITOFFLINE = -1;
    public static final int UNVISIT = 0;


    String CompID;
    String EmplID;
    int CustID;
    String DatePlan;
    int Prioritize;
    int Visited;
    String Note;
    int isSave; // db

    public static String COMPANY = "CompID";
    public static String EMPLID = "EmplID";
    public static String CUSTID = "CustID";
    public static String DATEPLAN = "DatePlan";
    public static String PRIORITIZE = "Prioritize";
    public static String ISVISITED = "Visited";
    public static String NOTE = "Note";

    public RoutePlan(JSONObject jsonObject){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            CompID = jsonObject.getString(COMPANY);
            EmplID = jsonObject.getString(EMPLID);
            CustID = jsonObject.getInt(CUSTID);
            DatePlan = jsonObject.getString(DATEPLAN);
            Prioritize = Integer.parseInt(jsonObject.getString(PRIORITIZE));
            boolean vsit = Boolean.parseBoolean(jsonObject.getString(ISVISITED));
            Visited = vsit ? VISITED : UNVISIT;
            Note = jsonObject.getString(NOTE);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("publicRoutePlanJson", e.getMessage());
        } catch(Exception e){
            Prioritize = 0;
        }
    }

    public RoutePlan(Cursor cursor){
        try {
            CompID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ROUTEPLAN_COMPANY));
            EmplID = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ROUTEPLAN_EMPLOYEEID));
            CustID = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ROUTEPLAN_CUSTOMERID));
            DatePlan = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ROUTEPLAN_DATEPLAN));
            Prioritize = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ROUTEPLAN_PRIORITIZE));
            Visited = cursor.getInt(cursor.getColumnIndex(SalesMobileAssistant.TB_ROUTEPLAN_VISITED));
            Note = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_ROUTEPLAN_NOTE));
        }catch (Exception ex){
            Log.d("LLLRoutePlanCursor", ex.getMessage());
        }
    }

    public String getCompID() {
        return CompID;
    }

    public void setCompID(String compID) {
        CompID = compID;
    }

    public String getEmplID() {
        return EmplID;
    }

    public void setEmplID(String emplID) {
        EmplID = emplID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getDatePlan() {
        return DatePlan;
    }

    public void setDatePlan(String datePlan) {
        DatePlan = datePlan;
    }

    public int getPrioritize() {
        return Prioritize;
    }

    public void setPrioritize(int prioritize) {
        Prioritize = prioritize;
    }

    public int isVisited() {
        return Visited;
    }

    public void setVisited(int visited) {
        Visited = visited;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
