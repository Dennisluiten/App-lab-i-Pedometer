package com.example.erikeppenhof.myapplication;

/**
 * Created by erikeppenhof on 27-05-15.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Json implements Runnable {

    private String url = "";
    private JSONObject jsonObject = null;

    private boolean at = false;
    private List<NameValuePair> params;

    public Json(String url, boolean at) {
        this.url = url;
        this.at = at;
    }

    public Json(String url, boolean at, List<NameValuePair> params) {
        this.url = url;
        this.at = at;
        this.params = params;
    }

    private static String getJson(String page) {
        // build a URL
        URL url = null;

        try {
            url = new URL(page);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // read from the URL
        Scanner scan = null;
        try {
            scan = new Scanner(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = new String();
        while (scan.hasNext()) {
            str += scan.nextLine();
            Log.d("jsonstring", str);
        }
        scan.close();

        return str;
    }

    private String postJSON(String address, List<NameValuePair> params){
/*
 * Create the POST request
 */
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(address);
        //HttpPost httpPost = new HttpPost("https://api.moves-app.com/oauth/v1/access_token?");

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        }
        /*
        * Execute the HTTP Request
        */
        String content = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity respEntity = response.getEntity();

            if (respEntity != null) {
                // EntityUtils to get the response content
                content =  EntityUtils.toString(respEntity);
            }
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        Log.d("jsontest", content);
        return content;
    }

    private JSONObject buildJSON(String jsonresponse) {
        // build a JSON object
        JSONObject obj = null;
        try {
            if (jsonresponse.charAt(0) == '{') {
                obj = new JSONObject(jsonresponse);
            }
            else if (jsonresponse.charAt(0) == '[') {
                jsonresponse = jsonresponse.substring(1, jsonresponse.length() - 1);
                Log.d("jsonbuild", jsonresponse);
                obj = new JSONObject(jsonresponse);
                Log.d("JsonArrayToObject", obj.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void run() {
        synchronized (this) {
            String jsonresponse = null;

            Log.d("jsonrun", url);
            if (at) {
                jsonresponse = getJson(url);
            } else {
                jsonresponse = postJSON(url, params);
            }
            Log.d("json", jsonresponse);
            jsonObject = buildJSON(jsonresponse);
            Log.d("json", "afterbuildJSON");

            notify();
        }
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }

    public ArrayList<String> parseType(JSONObject jsonObject, String jsonString, String jsonString2, String jsonString3) throws JSONException {
        List<String> list = new ArrayList<>();
        JSONArray array = jsonObject.getJSONArray(jsonString);
        Log.d("JSONstring", array.toString());
        for(int i = 0 ; i < array.length() ; i++) {
            JSONArray array2 = array.getJSONObject(i).getJSONArray(jsonString2);
            Log.d("JSONstring2", array2.toString());
            for(int j = 0 ; j < array2.length() ; j++){
                Log.d("JSONstring3", array.getJSONObject(i).getString(jsonString3));
                list.add(array.getJSONObject(i).getString(jsonString3));
            }
        }
        Log.d("JSONTEST", list.get(0));
        return (ArrayList<String>) list;
    }

    public ArrayList<String> parseActivities(JSONObject jsonObject, String jsonString, String jsonString2, String jsonString3) throws JSONException {
        List<String> list = new ArrayList<>();
        JSONArray array = jsonObject.getJSONArray(jsonString);
        Log.d("JSONstring1", array.toString());
        for(int i = 0 ; i < array.length() ; i++) {
            JSONArray array2 = array.getJSONObject(i).getJSONArray(jsonString2);
            Log.d("JSONstring21", array2.toString());
            for(int j = 0 ; j < array2.length() ; j++){
                Log.d("JSONstring3", array2.getJSONObject(j).getString(jsonString3));
                list.add(array2.getJSONObject(j).getString(jsonString3));
            }
        }
        Log.d("JSONTEST", list.get(0));
        return (ArrayList<String>) list;
    }

    public ArrayList<Integer> parseSteps(JSONObject jsonObject, String jsonString, String jsonString2, String jsonString3) throws JSONException {
        List<Integer> list = new ArrayList<>();
        JSONArray array = jsonObject.getJSONArray(jsonString);
        Log.d("JSONstring1", array.toString());
        for(int i = 0 ; i < array.length() ; i++) {
            JSONArray array2 = array.getJSONObject(i).getJSONArray(jsonString2);
            Log.d("JSONstring21", array2.toString());
            for(int j = 0 ; j < array2.length() ; j++){
                String group = array2.getJSONObject(j).getString("group");
                if(group.equals("running") || group.equals("walking")) {
                    Log.d("JSONstring3", array2.getJSONObject(j).getString(jsonString3));
                    list.add(array2.getJSONObject(j).getInt(jsonString3));
                }
            }
        }
        return (ArrayList<Integer>) list;
    }

    public ArrayList<String> parseTime(JSONObject jsonObject, String jsonString, String jsonString2, String jsonString3) throws JSONException {
        List<String> list = new ArrayList<>();
        JSONArray array = jsonObject.getJSONArray(jsonString);
        Log.d("JSONstring1", array.toString());
        for(int i = 0 ; i < array.length() ; i++) {
            JSONArray array2 = array.getJSONObject(i).getJSONArray(jsonString2);
            Log.d("JSONstring21", array2.toString());
            for(int j = 0 ; j < array2.length() ; j++){
                String group = array2.getJSONObject(j).getString("group");
                if(group.equals("running") || group.equals("walking")) {
                    Log.d("JSONstring3", array2.getJSONObject(j).getString(jsonString3));
                    list.add(array2.getJSONObject(j).getString(jsonString3));
                }
            }
        }
        return (ArrayList<String>) list;
    }

    public int getSteps() throws JSONException {
        int steps = 0;
        JSONArray summary = jsonObject.getJSONArray("summary");
        for (int i = 0 ; i < summary.length() ; i++) {
            if (summary.getJSONObject(i).get("steps") != null) {
                Log.d("JSONSTEPS", String.valueOf(summary.getJSONObject(i).get("steps")));
                steps += (int) summary.getJSONObject(i).get("steps");
            }
        }
        return steps;
    }
}