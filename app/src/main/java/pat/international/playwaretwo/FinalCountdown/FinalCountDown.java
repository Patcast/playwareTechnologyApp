package pat.international.playwaretwo.FinalCountdown;

import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;

import pat.international.playwaretwo.GameCountObserver;

public class FinalCountDown extends Game {
    MotoConnection connection = MotoConnection.getInstance();
    Context context;
    int gameCount;
    GameCountObserver countObserver;

    private void subscribeObserver(GameCountObserver obsv){
        countObserver = obsv;
    }
    private void unSubscribeObserver(){
        countObserver = null;
    }
    public FinalCountDown(Context context, GameCountObserver obsv ) {
        this.context = context;
        subscribeObserver(obsv);
        setName("Final Count Down");
        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 10, "1 player 30 sec",1);
        addGameType(gt);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameStart()
    {
        super.onGameStart();
        connection.setAllTilesIdle(LED_COLOR_OFF);
        connection.setAllTilesColor(LED_COLOR_BLUE);

        connection.connectedTiles.forEach(this::startClock);
    }

    private void startClock(int idOfTile){
        connection.setTileColorCountdown(LED_COLOR_BLUE,idOfTile,20);
    }

    @Override
    public void onGameUpdate(byte[] message)
    {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        if (event == AntData.EVENT_PRESS)
        {
            gameCount++;
            countObserver.notifyCount(gameCount);
            incrementPlayerScore(1,0);

        }
        if (event == AntData.CMD_COUNTDOWN_TIMEUP)
        {
            Toast.makeText(context, "Click", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onGameEnd()
    {
        super.onGameEnd();
        connection.setAllTilesBlink(4,LED_COLOR_RED);
        unSubscribeObserver();
    }



}
