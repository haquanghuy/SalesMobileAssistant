package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.MapRoutePlanDAO;
import com.doannganh.salesmobileassistant.model.MapPlaceItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapRoutePlanPresenter {
    private static MapRoutePlanPresenter instance;
    MapRoutePlanDAO mapDAO;
    Context context;

    private MapRoutePlanPresenter(){}

    public static MapRoutePlanPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new MapRoutePlanPresenter();
        }

        instance.mapDAO = MapRoutePlanDAO.Instance(context);
        instance.context = context;
        return MapRoutePlanPresenter.instance;
    }

    public MapPlaceItem searchInfoFrom(String addressCustomer){
        Map<String, Object> map = mapDAO.searchPlace(addressCustomer);

        HashMap<String, Object> hResult = (HashMap<String, Object>) ((ArrayList) map.get("results")).get(0);

        String name = hResult.get("name").toString();
        String address = hResult.get("formatted_address").toString();

        hResult = (HashMap<String, Object>) ((HashMap<String, Object>) hResult.get("geometry")).get("location");
        double lat = Double.parseDouble(hResult.get("lat").toString());
        double lng = Double.parseDouble(hResult.get("lng").toString());

        return new MapPlaceItem(name, address, lat, lng);
    }
}
