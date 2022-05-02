package pat.international.playwaretwo.ChaseTheLight;

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

import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.HomeActivity;
import pat.international.playwaretwo.R;


public class StartChase extends Fragment implements OnAntEventListener, GameCountObserver {


    MotoConnection connection = MotoConnection.getInstance();
    LinearLayout gt_container;
    ChaseTheLightClass chaseTheLightClass; // ChaseTheLightClass object
    TextView gameText,tilesNumText,scoreText;
    Button endGameBtn;
    int score = 0;
    NavController nav;
    View v;
    boolean register = false;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_chase, container, false);

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
        chaseTheLightClass = new ChaseTheLightClass(getContext(),this);
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
        chaseTheLightClass.stopGame();
    }


    private void tilesExecution(){
        for (final GameType gt : chaseTheLightClass.getGameTypes())
        {
            Button b = new Button(getContext());
            b.setText(gt.getName());
            b.setOnClickListener(v -> {
                    chaseTheLightClass.selectedGameType = gt;
                    gameText.setText("Game is running");
                    gt_container.setVisibility(View.GONE);
                    chaseTheLightClass.startGame();
                    register = true;
            });
            gt_container.addView(b);
        }
        chaseTheLightClass.setOnGameEventListener(new ChaseTheLightClass.OnGameEventListener()
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
                chaseTheLightClass.stopGame();
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
        chaseTheLightClass.addEvent(bytes);
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