package movingblocks;

public class MovingBlocks {
    public MovingBlocks() {}

    public static void main(String[] args) {
        playerObject player = new playerObject();
        mainObject obj = new mainObject(player, 1, true);
        // run the game
        obj.bigBang(720, 720, 1);
    }

}