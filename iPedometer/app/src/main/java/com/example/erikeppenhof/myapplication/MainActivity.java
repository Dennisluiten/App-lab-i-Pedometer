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
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import iPedometer3.MovesBlock;
import iPedometer3.MovesLoader;


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

        //Log.d("ProfileIntent", "Profile OnCreate");
        Intent myIntent = this.getIntent();
        String access_token = myIntent.getStringExtra("access_token");
        //Log.d("ProfileIntent", myIntent.getStringExtra("access_token"));
        //String profile = getString(R.string.baseurl) + getString(R.string.profile);
        //List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("access_token=", myIntent.getStringExtra("access_token")));
        //Json json = new Json(profile, true);
        //new Thread(json).start();
        //try {
        //    wait();
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        MovesLoader movesLoader = new MovesLoader(access_token);
        Json json = movesLoader.getJson(System.currentTimeMillis());
        new Thread(json).start();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int steps = 0;
        try {
            steps = json.getSteps();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("MainActivitySteps", String.valueOf(steps));
        TextView textView = (TextView)findViewById(R.id.steps);
        String st = getResources().getString(R.string.stappen);
        String stappen = String.format(st, steps);
        textView.setText(stappen);

        LinkedList<MovesBlock> storyLine = new LinkedList<>();
        try {
            storyLine = movesLoader.getStoryLine(System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MainActivityStoryLine", String.valueOf(storyLine.size()));
        MovesBlock movesBlock;
        if ( storyLine.size() > 0 ) {
            movesBlock = storyLine.get(0);
            Log.d("MainActivityStoryLine", "Start: " + movesBlock.getStartTime() + ", End: " + movesBlock.getEndTime());
        }

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

}

