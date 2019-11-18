package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.RoutePlan;

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

    public boolean setMadeOrder(RoutePlan routePlan){
        if(routePlan.isVisited()) return true;

        HashMap hashMap = new HashMap();
        hashMap.put(RoutePlan.COMPANY, routePlan.getCompID());
        hashMap.put(RoutePlan.EMPLID, routePlan.getEmplID());
        hashMap.put(RoutePlan.CUSTID, routePlan.getCustID());
        hashMap.put(RoutePlan.DATEPLAN, routePlan.getDatePlan());
        hashMap.put(RoutePlan.PRIORITIZE, routePlan.getPrioritize());
        hashMap.put(RoutePlan.VISITED, true);
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
