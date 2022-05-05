package pat.international.playwaretwo.http;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GameSessionManager {
    public static String endpoint = "https://centerforplayware.com/api/index.php";


    public boolean postGameSession(GameSessionPostRequest request, String token) {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameSession"); // The method name
        requestPackage.setParam("device_token", token);
        requestPackage.setParam("group_id",request.getGroupId()); // Your group ID
        requestPackage.setParam("gcid", request.getGcid());
        requestPackage.setParam("game_id",request.getGameId()); // The game ID (From the Game class > setGameId() function
        requestPackage.setParam("game_type_id",request.getGameTypeId()); // The game type ID (From the GameType class creation > first parameter)
        requestPackage.setParam("game_score",request.getGameScore()); // The game score
        requestPackage.setParam("game_time",request.getGameTime()); // The game elapsed time in seconds
        requestPackage.setParam("num_tiles",request.getNumTiles()); // The number of tiles used

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below
        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return o.getBoolean("response");
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean postGameSession(GameSessionPostRequest request) {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameSession"); // The method name
        requestPackage.setParam("device_token", "group7");
        requestPackage.setParam("group_id",request.getGroupId()); // Your group ID
        requestPackage.setParam("game_id",request.getGameId()); // The game ID (From the Game class > setGameId() function
        requestPackage.setParam("game_type_id",request.getGameTypeId()); // The game type ID (From the GameType class creation > first parameter)
        requestPackage.setParam("game_score",request.getGameScore()); // The game score
        requestPackage.setParam("game_time",request.getGameTime()); // The game elapsed time in seconds
        requestPackage.setParam("num_tiles",request.getNumTiles()); // The number of tiles used

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below
        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return o.getBoolean("response");
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getGameSessions(int groupId) {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","getGameSessions");
        requestPackage.setParam("group_id", String.valueOf(groupId));

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below

        try {
            String result = downloader.execute(requestPackage).get();
            JSONObject o = new JSONObject(result);
            return this.makeListFromJObject(o);
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> makeListFromJObject(JSONObject obj) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        JSONArray sessions = obj.getJSONArray("results");
        for(int i = 0; i < sessions.length();i++) {
            StringBuilder sb = new StringBuilder();
            JSONObject session = sessions.getJSONObject(i);
            sb.append("Username: ");
            sb.append("\n");
            sb.append("Level: ").append(session.getString("game_id"));
            sb.append("\n");
            sb.append("Score: ").append(session.getString("game_score"));
            sb.append("\n");
            sb.append("Game Id: ").append(session.getString("sid"));
            list.add(sb.toString());
        }
        return list;
    }

    private class Downloader extends AsyncTask<RemoteHttpRequest, String, String> {

        @Override
        protected String doInBackground(RemoteHttpRequest... params) {
            return HttpManager.getData(params[0]);
        }
    }
}
