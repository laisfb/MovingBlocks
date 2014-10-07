package movingBlocks;

import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;

public class MovingBlocks {
    
    private final static int blockSize = 120;
    private final static int width = 720;
    private final static int height = 720;
    private final static int numberBlocks = height/blockSize;
    
    private final static RectangleImage[][] world = new RectangleImage[numberBlocks][numberBlocks];
    
    public MovingBlocks() {}

    public static void main(String[] args) {
        playerObject player = new playerObject();
        
        mainObject obj = new mainObject(player);
        
        // run the game
        obj.bigBang(width, height, 1);
    }

}