package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.RoutePlanDAO;
import com.doannganh.salesmobileassistant.model.RoutePlan;

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

    public List<RoutePlan> GetListRoutePlan(String employID){
        try {
            return routePlanDAO.getListRoutePlan(employID);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setMadeOrder(RoutePlan routePlan){
        return routePlanDAO.setMadeOrder(routePlan);
    }
}
