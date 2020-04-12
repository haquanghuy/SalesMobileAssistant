package com.doannganh.salesmobileassistant.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapPlaceItem {
    String name;
    String address;
    double lat;
    double lng;

    public MapPlaceItem(String name, String address, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
/*
    public static List<MapPlaceItem> fromJson(JSONObject json) {
        List<MapPlaceItem> rs = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = json.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                var p = new MapPlaceItem(
                        jsonObject.getString("name")
                        , jsonObject.getString("formatted_address")
                        , jsonObject.getDouble("geometry")
                        , jsonObject.getDouble("formatted_address")
                );
                list.add(order);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("OrderDAO-executeJSON", e.getMessage());
                //return null;
            }
        }
        for ( item : results) {
            var p = new MapPlaceItem(
                    item['name'],
                    item['formatted_address'],
                    item['geometry']['location']['lat'],
                    item['geometry']['location']['lng']);

            rs.add(p);
        }

        return rs;
    }
    */

    /*
    static List<MapPlaceItem> fromJson(Map<String, Object> json) {
        List<MapPlaceItem> rs = new ArrayList<>();

        for (var item in json) {
            var p = new MapPlaceItem(
                    item['name'],
                    item['formatted_address'],
                    item['geometry']['location']['lat'],
                    item['geometry']['location']['lng']);

            rs.add(p);
        }

        return rs;
    }

     */
}

