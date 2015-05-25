package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesBlockMove extends MovesBlock {

    private MovesMoveType type;

    public MovesBlockMove(long startTime, long endTime, MovesMoveType type)
    {
        super(startTime, endTime);
        this.type = type;
    }

    public MovesMoveType getType()
    {
        return type;
    }
}
