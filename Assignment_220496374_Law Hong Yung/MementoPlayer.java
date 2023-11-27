public class MementoPlayer {
    private Player player;
    private int position;

    public MementoPlayer (Player player){
        this.player = player;
        player.getName();
        this.position = player.getPosition();
    }

    public Player getPlayer(){
        return player;
    }

    public void restore(){
        player.setPosition(position);
    }
}