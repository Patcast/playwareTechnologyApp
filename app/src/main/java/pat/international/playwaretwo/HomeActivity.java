package pat.international.playwaretwo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import pat.international.playwaretwo.PianoTiles.GameCode.FragmentListener;
import pat.international.playwaretwo.PianoTiles.GameCode.GameoverFragment;
import pat.international.playwaretwo.PianoTiles.GameCode.HighScoreFragment;
import pat.international.playwaretwo.PianoTiles.GameCode.MainFragment;
import pat.international.playwaretwo.PianoTiles.GameCode.SettingsFragment;


//public class HomeActivity extends AppCompatActivity implements FragmentListener {
public class HomeActivity extends AppCompatActivity  {

/*    ActivityMainBinding bind;
    FragmentManager fragmentManager;
    MainFragment mainFragment;
    GameoverFragment gameoverFragment;
    HomeFragmentPiano homeFragment;
    HighScoreFragment highScoreFragment;
    SettingsFragment settingsFragment;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.toolBar));
        actionBar.setBackgroundDrawable(colorDrawable);// don't worry about this
    }

  /*  @Override
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
    }*/

/*    @Override
    public void setScore(int score) {
        this.gameoverFragment = GameoverFragment.newInstance(score);
    }

    @Override
    public void closeApplication() {
        this.moveTaskToBack(true);
        this.finish();
    }*/

}