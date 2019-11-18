package com.doannganh.salesmobileassistant.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class RoutePlan {
    String CompID;
    String EmplID;
    int CustID;
    String DatePlan;
    int Prioritize;
    boolean Visited;
    String Note;

    public static String COMPANY = "CompID";
    public static String EMPLID = "EmplID";
    public static String CUSTID = "CustID";
    public static String DATEPLAN = "DatePlan";
    public static String PRIORITIZE = "Prioritize";
    public static String VISITED = "Visited";
    public static String NOTE = "Note";

    public RoutePlan(JSONObject jsonObject){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            CompID = jsonObject.getString(COMPANY);
            EmplID = jsonObject.getString(EMPLID);
            CustID = jsonObject.getInt(CUSTID);
            DatePlan = jsonObject.getString(DATEPLAN);
            Prioritize = Integer.parseInt(jsonObject.getString(PRIORITIZE));
            Visited = Boolean.parseBoolean(jsonObject.getString(VISITED));
            Note = jsonObject.getString(NOTE);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("publicRoutePlanJson", e.getMessage());
        } catch(Exception e){
            Prioritize = 0;
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

    public boolean isVisited() {
        return Visited;
    }

    public void setVisited(boolean visited) {
        Visited = visited;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
