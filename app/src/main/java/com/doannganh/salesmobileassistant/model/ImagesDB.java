package com.doannganh.salesmobileassistant.model;

import android.database.Cursor;

import com.doannganh.salesmobileassistant.Manager.DAO.SalesMobileAssistant;

public class ImagesDB {
    String company;
    String imagesKey;
    String imageName;
    byte[] imageData;

    public ImagesDB(String compID, String key, String name, byte[] data){
        company = compID;
        imagesKey = key;
        imageName = name;
        imageData = data;
    }

    public ImagesDB(Cursor cursor){
        company = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_IMAGES_COMPANY));
        imagesKey = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_IMAGES_KEY));
        imageName = cursor.getString(cursor.getColumnIndex(SalesMobileAssistant.TB_IMAGES_FIENAME));
        imageData = cursor.getBlob(cursor.getColumnIndex(SalesMobileAssistant.TB_IMAGES_DATA));
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImagesKey() {
        return imagesKey;
    }

    public void setImagesKey(String imagesKey) {
        this.imagesKey = imagesKey;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
