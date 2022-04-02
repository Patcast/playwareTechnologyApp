package pat.international.playwaretwo.ass8;

public class Challenge {
    private final String challengerName;
    private final String challengedName;
    private final int gameId;
    private final int status;

    private final String[] statuses = new String[]{"created","accepted","waiting for the challenged","waiting for the challenger","challenge completed"};

    public Challenge(String challengerName, String challengedName, int gameId, int status) {
        this.challengerName = challengerName;
        this.challengedName = challengedName;
        this.gameId = gameId;
        this.status = status;
    }

    public String getChallengerName() {
        return challengerName;
    }

    public String getChallengedName() {
        return challengedName;
    }

    public int getGameId() {
        return gameId;
    }

    public String getStatus() {
        return statuses[status];
    }
}
