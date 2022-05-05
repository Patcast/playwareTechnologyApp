package pat.international.playwaretwo.http;

public enum ChallengeStatus {

    ZERO("Created"), ONE("Accepted"), TWO("Waiting for challenged"),
    THREE("Waiting for challenger"), FOUR("Challenge Completed");


    private final String label;

    public String getAsString(){
        return label;
    }

    ChallengeStatus(String label){
        this.label = label;
    }
}
