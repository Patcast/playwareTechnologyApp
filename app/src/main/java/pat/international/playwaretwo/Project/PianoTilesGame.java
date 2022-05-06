package pat.international.playwaretwo.Project;


import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;

import pat.international.playwaretwo.GameColorObserver;

public class PianoTilesGame extends Game {
    MotoConnection connection = MotoConnection.getInstance();
    Context context;
    int gameCount;
    GameColorObserver colorObserver;

    private void subscribeObserver1(GameColorObserver obsv1){
        colorObserver = obsv1;
    }
    private void unSubscribeObserver1(){ colorObserver = null; }

    public PianoTilesGame(Context context, GameColorObserver obsv1 ) {
        this.context = context;
        subscribeObserver1(obsv1);
        setName("Piano Tiles");
        //TODO sync ending of the song with end of game.
        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 6000, "1 player 30 sec",1);
        addGameType(gt);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameStart()
    {
        super.onGameStart();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameUpdate(byte[] message)
    {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int color= AntData.getColorFromPress(message);
        if (event == AntData.EVENT_PRESS)
        {
            gameCount++; //TODO: Maybe we can add something to penalize players.
            colorObserver.notifyColor(color);
        }

    }
    @Override
    public void onGameEnd()
    {
        super.onGameEnd();
        connection.setAllTilesBlink(4,LED_COLOR_RED);
        unSubscribeObserver1();
    }

}

