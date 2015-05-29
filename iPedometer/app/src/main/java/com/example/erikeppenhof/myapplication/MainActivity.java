package com.example.erikeppenhof.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    final Context context = this;

    private static final int DISPLAY_DATA = 1;

    private int snoozetime = 5000; // 60000*30; // half an hour

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DISPLAY_DATA) {
                onCreateDialog().show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CalendarIntegration ci = new CalendarIntegration(this);
        setContentView(R.layout.activity_main);
        Log.d("ProfileIntent", "Profile OnCreate");
        Intent myIntent = this.getIntent();
        Log.d("ProfileIntent", myIntent.getStringExtra("access_token"));
        String profile = getString(R.string.baseurl) + getString(R.string.profile) + myIntent.getStringExtra("access_token");
        Json json = new Json(profile, true);
        new Thread(json).start();

        //CalendarIntegration ci = new CalendarIntegration(this);

        // SHOW POP_UP
        //Dialog dialog = onCreateDialog();
        //dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Cancel
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // OK
            }
        });
        builder.setNeutralButton(R.string.snooze, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Snooze
                mHandler.sendEmptyMessageDelayed(DISPLAY_DATA, snoozetime);
            }
        });
        return builder.create();
    }

    public void sendPostRequest() throws MalformedURLException, IOException {
        URL reqURL = new URL("testURL"); //the URL we will send the request to
        HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
        String post = "Test Post";
        request.setDoOutput(true);
        request.addRequestProperty("Content-Length", Integer.toString(post.length())); //add the content length of the post data
        request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //add the content type of the request, most post data is of this type
        request.setRequestMethod("POST");
        request.connect();
        OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream()); //we will write our request data here
        writer.write(post);
        writer.flush();
    }
/*
    public void sendGetRequest(String url) throws MalformedURLException, IOException {
        URL reqURL = new URL("http://api.moves-app.com" + url); //the URL we will send the request to
        HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
        request.setRequestMethod("GET");
        request.connect();
    }*/

    public static JSONObject GET(String url){
        //url = "http://api.moves-app.com" + url;
        InputStream inputStream = null;
        String result = "";
        JSONObject jsonObject = new JSONObject();
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            jsonObject = new JSONObject(responseStrBuilder.toString());

            // convert inputstream to string
            //if(inputStream != null)
            //    result = convertInputStreamToString(inputStream);
            //else
            //    result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return jsonObject;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}

