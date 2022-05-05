package pat.international.playwaretwo.Project;

import static com.livelife.motolibrary.AntData.LED_COLOR_RED;
import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_ORANGE;

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
import android.widget.Toast;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import pat.international.playwaretwo.R;


public class StartGame extends Fragment implements OnAntEventListener{


    MotoConnection connection = MotoConnection.getInstance();
    LinearLayout gt_container;

    TextView gameText,tilesNumText,scoreText;
    Button level1Btn, level2Btn, level3Btn ;
    int score = 0;
    NavController nav;
    View v;
    boolean register = false;
    private boolean isPairing = false;
    private int numOfTiles;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_game, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connection = MotoConnection.getInstance();
        connection.startMotoConnection(getContext());
        connection.saveRfFrequency(68); // See the back of your tile for your group’s RF
        connection.setDeviceId(7); //Your group number
        //connection.registerListener(this);
        int numberOfTiles = connection.connectedTiles.size();
        Button level1 = view.findViewById(R.id.level1);
        Button level2 = view.findViewById(R.id.level2);
        Button level3 = view.findViewById(R.id.level3);
        Button pairingButton = view.findViewById(R.id.connect);
        pairingButton.setOnClickListener(p->paring(pairingButton));
        tilesNumText = view.findViewById(R.id.NumberOfTiles);
        nav = Navigation.findNavController(view);
        level1.setOnClickListener(v-> startlevel1());
        level2.setOnClickListener(v-> startlevel2());
        level3.setOnClickListener(v-> startlevel3());

        if (numberOfTiles!=4){
            tilesNumText.setText("You dont have enough tiles :( :"+String.valueOf(numberOfTiles));

        }
        if (numberOfTiles==4){
            tilesNumText.setText("You have the right ammount of tiles :) :"+String.valueOf(numberOfTiles));

        }
    }



    // I have problems reestablishing the connection, how often do I need to pair.
    private void startlevel1(){
        nav.navigate(R.id.action_startGameProject_to_startPianoTiles);
    }
    private void startlevel2(){
        nav.navigate(R.id.action_startGameProject_to_startPianoTiles);
    }
    private void startlevel3(){
        nav.navigate(R.id.action_startGameProject_to_mainFragment2);
    }


    @Override
    public void onStart() {
        super.onStart();
        //connection.startMotoConnection(getContext());
        connection.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //connection.stopMotoConnection();
        connection.unregisterListener(this);
    }

    private void paring(Button b){
        if(!isPairing){
            connection.pairTilesStart();
            b.setText("Stop Pairing");
        } else {
            connection.pairTilesStop();
            b.setText("Start Pairing");
        }
        isPairing = !isPairing;
    }

    private int[] PutColors(){
        int[] color_list = {LED_COLOR_RED,LED_COLOR_BLUE,LED_COLOR_GREEN,LED_COLOR_ORANGE};
        int Index = 0;
        int[] id_list = new int[4];
        for (int t : connection.connectedTiles){
            id_list[Index] = t;
            connection.setTileColor(color_list[Index], t);
            Index++;
        }
        return id_list;
    }
    @Override
    public void onMessageReceived(byte[] bytes, long l) {

    }

    @Override
    public void onAntServiceConnected() {
        connection.setAllTilesToInit();
    }

    @Override
    public void onNumbersOfTilesConnected(int i) {
        numOfTiles = i;
        if (numOfTiles!=4){
            tilesNumText.setText("You have only "+String.valueOf(numOfTiles)+" tiles :( ");

        }
        if (numOfTiles==4){
            tilesNumText.setText("You have the right ammount of 4 tiles :)");
            PutColors();
        }
    }

}