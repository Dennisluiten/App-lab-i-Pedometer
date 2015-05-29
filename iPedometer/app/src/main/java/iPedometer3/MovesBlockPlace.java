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

    public MovesBlockType getType() {
        switch(type) {
            case WORK:
                return MovesBlockType.WORK;
            case HOME:
                return MovesBlockType.HOME;
            case SCHOOL:
                return MovesBlockType.SCHOOL;
            case OTHER:
                return MovesBlockType.OTHER;
            default: return null;
        }
    }

    public MovesPlaceType getPlaceType()
    {
        return type;
    }
}
