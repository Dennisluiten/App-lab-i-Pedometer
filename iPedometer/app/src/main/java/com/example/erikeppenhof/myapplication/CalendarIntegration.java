package com.example.erikeppenhof.myapplication;

import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import iPedometer3.CalendarEvent;

/**
 * Created by erikeppenhof on 27-05-15.
 */
public class CalendarIntegration {

        // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
        public static final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };

        // The indices for the projection array above.
        private static final int PROJECTION_ID_INDEX = 0;
        private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
        private static ActionBarActivity activity = null;

        public CalendarIntegration(ActionBarActivity act) {
            Log.d("CalendarIntegration", "CalendarIntegration started");
            activity = act;
        }

        public LinkedList<CalendarEvent> getCalendarEvents(Date startdate, Date enddate) {
            LinkedList<CalendarEvent> calendarEvents = new LinkedList<>();
            //String calendarEventURL = "content://com.android.calendar/events";  //Events
            //Log.d("CalendarIntegration", calendarEventURL);

            Log.d("CalendarIntegration", "new Thread started");
            String[] projection = new String[] { CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.ALL_DAY, CalendarContract.Events.EVENT_LOCATION };

            // 0 = January, 1 = February, ...
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(startdate);
            startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), 00, 00);

            Calendar endTime= Calendar.getInstance();
            endTime.setTime(enddate);
            endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH),00,00);

            // the range is all data from startdate to enddate

            String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))";
            Log.d("CalendarIntegration", "Before cursor");
            Cursor cursor = activity.getBaseContext().getContentResolver().query( CalendarContract.Events.CONTENT_URI, projection, selection, null, null );
            Log.d("CalendarIntegration", "After cursor");
            // output the events

            if (cursor.moveToFirst()) {
                do {
                    Log.d("CalendarIntegration", "CalendarIntegration: Title: " + cursor.getColumnName(1) + "," + cursor.getString(1) + " Start-Time: " + cursor.getColumnName(3) + ", " + cursor.getLong(3) + " End-Time: " + cursor.getColumnName(4) + ", " + cursor.getLong(4));
                    calendarEvents.add(new CalendarEvent(cursor.getLong(3), cursor.getLong(4), cursor.getString(1)));
                    //Toast.makeText(activity.getApplicationContext(), "Title: " + cursor.getString(1) + " Start-Time: " + (new Date(cursor.getLong(3))).toString(), Toast.LENGTH_LONG).show();
                } while ( cursor.moveToNext());
            }
            return calendarEvents;
        }

    }


