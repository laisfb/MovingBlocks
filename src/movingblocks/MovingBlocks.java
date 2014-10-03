package movingBlocks;

/**
 *
 * @author laisfb
 */
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;

public class MovingBlocks extends KeyAdapter {
    
    private final static int blockSize = 80;
    private final static int canvasWidth = 800;
    private final static int canvasHeight = 800;
    private final static int MAXLEVEL = 5;
    
    public MovingBlocks() {}
  
    public static void generateGrid(WorldCanvas c) {
        for(int i=(blockSize/2); i<canvasWidth; i+=blockSize) {
            for(int j=(blockSize/2); j<canvasHeight; j+=blockSize) {
                WorldImage frame = new FrameImage(new Posn(i, j), blockSize, blockSize, Color.black);
                c.drawImage(frame);
            }
        }
    }
    
    public static Posn endPoint(WorldCanvas c) {
        Random rn = new Random();
        int range = (canvasHeight / blockSize);
        int randomNum = rn.nextInt(range);
        
        int y = blockSize*randomNum + (blockSize/2);
        Posn pos = new Posn(canvasWidth-(blockSize/2), y);
        WorldImage end = new RectangleImage(pos, blockSize, blockSize, Color.black);
        c.drawImage(end);
        
        return pos;
    }
 
    public static void main(String[] args) {

        WorldCanvas c = new WorldCanvas(canvasWidth, canvasHeight);

        Posn end = endPoint(c);
        
        mainObject obj = new mainObject(c);
        
        //obstacles();
        
        generateGrid(c);

        // show several images in the canvas
        boolean makeDrawing = c.show();
   
    }
    
}