package pat.international.playwaretwo.PianoTiles.GameCode;

import java.util.List;

public class HighScoreFragmentPresenter {
    protected List<Score> list;
    protected IHighScoreFragment ui;
    protected DBHandler db;

    public HighScoreFragmentPresenter(IHighScoreFragment ui, DBHandler db){
        this.ui = ui;
        this.db = db;
    }

    public void loadData(int limit){
        this.list = this.db.getHighScore(limit);
        this.ui.updateList(this.list);
    }

    public interface IHighScoreFragment{
        void updateList(List<Score> list);
    }
}
