package iPedometer3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.erikeppenhof.myapplication.MainActivity;
import com.example.erikeppenhof.myapplication.NotificationActivity;
import com.example.erikeppenhof.myapplication.R;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Hans-Christiaan on 19-6-2015.
 */
public class MessageSendingService extends Service {

    private NotificationManager messageManager;

    public final static int CHECK_PER_N_MINUTES = 5;
    private LinkedList<TimedMessage> timedMessages;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startID) {
        super.onStartCommand(intent, flags, startID);

        String notification = intent.getStringExtra("MESSAGE");
        String userId = intent.getStringExtra("USER_ID");

        Context app_con = this.getApplicationContext();
        messageManager = (NotificationManager) app_con.getSystemService(app_con.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.appicon2)
                        .setContentTitle("iPedometer")
                        .setContentText(notification);

        Intent resultIntent = new Intent(app_con, NotificationActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        resultIntent.putExtra("notification", notification);
        resultIntent.putExtra("userId", userId);

        mBuilder.setContentIntent(resultPendingIntent);

        messageManager.notify(0, mBuilder.build());

        return START_NOT_STICKY;
    }

}
