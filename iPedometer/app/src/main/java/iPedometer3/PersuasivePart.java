package iPedometer3;

/**
 * Represents the persuasive part of a persuasive message.
 * Contains a string representation of the actual message
 * and the persuasion strategy it uses, e.g. authority, scarcity etc.
 *
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
