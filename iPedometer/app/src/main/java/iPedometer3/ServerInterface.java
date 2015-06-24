package iPedometer3;
import java.sql.Timestamp;
import java.util.LinkedList;


public interface ServerInterface {

    //Queries
    public String getAccessToken(String userEmail);
    public String getPassword(String userEmail);
    public double [] getEnqueteWeights(String userEmail);
    public LinkedList<PersuasivePart> [] getAllMessages();
    public String getLastMessageTo(String userEmail);
    public int stepsTakenBetween(String userEmail, Timestamp startTime, Timestamp endTime); //Type van argumenten is bespreekbaar.

    //Naar DB, returns booleans voor success.
    public boolean sendStepLog(String userEmail, int nrOfSteps, Timestamp startTime, Timestamp endTime);  //Type van argumenten is bespreekbaar.
    public boolean messageSent(String userEmail, String message, int response, Timestamp time); // response: -1, 0, 1. Voor negatief, later en positief.
    public boolean setMessageResponse(String userEmail, Timestamp responseTime, int response);
    public boolean newUser(String userEmail, String accesstoken, String password);
    public boolean setEnqueteWeights(String userEmail, double[] weights);
}


