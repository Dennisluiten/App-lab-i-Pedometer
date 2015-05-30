package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesBlockMove extends MovesBlock {

    private MovesBlockType type;

    public MovesBlockMove(long startTime, long endTime, MovesBlockType type)
    {
        super(startTime, endTime, type);
    }

    public MovesBlockType getType()
    {
        return type;
    }

    public boolean isMove() {
        return true;
    }

    public boolean isPlace() { return false; }

}
