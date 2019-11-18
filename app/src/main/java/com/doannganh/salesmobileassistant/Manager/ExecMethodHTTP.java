package com.doannganh.salesmobileassistant.Manager;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class ExecMethodHTTP {

    public static String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            Log.d("LLLDocNoiDungURL", e.getMessage());
        }
        return content.toString();
    }

    // link: truyền link nhận POST
    public static String makePostRequest(String link){
        HttpURLConnection connect;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error!";
        }
        try {
            // cấu hình HttpURLConnection
            connect = (HttpURLConnection)url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("POST");

            // Gán tham số vào URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("thamSo1", "KhoaPhamTraining")
                    .appendQueryParameter("thamSo2", "90 Lê Thị Riêng");
            String query = builder.build().getEncodedQuery();

            // Mở kết nối gửi dữ liệu
            OutputStream os = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            connect.connect();

        } catch (IOException e1) {
            e1.printStackTrace();
            return "Error!";
        }
        try {
            int response_code = connect.getResponseCode();

            // kiểm tra kết nối ok
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Đọc nội dung trả về
                InputStream input = connect.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }else{
                return "Error!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error!";
        } finally {
            connect.disconnect();
        }
    }

    public static boolean PostJSONToServer(String urlAdress, HashMap hashMap){
        JSONObject jsonObject = new JSONObject(hashMap);
        Log.d("LLLJSON_POST", jsonObject.toString());

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlAdress);
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.d("LLLpostJSONInitCon", e.getMessage());
            return false;
        }
        // start
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonObject.toString());

            os.flush();
            os.close();
            conn.connect();

            Log.d("LLLStatusJSON_POST", String.valueOf(conn.getResponseCode()));
            Log.d("LLLMessageJSON_POST" , conn.getResponseMessage());


            if(Integer.parseInt(conn.getResponseCode() + "")< 300)
                return true;
            return false;
        } catch (Exception e) {
            Log.d("LLLExceptJSON_POST" , "Loi khi POST len trung tam");
        } finally {

            conn.disconnect();
        }

        return false;
    }

    public static boolean DeleteJSONToServer(String urlAdress, String id){

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlAdress);
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.d("LLLdeleteJSONInitCon", e.getMessage());
            return false;
        }
        // start
        try {
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line=reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();

            Log.d("LLLStatusPostJSON", String.valueOf(conn.getResponseCode()));
            Log.d("LLLMessagePostJSON" , conn.getResponseMessage());

            if(Integer.parseInt(conn.getResponseCode() + "")< 300)
                return true;


            if(Integer.parseInt(conn.getResponseCode() + "")< 300)
                return true;
            return false;
        } catch (Exception e) {
            Log.d("LLLExceptPostJSON" , e.getMessage());
        } finally {

            conn.disconnect();
        }

        return false;
    }

    public static boolean putJSONToServer(String urlAdress, HashMap hashMap){
        CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );

        JSONObject jsonObject = new JSONObject(hashMap);
        Log.d("LLLJSON_PUT", jsonObject.toString());

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlAdress);
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.d("LLLputJSONInitCon", e.getMessage());
            return false;
        }
        // start
        try {
            try {
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
            }catch (Exception e){

            }

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonObject.toString());

            os.flush();
            os.close();
            conn.connect();

            Log.d("LLLStatusJSON_PUT", String.valueOf(conn.getResponseCode()));
            Log.d("LLLMessageJSON_PUT" , conn.getResponseMessage());


            if(Integer.parseInt(conn.getResponseCode() + "")< 300)
                return true;
            return false;
        } catch (Exception e) {
            Log.d("LLLExceptJSON_PUT" , "Loi khi PUT len trung tam");
        } finally {

            conn.disconnect();
        }

        return false;
    }
}
