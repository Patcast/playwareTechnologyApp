package pat.international.playwaretwo.ass8;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pat.international.playwaretwo.R;


public class ListMain extends Fragment {

    private ChallengesAdapter adapter;
    String endpoint = "https://centerforplayware.com/api/index.php";
    Button simulateGetGameChallenge;
    SharedPreferences sharedPref;
    private ArrayList<Challenge> challengesList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPref = getContext().getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);

        return inflater.inflate(R.layout.fragment_list_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        simulateGetGameChallenge = view.findViewById(R.id.get_challenge_btn);



        RecyclerView recyclerMicros = view.findViewById(R.id.recyclerView_games);
        recyclerMicros.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new ChallengesAdapter();
        recyclerMicros.setAdapter(adapter);


        simulateGetGameChallenge.setOnClickListener(v -> getGameChallenge());

    }

    private void getGameChallenge() {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","getGameChallenge"); // The method name
        requestPackage.setParam("device_token",getDeviceToken()); // Your device token

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

                // Log response
                Log.i("sessions","response: "+jsonObject.getBoolean("response"));
                // Update UI


                if(jsonObject.getString("method").equals("getGameChallenge")) {
                    JSONArray challenges = jsonObject.getJSONArray("results");
                    challengesList.clear();
                    ArrayList<Challenge> nonFilteredChallenges= new ArrayList<>();
                    for(int i = 0; i < challenges.length();i++) {
                        JSONObject challenge = challenges.getJSONObject(i);
                        Log.i("challenge",challenge.toString());
                        Challenge ch = new Challenge(
                                                challenge.getString("challenger_name"),
                                                challenge.getString("challenged_name"),
                                                challenge.getInt("game_id"),
                                                challenge.getInt("c_status"),challenge.getInt("group_id"));
                        nonFilteredChallenges.add(ch);
                    }
                    // Update UI
                    List<Challenge> filteredChallenges= nonFilteredChallenges.stream()
                                            .filter(i-> i.getGroupId()==69)
                                            .collect(Collectors.toList());
                    challengesList.addAll(filteredChallenges);
                    adapter.setSuperGameAdapter(challengesList);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

