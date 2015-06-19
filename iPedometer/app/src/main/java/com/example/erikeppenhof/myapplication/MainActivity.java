package com.example.erikeppenhof.myapplication;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.sql.Time;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedList;

import iPedometer3.AbstractTimedMessageGenerator;
import iPedometer3.CalendarEvent;
import iPedometer3.CalendarLoader;
import iPedometer3.MessageAlarmReceiver;
import iPedometer3.MovesBlock;
import iPedometer3.MovesLoader;
import iPedometer3.PersuasionType;
import iPedometer3.RandomCollection;
import iPedometer3.RandomTimedMessagesGenerator;
import iPedometer3.TimedMessage;
import iPedometer3.TimedMessagesGenerator;


public class MainActivity extends ActionBarActivity {

    public final static int CHECK_PER_N_MINUTES = 5;
    private boolean sendingStopped;
    public static String str = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Load the susceptibility scores of the user
        // (how well they score on 'Authority', 'Commitment' etc. on the survey).
        // Used to choose a persuasion strategy weighted for the user's score.
        RandomCollection<PersuasionType> userScores = getUserSusceptibilityScores();
        // Load the events from the calendar
        LinkedList<CalendarEvent> calendarEvents = loadCalendarData();

        MovesLoader movesLoader = new MovesLoader(access_token);
        LinkedList<MovesBlock> storyLine = loadMovesData(movesLoader);

        LinkedList<TimedMessage> timedMessages;

        // TODO: bepalen wanneer de gebruiker is begonnen met de studie?
        int startDay = 15;
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_MONTH) <= startDay + 7)
        {
            // Eerste week -> willekeurige berichten
            AbstractTimedMessageGenerator generator = new RandomTimedMessagesGenerator(userScores);
            timedMessages = generator.generateTimedMessages(storyLine, calendarEvents);
        }
        else
        {
            // Tweede week
            if(inControlGroup()) {
                // Gebruiker zit in controlegroep, ga door met willekeurige berichten sturen.
                AbstractTimedMessageGenerator generator = new TimedMessagesGenerator(userScores);
                timedMessages = generator.generateTimedMessages(storyLine, calendarEvents);
            }
            else {
                // Gebruiker zit in 'echte' groep, stuur getimede berichten.
                AbstractTimedMessageGenerator generator = new RandomTimedMessagesGenerator(userScores);
                timedMessages = generator.generateTimedMessages(storyLine, calendarEvents);
            }
        }

        sendMessagesNew(timedMessages);


        Json json = movesLoader.getJson(System.currentTimeMillis());
        new Thread(json).start();
        try {
            Thread.sleep(1000);
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


        Log.d("MainActivityStoryLine", String.valueOf(storyLine.size()));
        MovesBlock movesBlock;
        if ( storyLine.size() > 0 ) {
            movesBlock = storyLine.get(0);
            Log.d("MainActivityStoryLine", "Start: " + movesBlock.getStartTime() + ", End: " + movesBlock.getEndTime());
        }

        //CalendarIntegration ci = new CalendarIntegration(this);
    }

    private boolean inControlGroup()
    {
        //TODO: laad uit database of gebruiker in de controlegroep zit of niet.
        return false;
    }

    private RandomCollection<PersuasionType> getUserSusceptibilityScores()
    {
        return new RandomCollection<PersuasionType>();
    }

    private LinkedList<MovesBlock> loadMovesData(MovesLoader movesLoader)
    {
        LinkedList<MovesBlock> storyLine = new LinkedList<>();
        try {
            storyLine = movesLoader.getStoryLine(System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storyLine;
    }

    private LinkedList<CalendarEvent> loadCalendarData()
    {
        CalendarLoader calendarLoader = new CalendarLoader(this);
        Calendar cal = Calendar.getInstance();

        cal.set(cal.HOUR, 0);
        cal.set(cal.MINUTE, 0);
        cal.set(cal.SECOND, 0);
        cal.set(cal.MILLISECOND, 0);

        Date startTime = new Date(cal.getTimeInMillis());

        cal.set(cal.HOUR, 23);
        cal.set(cal.MINUTE, 59);
        cal.set(cal.SECOND, 59);

        Date endTime = new Date(cal.getTimeInMillis());

        /*
        int startYear = 2015;
        int startMonth = cal.JUNE;
        int startDay = 15;

        int endYear = 2015;
        int endMonth = cal.JUNE;
        int endDay = 30;

        cal.set(cal.YEAR, startYear);
        cal.set(cal.MONTH, startMonth);
        cal.set(cal.DATE, startDay);

        Date startdate = new Date();
        startdate.setTime(cal.getTimeInMillis());

        cal.set(cal.YEAR, endYear);
        cal.set(cal.MONTH, endMonth);
        cal.set(cal.DATE, endDay);

        Date enddate = new Date();
        enddate.setTime(cal.getTimeInMillis());
        */

        return calendarLoader.loadCalendar(startTime, endTime);
    }

    private void sendMessagesNew(LinkedList<TimedMessage> timedMessages) {

        // source: http://karanbalkar.com/2013/07/tutorial-41-using-alarmmanager-and-broadcastreceiver-in-android/
        for(TimedMessage m : timedMessages) {
            Intent messageIntent = new Intent(MainActivity.this, MessageAlarmReceiver.class);
            // Put the message in the intent so the MessageAlarmReceiver can route it to
            // the MessageSendingService, which puts it in the notification.
            messageIntent.putExtra("MESSAGE", m.getMessage().toString());

            PendingIntent resultIntent = PendingIntent.getBroadcast(MainActivity.this, 0, messageIntent, 0);
            // Set the notification to be sent at the right time using the alarm manager.
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, m.getTime(), resultIntent);
        }
    }

    private void sendMessages(LinkedList<TimedMessage> timedMessages) {
        Calendar now = Calendar.getInstance();
        TimedMessage nextMessage = timedMessages.poll();
        long currentTime = 0;
        while(!sendingStopped) {
            now = Calendar.getInstance();
            currentTime = now.getTimeInMillis();
            if(currentTime >= nextMessage.getTime()) {
                CreateNotification(timedMessages.toString());
            }
            try{
                wait(CHECK_PER_N_MINUTES * 60000);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public void CreateNotification(String notification) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.appicon2)
                        .setContentTitle("iPedometer")
                        .setContentText(notification);

        Intent resultIntent = new Intent(MainActivity.this, NotificationActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        resultIntent.putExtra("notification", notification);
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationID = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationID, mBuilder.build());
    }
}

