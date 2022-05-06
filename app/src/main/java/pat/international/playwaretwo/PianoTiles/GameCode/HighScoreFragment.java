package pat.international.playwaretwo.PianoTiles.GameCode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import pat.international.playwaretwo.R;
import pat.international.playwaretwo.ass8.Challenge;
import pat.international.playwaretwo.ass8.ChallengesAdapter;
import pat.international.playwaretwo.ass8.HttpManager;
import pat.international.playwaretwo.ass8.ListMain;
import pat.international.playwaretwo.ass8.RemoteHttpRequest;

public class HighScoreFragment extends Fragment implements HighScoreFragmentPresenter.IHighScoreFragment{
    HighScoreFragmentPresenter highScoreFragmentPresenter;
    ListAdapter adapterList;
    ListView listView;
    String endpoint = "https://centerforplayware.com/api/index.php";
    SharedPreferences sharedPref;
    private final ArrayList<Score> challengesList = new ArrayList<>();
    ArrayList<Score> nonFilteredChallenges= new ArrayList<>();


    public HighScoreFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        sharedPref = getContext().getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);
        getGameChallenge();



        this.adapterList = new ListAdapter(this.getActivity());
        this.listView = view.findViewById(R.id.lst_fragment_score);
        this.listView.setAdapter(this.adapterList);
        this.highScoreFragmentPresenter = new HighScoreFragmentPresenter(this);

        return view;
    }

    @Override
    public void updateList(List<Score> list) {
        this.adapterList.updateList(list);
    }

    private void getGameChallenge() {
        RemoteHttpRequest requestPackage = new RemoteHttpRequest();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(endpoint);
        requestPackage.setParam("method","getGameChallenge"); // The method name
        requestPackage.setParam("device_token",getDeviceToken()); // Your device token
        requestPackage.setParam("group_id","69"); // Your group ID

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




                if(jsonObject.getString("method").equals("getGameChallenge")) {
                    JSONArray challenges = jsonObject.getJSONArray("results");
                    challengesList.clear();

                    for(int i = 0; i < challenges.length();i++) {
                        JSONObject challenge = challenges.getJSONObject(i);
                        Score s = new Score(
                                challenge.getInt("game_id"),
                                challenge.getInt("c_status"),// score
                                challenge.getString("challenger_name"),
                                "");// difficulty
                        s.setDifficulty(challenge.getInt("game_type_id")); /// Set difficulty
                        nonFilteredChallenges.add(s);
                    }

                    /*List<Challenge> filteredChallenges= nonFilteredChallenges.stream()
                            .filter(i-> i.getGroupId()==69)
                            .collect(Collectors.toList());
                    challengesList.addAll(filteredChallenges);*/
                    highScoreFragmentPresenter.loadData(nonFilteredChallenges);
                }
                if(jsonObject.getString("method").equals("postGameChallenge")){
                    Toast.makeText(getContext(), "A new challenge was created", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
