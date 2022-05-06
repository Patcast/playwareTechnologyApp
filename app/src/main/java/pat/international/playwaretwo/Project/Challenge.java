package pat.international.playwaretwo.Project;

public class Challenge {
    private final String challengerName;
    private final String challengedName;
    private final int gameId;
    private final int status;
    private final int groupId;
    private final String dateCreated;


    private final String[] statuses = new String[]{"created","accepted","waiting for the challenged","waiting for the challenger","challenge completed"};

    public Challenge(String challengerName, String challengedName, int gameId, int status, int groupId, String dateCreated) {
        this.challengerName = challengerName;
        this.challengedName = challengedName;
        this.gameId = gameId;
        this.status = status;
        this.groupId = groupId;
        this.dateCreated = dateCreated;
    }
    public String getDateCreated() {
        return dateCreated;
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

    public int getGroupId() {
        return groupId;
    }

    public String getStatus() {
        return statuses[status];
    }
}
