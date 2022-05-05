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
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import pat.international.playwaretwo.ChaseTheLight.ChaseTheLightClass;
import pat.international.playwaretwo.ColorCall.ColorCall;
import pat.international.playwaretwo.FinalCountdown.FinalCountDown;
import pat.international.playwaretwo.FinalCountdown.FragmentFinalCountdownStartDirections;
import pat.international.playwaretwo.GameColorObserver;
import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.R;


public class PianoTiles extends Fragment implements OnAntEventListener, GameCountObserver, GameColorObserver {

    TextView scoreText;
    TextView colorText;
    Button endGameBtn;
    int score = 0;
    int color = 0;
    NavController nav;
    boolean register = false;
    View v;
    MotoConnection connection = MotoConnection.getInstance();
    PianoTilesGame PianoTilesGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.piano_tiles_screen, container, false);
        endGameBtn = v.findViewById(R.id.button_end);
        scoreText = v.findViewById(R.id.textScore);
        colorText = v.findViewById(R.id.textColor);
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
        PianoTilesGame = new PianoTilesGame(getContext(), this, this);
        tilesExecution();
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
        PianoTilesGame.stopGame();
    }

    private void tilesExecution(){
        PianoTilesGame.selectedGameType = PianoTilesGame.getGameTypes().get(0);
        PianoTilesGame.startGame();
        register = true;
        PianoTilesGame.setOnGameEventListener(new ChaseTheLightClass.OnGameEventListener()
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
                PianoTilesGame.stopGame();

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
        PianoTilesGame.addEvent(bytes);
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

    public void notifyColor(int new_color) {
        updateColor(new_color);
    }
    private void updateColor(int new_color){
        getActivity().runOnUiThread(() -> {
            color= new_color;
            colorText.setText(String.valueOf(color));
        });
    }
}