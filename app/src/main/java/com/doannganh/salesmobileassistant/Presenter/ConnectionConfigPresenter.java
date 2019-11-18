package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.ConnectionConfigDAO;
import com.doannganh.salesmobileassistant.model.ConnectionConfig;

public class ConnectionConfigPresenter {
    private static ConnectionConfigPresenter instance;
    ConnectionConfigDAO configDAO;
    Context context;

    private ConnectionConfigPresenter(){

    }

    public static ConnectionConfigPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new ConnectionConfigPresenter();
        }

        instance.configDAO = ConnectionConfigDAO.Instance(context);
        instance.context = context;
        return ConnectionConfigPresenter.instance;
    }

    public ConnectionConfig getConfigFromDB(){
        return configDAO.getConfigFromDB();
    }

    public long saveOrderToDB(ConnectionConfig config){
        return configDAO.saveOrderToDB(config);
    }

    public boolean deleteAllConfigDB(){
        return configDAO.deleteAllConfigDB();
    }
}
