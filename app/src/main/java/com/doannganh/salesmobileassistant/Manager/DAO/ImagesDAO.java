package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.MediaStore;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.Manager.HttpsTrustManager;
import com.doannganh.salesmobileassistant.Manager.Server;
import com.doannganh.salesmobileassistant.model.Account;
import com.doannganh.salesmobileassistant.model.Customer;
import com.doannganh.salesmobileassistant.model.ImagesDB;
import com.doannganh.salesmobileassistant.util.ConstantUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImagesDAO {
    SQLiteDatabase db;
    SalesMobileAssistant salesMobileAssistant;
    private static ImagesDAO instance;
    Context context;

    String getTAG = "ImagesDAO";

    private ImagesDAO(){}

    public static ImagesDAO Instance(Context context)
    {
        if (instance == null){
            instance = new ImagesDAO();
        }

        instance.context = context;
        instance.salesMobileAssistant = SalesMobileAssistant.getsInstance(context);
        return ImagesDAO.instance;
    }

    public List<ImagesDB> getImagesFromDB(String compID, String key) {
        List<ImagesDB> re = new ArrayList<>();
        db = salesMobileAssistant.getReadableDatabase();

        String sql = "SELECT * " + " FROM " + salesMobileAssistant.TB_IMAGES
                + " WHERE " + salesMobileAssistant.TB_IMAGES_COMPANY + " = '" + compID + "'"
                + " AND " + salesMobileAssistant.TB_IMAGES_KEY + " = '" + key + "'";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToPosition(-1);
            while ( cursor.moveToNext())
                re.add(new ImagesDB(cursor));

            cursor.close();
        }catch (Exception e){
            Log.d("LLLImagesDAO", e.getMessage());
        }
        finally {
            db.close();
            return re;
        }
    }


    public long saveImageToDB(ImagesDB imagesDB){
        db = salesMobileAssistant.getWritableDatabase();
        long num = ConstantUtil.DB_CRUD_RESPONSE_EMPTY;
        db.beginTransaction();

        // kiem tra ton tai

        String kt = "SELECT* FROM " + salesMobileAssistant.TB_IMAGES + " WHERE "
                + salesMobileAssistant.TB_IMAGES_COMPANY + " = '" + imagesDB.getCompany()
                + "' AND " + salesMobileAssistant.TB_IMAGES_KEY
                + " = '" + imagesDB.getImagesKey() + "' AND " + salesMobileAssistant.TB_IMAGES_FIENAME
                + " = '" + imagesDB.getImageName() + "'";
        Cursor cKT = db.rawQuery(kt, null);

        // neu ton tai -> return routeplan tu db
        // dang ky
        try {
            ContentValues values = new ContentValues();
            values.put(salesMobileAssistant.TB_IMAGES_FIENAME, imagesDB.getImageName());
            values.put(salesMobileAssistant.TB_IMAGES_DATA, imagesDB.getImageData());

            if (cKT.getCount() != 0) {
                num = db.update(salesMobileAssistant.TB_IMAGES, values, salesMobileAssistant.TB_IMAGES_COMPANY +
                        " = '" + imagesDB.getCompany() + "' AND " + salesMobileAssistant.TB_IMAGES_KEY
                        + " = " + imagesDB.getImagesKey() + " AND " + salesMobileAssistant.TB_IMAGES_FIENAME
                        + " = " + imagesDB.getImageName(), null);
            }else {
                values.put(salesMobileAssistant.TB_IMAGES_COMPANY, imagesDB.getCompany());
                values.put(salesMobileAssistant.TB_IMAGES_KEY, imagesDB.getImagesKey());
                num = db.insert(salesMobileAssistant.TB_IMAGES, null, values);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException ex){
            Log.d("LLL"+getTAG, ex.getMessage());
            num = ConstantUtil.DB_CRUD_RESPONSE_ERROR;
        }
        finally {
            cKT.close();
            db.endTransaction();
            return num;
        }
    }

}

