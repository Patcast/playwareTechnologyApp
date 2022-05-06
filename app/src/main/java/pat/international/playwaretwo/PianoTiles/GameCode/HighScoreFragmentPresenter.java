package pat.international.playwaretwo.PianoTiles.GameCode;

import java.util.ArrayList;
import java.util.List;

public class HighScoreFragmentPresenter {

    protected IHighScoreFragment ui;
    //protected DBHandler db;


    public HighScoreFragmentPresenter(IHighScoreFragment ui){
        this.ui = ui;
    }

    public void loadData(List<Score> list){
        //this.list = this.db.getHighScore(limit);
        this.ui.updateList(list);
    }

    public interface IHighScoreFragment{
        void updateList(List<Score> list);
    }
}
