package nl.ru.appelflap.ipedometer2;

import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Json implements Runnable {

    private String url = "";
    private JSONObject jsonObject;

    private String access_token = "";

    public Json(String url) {
        this.url = url;
    }

    public static JSONObject getJson(String page) {
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

        // build a JSON object
        JSONObject obj = null;
        try {
            obj = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void run() {
        url += "?access_token=" + access_token;
        Log.d("json", url);
        jsonObject = getJson(url); //getJson("http://engine.borderit.com/index.php/User/profile"); //
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