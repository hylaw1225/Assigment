import java.util.List;

public class MementoTeam {
    private Team team;
    private String teamName;
    private String teamId;
    private List<Player> player;


    public MementoTeam (Team team){
        this.team = team;
        this.teamName = team.getName();
        this.teamId = team.getTeamId();
        this.player = team.getAllPlayers();
    }

    public Team getTeam(){
        return team;
    }

    public void restore(){
        team.setName(teamName);
        team.removePlayer(teamId);

        for (Player player : player) {
            team.addPlayer(player);
        }
    }
}
