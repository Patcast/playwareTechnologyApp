package pat.international.playwaretwo.PianoTiles.GameCode;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity  {
/*    ActivityMainBinding bind;
    FragmentManager fragmentManager;
    com.example.pianotiles.view.MainFragment mainFragment;
    com.example.pianotiles.view.GameoverFragment gameoverFragment;
    com.example.pianotiles.view.HomeFragmentPiano homeFragment;
    HighScoreFragment highScoreFragment;
    com.example.pianotiles.view.SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bind = ActivityMainBinding.inflate(getLayoutInflater());
        View view = this.bind.getRoot();
        setContentView(view);

        this.fragmentManager = this.getSupportFragmentManager();
        this.homeFragment = new com.example.pianotiles.view.HomeFragmentPiano();
        this.mainFragment = new com.example.pianotiles.view.MainFragment();
        this.gameoverFragment = com.example.pianotiles.view.GameoverFragment.newInstance(0);
        this.highScoreFragment = new HighScoreFragment();
        this.settingsFragment = new com.example.pianotiles.view.SettingsFragment();

        changePage(0);

    }

    @Override
    public void changePage(int i) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        if(i == 0){
            ft.replace(R.id.fragment_container, this.homeFragment).addToBackStack(null);
        } else if(i == 1) {
            ft.replace(R.id.fragment_container, this.mainFragment).addToBackStack(null);
        } else if(i == 2){
            ft.replace(R.id.fragment_container, this.gameoverFragment).addToBackStack(null);
        } else if(i == 3){
            ft.replace(R.id.fragment_container, this.highScoreFragment).addToBackStack(null);
        } else if(i == 4){
            ft.replace(R.id.fragment_container, this.settingsFragment).addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void setScore(int score) {
        this.gameoverFragment = com.example.pianotiles.view.GameoverFragment.newInstance(score);
    }

    @Override
    public void closeApplication() {
        this.moveTaskToBack(true);
        this.finish();
    }*/
}