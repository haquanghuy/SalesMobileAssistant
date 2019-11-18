package com.doannganh.salesmobileassistant.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ProductInSite implements Serializable {
    String Company;
    String SiteID;
    String ProdID;
    double quantity;
    String UOM;

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getSiteID() {
        return SiteID;
    }

    public void setSiteID(String siteID) {
        SiteID = siteID;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ProductInSite(JSONObject jsonObject){
        try {
            Company = jsonObject.getString("CompID");
            SiteID = jsonObject.getString("SiteID");
            ProdID = jsonObject.getString("ProdID");
            quantity = jsonObject.getDouble("Quatity");
            //UOM = jsonObject.getString("UOM");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("LLLpublicProdInSiteJson", e.getMessage());
        }
    }
}
