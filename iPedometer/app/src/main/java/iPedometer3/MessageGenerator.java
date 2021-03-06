package iPedometer3;

import android.os.AsyncTask;

import com.example.erikeppenhof.myapplication.MainApp;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Generates messages based on a user's susceptibility
 * for different persuasive strategies.
 * Created by Hans-Christiaan on 26-5-2015.
 */
public class MessageGenerator {

    private LinkedList<PersuasivePart> authorityMessages;
    private LinkedList<PersuasivePart> scarcityMessages;
    private LinkedList<PersuasivePart> commitmentMessages;
    private LinkedList<PersuasivePart> consensusMessages;
    private LinkedList<PersuasivePart> funnyMessages;

    private RandomCollection<PersuasionType> userSusceptibilityScores;

    private ServerConnector server;

    public MessageGenerator(RandomCollection<PersuasionType> userSusceptibilityScores)
    {
        authorityMessages = new LinkedList<PersuasivePart>();
        scarcityMessages = new LinkedList<PersuasivePart>();
        commitmentMessages = new LinkedList<PersuasivePart>();
        consensusMessages = new LinkedList<PersuasivePart>();
        funnyMessages = new LinkedList<PersuasivePart>();

        this.userSusceptibilityScores = userSusceptibilityScores;

        server = MainApp.server;

        loadMessages();
    }

    private void loadMessages()
    {

        MessageLoader msg_loader = new MessageLoader();

        try {
            // Load messages from the server, using an asynctask,
            // but waits before the messages are loaded before continuing the program.
            LinkedList<PersuasivePart>[] all_messages = msg_loader.execute().get();
            // Split the messages in their respective persuasion strategies.
            for(PersuasivePart part : all_messages[0])
                authorityMessages.add(part);
            for(PersuasivePart part : all_messages[1])
                commitmentMessages.add(part);
            for(PersuasivePart part : all_messages[2])
                consensusMessages.add(part);
            for(PersuasivePart part : all_messages[3])
                funnyMessages.add(part);
            for(PersuasivePart part : all_messages[4])
                scarcityMessages.add(part);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a random persuasive message consisting of a given activity part
     * and a random persuasive part.
     * The strategy of the persuasive part is chosen based on the user's susceptibility for
     * certain strategies.
     * @param activityPart The activity part of the message, e.g. "Take the stairs."
     * @return An persuasive message, consisting of the activity part followed by a persuasive part.
     */
    public PersuasiveMessage generateMessage(String activityPart)
    {
        Random random = new Random();
        int randomIndex = 0;
        PersuasivePart msg;

        PersuasionType type = userSusceptibilityScores.takeWeightedSample();

        switch(type) {
            case FUNNY:
            case CONSENSUS:
                randomIndex = random.nextInt(consensusMessages.size());
                msg = consensusMessages.get(randomIndex); break;
            case COMMITMENT:
                randomIndex = random.nextInt(commitmentMessages.size());
                msg = commitmentMessages.get(randomIndex); break;
            case AUTHORITY:
                randomIndex = random.nextInt(authorityMessages.size());
                msg = authorityMessages.get(randomIndex); break;
            case SCARCITY:
                randomIndex = random.nextInt(scarcityMessages.size());
                msg = scarcityMessages.get(randomIndex); break;
            default:
                randomIndex = random.nextInt(commitmentMessages.size());
                msg = commitmentMessages.get(randomIndex); break;
        }
        return new PersuasiveMessage(msg, activityPart);
    }

    public PersuasiveMessage generateFunnyMessage()
    {
        Random random = new Random();
        int randomIndex = random.nextInt(funnyMessages.size());
        PersuasivePart msg = funnyMessages.get(randomIndex);
        return new PersuasiveMessage(msg, "");
    }

    /**
     * For loading the messages from the server.
     */
    private class MessageLoader extends AsyncTask<String, String, LinkedList<PersuasivePart>[]> {

        @Override
        protected LinkedList<PersuasivePart>[] doInBackground(String... params) {
            return server.getAllMessages();
        }
    }
}
