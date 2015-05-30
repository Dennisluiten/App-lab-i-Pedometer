package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesBlockPlace extends MovesBlock {

    private MovesBlockType type;

    public MovesBlockPlace(long startTime, long endTime, MovesBlockType type)
    {
        super(startTime, endTime, type);
    }

    public MovesBlockType getType()
    {
        return type;
    }

    public boolean isPlace() {
        return true;
    }

    public boolean isMove() { return false; }

}
