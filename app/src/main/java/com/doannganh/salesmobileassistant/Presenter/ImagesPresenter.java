package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.doannganh.salesmobileassistant.Manager.DAO.ImagesDAO;
import com.doannganh.salesmobileassistant.model.Account;
import com.doannganh.salesmobileassistant.model.ImagesDB;
import com.doannganh.salesmobileassistant.util.ConstantUtil;

import java.util.List;

public class ImagesPresenter {
    private static ImagesPresenter instance;
    ImagesDAO imagesDAO;
    Context context;

    public static final String KEY_IMAGES_HEADER_MAIN = "IMAGE_HEADER_MAIN";

    private ImagesPresenter(){

    }

    public static ImagesPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new ImagesPresenter();
        }

        instance.imagesDAO = ImagesDAO.Instance(context);
        instance.context = context;
        return ImagesPresenter.instance;
    }

    public List<ImagesDB> getImagesFromDB(String compID, String key){
        try {
            return imagesDAO.getImagesFromDB(compID, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long saveImageToDB(ImagesDB imagesDB){
        try {
            return imagesDAO.saveImageToDB(imagesDB);
        } catch (Exception e) {
            e.printStackTrace();
            return ConstantUtil.DB_CRUD_RESPONSE_ERROR;
        }
    }


    public long saveListImagesToDB(List<ImagesDB> imagesDBs) {
        long re = 0;
        for (ImagesDB i : imagesDBs){
            re += saveImageToDB(i);
        }
        return re;
    }
}
