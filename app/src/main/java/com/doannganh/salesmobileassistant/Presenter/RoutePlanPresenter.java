package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.DAO.RoutePlanDAO;
import com.doannganh.salesmobileassistant.util.UtilFilter;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.MyJob;
import com.doannganh.salesmobileassistant.model.RoutePlan;

import java.util.ArrayList;
import java.util.List;

public class RoutePlanPresenter {
    private static RoutePlanPresenter instance;
    RoutePlanDAO routePlanDAO;
    Context context;

    private RoutePlanPresenter(){

    }

    public static RoutePlanPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new RoutePlanPresenter();
        }

        instance.routePlanDAO = RoutePlanDAO.Instance(context);
        instance.context = context;
        return RoutePlanPresenter.instance;
    }

    public List<RoutePlan> getListRoutePlanFromDB(String employID){
        try {
            return routePlanDAO.getListRoutePlanFromDB(employID);
        } catch (Exception e) {
            return null;
        }
    }

    public List<RoutePlan> getListRoutePlan(String employID){
        try {
            return routePlanDAO.getListRoutePlan(employID);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setMadeOrder(RoutePlan routePlan){
        return routePlanDAO.setMadeOrder(routePlan);
    }

    /*
     * Save route plan. found return routeplan in db else return input
     */
    public RoutePlan saveRoutePlanToDB(RoutePlan routePlan){
        return routePlanDAO.saveRoutePlanToDB(routePlan);
    }

    /*
    * Update route plan. Not found return -1
    */
    public long setRoutePlanToDB(RoutePlan routePlan){
        return routePlanDAO.setRoutePlanToDB(routePlan);
    }

    public List<RoutePlan> getListRoutePlanFromDB(String employID, int cusID, String datePlan){
        return routePlanDAO.getListRoutePlanFromDB(employID, cusID, datePlan);
    }

    /**
     * get MyJob list from RoutePlan list
     */
    public List<MyJob> getListMyJob(List<Customer> listCuss, String employID, int cusID, String datePlan){
        List<MyJob> listJob = new ArrayList<>();
        List<RoutePlan> l = getListRoutePlanFromDB(employID, cusID, datePlan);

        for (RoutePlan r : l) {
            Customer c = UtilFilter.getCustomerByID(listCuss, r.getCustID());
            if (c == null)
                Log.d("RoutePlanPresenter", "Can't determined this customer: " + r.getCustID());
            else {
                listJob.add(new MyJob(r, c.getCustName(), c.getAddress1() + " " + c.getAddress2()));
            }

        }
        return listJob;
    }
}
