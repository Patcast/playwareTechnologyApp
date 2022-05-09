package pat.international.playwaretwo.PianoTiles.GameCode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import pat.international.playwaretwo.ass8.HttpManager;
import pat.international.playwaretwo.ass8.ListMain;
import pat.international.playwaretwo.ass8.RemoteHttpRequest;

public enum DataBasePlayWare {
    INSTANCE;
    private final int  GROUP_ID = 123456;
    String endpoint = "https://centerforplayware.com/api/index.php";
    SharedPreferences sharedPref;
    private final ArrayList<Score> challengesList = new ArrayList<>();
    ArrayList<Score> nonFilteredChallenges= new ArrayList<>();
    Context context;

    public void setContext(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);
    }

    public void requestData(Context context){
        this.context = context;
        challengesList.clear();
        nonFilteredChallenges.clear();
        getGameChallenge();
    }

    private void getGameChallenge() {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","getGameChallenge"); // The method name
        requestPackage.setParam("device_token",getDeviceToken()); // Your device token
        requestPackage.setParam("group_id",String.valueOf(GROUP_ID)); // Your group ID

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below
        downloader.execute(requestPackage);
    }
    public void postGameChallenge(int score,String name) {


        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameChallenge"); // The method name
        requestPackage.setParam("device_token",getDeviceToken()); // Your device token
        requestPackage.setParam("game_type_id","1"); // The game type ID (From the GameType class creation > first parameter)
        requestPackage.setParam("challenger_name",name); // The challenger name
        requestPackage.setParam("group_id",String.valueOf(GROUP_ID));
        requestPackage.setParam("game_id",String.valueOf(score));

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below

        downloader.execute(requestPackage);
    }

    private String getDeviceToken() {
        // Get unique device_token from shared preferences
        // Remember that what is saved in sharedPref exists until you delete the app!
        String device_token = sharedPref.getString("device_token",null);

        if(device_token == null) { // If device_token was never saved and null create one
            device_token =  UUID.randomUUID().toString(); // Get a new device_token
            sharedPref.edit().putString("device_token",device_token).apply(); // save it to shared preferences so next time will be used
        }

        return device_token;
    }

    public ArrayList<Score> getChallengesList() {
        return challengesList;
    }

    private class Downloader extends AsyncTask<RemoteHttpRequest, String, String> {
        @Override
        protected String doInBackground(RemoteHttpRequest... params) {
            return HttpManager.getData(params[0]);
        }

        //The String that is returned in the doInBackground() method is sent to the
        // onPostExecute() method below. The String should contain JSON data.
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String result) {
            try {
                //We need to convert the string in result to a JSONObject
                JSONObject jsonObject = new JSONObject(result);

                String message = jsonObject.getString("message");
                Log.i("sessions",message);

                // Log the entire response if needed to check the data structure
                Log.i("sessions",jsonObject.toString());

                if(jsonObject.getString("method").equals("getGameChallenge")) {
                    JSONArray challenges = jsonObject.getJSONArray("results");
                    challengesList.clear();
                    nonFilteredChallenges.clear();

                    for(int i = 0; i < challenges.length();i++) {
                        JSONObject challenge = challenges.getJSONObject(i);
                        if(challenge.getInt("group_id")==GROUP_ID){
                            Score s = new Score(0,
                                    challenge.getInt("game_id"),// score
                                    challenge.getString("challenger_name"),
                                    "");
                            s.setDifficulty(challenge.getInt("game_type_id")); /// Set difficulty
                            nonFilteredChallenges.add(s);
                        }

                    }

                    List<Score> filteredChallenges= nonFilteredChallenges.stream()
                            .sorted(Comparator.comparingInt(Score::getScore))
                            .collect(Collectors.toList());

                    challengesList.addAll(filteredChallenges);
                    Collections.reverse(challengesList);

                }
                if(jsonObject.getString("method").equals("postGameChallenge")){
                    Toast.makeText(context, "Score saved!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
