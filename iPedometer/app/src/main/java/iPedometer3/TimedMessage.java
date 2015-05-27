package iPedometer3;

/**
 * A persuasive message and the time that it needs to be send / has been send.
 *
 * Created by Hans-Christiaan on 26-5-2015.
 */
public class TimedMessage {

    private long time;
    private PersuasiveMessage message;

    public TimedMessage(long time, PersuasiveMessage message) {
        this.time = time;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public PersuasiveMessage getMessage() {
        return message;
    }
}
