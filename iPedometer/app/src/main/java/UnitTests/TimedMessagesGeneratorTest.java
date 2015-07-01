package UnitTests;

import android.util.Log;

import junit.framework.TestCase;

import java.sql.Time;
import java.util.LinkedList;

import iPedometer3.CalendarEvent;
import iPedometer3.MovesBlock;
import iPedometer3.MovesBlockType;
import iPedometer3.PersuasionType;
import iPedometer3.RandomCollection;
import iPedometer3.TimedMessage;
import iPedometer3.TimedMessagesGenerator;

/**
 * Created by Hans-Christiaan on 1-7-2015.
 */
public class TimedMessagesGeneratorTest extends TestCase {

    private final static long HOUR_IN_MILLI = 3600000;
    private final static long MIN_IN_MILLI = 60000;

    public void testWalkCycleMessage() throws Exception {

        RandomCollection<PersuasionType> scores = new RandomCollection<>();
        scores.add(10.0, PersuasionType.AUTHORITY);
        scores.add(20.0, PersuasionType.SCARCITY);
        scores.add(15.0, PersuasionType.CONSENSUS);
        scores.add(5.0, PersuasionType.COMMITMENT);

        LinkedList<MovesBlock> movesBlocks = new LinkedList<MovesBlock>();
        LinkedList<CalendarEvent > calendarEvents = new LinkedList<CalendarEvent>();

        long time_start = 1 * HOUR_IN_MILLI;
        long time_end = time_start + 10 * MIN_IN_MILLI;

        MovesBlock movesBlock_1 = new MovesBlock(time_start, time_end, MovesBlockType.TRANSPORT);

        TimedMessagesGenerator gen = new TimedMessagesGenerator(scores);

        LinkedList<TimedMessage> messages = new LinkedList<TimedMessage>();

        gen.walkCycleMessage(movesBlock_1, messages);

    }
}