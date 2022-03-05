package pat.international.playwaretwo.FinalCountdown;

import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import pat.international.playwaretwo.GameCountObserver;
import pat.international.playwaretwo.GameCountTimeObserver;

public class FinalCountDown extends Game {
    MotoConnection connection = MotoConnection.getInstance();
    Context context;
    int gameCount;
    GameCountTimeObserver countObserver;
    int speedOfTile = 20;
    double counter = 2;
    int slowestSpeed = 29;
    boolean playing = false;



    private void subscribeObserver(GameCountTimeObserver obsv){
        countObserver = (GameCountTimeObserver) obsv;
    }
    private void unSubscribeObserver(){
        countObserver = null;
    }
    public FinalCountDown(Context context, GameCountObserver obsv ) {
        this.context = context;
        subscribeObserver((GameCountTimeObserver) obsv);
        setName("Final Count Down");
        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 6000, "1 player 30 sec",1);
        addGameType(gt);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameStart()
    {
        super.onGameStart();
        connection.setAllTilesIdle(LED_COLOR_OFF);
        connection.setAllTilesColor(LED_COLOR_BLUE);
        connection.connectedTiles.forEach(id ->startClock(id,speedOfTile,playing));
        playing = true;
    }

    private void startClock(int idOfTile,int speedOfTile, boolean playing){
        if(playing) connection.setTileColorCountdown(LED_COLOR_BLUE,idOfTile,speedOfTile);
        else connection.setTileColorCountdown(LED_COLOR_BLUE,idOfTile,slowestSpeed);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameUpdate(byte[] message)
    {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        if (event == AntData.EVENT_PRESS)
        {
            gameCount++;
            int speed = getSpeedOfClock();
            countObserver.notifyCount(gameCount);
            incrementPlayerScore(1,0);
            startClock(AntData.getId(message),speed,playing); // game mode one
        }
        if (event == AntData.CMD_COUNTDOWN_TIMEUP)
        {
            countObserver.StopTime();
            this.stopGame();
        }
    }

    @Override
    public void onGameEnd()
    {
        super.onGameEnd();
        connection.setAllTilesBlink(4,LED_COLOR_RED);
        unSubscribeObserver();
    }
    private int getSpeedOfClock(){
        double nextSpeedIndicator =(1 /(2*(Math.log10(0.001*counter))+6));
        int nextSpeed = (int)((slowestSpeed/1.2)*(nextSpeedIndicator-0.5))+1;
        counter = counter+0.1;
        return nextSpeed;

    }



}
