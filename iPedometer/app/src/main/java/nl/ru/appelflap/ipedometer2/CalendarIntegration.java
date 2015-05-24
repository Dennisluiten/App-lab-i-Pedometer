package nl.ru.appelflap.ipedometer2;

import android.database.Cursor;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by Senna on 25-4-2015.
 */

public class CalendarIntegration {

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
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

        //String calendarEventURL = "content://com.android.calendar/events";  //Events
        //Log.d("CalendarIntegration", calendarEventURL);

        Log.d("CalendarIntegration", "new Thread started");
        String[] projection = new String[] { Events.CALENDAR_ID, Events.TITLE, Events.DESCRIPTION, Events.DTSTART, Events.DTEND, Events.ALL_DAY, Events.EVENT_LOCATION };

// 0 = January, 1 = February, ...

        Calendar startTime = Calendar.getInstance();
        startTime.set(2014,00,01,00,00);

        Calendar endTime= Calendar.getInstance();
        endTime.set(2015,00,01,00,00);

// the range is all data from 2014

        String selection = "(( " + Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))";
        Log.d("CalendarIntegration", "Before cursor");
        Cursor cursor = activity.getBaseContext().getContentResolver().query( Events.CONTENT_URI, projection, selection, null, null );
        Log.d("CalendarIntegration", "After cursor");
// output the events

        if (cursor.moveToFirst()) {
            do {
                Log.d("CalendarIntegration", "CalendarIntegration: Title: " + cursor.getString(1) + " Start-Time: " + (new Date(cursor.getLong(3))).toString());
                //Toast.makeText(activity.getApplicationContext(), "Title: " + cursor.getString(1) + " Start-Time: " + (new Date(cursor.getLong(3))).toString(), Toast.LENGTH_LONG).show();
            } while ( cursor.moveToNext());
        }
        //String calendarEventURL = "content://com.android.calendar/events";  //Events
        //Log.d("CalendarIntegration", calendarEventURL);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("CalendarIntegration", "new Thread started");
                String[] projection = new String[] { Events.CALENDAR_ID, Events.TITLE, Events.DESCRIPTION, Events.DTSTART, Events.DTEND, Events.ALL_DAY, Events.EVENT_LOCATION };

// 0 = January, 1 = February, ...

                Calendar startTime = Calendar.getInstance();
                startTime.set(2014,00,01,00,00);

                Calendar endTime= Calendar.getInstance();
                endTime.set(2015,00,01,00,00);

// the range is all data from 2014

                String selection = "(( " + Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))";
                Log.d("CalendarIntegration", "Before cursor");
                Cursor cursor = activity.getBaseContext().getContentResolver().query( Events.CONTENT_URI, projection, selection, null, null );
                Log.d("CalendarIntegration", "After cursor");
// output the events

                if (cursor.moveToFirst()) {
                    do {
                        Log.d("CalendarIntegration", "CalendarIntegration: Title: " + cursor.getString(1) + " Start-Time: " + (new Date(cursor.getLong(3))).toString());
                        Toast.makeText(activity.getApplicationContext(), "Title: " + cursor.getString(1) + " Start-Time: " + (new Date(cursor.getLong(3))).toString(), Toast.LENGTH_LONG).show();
                    } while ( cursor.moveToNext());
                }

            /*    // Run query
                Cursor cur = null;

                ContentResolver cr = activity.getContentResolver();
                Uri uri = Calendars.CONTENT_URI;
                //String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                //        + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                //        + Calendars.OWNER_ACCOUNT + " = ?))";
                // TODO: let user enter own e-mail (and account type)
                //String[] selectionArgs = new String[] {"sennavaniersel@gmail.com", "com.google",
                //        "sennavaniersel@gmail.com"};
                // Submit the query and get a Cursor object back.
                cur = cr.query(uri, EVENT_PROJECTION, null, null, null);



                // Use the cursor to step through the returned records
                while (cur.moveToNext()) {
                    long calID = 0;
                    String displayName = null;
                    String accountName = null;
                    String ownerName = null;

                    // Get the field values
                    calID = cur.getLong(PROJECTION_ID_INDEX);
                    displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                    accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                    ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                    // Do something with the values...

                }

            }
        });*/
    }
}
