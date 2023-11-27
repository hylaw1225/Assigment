import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public abstract class CommandIndex {
    protected Scanner scanner;

    protected CommandIndex(Scanner scanner) {
        this.scanner = scanner;
    }

    public abstract void execute();
}

abstract class UndoableCommandIndex extends CommandIndex {
    protected UndoableCommandIndex(Scanner scanner) {
        super(scanner);
    }

    public abstract void undo();
}

class Create extends CommandIndex {
    private String teamId;
    private String teamName;
    private String sportType;

    public Create(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        System.out.print("Enter sport type (v = volleyball | f = football) :- ");
        sportType = scanner.nextLine();
        System.out.print("Team ID:- ");
        teamId = scanner.nextLine();
        System.out.print("Team Name:- ");
        teamName = scanner.nextLine();

        Team team;
        if ("v".equalsIgnoreCase(sportType)) {
            sportType = "Volleyball";
            team = new VolleyballTeam(teamId);
        } else if ("f".equalsIgnoreCase(sportType)) {
            sportType = "Football";
            team = new FootballTeam(teamId);
        } else {
            throw new IllegalArgumentException("Invalid sport type: " + sportType);
        }

        team.setName(teamName);
        Main.addTeam(team);
        Main.setCurrentTeam(team);

        System.out.println(sportType + " team is created.");
        System.out.println("Current team is changed to " + teamId + ".");
        System.out.println("");
    }

    public String getTeamId() {
        return teamId;
    }

    public String toString() {
        return "Create " + sportType.toLowerCase() + " team, " + teamId + ", " + teamName;
    }
}

class AddPlayer extends CommandIndex {
    private String playerId;
    private String playerName;
    private int playerPosition;

    public AddPlayer(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        System.out.print("Please input player information (id, name):");
        String[] playerInfo = scanner.nextLine().split(", ");
        playerId = playerInfo[0];
        playerName = playerInfo[1];

        Team team = Main.getCurrentTeam();

        if (team instanceof VolleyballTeam) {
            System.out.print("Position (1 = attacker | 2 = defender ):");
        } else if (team instanceof FootballTeam) {
            System.out.print("Position (1 = goal keeper | 2 = defender | 3 = midfielder | 4 = forward):");
        } else {
            throw new IllegalArgumentException("Invalid sport type: " + team.getClass());
        }

        playerPosition = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over
        Player player = new Player(playerId, playerName);
        player.setPosition(playerPosition);
        team.addPlayer(player);
        System.out.println("Player is added.");
    }

    public String toString() {
        return "Add player, " + playerId + ", " + playerName + ", " + playerPosition;
    }
}

class Show extends CommandIndex {
    private Team team;

    public Show(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        team = Main.getCurrentTeam();

        if (team instanceof VolleyballTeam) { // display VolleyballTeam information
            displayVolleyballTeam();
        } else if (team instanceof FootballTeam) {// display FootballTeam information
            displayFootballTeam();
        } else {
            System.out.println("Invalid sport type");
        }
    }

    private void displayVolleyballTeam() {
        // Create a map to store players according to their positions
        Map<String, List<String>> positionToPlayers = new HashMap<>();
        positionToPlayers.put("Attacker", new ArrayList<>());
        positionToPlayers.put("Defender", new ArrayList<>());

        for (Player player : team.getAllPlayers()) {
            String positionName = player.getPosition() == VolleyballTeam.ATTACKER_POSITION ? "Attacker" : "Defender";
            positionToPlayers.get(positionName).add(player.getPlayerId() + "," + player.getName());
        }

        System.out.println("Volleyball Team: " + team.getName() + " (" + team.getTeamId() + ")");
        for (Map.Entry<String, List<String>> entry : positionToPlayers.entrySet()) {
            System.out.println(entry.getKey() + ":\n" + String.join("\n", entry.getValue()));
        }
    }

    private void displayFootballTeam() {
        // Create a map to store players according to their positions
        Map<String, List<String>> positionToPlayers = new LinkedHashMap<>();
        positionToPlayers.put("Goal Keeper", new ArrayList<>());
        positionToPlayers.put("Defender", new ArrayList<>());
        positionToPlayers.put("Midfielder", new ArrayList<>());
        positionToPlayers.put("Forward", new ArrayList<>());

        for (Player player : team.getAllPlayers()) {
            String positionName;
            switch (player.getPosition()) {
                case 1:
                    positionName = "Goal Keeper";
                    break;
                case 2:
                    positionName = "Defender";
                    break;
                case 3:
                    positionName = "Midfielder";
                    break;
                case 4:
                    positionName = "Forward";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid player position: " + player.getPosition());
            }
            positionToPlayers.get(positionName).add(player.getPlayerId() + "," + player.getName());
        }

        System.out.println("Football Team: " + team.getName() + " (" + team.getTeamId() + ")");
        for (Map.Entry<String, List<String>> entry : positionToPlayers.entrySet()) {
            System.out.println(entry.getKey() + ":\n" + String.join("\n", entry.getValue()));
        }
    }
}

class DisplayAll extends CommandIndex {
    public DisplayAll(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        for (Team team : Main.getTeams()) {
            String sportType;
            if (team instanceof VolleyballTeam) {
                sportType = "Volleyball";
            } else if (team instanceof FootballTeam) {
                sportType = "Football";
            } else {
                throw new IllegalArgumentException("Invalid sport type: " + team.getClass());
            }
            System.out.println(sportType + " Team " + team.getName() + " (" + team.getTeamId() + ")");
        }
    }
}

class Set extends CommandIndex {

    private String teamId;

    public Set(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        System.out.print("Please input team ID:- ");
        teamId = scanner.nextLine();
        Team team = Main.findTeam(teamId);
        if (team != null) {
            Main.setCurrentTeam(team);
            System.out.println("Changed current team to " + teamId + ".");
        } else {
            System.out.println("Team ID " + teamId + " does not exist.");
        }
    }

    public String getTeamId() {
        return teamId;
    }

    public String toString() {
        return "Set current team, " + teamId;
    }
}

class Modify extends UndoableCommandIndex {
    private String playerId; // Declare playerId as a field of the class
    private int playerPosition;

    public Modify(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        Team team = Main.getCurrentTeam();
        System.out.print("Please input player ID:- ");
        playerId = scanner.nextLine();

        Player player = team.getPlayers().stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID: " + playerId));

        if (team instanceof VolleyballTeam) {
            System.out.print("Position (1 = attacker | 2 = defender ):- ");
        } else if (team instanceof FootballTeam) {
            System.out.print("Position (1 = goal keeper | 2 = defender | 3 = midfielder | 4 = forward):- ");
        } else {
            throw new IllegalArgumentException("Invalid sport type: " + team.getClass());
        }

        playerPosition = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over

        player.setPosition(playerPosition);
        System.out.println("Position is updated.");
    }

    public String toString() {
        return "Modify player’s position, " + playerId + ", " + playerPosition;
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'undo'");
    }
}

class Change extends CommandIndex {
    private String newName; // Declare newName as a field of the class
    private String teamId; // Declare teamId as a field of the class

    public Change(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        Team team = Main.getCurrentTeam();
        System.out.print("Please input new name of the current team:- ");
        newName = scanner.nextLine();
        team.setName(newName);
        System.out.println("Teams name is updated.");
    }

    public String toString() {
        return "Change team’s name, " + teamId + ", " + newName;
    }
}

class Delete extends CommandIndex {
    private String playerId;

    public Delete(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        System.out.print("Please input player ID:- ");
        playerId = scanner.nextLine();

        Team team = Main.getCurrentTeam();

        boolean result = team.removePlayer(playerId);
        if (result) {
            System.out.println("Player is deleted.");
        } else {
            System.out.println("Player ID " + playerId + " does not exist.");
        }
    }

    public String toString() {
        return "Delete player, " + playerId;
    }
}

class ListUndoRedoCommand extends CommandIndex {
    public ListUndoRedoCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void execute() {
        System.out.println("Undo List");
        for (CommandIndex command : Main.getUndo()) {
            System.out.println(command.toString());
        }
        System.out.println("-- End of undo list --");

        System.out.println("Redo List");
        for (CommandIndex command : Main.getRedo()) {
            System.out.println(command.toString());
        }
        System.out.println("-- End of redo list --");
    }
}


class RedoCommand extends CommandIndex {
    protected RedoCommand(Scanner scanner) {
        super(scanner);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute() {
        Stack<UndoableCommandIndex> undoStack = Main.getUndo();
        Stack<UndoableCommandIndex> redoStack = Main.getRedo();

        if (!redoStack.isEmpty()) {
            UndoableCommandIndex com = redoStack.pop();
            com.undo();
            undoStack.push(com);
        } else {
            System.out.println("Nothing to redo!");
        }
    }
}

class UndoCommand extends CommandIndex {
    protected UndoCommand(Scanner scanner) {
        super(scanner);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute() {
        Stack<UndoableCommandIndex> undoStack = Main.getUndo();
        Stack<UndoableCommandIndex> redoStack = Main.getRedo();

        if (!undoStack.isEmpty()) {
            UndoableCommandIndex com = undoStack.pop();
            com.undo();
            redoStack.push(com);
        } else {
            System.out.println("Nothing to undo!");
        }
    }
}