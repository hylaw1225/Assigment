public class Player {
    private String playerId;
    private String name;
    private int position;

    public Player(String id, String name) {
        this.playerId = id;
        this.name = name;
    }

    public String getPlayerId() {
        return playerId;
    }

    // This method should return playerId, not id.
    public String getId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}