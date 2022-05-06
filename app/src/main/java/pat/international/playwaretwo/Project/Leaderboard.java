package pat.international.playwaretwo.Project;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import pat.international.playwaretwo.R;
import pat.international.playwaretwo.Project.Challenge;
import pat.international.playwaretwo.Project.ChallengesAdapter;
import pat.international.playwaretwo.Project.HttpManager;
import pat.international.playwaretwo.Project.Leaderboard;
import pat.international.playwaretwo.Project.RemoteHttpRequest;

public class Leaderboard extends Fragment implements OnAntEventListener {

    private MotoConnection connection;
    private boolean isPairing = false;
    private TextView tilesNumText;
    private int numOfTiles;
    private NavController nav;
    Spinner spin;
    String value_level;

    private ChallengesAdapter adapter;
    String endpoint = "https://centerforplayware.com/api/index.php";
    Button simulateGetGameChallenge, postGameChallenge;
    SharedPreferences sharedPref;
    private ArrayList<Challenge> challengesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_main, container, false);
        sharedPref = getContext().getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connection = MotoConnection.getInstance();
        connection.startMotoConnection(getContext());
        connection.saveRfFrequency(68); // See the back of your tile for your group’s RF
        connection.setDeviceId(7); //Your group number
        //connection.registerListener(this);
        //get the spinner from the xml.
        simulateGetGameChallenge = view.findViewById(R.id.get_challenge_btn);
        postGameChallenge= view.findViewById(R.id.createChallenge_btn);


        RecyclerView recyclerMicros = view.findViewById(R.id.recyclerView_games);
        recyclerMicros.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new ChallengesAdapter();
        recyclerMicros.setAdapter(adapter);


        simulateGetGameChallenge.setOnClickListener(v -> getGameChallenge());
        postGameChallenge.setOnClickListener(v -> postGameChallenge());


    }


    private void getGameChallenge() {
        pat.international.playwaretwo.Project.RemoteHttpRequest requestPackage = new pat.international.playwaretwo.Project.RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","getGameChallenge"); // The method name
        requestPackage.setParam("device_token",getDeviceToken()); // Your device token
        requestPackage.setParam("group_id","1"); // Your group ID

        Leaderboard.Downloader downloader = new Leaderboard.Downloader(); //Instantiation of the Async task
        //that’s defined below
        downloader.execute(requestPackage);
    }

    private void postGameChallenge() {

        pat.international.playwaretwo.Project.RemoteHttpRequest requestPackage = new pat.international.playwaretwo.Project.RemoteHttpRequest();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","postGameChallenge"); // The method name
        requestPackage.setParam("device_token",getDeviceToken()); // Your device token
        requestPackage.setParam("game_id","1"); // The game ID (From the Game class > setGameId() function
        requestPackage.setParam("game_type_id","1"); // The game type ID (From the GameType class creation > first parameter)
        requestPackage.setParam("challenger_name","Lolo"); // The challenger name
        requestPackage.setParam("group_id","1");

        Leaderboard.Downloader downloader = new Leaderboard.Downloader(); //Instantiation of the Async task
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

    private class Downloader extends AsyncTask<pat.international.playwaretwo.Project.RemoteHttpRequest, String, String> {
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
                    ArrayList<pat.international.playwaretwo.Project.Challenge> nonFilteredChallenges= new ArrayList<>();
                    for(int i = 0; i < challenges.length();i++) {
                        JSONObject challenge = challenges.getJSONObject(i);
                        pat.international.playwaretwo.Project.Challenge ch = new pat.international.playwaretwo.Project.Challenge(
                                challenge.getString("challenger_name"),
                                challenge.getString("challenged_name"),
                                challenge.getInt("game_id"),
                                challenge.getInt("c_status"),
                                challenge.getInt("group_id"), challenge.getString("created"));
                        nonFilteredChallenges.add(ch);
                    }
                    // Update UI
                    List<Challenge> filteredChallenges= nonFilteredChallenges.stream()
                            .filter(i-> i.getGroupId()==1)
                            .collect(Collectors.toList());
                    challengesList.addAll(filteredChallenges);
                    adapter.setSuperGameAdapter(challengesList);
                }
                if(jsonObject.getString("method").equals("postGameChallenge")){
                    Toast.makeText(getContext(), "A new challenge was created", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAntServiceConnected() {
        connection.setAllTilesToInit();
    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }

    // I have problems reestablishing the connection, how often do I need to pair.
    private void startGamePiano(){
        nav.navigate(R.id.action_startProject_to_startGameProject);
    }

    private void startLeaderboardScreen(){
        nav.navigate(R.id.action_startProject_to_startGameProject);
    }

    private void startChallengesScreen(){
        nav.navigate(R.id.action_startProject_to_startGameProject);
    }

    @Override
    public void onStart() {
        super.onStart();
        //connection.startMotoConnection(getContext());
        connection.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //connection.stopMotoConnection();
        connection.unregisterListener(this);
    }

    private void paring(Button b){
        if(!isPairing){
            connection.pairTilesStart();
            b.setText("Stop Pairing");
        } else {
            connection.pairTilesStop();
            b.setText("Start Pairing");
        }
        isPairing = !isPairing;
    }
    @Override
    public void onMessageReceived(byte[] bytes, long l) {

    }


}