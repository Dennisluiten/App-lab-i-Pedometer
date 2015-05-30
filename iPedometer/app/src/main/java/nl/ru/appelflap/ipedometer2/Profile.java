package nl.ru.appelflap.ipedometer2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.erikeppenhof.myapplication.R;

public class Profile extends ActionBarActivity {

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
        setContentView(R.layout.activity_profile);
        //Log.d("app", "Profile OnCreate");
        Intent myIntent = this.getIntent();
        Log.d("ProfileIntent", myIntent.getStringExtra("access_token"));
        String profile = getString(R.string.baseurl) + getString(R.string.profile) + myIntent.getStringExtra("access_token");
        Json json = new Json(profile, true);
        new Thread(json).start();

        CalendarIntegration ci = new CalendarIntegration(this);

        // SHOW POP_UP
        //Dialog dialog = onCreateDialog();
        //dialog.show();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.abc_ab_share_pack_mtrl_alpha)
                .setContentTitle("iPedometer")
                .setContentText("Notification Text");

        Intent resultIntent = this.getIntent();

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationID = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationID, mBuilder.build());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
