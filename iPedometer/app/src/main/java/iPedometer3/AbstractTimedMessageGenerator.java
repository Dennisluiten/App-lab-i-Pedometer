package iPedometer3;

import java.util.LinkedList;

/**
 * Created by Hans-Christiaan on 11-6-2015.
 */
public abstract class AbstractTimedMessageGenerator {

    protected MessageGenerator msg_gen;

    public AbstractTimedMessageGenerator(RandomCollection<PersuasionType> userSusceptibilityScores) {
        msg_gen = new MessageGenerator(userSusceptibilityScores);
    }

    public abstract LinkedList<TimedMessage> generateTimedMessages(
            LinkedList<MovesBlock> movesBlocks, LinkedList<CalendarEvent> calendarEvents);
}
