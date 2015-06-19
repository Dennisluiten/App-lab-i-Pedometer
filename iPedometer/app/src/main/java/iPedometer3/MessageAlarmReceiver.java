package iPedometer3;

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
        Intent service = new Intent(context, MessageSendingService.class);
        service.putExtra("MESSAGE", message);
        context.startService(service);
    }

}
