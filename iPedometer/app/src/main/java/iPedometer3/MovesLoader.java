package iPedometer3;

import android.util.Log;

import com.example.erikeppenhof.myapplication.Json;
import com.example.erikeppenhof.myapplication.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Loads Moves data from the Moves-API. *
 *
 * Created by Hans-Christiaan on 25-5-2015.
 */
public class MovesLoader {

    private String access_token = null;

    public MovesLoader(String access_token)
    {
        this.access_token = access_token;
    }

    public LinkedList<MovesBlock> getStoryLine(long day) throws InterruptedException, JSONException {
        LinkedList<MovesBlock> storyLine = new LinkedList<MovesBlock>();
        Json json = getJson(day);

        new Thread(json).start();
        //while(json.getJSONObject() == null) {
        //    wait();
        //}
        Thread.sleep(6000);

        JSONObject jsonObject = json.getJSONObject();

        ArrayList<String> type = json.parseType(jsonObject, "segments", "activities", "type");
        Log.d("MovesLoaderListType", String.valueOf(type.size()));
        ArrayList<String> activities = json.parseActivities(jsonObject, "segments", "activities", "group");
        Log.d("MovesLoaderListAct", String.valueOf(activities.size()));
        ArrayList<String> startTime = json.parseActivities(jsonObject, "segments", "activities", "startTime");
        Log.d("MovesLoaderListStart", String.valueOf(startTime.size()));
        ArrayList<String> endTime = json.parseActivities(jsonObject, "segments", "activities", "endTime");
        Log.d("MovesLoaderListEnd", String.valueOf(endTime.size()));

        try {
            storyLine = makeStoryLine(type, activities, startTime, endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return storyLine;
    }

    private LinkedList<MovesBlock> makeStoryLine(ArrayList<String> type, ArrayList<String> act, ArrayList<String> start, ArrayList<String> end) throws ParseException {
        LinkedList<MovesBlock> storyLine = new LinkedList<MovesBlock>();

        if ((type.size() == act.size()) && (act.size() == start.size()) && (start.size() == end.size())) {
            Log.d("MovesLoader", "Mooi");
            for (int i = 0 ; i < type.size() ; i++) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmssZ");
                Date st = (Date)sdf.parse(start.get(i));
                long starttime = st.getTime();
                Date et = (Date)sdf.parse(end.get(i));
                long endtime = et.getTime();
                Log.d("MovesLoader", "forloop");
                if (type.get(i).equals("move")) {
                    Log.d("MovesLoader", "if");
                    switch(act.get(i)) {
                        case "walking": storyLine.add(new MovesBlockMove(starttime, endtime, MovesBlockType.WALKING));
                            break;
                        case "transport": storyLine.add(new MovesBlockMove(starttime, endtime, MovesBlockType.TRANSPORT));
                            break;
                        case "running": storyLine.add(new MovesBlockMove(starttime, endtime, MovesBlockType.RUNNING));
                            break;
                        case "cycling": storyLine.add(new MovesBlockMove(starttime, endtime, MovesBlockType.CYCLING));
                            break;
                        default: Log.d("MovesLoaderStoryLine", act.get(i));
                            break;
                    }
                }
                else if (type.get(i).equals("place")) {
                    Log.d("MovesLoader", "else if");
                    switch(act.get(i)) {
                        case "home": storyLine.add(new MovesBlockPlace(starttime, endtime, MovesBlockType.HOME));
                            break;
                        case "school": storyLine.add(new MovesBlockPlace(starttime, endtime, MovesBlockType.SCHOOL));
                            break;
                        case "other": storyLine.add(new MovesBlockPlace(starttime, endtime, MovesBlockType.OTHER));
                            break;
                        case "work": storyLine.add(new MovesBlockPlace(starttime, endtime, MovesBlockType.WORK));
                            break;
                        default: Log.d("MovesLoaderStoryLine", act.get(i));
                            break;
                    }
                }
                else {
                    Log.d("MovesLoader", "else");
                }
                Log.d("MovesLoaderStoryLine", String.valueOf(storyLine.size()));
            }
        }
        return storyLine;
    }

    public Json getJson(long day){
        // Get date in right format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date();
        d.setTime(day);
        String date = sdf.format(d);
        // Make request
        String baseurl = "https://api.moves-app.com/api/1.1";
        String act = baseurl + "/user/activities/daily/" + date + "?access_token=" + access_token;
        //List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("access_token=", access_token));
        // Create JSONObject
        boolean get = true;
        boolean post = false;

        return new Json(act, get);
    }

}
