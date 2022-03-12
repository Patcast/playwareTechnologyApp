package pat.international.playwaretwo.ColorCall;

import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.GameColorObserver;
import android.os.CountDownTimer;


public class ColorCall extends Game {
    MotoConnection connection = MotoConnection.getInstance();

    Context context;
    int gameCount;
    GameCountObserver countObserver;
    GameColorObserver colorObserver;
    int randomTile;
    int randomColor;
    int numberOfTiles = connection.connectedTiles.size();
    int time = 6000;
    CountDownTimer countDownTimer = new CountDownTimer(100000, 100) {
        public void onTick(long millisUntilFinished) {
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onFinish() {
            selectRandomTile();
        }
    };


    private void subscribeObserver(GameCountObserver obsv){
        countObserver = obsv;
    }
    private void unSubscribeObserver(){
        countObserver = null;
    }

    private void subscribeObserverColor(GameColorObserver obsv){
        colorObserver = obsv;
    }
    private void unSubscribeObserverColor(){
        colorObserver = null;
    }



    public ColorCall(Context context, GameCountObserver obsv, GameColorObserver obsv1) {
        this.context = context;
        subscribeObserver(obsv);
        subscribeObserverColor(obsv1);
        setName("Colour Race");
        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 30, "1 player 30 sec",1);
        addGameType(gt);
        GameType gt2 = new GameType(2, GameType.GAME_TYPE_TIME, 60, "1 player 1 min",1);
        addGameType(gt2);
        GameType gt3 = new GameType(3, GameType.GAME_TYPE_TIME, 60*2, "1 player 2 min",1);
        addGameType(gt3);
    }

    // Game setup code
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameStart()
    {
        super.onGameStart();
        int numberOfTiles = connection.connectedTiles.size();
        Toast.makeText(context, "Starting Game with " + numberOfTiles+ " tiles.", Toast.LENGTH_LONG).show();
        countDownTimer.cancel();
        selectRandomTile();
    }
    // Put game logic here
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectRandomTile(){
        connection.setAllTilesIdle(LED_COLOR_OFF);
        randomColor = (int)Math.floor(Math.random()*(6-1+1)+1);
        randomTile = connection.randomIdleTile();
        connection.connectedTiles.forEach(this::assignColor);
        colorObserver.notifyColor(randomColor);
        countDownTimer = new CountDownTimer(time, 100) {
            public void onTick(long millisUntilFinished) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onFinish() {
                time = getNewTimeMalus(time);
                selectRandomTile();
            }
        };
        countDownTimer.start();


    }

    private void assignColor(Integer tile) {
        int new_rand_color = (int)Math.floor(Math.random()*(6-1+1)+1);
        if (tile==randomTile)connection.setTileColor(randomColor, tile);
        else connection.setTileColor(new_rand_color, tile);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameUpdate(byte[] message)
    {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int color= AntData.getColorFromPress(message);
        if (event == AntData.EVENT_PRESS && color==randomColor)
        {
            countDownTimer.cancel();
            gameCount++;
            countObserver.notifyCount(gameCount);
            incrementPlayerScore(1,0);
            if (time>500)time = getNewTimeBonus(time);
            else time = 200;
            selectRandomTile();
        }
        if (event == AntData.EVENT_PRESS && color!=randomColor)
        {
            time = getNewTimeMalus(time);
            countDownTimer.cancel();
            selectRandomTile();

        }

        else
        {
            incrementPlayerScore(0,1);
            this.getOnGameEventListener().onGameTimerEvent(0);
        }
    }
    // Some animation on the tiles to signal that the game is over
    @Override
    public void onGameEnd()
    {
        super.onGameEnd();
        connection.setAllTilesBlink(4,LED_COLOR_RED);
        countDownTimer.cancel();
        unSubscribeObserver();
        unSubscribeObserverColor();
    }

    private int getNewTimeBonus(int time) {
        time = time - 1000;
        return time;
    }
    private int getNewTimeMalus(int time) {
        time = time + 500;
        return time;
    }
}
