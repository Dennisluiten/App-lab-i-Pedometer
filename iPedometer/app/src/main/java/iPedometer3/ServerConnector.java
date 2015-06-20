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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;


public class ServerConnector implements ServerInterface{

    private String urlstring = "http://applab.ai.ru.nl:8080/teamD/ServletTest2";
    private URLConnection conn;
    private BufferedWriter writeToServer;
    private String queryTemplate = String.format("SELECT returns FROM table WHERE conditions;");
    private String insertTemplate = String.format("INSERT INTO table (variables) VALUES (values);");
    private boolean serverResponded = false;
    protected BufferedReader serverResponse;


    public static void main(String[] args) throws MalformedURLException, IOException {
        new ServerConnector();
        System.out.println("Process terminated.");
    }

    public ServerConnector(){
    }

    private void sendRequestToServer(String request) throws IOException{
        serverResponded = false;
        Requester  r = new Requester();
        r.execute(request);
        while(!serverResponded){
            System.out.println("Thread going to sleep.");
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean insert(String sql){
        try {
            sendRequestToServer("dbAction=insert&args="+sql);
            String response;
            if(serverResponse == null)
                System.out.println("bufferedReader: serverResponse = null");
            while ( (response = serverResponse.readLine()) != null ) {
                System.out.println( response );
                if(response.contains("successful")){
                    serverResponse.close();
                    return true;
                }else if (response.contains("failed")){
                    serverResponse.close();
                    return false;
                }
            }
            serverResponse.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("DB response not recognized. Assuming fail.");
        return false;
    }

    public ArrayList<Double> queryArray(String sql){
        ArrayList<Double> returnValues= new ArrayList<Double>();
        try {
            sendRequestToServer("dbAction=query&returnType=doubles&args="+sql);
            String response;
            while ( (response = serverResponse.readLine()) != null ) {
                if(response.startsWith("return:")){
                    returnValues.add(Double.parseDouble(response.substring(7)));
                }
                System.out.println( response );
            }
            serverResponse.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(returnValues.isEmpty())
            System.out.println("DB response not recognized. (arraylist double) Query failed.");
        return returnValues;
    }

    public int queryInt(String sql){
        try {
            sendRequestToServer("dbAction=query&returnType=int&args="+sql);
            String response;
            while ( (response = serverResponse.readLine()) != null ) {
                if(response.startsWith("return:")){
                    serverResponse.close();
                    return Integer.parseInt(response.substring(7));
                }
                System.out.println( response );
            }
            serverResponse.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("DB response not recognized. Query failed.");
        return -1;
    }

    public String queryString(String sql, String returnType){
        try {
            sendRequestToServer("dbAction=query&returnType=" + returnType +"&args="+sql);
            String response;
            while ( (response = serverResponse.readLine()) != null ) {
                if(response.startsWith("return:"))
                    return response.substring(7);
                System.out.println( response );
            }
            serverResponse.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("DB response not recognized. Query failed.");
        return null;
    }

    public String queryString(String sql){
        return queryString(sql, "String");
    }

    @Override//Werkt
    public String getAccessToken(String userEmail) {
        return queryString(String.format("SELECT accesstoken FROM users WHERE email = '%s';", userEmail));
    }

    @Override//Werkt
    public String getPassword(String userEmail) {
        return queryString(String.format("SELECT password FROM users WHERE email = '%s';", userEmail));
    }

    @Override // Werkt
    public ArrayList<Double> getEnqueteWeights(String userEmail) {
        return queryArray(String.format("SELECT weight FROM enqueteWeights WHERE userEmail = '%s';", userEmail));
    }



    @Override // Werkt
    public String getLastMessageTo(String userEmail) {
        return queryString(String.format("SELECT message FROM sentmessages WHERE userEmail = '%s' ORDER BY timestampID DESC LIMIT 1;", userEmail));
    }

    @Override // Werkt
    public int stepsTakenBetween(String userEmail, Timestamp startTime, Timestamp endTime) {
        return queryInt(String.format("SELECT sum(nrsteps) FROM stepslog WHERE userEmail = '%s' AND starttime > '%s' AND endtime < '%s';", userEmail, startTime, endTime ));
    }

    @Override //Werkt
    public boolean sendStepLog(String userEmail, int nrOfSteps,
                               Timestamp startTime, Timestamp endTime) {
        return insert(String.format("INSERT INTO stepslog (userEmail, nrsteps, starttime, endtime) VALUES ('%s', %d, '%s', '%s');", userEmail, nrOfSteps, startTime, endTime));
    }

    @Override //Werkt
    public boolean messageSent(String userEmail, String message, int response,
                               Timestamp time) {
        System.out.println(String.format("INSERT INTO sentmessages (userEmail, message, response, time) VALUES ('%s','%s', %d,'%s')",userEmail, message, response, time));
        return insert(String.format("INSERT INTO sentmessages (userEmail, message, response, time) VALUES ('%s','%s', %d,'%s');",userEmail, message, response, time));
    }

    @Override // Werkt
    public boolean newUser(String userEmail, String accesstoken, String password) {
        return insert(String.format("INSERT INTO users (email, password, accesstoken) VALUES ('%s', '%s', '%s');", userEmail, password, accesstoken));
    }

    @Override
    public LinkedList<PersuasivePart>[] getAllMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override // TODO check of dit nog steeds werkt.
    public boolean setMessageResponse(String userEmail, Timestamp responseTime, int response) {
        return insert(String.format("UPDATE sentmessage SET responseTime = %s, response = %d WHERE userEmail = '%s' ORDER BY timestampID DESC LIMIT 1;", responseTime, response, userEmail));

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
    private class Requester extends AsyncTask<String, Void, BufferedReader>{

        public Requester(){
            super();
        }

        @Override
        protected BufferedReader doInBackground(String... params) {
            System.out.println("doInBackground starting");
            try {
                URL url = new URL(urlstring);
                conn = url.openConnection();
                conn.setDoOutput(true);
                writeToServer = new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ) );
                String request = "";
                for (String param : params) {
                    request = param;
                }

                writeToServer.write(request);
                writeToServer.flush();
                writeToServer.close();
                serverResponse =  new BufferedReader( new InputStreamReader( conn.getInputStream() ) );

            } catch (IOException e) {
                e.printStackTrace();
            }
            serverResponded = true;
            System.out.println("doInbackground finished");
            return null;
        }
    }
}

