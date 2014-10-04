package movingBlocks;

/**
 *
 * @author laisfb
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javalib.worldcanvas.WorldCanvas;
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
        

        // show several images in the canvas
        boolean makeDrawing = c.show();
   
    }
}