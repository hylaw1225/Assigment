import java.util.List;
import java.util.Vector;

public class Team {
    private String teamId;
    private String name;
    private Vector<Player> players;
    private String sportType;

    public Team(String teamId) {
        this.teamId = teamId;
        this.players = new Vector<>();
    }

    public String getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void deletePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getAllPlayers() {
        return players;
    }

    public void updatePlayerPosition() {
        // Implementation depends on specific requirements
    }

    public void showTeam() {
        // Implementation depends on specific requirements
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean removePlayer(String playerId) {
        return players.removeIf(player -> player.getPlayerId().equals(playerId));
    }
}