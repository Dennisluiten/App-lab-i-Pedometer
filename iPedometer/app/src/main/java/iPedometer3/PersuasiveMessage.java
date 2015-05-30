package iPedometer3;

/**
 * Represents a persuasive message.
 * A persuasive message consists of two parts:
 * A part that tries to persuade the user, e.g. "You have only limited
 * time to live." (scarcity)
 * and a part that proposes an activity, e.g. "Take the stairs
 * instead of the elevator."
 *
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

    public String toString() {
        return persuasivePart + " " + activityPart;
    }
}
