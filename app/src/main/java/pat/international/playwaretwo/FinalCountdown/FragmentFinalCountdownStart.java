package pat.international.playwaretwo.FinalCountdown;

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
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import pat.international.playwaretwo.ChaseTheLight.ChaseTheLightClass;
import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.R;


public class FragmentFinalCountdownStart extends Fragment implements OnAntEventListener, GameCountObserver {

    TextView scoreText;
    Button endGameBtn;
    Chronometer simpleChronometer;
    int score = 0;
    NavController nav;
    boolean register = false;
    View v;
    MotoConnection connection = MotoConnection.getInstance();
    FinalCountDown finalCountDown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_game_four, container, false);
        simpleChronometer = (Chronometer) v.findViewById(R.id.simpleChronometer); // initiate a chronometer
        endGameBtn = v.findViewById(R.id.button_end);
        scoreText = v.findViewById(R.id.textScore);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connection.startMotoConnection(getContext());
        connection.registerListener(this);
        nav = Navigation.findNavController(view);
        endGameBtn.setOnClickListener(v->{
            endGame();
            nav.popBackStack();
        });
        finalCountDown = new FinalCountDown(getContext(),this);
        tilesExecution();
        finalCountDown.selectedGameType = finalCountDown.getGameTypes().get(0);
        finalCountDown.startGame();
        simpleChronometer.start(); // start a chronometer
        register = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!register){
            //connection.startMotoConnection(getContext());
            connection.registerListener(this);
            register = true;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        register = false;
        //connection.stopMotoConnection();
        connection.unregisterListener(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        endGame();
    }
    private void endGame(){
        //connection.stopMotoConnection();
        connection.unregisterListener(this);
        finalCountDown.stopGame();
    }

    private void tilesExecution(){
        finalCountDown.setOnGameEventListener(new ChaseTheLightClass.OnGameEventListener()
        {
            @Override
            public void onGameTimerEvent(int i)
            {
            }
            @Override
            public void onGameScoreEvent(int i, int i1)
            {
            }
            @Override
            public void onGameStopEvent()
            {
                finalCountDown.stopGame();
                CharSequence time = simpleChronometer.getText();
                simpleChronometer.stop();
                FragmentFinalCountdownStartDirections.ActionStartGame4ToEndChase action = FragmentFinalCountdownStartDirections.actionStartGame4ToEndChase();
                action.setGameCount(score);
                action.setTimeResult((String) time);

                getActivity().runOnUiThread(() -> { Navigation.findNavController(v).navigate(action); });


            }

            @Override
            public void onSetupMessage(String s)
            {
            }
            @Override
            public void onGameMessage(String s)
            {

            }
            @Override
            public void onSetupEnd()
            {

            } });
    }


    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        finalCountDown.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }
    @Override
    public void notifyCount(int count) {
        updateCount(count);
    }
    private void updateCount(int count){
        getActivity().runOnUiThread(() -> {
            score= count;
            scoreText.setText(String.valueOf(score));
        });
    }
}