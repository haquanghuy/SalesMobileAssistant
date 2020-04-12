package com.doannganh.salesmobileassistant.util;

import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.Product;
import com.doannganh.salesmobileassistant.model.ProductInSite;

import java.util.List;

public class UtilFilter {
    public static String getCustomerNameByID(List<Customer> list_items, int id){
        for (Customer c : list_items) {
                if (c.getCustID()==id) {
                    return c.getCustName();
                }
        }
        return null;
    }

    public static Customer getCustomerByID(List<Customer> list_items, int id){
        for (Customer c : list_items) {
            if (c.getCustID() == id) {
                return c;
            }
        }
        return null;
    }

    public static Product getProductByID(List<Product> list_items, String id){
        for (Product p : list_items) {
            if (p.getProdID().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public static ProductInSite getProductInSite(List<ProductInSite> list, String id){
        for (ProductInSite p : list) {
            if (p.getProdID().equals(id)) {
                return p;
            }
        }
        return null;
    }
}
