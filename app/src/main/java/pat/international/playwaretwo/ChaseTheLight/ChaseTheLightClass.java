package pat.international.playwaretwo.ChaseTheLight;

import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.content.Context;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import android.media.SoundPool;

import androidx.annotation.RequiresApi;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;

import pat.international.playwaretwo.GameCountObserver;


public class ChaseTheLightClass extends Game {
    MotoConnection connection = MotoConnection.getInstance();
    Context context;
    int gameCount;
    GameCountObserver countObserver;
    int randomTile;
    int randColorOne;
    int randColorAll;


    private void subscribeObserver(GameCountObserver obsv) {
        countObserver = obsv;
    }

    private void unSubscribeObserver() {
        countObserver = null;
    }


    public ChaseTheLightClass(Context context, GameCountObserver obsv) {
        this.context = context;
        subscribeObserver(obsv);
        setName("Colour Race");
        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 30, "1 player 30 sec", 1);
        addGameType(gt);
        GameType gt2 = new GameType(2, GameType.GAME_TYPE_TIME, 60, "1 player 1 min", 1);
        addGameType(gt2);
        GameType gt3 = new GameType(3, GameType.GAME_TYPE_TIME, 60 * 2, "1 player 2 min", 1);
        addGameType(gt3);


    }

    // Game setup code
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameStart() {
        super.onGameStart();
        int numberOfTiles = connection.connectedTiles.size();
        Toast.makeText(context, "Starting Game with " + numberOfTiles + " tiles.", Toast.LENGTH_LONG).show();
        selectRandomTile();
    }

    // Put game logic here
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectRandomTile() {
        connection.setAllTilesIdle(LED_COLOR_OFF);
        randomTile = connection.randomIdleTile();
        Log.i("TAG", "random id " + randomTile);
        randColorOne = (int) Math.floor(Math.random() * (7 - 1 + 1) + 1);
        randColorAll = (int) Math.floor(Math.random() * (7 - 1 + 1) + 1);
        while (randColorAll == randColorOne) {
            randColorAll = (int) Math.floor(Math.random() * (7 - 1 + 1) + 1);
        }
        connection.connectedTiles.forEach(this::assignColor);

    }

    private void assignColor(Integer tile) {
        if (tile == randomTile) connection.setTileColor(randColorOne, tile);
        else connection.setTileColor(randColorAll, tile);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int color = AntData.getColorFromPress(message);

        if (event == AntData.EVENT_RELEASE) {
            selectRandomTile();
            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        }
        if (event == AntData.EVENT_PRESS && color != randColorAll) {
            gameCount++;
            countObserver.notifyCount(gameCount);
            incrementPlayerScore(1, 0);
        }

    }

        @Override
        public void onGameEnd()
        {
            super.onGameEnd();
            connection.setAllTilesBlink(4, LED_COLOR_RED);
            unSubscribeObserver();
        }
}
