import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class main {
    private static final Stack<UndoableCommandIndex> undoStack = new Stack<>();
    private static final Stack<UndoableCommandIndex> redoStack = new Stack<>();
    private static final Map<String, Team> teams = new LinkedHashMap<>();
    private static Team currentTeam = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
                undoStack.push(command);
                redoStack.clear();
            }
        }
    }

    public void setCurrentTeam(Team team) {
        currentTeam = team;
    }

    public void addTeam(Team team) {
        teams.put(team.getTeamId(), team);
    }

    private static CommandIndex createCommand(String inputCommand) {
        switch (inputCommand) {
            case "c":
                return new Create(scanner); // Command for create team
                command.execute();
                currentTeam = teams.get(((Create) command).getTeamId());
                undoStack.push(command);
                redoStack.clear();
                break;
            case "a":
                return new Add(scanner); // Command for create team
                break;
            case "s":
                return new Show(); // Command for show team
                command.execute();
                break;
            case "x":
                System.out.println("Exiting system...");
                scanner.close();
                return;
            case "p":
                return new DisplayAll(); // Command for display all team
                command.execute();
                break;
            case "g":
                return new Set(scanner); // Command for set current team
                command.execute();
                currentTeam = teams.get(((Set) command).getTeamId());
                undoStack.push(command);
                redoStack.clear();
                break;
            case "m":
                return new Modify(scanner); // Command for modify player position
                command.execute();
                undoStack.push(command);
                redoStack.clear();
                break;
            case "d":
                return new Delete(scanner); // Command for delete player
                command.execute();
                undoStack.push(command);
                redoStack.clear();
                break;
            case "t":
                return new Change(scanner); // Command for change team name
                command.execute();
                undoStack.push(command);
                redoStack.clear();
                break;
            case "u":
                if (!undoStack.isEmpty()) {
                    command = undoStack.pop();
                    command.undo();
                    redoStack.push(command);
                } else {
                    System.out.println("Undo stack is empty. No command to undo.");
                }
                break;
            case "r":
                if (!redoStack.isEmpty()) {
                    command = redoStack.pop();
                    command.execute();
                    undoStack.push(command);
                } else {
                    System.out.println("Redo stack is empty. No command to redo.");
                }
                break;
            case "l":
                System.out.println("Undo stack:");
                for (CommandIndex undoCommand : undoStack) {
                    System.out.println(undoCommand);
                }
                System.out.println("Redo stack:");
                for (CommandIndex redoCommand : redoStack) {
                    System.out.println(redoCommand);
                }
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }
}