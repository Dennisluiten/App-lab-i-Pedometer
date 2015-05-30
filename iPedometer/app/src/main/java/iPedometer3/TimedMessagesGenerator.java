package iPedometer3;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Makes a list of timed messages for a given day
 * from this weekday's previous weeks' Move data, and current day's calendar data.
 *
 * Created by Hans-Christiaan on 26-5-2015.
 */
public class TimedMessagesGenerator {

    // TODO: Waardes bepalen, deze zijn willekeurige placeholders.
    private static final int MAX_CYCLING_DURATION = 5000;
    private static final int MAX_WALKING_DURATION = 2500;
    private static final int MSG_TIME_OFFSET = 500;
    private static final int WALK_AROUND_BLOCK_TIME = 10000;
    private static final int DESK_EXERCISE_TIME = 10000;

    private MessageGenerator msg_gen;

    public TimedMessagesGenerator(RandomCollection<PersuasionType> userSusceptibilityScores) {
        msg_gen = new MessageGenerator(userSusceptibilityScores);
    }

    public LinkedList<TimedMessage> generateTimedMessages(
            LinkedList<MovesBlock> movesBlocks, LinkedList<CalendarEvent> calendarEvents)
    {
        LinkedList<TimedMessage> messages = new LinkedList<TimedMessage>();

        for(MovesBlock mb : movesBlocks)
        {
            // TODO: altijd checken of gebruiker geen afspraak in agenda heeft tijdens versturen bericht.
            // Check wanneer er berichten verstuurd kunnen worden
            // en voeg deze toe aan de te vertsuren berichten.
            betweenEventsMessage(mb, messages, calendarEvents);
            walkCycleMessage(mb, messages);
            transportMessage(mb, messages);
            stairsMessage(mb, messages);
            standMessage(messages, calendarEvents);

        }
        return messages;
    }

    /**
     * Als de gebruiker op het werk of op school zit, en hij/zij tijd heeft tussen twee
     * afspraken: haal hem/haar over om wat te bewegen, bijv. een wandeling maken of de benen strekken.
     * @param mb
     * @param messages
     * @param calendarEvents
     */
    private void betweenEventsMessage(MovesBlock mb, LinkedList<TimedMessage> messages,
                               LinkedList<CalendarEvent> calendarEvents)
    {
        if(mb.getType()  == MovesBlockType.WORK || mb.getType() == MovesBlockType.SCHOOL)
        {
            ListIterator<CalendarEvent> ce_it = calendarEvents.listIterator();
            while(ce_it.hasNext())
            {
                CalendarEvent ce_1 = ce_it.next();
                if(ce_it.hasNext())
                {
                    CalendarEvent ce_2 = ce_it.next();
                    if(ce_1.getEndTime() >= mb.getStartTime()
                            && ce_2.getStartTime() <= mb.getEndTime())
                    {
                        long dur = ce_2.getStartTime() - ce_1.getEndTime();
                        if(dur >= WALK_AROUND_BLOCK_TIME) {
                            PersuasiveMessage p_msg = msg_gen.generateMessage(
                                    "Heb je wat tijd tussen afspraken? Maak eens een wandeling.");
                            TimedMessage msg = new TimedMessage(
                                    ce_1.getEndTime() + MSG_TIME_OFFSET,
                                    p_msg);
                            messages.add(msg);
                        }
                        else if(dur >= DESK_EXERCISE_TIME) {
                            PersuasiveMessage p_msg = msg_gen.generateMessage(
                                    "Heb je wat tijd tussen afspraken? Strek je benen eens.");
                            TimedMessage msg = new TimedMessage(
                                    ce_1.getEndTime() + MSG_TIME_OFFSET,
                                    p_msg);
                            messages.add(msg);
                        }
                    }
                }
            }
        }
    }

    private void standMessage(LinkedList<TimedMessage> messages,
                              LinkedList<CalendarEvent> calendarEvents)
    {
        for(CalendarEvent ce : calendarEvents)
        {
            PersuasiveMessage p_msg = msg_gen.generateMessage(
                    "Ga eens staan tijdens een college/afspraak in plaats van te zitten.");
            TimedMessage msg = new TimedMessage(
                    ce.getStartTime() - MSG_TIME_OFFSET,
                    p_msg);
            messages.add(msg);
        }
    }

    /**
     * Als de gebruiker maar een korte afstand te gaan heeft:
     * probeer hem/haar over te halen de fiets te pakken of te gaan lopen.
     * @param mb
     * @param messages
     */
    private void walkCycleMessage(MovesBlock mb, LinkedList<TimedMessage> messages)
    {
        // fietsbare afstand?
        if(mb.getType() == MovesBlockType.TRANSPORT
                && mb.getDuration() <= MAX_CYCLING_DURATION)
        {
            PersuasiveMessage p_msg = msg_gen.generateMessage(
                    "Als je maar een kleine afstand moet rijden, kun je overwegen om met de fiets te gaan.");
            TimedMessage msg = new TimedMessage(
                    mb.getStartTime() - MSG_TIME_OFFSET,
                    p_msg);
            messages.add(msg);
        }
        // loopbare afstand?
        else if(mb.getType() == MovesBlockType.TRANSPORT || mb.getType() == MovesBlockType.CYCLING
                && mb.getDuration() <= MAX_WALKING_DURATION)
        {
            PersuasiveMessage p_msg = msg_gen.generateMessage(
                    "Als je maar een kleine afstand moet rijden, kun je overwegen om te voet te gaan.");
            TimedMessage msg = new TimedMessage(
                    mb.getStartTime() - MSG_TIME_OFFSET,
                    p_msg);
            messages.add(msg);
        }
    }

    /**
     * Als de gebruiker met een transportmiddel gaat:
     * bus? ->  probeer hem/haar over te halen een halte later op te stappen en
     *          een halte eerder uit te stappen.
     * auto? -> probeer de auto verder weg te parkeren.
     * @param mb
     * @param messages
     */
    private void transportMessage(MovesBlock mb, LinkedList<TimedMessage> messages)
    {
        // TODO: Hoe te bepalen of de gebruiker in de bus zit, en niet in de auto of trein? Vragen in enquete?
        if(mb.getType() == MovesBlockType.TRANSPORT)
        {
            PersuasiveMessage p_msg = msg_gen.generateMessage(
                    "Neem je de bus? Stap eens een halte later op, en/of eerder uit. " +
                            "Pak je de auto? Parkeer je auto dan verder weg.");
            TimedMessage msg = new TimedMessage(
                    mb.getStartTime() - MSG_TIME_OFFSET,
                    p_msg);
            messages.add(msg);
        }
    }

    /**
     * Als de gebruiker gaat lopen:
     * probeer hem/haar over te halen om, als hij/zij op een andere verdieping moet zijn,
     * om de trap te pakken.
     * @param mb
     * @param messages
     */
    private void stairsMessage(MovesBlock mb, LinkedList<TimedMessage> messages)
    {
        if(mb.getType() == MovesBlockType.WALKING)
        {
            PersuasiveMessage p_msg = msg_gen.generateMessage(
                    "Moet je op een andere verdieping zijn? Neem eens de trap in plaats van de lift!");
            TimedMessage msg = new TimedMessage(
                    mb.getStartTime() - MSG_TIME_OFFSET,
                    p_msg);
            messages.add(msg);
        }
    }
}
