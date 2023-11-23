import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VolleyballTeam extends Team {
    public static final int ATTACKER_POSITION = 1;
    public static final int DEFENDER_POSITION = 2;

    public VolleyballTeam(String teamId) {
        super(teamId);
    }

    @Override
    public void updatePlayerPosition() {
        // Implementation depends on specific requirements
    }

    @Override
    public void showTeam() {
        Map<Integer, List<String>> positionToPlayers = new HashMap<>();
        positionToPlayers.put(ATTACKER_POSITION, new ArrayList<>());
        positionToPlayers.put(DEFENDER_POSITION, new ArrayList<>());

        for (Player player : getPlayers()) {
            switch (player.getPosition()) {
                case ATTACKER_POSITION:
                    break;
                case DEFENDER_POSITION:
                    break;
                default:
                    throw new IllegalArgumentException("Invalid player position: " + player.getPosition());
            }
            positionToPlayers.get(player.getPosition()).add(player.getId() + ": " + player.getName());
        }

        for (Map.Entry<Integer, List<String>> entry : positionToPlayers.entrySet()) {
            String position;
            switch (entry.getKey()) {
                case ATTACKER_POSITION:
                    position = "Attacker";
                    break;
                case DEFENDER_POSITION:
                    position = "Defender";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid player position: " + entry.getKey());
            }
            System.out.println(position + ": " + String.join(", ", entry.getValue()));
        }
    }
}

class ChangeTeamNameCommand implements CommandIndex {
    private Team team;
    private Scanner scanner;

    public ChangeTeamNameCommand(Scanner scanner, Team team) {
        this.scanner = scanner;
        this.team = team;
    }

    @Override
    public void execute() {
        System.out.print("Please input the new name of the current team: ");
        String newName = scanner.nextLine();
        team.setName(newName);
        System.out.println("The team's name has been updated.");
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("Unimplemented method 'undo'");
    }
}