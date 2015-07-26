package iPedometer3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.erikeppenhof.myapplication.MainActivity;
import com.example.erikeppenhof.myapplication.NotificationActivity;
import com.example.erikeppenhof.myapplication.R;

/**
 * Created by Hans-Christiaan on 19-6-2015.
 */
public class MessageAlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra("MESSAGE");
        String email = intent.getStringExtra("EMAIL");

        System.out.println("Notification! "+message);

        NotificationManager messageManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.appicon_status_bar)
                        .setContentTitle("iPedometer")
                        .setContentText(message);

        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(context, NotificationActivity.class);

        resultIntent.putExtra("notification", message);
        resultIntent.putExtra("email", email);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        messageManager.notify(123, mBuilder.build());

    }

}
