package iPedometer3;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Hans-Christiaan on 11-6-2015.
 */
public class RandomTimedMessagesGenerator extends AbstractTimedMessageGenerator {

    private final static long HOUR_IN_MILLI = 3600000;
    private final static long MIN_IN_MILLI = 60000;

    public RandomTimedMessagesGenerator(RandomCollection<PersuasionType> userSusceptibilityScores) {
        super(userSusceptibilityScores);
    }

    @Override
    public LinkedList<TimedMessage> generateTimedMessages(
            LinkedList<MovesBlock> movesBlocks, LinkedList<CalendarEvent> calendarEvents) {
        int startHour = 10;
        int endHour = 19;

        LinkedList<TimedMessage> messages = new LinkedList<TimedMessage>();

        for(int hour = startHour; hour <= endHour; hour = hour+2)
        {
            Random r = new Random();
            int time_offset_minutes = r.nextInt(30);
            boolean plus = r.nextBoolean();
            if(plus) {
                PersuasiveMessage per_message = msg_gen.generateMessage(getRandomActivity());
                TimedMessage message = new TimedMessage(
                        hour * HOUR_IN_MILLI + time_offset_minutes * MIN_IN_MILLI,
                        per_message);
                messages.add(message);
            }
        }
        return messages;
    }

    private String getRandomActivity() {
        Random r = new Random();
        int i = r.nextInt(7);

        switch(i){
            case 0: return "Heb je wat tijd tussen afspraken? Maak eens een wandeling.";
            case 1: return "Heb je wat tijd tussen afspraken? Strek je benen eens.";
            case 2: return "Ga eens staan tijdens een college/afspraak in plaats van te zitten.";
            case 3: return "Als je maar een kleine afstand moet rijden, kun je overwegen om met de fiets te gaan.";
            case 4: return "Als je maar een kleine afstand moet rijden, kun je overwegen om te voet te gaan.";
            case 5: return "Neem je de bus? Stap eens een halte later op, en/of eerder uit. " +
                    "Pak je de auto? Parkeer je auto dan verder weg.";
            case 6: return "Moet je op een andere verdieping zijn? Neem dan eens de trap in plaats van de lift!";
            default: return "Moet je op een andere verdieping zijn? Neem dan eens de trap in plaats van de lift!";
        }
    }

}
