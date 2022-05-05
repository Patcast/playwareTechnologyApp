package pat.international.playwaretwo.PianoTiles.GameCode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import pat.international.playwaretwo.ChaseTheLight.ChaseTheLightClass;
import pat.international.playwaretwo.GameColorObserver;
import pat.international.playwaretwo.Project.PianoTilesGame;
import pat.international.playwaretwo.R;


public class MainFragment extends Fragment implements OnAntEventListener, MainFragmentPresenter.IMainFragment, View.OnClickListener, View.OnTouchListener, SensorEventListener {
    FragmentListener fragmentListener;
    MainFragmentPresenter mainFragmentPresenter;
    Button startButton;
    ImageView ivCanvas;
    Canvas canvas;
    Bitmap bitmap;
    Button score, health,button1,button2,button0,button3;
    Boolean initiated;
    SettingsPrefSaver settingsPrefSaver;
    CustomToast toast;
    SensorManager sensorManager;
    Sensor accelerometer, magnetometer;
    float[] accelerometerReading, magnetometerReading;
    SoundPool soundPool;
    int pianoA, pianoB, pianoC, pianoD;
    int totalWidth;
    public MainFragment(){}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main_tiles_menu, container, false);
        View toastView = inflater.inflate(R.layout.custom_toast, container, false);
        this.toast = new CustomToast(toastView, getActivity().getApplicationContext());
        this.ivCanvas = view.findViewById(R.id.ivCanvas);
        this.startButton = view.findViewById(R.id.start_btn);
        this.score = view.findViewById(R.id.score);
        this.health = view.findViewById(R.id.health);
        this.button0 = view.findViewById(R.id.b0);
        this.button1 = view.findViewById(R.id.b1);
        this.button2 = view.findViewById(R.id.b2);
        this.button3 = view.findViewById(R.id.b3);

        button0.setOnClickListener(v->playTile(v));
        button1.setOnClickListener(v->playTile(v));
        button2.setOnClickListener(v->playTile(v));
        button3.setOnClickListener(v->playTile(v));

        this.settingsPrefSaver = new SettingsPrefSaver(this.getActivity());
        this.startButton.setOnClickListener(this);
        this.health.setOnClickListener(this);

        this.ivCanvas.setOnTouchListener(this);
        this.mainFragmentPresenter = new MainFragmentPresenter(this, this.toast, this.settingsPrefSaver);

        this.initiated = false;

        this.sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        this.accelerometerReading = new float[3];
        this.magnetometerReading = new float[3];
        this.soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        this.pianoA = this.soundPool.load(this.getActivity(), R.raw.piano_a, 1);
        this.pianoB = this.soundPool.load(this.getActivity(), R.raw.piano_b, 1);
        this.pianoC = this.soundPool.load(this.getActivity(), R.raw.piano_c, 1);
        this.pianoD = this.soundPool.load(this.getActivity(), R.raw.piano_d, 1);
        totalWidth = ivCanvas.getWidth();

        connection.startMotoConnection(getContext());
        connection.registerListener(this);
        PianoTilesGame = new PianoTilesGame(getContext(), mainFragmentPresenter);
        tilesExecution();

        ////////
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void playTile(View v) {
        Button b = (Button) v;
        String textButton = b.getText().toString();

        //this.mainFragmentPresenter.checkScore(textButton);
    }

   public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof FragmentListener){
            this.fragmentListener = (FragmentListener) context;
        } else{
            throw new ClassCastException(context + " must implement FragmentListener");
        }
    }

    @Override
    public void updateCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.ivCanvas.postInvalidate();
    }

    @Override
    public void initiateCanvas(Bitmap bitmap, Canvas canvas) {
        this.bitmap = bitmap;
        this.ivCanvas.setImageBitmap(this.bitmap);
        this.canvas = canvas;
    }

    @Override
    public void updateScore(int score) {
        this.score.setText(Integer.toString(score));
    }

    @Override
    public void updateHealth(int health) {
        if(health > 0){
            this.health.setText(Integer.toString(health));
        }
    }

    @Override
    public void gameOver(int score) {
        this.fragmentListener.setScore(score);
        //this.fragmentListener.changePage(2);
    }

    @Override
    public void registerSensor() {
        if(this.accelerometer != null){
            this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(this.magnetometer != null){
            this.sensorManager.registerListener(this, this.magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void unregisterSensor() {
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void playNotes(int pos) {
        if(pos == 1){
            this.soundPool.play(this.pianoA, 1, 1, 1, 0, 1f);
            //this.pianoA.release();
        } else if(pos == 2){
            this.soundPool.play(this.pianoB, 1, 1, 1, 0, 1f);
            //this.pianoB.release();
        } else if(pos == 3){
            this.soundPool.play(this.pianoC, 1, 1, 1, 0, 1f);
            //this.pianoC.release();
        } else if(pos == 4){
            this.soundPool.play(this.pianoD, 1, 1, 1, 0, 1f);
            //this.pianoD.release();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == this.startButton){
            if(!this.initiated){
                this.mainFragmentPresenter.initiateCanvas(this.ivCanvas);
                this.startButton.setVisibility(View.INVISIBLE);
                this.score.setVisibility(View.VISIBLE);
                this.health.setVisibility(View.VISIBLE);
                this.initiated = true;
            }
        } else if(v == this.health){
            for(int i = 0; i < this.settingsPrefSaver.getKeyHealth(); i++){
                this.mainFragmentPresenter.removeHealth();
            }
        }
    }
    /// Maybe I can only substitute the onTouch and a similar method with the buttons.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){ /// Trigger by click event on Tile
            //this.mainFragmentPresenter.checkScore(new PointF(event.getX(), event.getY()));
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                this.accelerometerReading = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                this.magnetometerReading = event.values.clone();
                break;
        }

        final float[] rotationMatrix = new float[9];
        this.sensorManager.getRotationMatrix(rotationMatrix, null, this.accelerometerReading, this.magnetometerReading);

        final float[] orientationAngles = new float[3];
        this.sensorManager.getOrientation(rotationMatrix, orientationAngles);

        //float azimuth = orientationAngles[0];
        //float pitch = orientationAngles[1];
        float roll = orientationAngles[2];
        if(roll > 0.8f || roll < -0.8f){
            this.mainFragmentPresenter.checkSensor(roll);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    ///////////// GAME CODE /////////////

    boolean register = false;

    MotoConnection connection = MotoConnection.getInstance();
    PianoTilesGame PianoTilesGame;


    @Override
    public void onStart() {
        super.onStart();
        if(!register){
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

    //TODO: Call this method at the end of the song.
    private void endGame(){
        //connection.stopMotoConnection();
        connection.unregisterListener(this);
        PianoTilesGame.stopGame();
    }

    private void tilesExecution(){
        PianoTilesGame.selectedGameType = PianoTilesGame.getGameTypes().get(0);
        PianoTilesGame.startGame();
        register = true;
        PianoTilesGame.setOnGameEventListener(new ChaseTheLightClass.OnGameEventListener()
        {
            @Override
            public void onGameTimerEvent(int i)
            {
            }
            @Override
            public void onGameScoreEvent(int i, int i1)
            {
            }
            @Override
            public void onGameStopEvent()
            {
                PianoTilesGame.stopGame();

            }

            @Override
            public void onSetupMessage(String s)
            {
            }
            @Override
            public void onGameMessage(String s)
            {

            }
            @Override
            public void onSetupEnd()
            {

            } });
    }


    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        PianoTilesGame.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }

/*    @RequiresApi(api = Build.VERSION_CODES.N)
    public void notifyColor(int new_color) {
        getActivity().runOnUiThread(() -> {
            this.mainFragmentPresenter.checkScore(new_color);
        });
    }*/
 /*   @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateColor(int new_color){
        getActivity().runOnUiThread(() -> {
            color= new_color;
            colorText.setText(String.valueOf(color));
            this.mainFragmentPresenter.checkScore(textButton);
        });
    }*/
}