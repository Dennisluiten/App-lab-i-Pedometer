package iPedometer3;

/**
 * Created by Hans-Christiaan on 26-5-2015.
 */
public class PersuasiveMessage {

    private PersuasivePart persuasivePart;
    private String activityPart;

    public PersuasiveMessage(PersuasivePart persuasivePart, String activityPart) {
        this.persuasivePart = persuasivePart;
        this.activityPart = activityPart;
    }

    public PersuasivePart getPersuasivePart() {
        return persuasivePart;
    }

    public String getActivityPart() {
        return activityPart;
    }
}
