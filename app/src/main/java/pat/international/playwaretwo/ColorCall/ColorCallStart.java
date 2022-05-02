package pat.international.playwaretwo.ColorCall;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import pat.international.playwaretwo.ChaseTheLight.ChaseTheLightClass;
import pat.international.playwaretwo.ChaseTheLight.StartChaseDirections;
import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.GameColorObserver;
import pat.international.playwaretwo.R;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class ColorCallStart extends Fragment implements OnAntEventListener, GameCountObserver, GameColorObserver, TextToSpeech.OnInitListener{


    MotoConnection connection = MotoConnection.getInstance();
    LinearLayout gt_container;
    ColorCall ColorCall; // ColorCall object
    TextView gameText, tilesNumText, scoreText, scoreColor;
    Button endGameBtn;
    int score = 0;
    int color_value = 0;
    NavController nav;
    View v;
    boolean register = false;
    TextToSpeech textToSpeech;
    String text = "Display color";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_game_five, container, false);


        endGameBtn = v.findViewById(R.id.button_end);
            gameText = v.findViewById(R.id.textGame);
        tilesNumText = v.findViewById(R.id.textNumTiles);
        gt_container = v.findViewById(R.id.game_type_container);
        scoreText = v.findViewById(R.id.textScore);
        scoreColor = v.findViewById((R.id.textColor));
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
        endGameBtn.setOnClickListener(v -> {
            endGame();
            nav.popBackStack();
        });
        scoreText.setText(String.valueOf(score));
        ColorCall = new ColorCall(getContext(), this, this);
        tilesExecution();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!register) {
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

    private void endGame() {
        //connection.stopMotoConnection();
        connection.unregisterListener(this);
        ColorCall.stopGame();
    }


    private void tilesExecution() {
        for (final GameType gt : ColorCall.getGameTypes()) {
            Button b = new Button(getContext());
            b.setText(gt.getName());
            b.setOnClickListener(v -> {
                ColorCall.selectedGameType = gt;
                gameText.setText("Game is running");
                gt_container.setVisibility(View.GONE);
                ColorCall.startGame();
                register = true;
            });
            gt_container.addView(b);
        }
        ColorCall.setOnGameEventListener(new ChaseTheLightClass.OnGameEventListener() {
            @Override
            public void onGameTimerEvent(int i) {

            }

            @Override
            public void onGameScoreEvent(int i, int i1) {

            }

            @Override
            public void onGameStopEvent() {ColorCall.stopGame();
                ColorCallStartDirections.ActionStartGame5ToEndChase action = ColorCallStartDirections.actionStartGame5ToEndChase();
                action.setGameCount(score);
                Navigation.findNavController(v).navigate(action);
            }

            @Override
            public void onSetupMessage(String s) {
            }

            @Override
            public void onGameMessage(String s) {

            }

            @Override
            public void onSetupEnd() {

            }
        });
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        ColorCall.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }

    public void notifyColor(int color) {
        updateColor(color);
    }

    private void updateColor(int color) {
        getActivity().runOnUiThread(() -> {
            int random_number = color;
            String random_string = "error color";
            System.out.println(color);
            if (random_number == 1)
            {
                random_string = "Red";
            }
            else if (random_number == 2)
            {
                random_string = "Blue";
            }
            else if (random_number == 3)
            {
                random_string = "Green";
            }
            else if (random_number == 4)
            {
                random_string = "Pink";
            }
            else if (random_number == 5)
            {
                random_string = "Yellow";
            }
            else if (random_number == 6)
            {
                random_string = "White";
            }
            scoreColor.setText(random_string);
            text = random_string;

        });
    }

    public void notifyCount(int count) {
        updateCount(count);
    }

    private void updateCount(int count) {
        getActivity().runOnUiThread(() -> {
            score = count;
            scoreText.setText(String.valueOf(score));
        });
    }
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                texttoSpeak();
            }
        } else {
            Log.e("error", "Failed to Initialize");
        }
    }

    private void texttoSpeak() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
