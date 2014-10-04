package movingBlocks;

/**
 *
 * @author laisfb
 */

import javalib.appletworld.World;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;
import movingblocks.staticObjects;

public class MovingBlocks {
    
    private final static int blockSize = 120;
    private final static int canvasWidth = 720;
    private final static int canvasHeight = 720;
    private final static int MAXLEVEL = 5;
    
    public MovingBlocks() {}
    
    public static void main(String[] args) {       
        
        WorldCanvas c = new WorldCanvas(canvasWidth, canvasHeight);
        mainObject obj = new mainObject(c);
        staticObjects world = new staticObjects(c, obj);
        
        System.out.println("Sigh");

        // show several images in the canvas
        boolean makeDrawing = c.show();
   
    }

}