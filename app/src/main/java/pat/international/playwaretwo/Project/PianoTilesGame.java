package pat.international.playwaretwo.Project;


import static com.livelife.motolibrary.AntData.LED_COLOR_RED;
import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_ORANGE;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;

import java.util.Random;

import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.GameColorObserver;

public class PianoTilesGame extends Game {
    MotoConnection connection = MotoConnection.getInstance();
    Context context;
    int gameCount;
    GameCountObserver countObserver;
    GameColorObserver colorObserver;
    int speedOfTile = 20;
    double counter = 2;
    int slowestSpeed = 29;
    boolean playing = false;
    int randomTile;
    int randomTileColor = 1;
    int[] ColorList = new int[]{LED_COLOR_RED,LED_COLOR_BLUE,LED_COLOR_GREEN,LED_COLOR_ORANGE};
    Random random = new Random();


    private void subscribeObserver(GameCountObserver obsv){
        countObserver = obsv;
    }
    private void unSubscribeObserver(){ countObserver = null; }

    private void subscribeObserver1(GameColorObserver obsv1){
        colorObserver = obsv1;
    }
    private void unSubscribeObserver1(){ colorObserver = null; }

    public PianoTilesGame(Context context, GameCountObserver obsv, GameColorObserver obsv1 ) {
        this.context = context;
        subscribeObserver(obsv);
        subscribeObserver1(obsv1);
        setName("Piano Tiles");
        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 6000, "1 player 30 sec",1);
        addGameType(gt);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameStart()
    {
        super.onGameStart();
        int numberOfTiles = connection.connectedTiles.size();
        Toast.makeText(this.context, "Lolilol", Toast.LENGTH_SHORT).show();
    }
    // Put game logic here
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void selectRandomTileColor(){
        randomTileColor = ColorList[(int) Math.floor(Math.random()*ColorList.length)];
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameUpdate(byte[] message)
    {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int color= AntData.getColorFromPress(message);
        if (event == AntData.EVENT_PRESS && color==randomTileColor)
        {
            gameCount++;
            countObserver.notifyCount(gameCount);
            incrementPlayerScore(1,0);
            selectRandomTileColor();
            colorObserver.notifyColor(randomTileColor);
        }
        if (event == AntData.EVENT_PRESS && color!=randomTileColor)
        {
            gameCount=gameCount-1;
            countObserver.notifyCount(gameCount);

        }
    }
    // Some animation on the tiles to signal that the game is over
    @Override
    public void onGameEnd()
    {
        super.onGameEnd();
        connection.setAllTilesBlink(4,LED_COLOR_RED);
        unSubscribeObserver();
    }

}

