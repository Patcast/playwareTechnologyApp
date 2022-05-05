package pat.international.playwaretwo.ChaseTheLight;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
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

import com.livelife.motolibrary.AntData;
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
    private long time_press = 0;

    private String randomNote = "l";
    private String note_list[] = {"a","b","c","d","e","f","g"};

    private String partition[] = {"e","e","e","e",
            "e","e","e","g","c","d","e","f","f","f","f","e","e","e","e","e","d","d","e","d","g"};

    private Integer note_count = 0;

    private SoundPool soundPool;
    private Integer integerSoundIDa;
    private Integer integerSoundIDb;
    private Integer integerSoundIDc;
    private Integer integerSoundIDd;
    private Integer integerSoundIDe;
    private Integer integerSoundIDf;
    private Integer integerSoundIDg;

    private float floatSpeed = 1.0f;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_chase, container, false);

        endGameBtn = v.findViewById(R.id.button_end);
        gameText = v.findViewById(R.id.textGame);
        tilesNumText = v.findViewById(R.id.textNumTiles);
        gt_container = v.findViewById(R.id.game_type_container);
        scoreText = v.findViewById(R.id.textScore);

        SoundPool.Builder builder = new SoundPool.Builder();
        soundPool = builder.build();


//        integerSoundIDa = soundPool.load(getContext(), R.raw.a3, 1);
//        integerSoundIDb = soundPool.load(getContext(), R.raw.b3, 1);
//        integerSoundIDc = soundPool.load(getContext(), R.raw.c3, 1);
//        integerSoundIDd = soundPool.load(getContext(), R.raw.d3, 1);
//        integerSoundIDe = soundPool.load(getContext(), R.raw.e3, 1);
//        integerSoundIDf = soundPool.load(getContext(), R.raw.f3, 1);
//        integerSoundIDg = soundPool.load(getContext(), R.raw.g3, 1);

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

    public void button_play_note(float speed) {
        String randomNote = partition[note_count];
        note_count = note_count + 1;
        if (note_count > 24){
            note_count=0;
        }
        if (randomNote == "a") {

            soundPool.play(integerSoundIDa, 1, 1, 1, 0, speed*floatSpeed);
            soundPool.setLoop(integerSoundIDa,4);
        }
        if (randomNote == "b") {

            soundPool.play(integerSoundIDb, 1, 1, 1, 0, speed*floatSpeed);
            soundPool.setLoop(integerSoundIDb,4);
        }
        if (randomNote == "c") {

            soundPool.play(integerSoundIDc, 1, 1, 1, 0, speed*floatSpeed);
            soundPool.setLoop(integerSoundIDc,4);
        }
        if (randomNote == "d") {

            soundPool.play(integerSoundIDd, 1, 1, 1, 0, speed*floatSpeed);
            soundPool.setLoop(integerSoundIDd,4);
        }
        if (randomNote == "e") {

            soundPool.play(integerSoundIDe, 1, 1, 1, 0, speed*floatSpeed);
            soundPool.setLoop(integerSoundIDe,4);
        }
        if (randomNote == "f") {

            soundPool.play(integerSoundIDf, 1, 1, 1, 0, speed*floatSpeed);
            soundPool.setLoop(integerSoundIDf,4);
        }
        if (randomNote == "g") {

            soundPool.play(integerSoundIDg, 1, 1, 1, 0, speed*floatSpeed);
            soundPool.setLoop(integerSoundIDg,4);
        }
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
                button_play_note(1);
            }
            @Override
            public void onGameStopEvent()
            {
//                chaseTheLightClass.stopGame();
//                StartChaseDirections.ActionStartChaseToEndChase action = StartChaseDirections.actionStartChaseToEndChase();
//                action.setGameCount(score);
//                Navigation.findNavController(v).navigate(action);
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
        int event = AntData.getCommand(bytes);
        if (event == AntData.EVENT_RELEASE){
            System.out.println("released");
        if (randomNote == "a") {
            soundPool.resume(integerSoundIDa);
        }
        if (randomNote == "b") {
            soundPool.resume(integerSoundIDb);
        }
        if (randomNote == "c") {
            soundPool.resume(integerSoundIDc);
        }
        if (randomNote == "d") {
            soundPool.resume(integerSoundIDd);
        }
        if (randomNote == "e") {
            soundPool.resume(integerSoundIDe);
        }
        if (randomNote == "f") {
            soundPool.resume(integerSoundIDf);
        }
        if (randomNote == "g") {
            soundPool.resume(integerSoundIDg);
        }
    }}

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