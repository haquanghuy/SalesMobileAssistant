package com.doannganh.salesmobileassistant.Views.util;

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskGetJSONArray extends AsyncTask<String, Void, JSONArray> {

    // not use
    @Override
    protected JSONArray doInBackground(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = null;
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
            bufferedReader.close();

            return new JSONArray(stringBuilder.toString());
        }
        catch (Exception e){
            Log.d("LLLAsyncTaskGetJsonArr", e.getMessage());
        }
        return null;
    }
}
