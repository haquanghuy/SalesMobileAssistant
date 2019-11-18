package com.doannganh.salesmobileassistant.Manager.DAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UtilMethod {

    public static Object getLastIdentifyFromDB(SQLiteDatabase dbGotWrote, SalesMobileAssistant salesMobileAssistant,
                                               String tableName, String idName, String typeOutPut){

        String sql = "SELECT " + idName + " FROM " + tableName + " ORDER BY " + idName + " DESC LIMIT 1";
        try {
            Cursor cursor = dbGotWrote.rawQuery(sql, null);
            if (cursor.getCount() != 0){
                if(typeOutPut.equals("String"))
                    return cursor.getString(0);
                if(typeOutPut.equals("int"))
                    return cursor.getInt(0);
            }
        }catch (Exception e){
            Log.d("LLLUtilGetLastID", e.getMessage());
        }
        return null;
    }


}
