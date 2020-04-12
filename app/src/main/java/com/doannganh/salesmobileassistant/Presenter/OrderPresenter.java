package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.OrderDAO;
import com.doannganh.salesmobileassistant.model.Order;

import java.util.HashMap;
import java.util.List;

public class OrderPresenter {
    private static OrderPresenter instance;
    OrderDAO orderDAO;
    Context context;

    private OrderPresenter(){

    }

    public static OrderPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new OrderPresenter();
        }

        instance.orderDAO = OrderDAO.Instance(context);
        instance.context = context;
        return OrderPresenter.instance;
    }

    public List<Order> getListOrderFromAPI(String emplID){
        try {
            return orderDAO.getListOrderFromAPI(emplID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean postNewOrderToAPI(HashMap hashMap){
        boolean re = orderDAO.postNewOrderToAPI(hashMap);
        if(re) {
            Order o = new Order(hashMap);
            orderDAO.saveOrderToDB(o);
        }
        return re;
    }

    public long saveOrderToDB(Order order){
        return orderDAO.saveOrderToDB(order);
    }

    public boolean deleteOrderFromAPI(String myID){
        return orderDAO.deleteOrderFromAPI(myID);
    }

    public String getOrderIDCurrent(String emplpyID){
        return orderDAO.getOrderIDCurrent(emplpyID);
    }

    public List<Order> getListOrderFromDB(String emplID, int[] status){
        return orderDAO.getListOrderFromDB(emplID, status);
    }

    public boolean setSyncedToCenter(Order order){
        return orderDAO.setSyncedToCenter(order);
    }

    public boolean deleteOrderFromDB(Order order){
        return orderDAO.deleteOrderFromDB(order);
    }

    public Order getOrderFromDBJob(int cusID, String dateRoute){
        return orderDAO.getOrderFromDBJob(cusID, dateRoute);
    }
}
