package pat.international.playwaretwo.GameHitTheTarget;

import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.content.Context;
import android.os.Build;
import android.os.Handler;


import androidx.annotation.RequiresApi;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import pat.international.playwaretwo.GameCountObserver;

public class GameHitTheTarget extends Game {

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

    public long targetSpeed;

    public int targetIndex;

    public long minSpeed = 50;
    public long maxSpeed = 8000;

    public GameHitTheTarget(Context context, GameCountObserver obsv ) {
        this.context = context;
        subscribeObserver(obsv);
        GameType gt1 = new GameType(21, GameType.GAME_TYPE_TIME, 60, "1 player 1 min",1);
        addGameType(gt1);
        GameType gt2 = new GameType(31, GameType.GAME_TYPE_TIME, 60*2, "1 player 2 min",1);
        addGameType(gt2);
    }

    public Handler targetHandler = new Handler();
    public Runnable targetRunnable = new Runnable() {
        @Override
        public void run() {
            targetIndex++;
            if (targetIndex == connection.connectedTiles.size()) {
                targetIndex=0;
            }
            int nextTile = connection.connectedTiles.get(targetIndex);
            for (int t : connection.connectedTiles){
                if (t == nextTile){
                    connection.setTileColor(LED_COLOR_RED, t);
                }
                if (t != nextTile){
                    connection.setTileColor(LED_COLOR_OFF, t);
                }
            }
            float percent = 1.5f;
            targetSpeed += (targetSpeed * percent)/100;
            if (targetSpeed>maxSpeed){
                targetSpeed=maxSpeed;
            }
            targetHandler.postDelayed(this, targetSpeed);

        }
    };

    public void onGameStart() {
        super.onGameStart();

        targetIndex = 0;
        targetSpeed = 4500;
        int numPlayers = getPlayers().size();

        int nextTile = connection.connectedTiles.get(0);
        for (int t : connection.connectedTiles){
            if (t == nextTile){
                connection.setTileColor(LED_COLOR_RED, t);
            }
            if (t != nextTile){
                connection.setTileColor(LED_COLOR_OFF, t);
            }
        }
        targetHandler.postDelayed(targetRunnable, targetSpeed);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        if (event == AntData.EVENT_PRESS) {
            int numPlayers = getPlayers().size();
            int color = AntData.getColorFromPress(message);
            boolean success = false;
            for (int t : connection.connectedTiles){
                    connection.setTileColor(LED_COLOR_OFF, t);
            }


            if (color == LED_COLOR_OFF) {
            } else {
                gameCount+=10;
                countObserver.notifyCount(gameCount);
                incrementPlayerScore(1, 0);
                success=true;
            }

            int percent = 10;

            if (success) {

                targetSpeed -= (targetSpeed * percent) / 100;
                if (targetSpeed < minSpeed) {
                    targetSpeed = minSpeed;
                }

            }
            else{
                targetSpeed += (targetSpeed * percent)/100;
                if (targetSpeed>maxSpeed){
                targetSpeed=maxSpeed;
                }

                }
            }
        }
    // Some animation on the tiles to signal that the game is over
    @Override
    public void onGameEnd()
    {
        super.onGameEnd();
        targetHandler.removeCallbacksAndMessages(null);
        connection.setAllTilesBlink(4,LED_COLOR_BLUE);
        unSubscribeObserver();
    }
}
