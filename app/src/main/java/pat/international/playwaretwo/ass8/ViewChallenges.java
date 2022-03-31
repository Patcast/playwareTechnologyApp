package pat.international.playwaretwo.ass8;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ViewChallenges extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection;

    TextView apiOutput;
    Button simulateGetGameSessions, simulatePostGameSession;
    String endpoint = "https://centerforplayware.com/api/index.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_challenges);

        connection = MotoConnection.getInstance();
        connection.startMotoConnection(this);
        connection.saveRfFrequency(76);
        connection.setDeviceId(7);
        connection.registerListener(this);

        apiOutput = findViewById(R.id.apiOutput);


        simulateGetGameSessions = findViewById(R.id.simulateGetGameSessions);
        simulateGetGameSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGameSessions();
            }
        });

        simulatePostGameSession = findViewById(R.id.simulatePostGameSession);
        simulatePostGameSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                postGameSession();


            }
        });
    }


    private void postGameSession() {
        com.example.list.RemoteHttpRequest requestPackage = new com.example.list.RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method", "postGameSession"); // The method name
        requestPackage.setParam("group_id", "99"); // Your group ID
        requestPackage.setParam("game_id", "1"); // The game ID (From the Game class > setGameId() function
        requestPackage.setParam("game_type_id", "1"); // The game type ID (From the GameType class creation > first parameter)
        requestPackage.setParam("game_score", "30"); // The game score
        requestPackage.setParam("game_time", "60"); // The game elapsed time in seconds
        requestPackage.setParam("num_tiles", "4"); // The number of tiles used

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below

        downloader.execute(requestPackage);
    }

    private void getGameSessions() {
        com.example.list.RemoteHttpRequest requestPackage = new com.example.list.RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method", "getGameSessions");
        requestPackage.setParam("group_id", "99");

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below

        downloader.execute(requestPackage);
    }

    private class Downloader extends AsyncTask<com.example.list.RemoteHttpRequest, String, String> {
        @Override
        protected String doInBackground(com.example.list.RemoteHttpRequest... params) {
            return com.example.list.HttpManager.getData(params[0]);
        }

        //The String that is returned in the doInBackground() method is sent to the
        // onPostExecute() method below. The String should contain JSON data.
        @Override
        protected void onPostExecute(String result) {
            try {
                //We need to convert the string in result to a JSONObject
                JSONObject jsonObject = new JSONObject(result);
                String message = jsonObject.getString("message");

                // Update UI
                apiOutput.setText(message);
                Log.i("sessions", message);


                if (jsonObject.getString("method") == "getGameSessions") {

                    JSONArray sessions = jsonObject.getJSONArray("results");
                    for (int i = 0; i < sessions.length(); i++) {
                        JSONObject session = sessions.getJSONObject(i);
                        Log.i("sessions", session.toString());

                        // get score example:
                        // String score = session.getString("game_score");

                    }

                } else if (jsonObject.getString("method") == "postGameSession") {

                    Log.i("sessions", message);

                    // Update UI


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onMessageReceived(byte[] bytes, long l) {

    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }
}
