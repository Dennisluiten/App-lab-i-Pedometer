package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesBlockMove extends MovesBlock {

    private MovesBlockType type;
    private long distance;

    public MovesBlockMove(long startTime, long endTime, MovesBlockType type, long distance)
    {
        super(startTime, endTime, type);
        this.distance = distance;
    }

    public MovesBlockType getType()
    {
        return type;
    }

    public long getDistance() {
        return distance;
    }

    public boolean isMove() {
        return true;
    }

    public boolean isPlace() { return false; }

}
