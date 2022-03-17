package pat.international.playwaretwo.GameHitTheTarget;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import pat.international.playwaretwo.ChaseTheLight.ChaseTheLightClass;
import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.R;

public class GameHitTheTargetStart extends Fragment implements OnAntEventListener, GameCountObserver {

    MotoConnection connection = MotoConnection.getInstance();
    LinearLayout gt_container;

    GameHitTheTarget gameHitTheTarget; // ChaseTheLightClass object
    TextView gameText,tilesNumText,scoreText;
    Button endGameBtn;
    int score = 0;
    NavController nav;
    View v;
    boolean register = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_game_six, container, false);

        endGameBtn = v.findViewById(R.id.button_end);
        gameText = v.findViewById(R.id.textGame);
        tilesNumText = v.findViewById(R.id.textNumTiles);
        gt_container = v.findViewById(R.id.game_type_container);
        scoreText = v.findViewById(R.id.textScore);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connection.startMotoConnection(getContext());
        connection.registerListener(this);
        nav = Navigation.findNavController(view);
        gt_container.setVisibility(View.VISIBLE);
        gameText.setText("Select a mode!");
        endGameBtn.setOnClickListener(v->{
            endGame();
            nav.popBackStack();
        });
        scoreText.setText(String.valueOf(score));
        gameHitTheTarget = new GameHitTheTarget(getContext(),this);
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
        gameHitTheTarget.stopGame();
    }
    private void tilesExecution(){
        for (final GameType gt : gameHitTheTarget.getGameTypes())
        {
            Button b = new Button(getContext());
            b.setText(gt.getName());
            b.setOnClickListener(v -> {
                gameHitTheTarget.selectedGameType = gt;
                gameText.setText("Game is running");
                gt_container.setVisibility(View.GONE);
                gameHitTheTarget.startGame();
                register = true;
            });
            gt_container.addView(b);
        }
        gameHitTheTarget.setOnGameEventListener(new ChaseTheLightClass.OnGameEventListener()
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
                gameHitTheTarget.stopGame();
                GameHitTheTargetStartDirections.ActionStartGame6ToEndChase action = GameHitTheTargetStartDirections.actionStartGame6ToEndChase();
                action.setGameCount(score);
                Navigation.findNavController(v).navigate(action);
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
        gameHitTheTarget.addEvent(bytes);
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
