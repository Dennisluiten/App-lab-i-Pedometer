package com.example.erikeppenhof.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;

import iPedometer3.TimedMessage;

/**
 * This class is responsible for displaying persuasive messages
 * in a dialog on the screen, and pushing notifications to the
 * smart phone home screen.
 *
 * [Work in Progress]
 *
 * Created by Hans-Christiaan on 4-6-2015.
 */
public class MessageSender {

    private Context context;

    public MessageSender(Context context) {
        this.context = context;
    }

    public void displayMessage(TimedMessage message) {
        sendNotification(message);
        createDialog(message);
    }

    private void sendNotification(TimedMessage message) {
        // Bron: http://developer.android.com/guide/topics/ui/notifiers/notifications.html

        // Generate the notification that needs to be send.
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("iPedometer - bericht")
                .setContentText(message.getMessage().toString())
                .setSmallIcon(R.drawable.appicon);

        // TODO: Breng de gebruiker naar de app met de dialog als die op de notifactie drukt.

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Send notification.
        mNotificationManager.notify(10, builder.build());

    }

    private Dialog createDialog(TimedMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set message and title.
        builder.setMessage(message.getMessage().toString()).setTitle("iPedometer - bericht");
        builder.setNegativeButton("Nu niet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO: 'cancel'-functionaliteit implementeren.
            }
        });
        builder.setPositiveButton("Ik doe het", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO: 'ok'-functionaliteit implementeren.
            }
        });
        builder.setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO: 'snooze'-functionaliteit implementeren.
            }
        });
        return builder.create();
    }
}
