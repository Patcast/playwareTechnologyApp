package pat.international.playwaretwo.FinalCountdown;

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
    @Override
    public void onGameStart()
    {
        super.onGameStart();
        int numberOfTiles = connection.connectedTiles.size();
        Toast.makeText(context, "Starting Game with " + numberOfTiles+ " tiles.", Toast.LENGTH_LONG).show();
        connection.setAllTilesIdle(LED_COLOR_OFF);

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
    }

    @Override
    public void onGameEnd()
    {
        super.onGameEnd();
        connection.setAllTilesBlink(4,LED_COLOR_RED);
        unSubscribeObserver();
    }



}
