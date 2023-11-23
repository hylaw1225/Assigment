import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FootballTeam extends Team {
    public static final int GOALKEEPER_POSITION = 1;
    public static final int DEFENDER_POSITION = 2;
    public static final int MIDFIELDER_POSITION = 3;
    public static final int FORWARD_POSITION = 4;

    public FootballTeam(String teamId) {
        super(teamId);
    }

    @Override
    public void updatePlayerPosition() {
        // Implementation depends on specific requirements
    }

    @Override
    public void showTeam() {
        Map<Integer, String> positionToPlayers = new HashMap<>();
        positionToPlayers.put(GOALKEEPER_POSITION, "Goalkeeper");
        positionToPlayers.put(DEFENDER_POSITION, "Defender");
        positionToPlayers.put(MIDFIELDER_POSITION, "Midfielder");
        positionToPlayers.put(FORWARD_POSITION, "Forward");

        Map<String, List<String>> playersByPosition = new HashMap<>();
        for (Player player : getPlayers()) {
            String position = positionToPlayers.get(player.getPosition());
            if (!playersByPosition.containsKey(position)) {
                playersByPosition.put(position, new ArrayList<>());
            }
            playersByPosition.get(position).add(player.getName());
        }

        for (Map.Entry<String, List<String>> entry : playersByPosition.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (String playerName : entry.getValue()) {
                System.out.println(playerName);
            }
        }
    }
}