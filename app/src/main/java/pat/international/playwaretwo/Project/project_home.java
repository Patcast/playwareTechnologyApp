package pat.international.playwaretwo.Project;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import pat.international.playwaretwo.PianoTiles.GameCode.DataBasePlayWare;
import pat.international.playwaretwo.R;


public class project_home extends Fragment implements OnAntEventListener {


    private MotoConnection connection;
    private boolean isPairing = false;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connection = MotoConnection.getInstance();
        connection.startMotoConnection(getContext());
        connection.saveRfFrequency(68); // See the back of your tile for your group’s RF
        connection.setDeviceId(7); //Your group number

        Button startGame = view.findViewById(R.id.startgame);
        Button startLeaderboard = view.findViewById(R.id.leaderboard);
        nav = Navigation.findNavController(view);
        startGame.setOnClickListener(v-> startGamePiano());
        startLeaderboard.setOnClickListener(v-> startLeaderboardScreen());
        DataBasePlayWare.INSTANCE.setContext(getContext());
    }



    private void startGamePiano(){
        nav.navigate(R.id.action_startProject_to_homeFragment2);
    }

    private void startLeaderboardScreen(){
        nav.navigate(R.id.action_startProject_to_highScoreFragment);
    }

    private void startChallengesScreen(){
        //nav.navigate(R.id.action_startProject_to_startGameProject);
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

    @Override
    public void onAntServiceConnected() {
        connection.setAllTilesToInit();
    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }


}