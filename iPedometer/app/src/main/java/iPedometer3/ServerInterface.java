import java.sql.Timestamp;


public interface ServerInterface {
	
	//Queries
	public String getAccessToken(String userEmail);
	public String getPassword(String userEmail);
	public double[] getEnqueteWeights(String userEmail);
	public String getRandomMessage(String type);
	public String getLastMessageTo(String userEmail);
	public int stepsTakenBetween(Timestamp startTime, Timestamp endTime); //Type van argumenten is bespreekbaar.
	
	//Naar DB, returns booleans voor success.
	public boolean sendStepLog(String userEmail, int nrOfSteps, Timestamp startTime, Timestamp endTime);  //Type van argumenten is bespreekbaar.
	public boolean messageSent(String userEmail, String message, int response, Timestamp time); // response: -1, 0, 1. Voor negatief, later en positief.
	public boolean newUser(String userEmail, String accesstoken, String password);
	public boolean setEnqueteWeights(String userEmail, double [] weights);
}