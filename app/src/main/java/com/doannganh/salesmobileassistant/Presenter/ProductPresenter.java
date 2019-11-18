package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.ProductDAO;
import com.doannganh.salesmobileassistant.model.Product;

import java.util.List;

public class ProductPresenter {
    private static ProductPresenter instance;
    ProductDAO productDAO;
    Context context;

    private ProductPresenter(){

    }

    public static ProductPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new ProductPresenter();
        }

        instance.productDAO = ProductDAO.Instance(context);
        instance.context = context;
        return ProductPresenter.instance;
    }

    public List<Product> GetListProduct(){
        try {
            return productDAO.getListProduct();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getQuantityCurent(String productID){
        return productDAO.getQuantityCurent(productID);
    }
}
