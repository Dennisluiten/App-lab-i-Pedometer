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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Json implements Runnable {

    private String url = "";
    private JSONObject jsonObject;

    private boolean at = false;
    private String access_token = "";
    private String authorization = "";

    public Json(String url, boolean at) {
        this.url = url;
        this.at = at;
    }

    public Json(String url, boolean at, String auth) {
        this.url = url;
        this.at = at;
        this.authorization = auth;
    }

    public static String getJson(String page) {
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
            Log.d("json", str);
        }
        scan.close();

        return str;
    }

    public String postJSON(String address){
/*
 * Create the POST request
 */
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://api.moves-app.com/oauth/v1/access_token?");
// Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", authorization));
        params.add(new BasicNameValuePair("client_id", "yilMNmjo803XfXhwoQ76sre9Ozlx3Soc"));
        params.add(new BasicNameValuePair("client_secret", "OLgzS7A0Ht9496umdeFkV262thaicI8e6E4vhlo5R6mUKrbL7Nfaf9TjQ9KeD8Mv"));
        params.add(new BasicNameValuePair("redirect_uri", "https://www.google.nl"));
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

    public JSONObject buildJSON(String jsonresponse) {
        // build a JSON object
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonresponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void run() {
        String jsonresponse = "";
        //if (at) {
        //    url += "?access_token=" + access_token;
        //}
        Log.d("json", url);
        if (at) {
            jsonresponse = getJson(url); //getJson("http://engine.borderit.com/index.php/User/profile"); //
        }
        else {
            jsonresponse = postJSON(url);
        }
        Log.d("json", "beforebuildJSON");
        jsonObject = buildJSON(jsonresponse);
        Log.d("json", "afterbuildJSON");
        /*String s = "Test: ";
        try {
            s += jsonObject.getJSONObject("userId").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("app", s);*/
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }

}