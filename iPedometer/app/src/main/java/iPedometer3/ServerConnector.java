package iPedometer3;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import iPedometer3.ServerInterface;


public class ServerConnector implements ServerInterface {

    private String urlstring = "http://applab.ai.ru.nl:8080/teamD/ServletTest2";
    private URLConnection conn;
    protected ArrayList<String> serverResponse;
    private ArrayList<String> responseStrings;


    public static void main(String[] args) throws IOException {
        new ServerConnector();
        System.out.println("Process terminated.");
    }

    public ServerConnector(){
    }

    private BufferedReader actuallySendRequest(String request) throws IOException, UnknownHostException{
        URL url = new URL(urlstring);
        conn = url.openConnection();
        conn.setDoOutput(true);
        BufferedWriter out = new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ) );
        out.write(request);
        out.flush();
        out.close();
        return new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
    }

    public boolean insert(String sql){
        ArrayList<String> responses = sendRequestToServer(sql, "String", "insert");
        if(responses.size()>1)
            System.out.println("Multiple return values received while expecting 1.");
        return responses.get(0).equalsIgnoreCase("success");
    }

    /**
     * Op dit moment alleen gebruikt door getEnqueteWeights. Vandaar de 5 elementen restrictie.
     * @param sql String van de query.
     * @return doubles
     */
    private double [] queryDoubles(String sql){
        ArrayList<String> responses = sendRequestToServer(sql, "doubles", "query");
        if(responses.get(0).startsWith("FAIL"))
            return null;
        double [] doubles = new double[5];
        for(int i = 0; i < 5; i++)
            doubles[i] = Double.parseDouble(responses.get(i));
        return doubles;
    }

    private int queryInt(String sql){
        ArrayList<String> responses = sendRequestToServer(sql, "int", "query");
        if(responses.size()>1)
            System.out.println("Multiple return values received while expecting 1.");
        if(responses.get(0).startsWith("FAIL"))
            return -1;
        return Integer.parseInt(responses.get(0));
    }

    private String queryString(String sql){
        ArrayList<String> responses = sendRequestToServer(sql, "String", "query");
        if(responses.size()>1)
            System.out.println("Multiple return values received while expecting 1.");
        return responses.get(0);
    }

    private ArrayList<String> sendRequestToServer(String sql, String returnType, String action){
        responseStrings = null;
        new Requester().execute(sql, returnType, action);
        while(responseStrings == null){
            try {
                System.out.println("Putting thread to sleep  for 400s");
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return responseStrings;
    }

    @Override//Werkt
    public String getAccessToken(String userEmail) {
        return queryString(String.format("SELECT accesstoken FROM users WHERE email = '%s' LIMIT 1;", userEmail));
    }

    @Override//Werkt
    public String getPassword(String userEmail) {
        return queryString(String.format("SELECT password FROM users WHERE email = '%s' LIMIT 1;", userEmail));
    }

    @Override // Werkt
    public double [] getEnqueteWeights(String userEmail) {
        return queryDoubles(String.format("SELECT weight FROM enqueteWeights WHERE userEmail = '%s' LIMIT 5;", userEmail));
    }

    @Override // Werkt
    public String getLastMessageTo(String userEmail) {
        return queryString(String.format("SELECT message FROM sentmessages WHERE userEmail = '%s' ORDER BY timestampID DESC LIMIT 1;", userEmail));
    }

    @Override // Werkt
    public int stepsTakenBetween(String userEmail, Timestamp startTime, Timestamp endTime) {
        return queryInt(String.format("SELECT sum(nrsteps) FROM stepslog WHERE userEmail = '%s' AND starttime > '%s' AND endtime < '%s';", userEmail, startTime, endTime));
    }

    @Override //Werkt
    public boolean sendStepLog(String userEmail, int nrOfSteps,
                               Timestamp startTime, Timestamp endTime) {
        return insert(String.format("INSERT INTO stepslog (userEmail, nrsteps, starttime, endtime) VALUES ('%s', %d, '%s', '%s');", userEmail, nrOfSteps, startTime, endTime));
    }

    @Override //Werkt
    public boolean messageSent(String userEmail, String message, int response,
                               Timestamp time) {
        return insert(String.format("INSERT INTO sentmessages (userEmail, message, response, timeSent) VALUES ('%s','%s', %d,'%s');", userEmail, message, response, time));
    }

    @Override // Werkt
    public boolean newUser(String userEmail, String accesstoken, String password) {
        return insert(String.format("INSERT INTO users (email, password, accesstoken) VALUES ('%s', '%s', '%s');", userEmail, password, accesstoken));
    }

    @Override
    public LinkedList<PersuasivePart> getAllMessages() {
        ArrayList<String> content = sendRequestToServer("SELECT message FROM messages", "String", "query");
        ArrayList<String> types = sendRequestToServer("SELECT type FROM messages", "String", "query");
        LinkedList<PersuasivePart> messages = new LinkedList<PersuasivePart>();
        for(int i = 0; i < content.size(); i++){
            PersuasionType type = PersuasionType.valueOf(types.get(i).toUpperCase());
            messages.add(new PersuasivePart(content.get(i), type));
        }
        return messages;
    }

    @Override // Werkt
    public boolean setMessageResponse(String userEmail, Timestamp responseTime, int response) {
        return insert(String.format("UPDATE sentmessages SET timeResponded = '%s', response = %d WHERE userEmail = '%s' ORDER BY timestampID DESC LIMIT 1;", responseTime, response, userEmail));

    }


    @Override //Werkt
    public boolean setEnqueteWeights(String userEmail, double[] weights) {
        String template = "INSERT INTO enqueteWeights (userEmail, type, weight) VALUES ('%s', '%s', %1.2f);";
        boolean b1 = insert(String.format(Locale.US, template, userEmail, "Consensus" , weights[0]));
        boolean b2 = insert(String.format(Locale.US, template, userEmail, "Authority" , weights[1]));
        boolean b3 = insert(String.format(Locale.US, template, userEmail, "Scarcity"  , weights[2]));
        boolean b4 = insert(String.format(Locale.US, template, userEmail, "Commitment", weights[3]));
        boolean b5 = insert(String.format(Locale.US, template, userEmail, "Funny"     , weights[4]));
        if(b1 && b2 && b3 && b4 && b5)
            return true;
        else
            return false;
    }

    private class Requester extends AsyncTask<String, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> responses = new ArrayList<String>();
            try {
                BufferedReader serverResponse = actuallySendRequest(String.format("dbAction=%s&returnType=%s&args=%s", params[2], params[1], params[0]));
                String nextResponse;
                while ((nextResponse = serverResponse.readLine()) != null) {
                    if (nextResponse.startsWith("return:"))
                        responses.add(nextResponse.substring(7));
                    System.out.println(nextResponse);
                }
                serverResponse.close();

            }catch (UnknownHostException e){
                responses = new ArrayList<String>();
                responses.add("FAIL due to UnknownHostException. Likely due to no internet connection.");
                System.out.println(responses.get(0));
            }   catch (IOException e) {
                e.printStackTrace();
            }
            if(responses.isEmpty()){
                System.out.println("DB response not recognized. Query failed.");
            }
            responseStrings = responses;
            return responses;
        }
    }
}

