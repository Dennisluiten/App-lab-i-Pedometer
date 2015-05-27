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
        {   // TODO: functie opsplitsen en overzichtelijkere sub-functies
            // TODO: altijd checken of gebruiker geen afspraak in agenda heeft tijdens versturen bericht.
            // Als de gebruiker maar een korte afstand te gaan heeft:
            // probeer hem/haar over te halen de fiets te pakken.
            if(mb.getType() == MovesBlockType.TRANSPORT
                    && mb.getDuration() <= MAX_CYCLING_DURATION)
            {
                PersuasiveMessage p_msg = msg_gen.generateMessage(
                        "Je hoeft maar een kleine afstand te gaan, neem eens de fiets!");
                TimedMessage msg = new TimedMessage(
                        mb.getStartTime() - MSG_TIME_OFFSET,
                        p_msg);
                messages.add(msg);
            }

            // Als de gebruiker maar een korte afstand te gaan heeft:
            // probeer hem/haar over te halen te lopen.
            if(mb.getType() == MovesBlockType.TRANSPORT
                    && mb.getDuration() <= MAX_WALKING_DURATION)
            {
                PersuasiveMessage p_msg = msg_gen.generateMessage(
                        "Je hoeft maar een kleine afstand te gaan, ga eens te voet!");
                TimedMessage msg = new TimedMessage(
                        mb.getStartTime() - MSG_TIME_OFFSET,
                        p_msg);
                messages.add(msg);
            }

            // Als de gebruiker de bus gaat nemen / in de bus zit:
            // probeer hem/haar over te halen een halte later op te stappen en
            // een halte eerder uit te stappen.
            // TODO: Hoe te bepalen of de gebruiker in de bus zit, en niet in de auto of trein? Vragen in enquête?
            if(mb.getType() == MovesBlockType.TRANSPORT)
            {
                PersuasiveMessage p_msg = msg_gen.generateMessage(
                        "Neem je de bus? Stap eens een halte later op!");
                TimedMessage msg = new TimedMessage(
                        mb.getStartTime() - MSG_TIME_OFFSET,
                        p_msg);
                messages.add(msg);

                p_msg = msg_gen.generateMessage(
                        "Zit je in de bus? Stap eens een halte eerder uit!");
                msg = new TimedMessage(
                        mb.getEndTime() - MSG_TIME_OFFSET*2,
                        p_msg);
                messages.add(msg);
            }
            // Als de gebruiker gaat lopen:
            // probeer hem/haar over te halen om, als hij/zij op een andere verdieping moet zijn,
            // om de trap te pakken.
            if(mb.getType() == MovesBlockType.WALKING)
            {
                PersuasiveMessage p_msg = msg_gen.generateMessage(
                        "Moet je op een andere verdieping zijn? Neem eens de trap in plaats van de lift!");
                TimedMessage msg = new TimedMessage(
                        mb.getStartTime() - MSG_TIME_OFFSET,
                        p_msg);
            }
            // Als de gebruiker op het werk of op school zit, en hij/zij tijd heeft tussen twee
            // afspraken:
            // haal hem/haar over om wat te bewegen, bijv. een wandeling maken of de benen strekken.
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
                                        ce_1.getStartTime() + MSG_TIME_OFFSET,
                                        p_msg);
                            }
                             else if(dur >= DESK_EXERCISE_TIME) {
                                PersuasiveMessage p_msg = msg_gen.generateMessage(
                                        "Heb je wat tijd tussen afspraken? Strek je benen eens.");
                                TimedMessage msg = new TimedMessage(
                                        ce_1.getStartTime() + MSG_TIME_OFFSET,
                                        p_msg);
                            }
                        }
                    }
                }
            }
        }
        return messages;
    }
}
