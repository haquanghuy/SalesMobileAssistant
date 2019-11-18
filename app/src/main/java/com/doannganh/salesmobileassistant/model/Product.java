package com.doannganh.salesmobileassistant.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product implements Serializable {
    String CompID;
    String PTypeID;
    String ProdID;
    String ProdName;
    double UnitPrice;
    String UOM;
    Date DateUpdate;

    public Product(String compID, String PTypeID, String prodID, String prodName, double unitPrice, String UOM, Date dateUpdate) {
        CompID = compID;
        this.PTypeID = PTypeID;
        ProdID = prodID;
        ProdName = prodName;
        UnitPrice = unitPrice;
        this.UOM = UOM;
        DateUpdate = dateUpdate;
    }

    public Product(JSONObject jsonObject){
        try {
            CompID = jsonObject.getString("CompID");
            PTypeID = jsonObject.getString("PGroupID");
            ProdID = jsonObject.getString("ProdID");
            ProdName = jsonObject.getString("ProdName");
            UnitPrice = jsonObject.getDouble("UnitPrice");
            UOM = jsonObject.getString("UOM");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            DateUpdate = format.parse(jsonObject.getString("DateUpdate"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("publicProductJson", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("publicProductJsonDate", e.getMessage());
        }
    }

    public String getCompID() {
        return CompID;
    }

    public void setCompID(String compID) {
        CompID = compID;
    }

    public String getPTypeID() {
        return PTypeID;
    }

    public void setPTypeID(String PTypeID) {
        this.PTypeID = PTypeID;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public Date getDateUpdate() {
        return DateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        DateUpdate = dateUpdate;
    }
}
