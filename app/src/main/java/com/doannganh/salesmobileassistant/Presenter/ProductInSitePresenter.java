package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.ProductInSiteDAO;
import com.doannganh.salesmobileassistant.model.ProductInSite;

import java.util.List;

public class ProductInSitePresenter {
    private static ProductInSitePresenter instance;
    ProductInSiteDAO productDAO;
    Context context;

    private ProductInSitePresenter(){

    }

    public static ProductInSitePresenter Instance(Context context)
    {
        if (instance == null){
            instance = new ProductInSitePresenter();
        }

        instance.productDAO = ProductInSiteDAO.Instance(context);
        instance.context = context;
        return ProductInSitePresenter.instance;
    }


    public List<ProductInSite> getListProductInSite(){
        return productDAO.getListProductInSite();
    }
}
