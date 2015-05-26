package iPedometer3;

/**
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class PersuasivePart {

    private String contents;
    private PersuasionType type;

    public PersuasivePart(String contents, PersuasionType type)
    {
        this.contents = contents;
        this.type = type;
    }

    public PersuasionType getType()
    {
        return type;
    }

    public String getContents()
    {
        return contents;
    }
}
