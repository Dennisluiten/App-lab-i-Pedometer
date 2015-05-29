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

    public MovesBlockType getType() {
        switch(type) {
            case CYCLING:
                return MovesBlockType.CYCLING;
            case WALKING:
                return MovesBlockType.WALKING;
            case RUNNING:
                return MovesBlockType.RUNNING;
            case TRANSPORT:
                return MovesBlockType.TRANSPORT;
            default: return null;
        }
    }

    public MovesMoveType getMoveType()
    {
        return type;
    }
}
