package iPedometer3;

import android.app.Notification;
import android.support.v7.app.ActionBarActivity;

import com.example.erikeppenhof.myapplication.CalendarIntegration;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class CalendarLoader {

    private ActionBarActivity act = null;

    public CalendarLoader(ActionBarActivity actionBarActivity)
    {
        this.act = actionBarActivity;
    }

    public LinkedList<CalendarEvent> loadCalendar(Date startdate, Date enddate)
    {
        CalendarIntegration calendarIntegration = new CalendarIntegration(act);
        LinkedList<CalendarEvent> events = calendarIntegration.getCalendarEvents(startdate, enddate);
        return events;
    }
}
