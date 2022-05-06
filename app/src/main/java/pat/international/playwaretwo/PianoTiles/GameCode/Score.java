package pat.international.playwaretwo.PianoTiles.GameCode;

public class Score {
    protected int id;
    protected int score;
    protected String name;
    protected String datetime;
    protected  int difficulty;

    public Score(int id, int score, String name, String datetime) {
        this.id = id;
        this.score = score;
        this.name = name;
        this.datetime = datetime;
    }


    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Score(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
