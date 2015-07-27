package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesBlock {

    private long startTime;
    private long endTime;
    private MovesBlockType type;

    public MovesBlock(long startTime, long endTime, MovesBlockType type)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration()
    {
        return endTime - startTime;
    }

    public MovesBlockType getType() {
        return type;
    }
}
