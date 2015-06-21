package com.example.erikeppenhof.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.erikeppenhof.myapplication.R;

import java.sql.Timestamp;
import java.util.Calendar;

import iPedometer3.ServerConnector;

public class NotificationActivity extends ActionBarActivity {

    final Context context = this;

    private static final int DISPLAY_DATA = 1;

    private int snoozetime = 60000*30; // half an hour

    private String email;

    private ServerConnector server = MainApp.server;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DISPLAY_DATA) {
                onCreateDialog(email).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        email = this.getIntent().getStringExtra("email");

        // SHOW POP_UP
        Dialog dialog = onCreateDialog(email);
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
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

    public Dialog onCreateDialog(final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 1) create a java calendar instance
        Calendar calendar = Calendar.getInstance();

        // 2) get a java.util.Date from the calendar instance.
        //    this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();

        // 3) a java current time (now) instance
        final java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        final String notification = this.getIntent().getStringExtra("notification");
        builder.setMessage(notification).setTitle(R.string.dialog_title);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                server.messageSent(email, notification, -1, currentTimestamp);
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                server.messageSent(email, notification, 0, currentTimestamp);
            }
        });
        builder.setNeutralButton(R.string.snooze, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Snooze
                server.messageSent(email, notification, 1, currentTimestamp);

                mHandler.sendEmptyMessageDelayed(DISPLAY_DATA, snoozetime);
            }
        });
        return builder.create();
    }
}
