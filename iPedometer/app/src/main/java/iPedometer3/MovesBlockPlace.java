package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesBlockPlace extends MovesBlock {

    private MovesPlaceType type;

    public MovesBlockPlace(long startTime, long endTime, MovesPlaceType type)
    {
        super(startTime, endTime);
        this.type = type;
    }

    public MovesPlaceType getType()
    {
        return type;
    }
}
