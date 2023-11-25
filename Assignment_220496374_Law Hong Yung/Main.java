import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Stack<UndoableCommandIndex> undoStack = new Stack<>();
    private static final Stack<UndoableCommandIndex> redoStack = new Stack<>();
    private static final Map<String, Team> teams = new LinkedHashMap<>();
    private static Team currentTeam = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("Sport Teams Management System (STMS)");
            System.out.println("");
            System.out.println("c = create team, g = set current team, a = add player, m = modify player's position");
            System.out.println("d = delete player, s = show team, p = display all teams, t = change team's name");
            System.out.println("u = undo, r = redo, l = list undo/redo, x = exit system");
            System.out.println("");

            if (currentTeam != null) {
                System.out
                        .println("The current team is " + currentTeam.getTeamId() + " " + currentTeam.getName() + ".");
            }

            System.out.print("Please enter command [ c | g | a | m | d | s | p | t | u | r | l | x ]:");

            CommandIndex command = createCommand(scanner.nextLine().toLowerCase());

            if (command == null)
                continue;

            command.execute();

            if (command instanceof UndoableCommandIndex) {
                undoStack.push((UndoableCommandIndex) command);
                redoStack.clear();
            }
        }
    }

    public static Team findTeam(String teamID) {
        return teams.get(teamID);
    }

    public static Collection<Team> getTeams() {
        return teams.values();
    }

    public static Team getCurrentTeam() {
        return currentTeam;
    }

    public static void setCurrentTeam(Team team) {
        currentTeam = team;
    }

    public static Stack<UndoableCommandIndex> getUndo() {
        return undoStack;
    }

    public static Stack<UndoableCommandIndex> getRedo() {
        return redoStack;
    }

    public static void addTeam(Team team) {
        teams.put(team.getTeamId(), team);
    }

    private static CommandIndex createCommand(String inputCommand) {
        switch (inputCommand) {
            case "c":
                return new Create(scanner); // Command for create team
            case "a":
                return new AddPlayer(scanner); // Command add player
            case "s":
                return new Show(scanner); // Command for show team
            case "x":
                System.out.println("Exiting system...");
                scanner.close();
                return null;
            case "p":
                return new DisplayAll(scanner); // Command for display all team
            case "g":
                return new Set(scanner); // Command for set current team
            case "m":
                return new Modify(scanner); // Command for modify player position
            case "d":
                return new Delete(scanner); // Command for delete player
            case "t":
                return new Change(scanner); // Command for change team name
            case "u":
                if (!undoStack.isEmpty()) {
                    UndoableCommandIndex command = undoStack.pop();
                    command.undo();
                    redoStack.push(command);
                } else {
                    System.out.println("Undo stack is empty. No command to undo.");
                }
                return null;
            case "r":
                if (!redoStack.isEmpty()) {
                    UndoableCommandIndex command = redoStack.pop();
                    command.execute();
                    undoStack.push(command);
                } else {
                    System.out.println("Redo stack is empty. No command to redo.");
                }
                return null;
            case "l":
                System.out.println("Undo stack:");
                for (CommandIndex undoCommand : undoStack) {
                    System.out.println(undoCommand);
                }
                System.out.println("Redo stack:");
                for (CommandIndex redoCommand : redoStack) {
                    System.out.println(redoCommand);
                }
                return null;
            default:
                System.out.println("Invalid command");
                return null;
        }
    }
}