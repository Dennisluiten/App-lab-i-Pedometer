package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesBlock {

    private long startTime;
    private long endTime;
    private MovesBlockType type;
    private MovesMoveType movetype;
    private MovesPlaceType placetype;

    public MovesBlock(long startTime, long endTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
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

    public MovesBlockType getType() {
        return type;
    }

    public MovesMoveType getMoveType() {return movetype;}

    public MovesPlaceType getPlaceType() {return placetype;}
}
