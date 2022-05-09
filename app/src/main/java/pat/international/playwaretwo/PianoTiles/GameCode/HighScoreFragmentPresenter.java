package pat.international.playwaretwo.PianoTiles.GameCode;

import java.util.List;

public class HighScoreFragmentPresenter {

    protected IHighScoreFragment ui;



    public HighScoreFragmentPresenter(IHighScoreFragment ui){
        this.ui = ui;
    }

    public void loadData(List<Score> list){
        this.ui.updateList(list);
    }

    public interface IHighScoreFragment{
        void updateList(List<Score> list);
    }
}
