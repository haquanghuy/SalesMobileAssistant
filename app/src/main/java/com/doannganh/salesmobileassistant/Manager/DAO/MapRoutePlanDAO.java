package com.doannganh.salesmobileassistant.Manager.DAO;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.doannganh.salesmobileassistant.Manager.ExecMethodHTTP;
import com.doannganh.salesmobileassistant.R;
import com.doannganh.salesmobileassistant.util.ConvertUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MapRoutePlanDAO {
    private static MapRoutePlanDAO instance;
    Context context;

    private String map_API_key = "";
    private final String map_api_search_key = "https://maps.googleapis.com/maps/api/place/textsearch/json?key=%s"
            + "&language=vi&region=VN&query=%s";

    private MapRoutePlanDAO(){}

    public static MapRoutePlanDAO Instance(Context context)
    {
        if (instance == null){
            instance = new MapRoutePlanDAO();
        }

        instance.context = context;
        instance.map_API_key = context.getString(R.string.google_maps_key);
        return MapRoutePlanDAO.instance;
    }

    public Map<String, Object> searchPlace(String keyword) {
        String url = String.format(map_api_search_key, map_API_key, keyword);

        try {
            String content = ExecMethodHTTP.docNoiDung_Tu_URL(url);
            JSONObject jsonObject = new JSONObject(content);
            return ConvertUtil.jsonToMap(jsonObject);
        } catch (JSONException e) {
            Log.d("LLLMapRoutePlanDAO-json", e.getMessage());
        }
        return null;
    }

}
