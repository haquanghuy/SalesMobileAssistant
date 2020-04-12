package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.CustomerDAO;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.Product;

import java.util.List;

public class CustomerPresenter {
    private static CustomerPresenter instance;
    CustomerDAO customerDAO;
    Context context;

    private CustomerPresenter(){

    }

    public static CustomerPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new CustomerPresenter();
        }

        instance.customerDAO = CustomerDAO.Instance(context);
        instance.context = context;
        return CustomerPresenter.instance;
    }

    public List<Customer> GetListCustomer(String employID){
        try {
            return customerDAO.getListCustomers(employID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Customer> getListCustomerFromDB(String employID){
        try {
            return customerDAO.getListCustomersFromDB(employID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long saveCustomerToDB(List<Customer> products){
        long num = 0;
        try {
            for(Customer product : products)
                num += customerDAO.saveCustomerToDB(product);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            return num;
        }
    }
}
