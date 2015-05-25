package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class CalendarEvent {

    private long startTime;
    private long endTime;
    private String title;

    public CalendarEvent(long startTime, long endTime, String title)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public long getDuration()
    {
        return endTime - startTime;
    }

    public String getTitle()
    {
        return title;
    }

    public String toString()
    {
        return String.format("[%s, %s] : %s",startTime, endTime, title);
    }
}
