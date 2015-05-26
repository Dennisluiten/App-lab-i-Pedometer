package iPedometer3;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Hans-Christiaan on 26-5-2015.
 */
public class MessageGenerator {

    private LinkedList<PersuasivePart> authorityMessages;
    private LinkedList<PersuasivePart> scarcityMessages;
    private LinkedList<PersuasivePart> commitmentMessages;
    private LinkedList<PersuasivePart> consensusMessages;
    private LinkedList<PersuasivePart> funnyMessages;

    public MessageGenerator()
    {
        authorityMessages = new LinkedList<PersuasivePart>();
        scarcityMessages = new LinkedList<PersuasivePart>();
        commitmentMessages = new LinkedList<PersuasivePart>();
        consensusMessages = new LinkedList<PersuasivePart>();
        funnyMessages = new LinkedList<PersuasivePart>();

        loadMessages();
    }

    private void loadMessages()
    {
        // TODO: Laden van het 'persuasion'-gedeelte van de berichten en in de lists zetten. Ophalen uit database of hardcoderen?
        consensusMessages.add(
                new PersuasivePart("Minder dan 10% van de mensen is inactief.",
                        PersuasionType.CONSENSUS));
        authorityMessages.add(
                new PersuasivePart("Volgens Thuisarts.nl maakt bewegen je spieren en botten sterker.",
                        PersuasionType.AUTHORITY));
        scarcityMessages.add(
                new PersuasivePart("Per dag is er maar één kans om meer te bewegen. Neem die kans vandaag!",
                        PersuasionType.SCARCITY));
        commitmentMessages.add(
                new PersuasivePart("Probeer je aan je doel te houden om gezonder te leven door meer te bewegen. Zet nog een tandje bij!",
                        PersuasionType.COMMITMENT));
        funnyMessages.add(
                new PersuasivePart("Drink veel water waardoor je vaak naar het toilet moet; het liefst op een andere verdieping.",
                        PersuasionType.FUNNY));
    }

    public PersuasiveMessage generateMessage(PersuasionType type, String activityPart)
    {
        Random random = new Random();
        int randomIndex = random.nextInt(consensusMessages.size());
        PersuasivePart msg;
        switch(type) {
            case CONSENSUS:
                msg = consensusMessages.get(randomIndex); break;
            case COMMITMENT:
                msg = commitmentMessages.get(randomIndex); break;
            case AUTHORITY:
                msg = authorityMessages.get(randomIndex); break;
            case FUNNY:
                msg = funnyMessages.get(randomIndex); break;
            case SCARCITY:
                msg = scarcityMessages.get(randomIndex); break;
            default:
                msg = commitmentMessages.get(randomIndex); break;
        }
        // Funny messages have already incorporated activity parts.
        if(type == PersuasionType.FUNNY)
            activityPart = "";
        return new PersuasiveMessage(msg, activityPart);
    }
}
