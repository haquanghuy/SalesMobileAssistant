package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.OrderDetailDAO;
import com.doannganh.salesmobileassistant.model.OrderDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDetailPresenter {
    private static OrderDetailPresenter instance;
    OrderDetailDAO orderDetailDAO;
    Context context;

    private OrderDetailPresenter(){

    }

    public static OrderDetailPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new OrderDetailPresenter();
        }

        instance.orderDetailDAO = OrderDetailDAO.Instance(context);
        instance.context = context;
        return OrderDetailPresenter.instance;
    }

    public List<OrderDetail> GetListOrder(){
        try {
            return orderDetailDAO.getListOrder();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OrderDetail> getListOrderDetail(String myID){
        return orderDetailDAO.getListOrderDetail(myID);
    }

    public boolean postNewOrderDetail(List<HashMap> hashMap){
        boolean re = orderDetailDAO.postNewOrderDetail(hashMap);
        if(re) {
            List<OrderDetail> l = new ArrayList<>();
            for(HashMap h : hashMap){
                OrderDetail o = new OrderDetail(h);
                l.add(o);
            }
            orderDetailDAO.saveListOrderDetailToDB(l);
        }
        return re;
    }

    public long saveListOrderDetailToDB(List<OrderDetail> orderDetails){
        return orderDetailDAO.saveListOrderDetailToDB(orderDetails);
    }

    public List<OrderDetail> getListOrderDetailFromDB(String company, String myOrderID){
        return orderDetailDAO.getListOrderDetailFromDB(company, myOrderID);
    }

    public boolean deleteOrderDetailFromDB(String companyID, String myOrderID){
        return orderDetailDAO.deleteOrderDetailFromDB(companyID, myOrderID);
    }
}
