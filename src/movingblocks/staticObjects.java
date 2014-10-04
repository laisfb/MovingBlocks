package movingblocks;

import java.awt.Color;
import java.util.Random;
import movingBlocks.mainObject;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.FrameImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
/**
 *
 * @author laisfb
 */

interface Block {
    public int size(); //the size of the block
    public Posn pos(); //where in the grid the block is (x,y)
    public String type(); //what kind of block it is
    public WorldImage getRect();
}

public class staticObjects {    
    public staticObjects(WorldCanvas c, mainObject obj) {
        Posn p = obj.getPos();
        
        //There's no need to check if the position is the same as the mainObject
        //Because they will always be in different rows
        endPoint ep = new endPoint();
        c.drawImage(ep.getRect());
        
        redSquare rs = new redSquare();
        while(rs.pos().isEqual(p) || rs.pos().isEqual(ep.pos()))
            rs = new redSquare();
        c.drawImage(rs.getRect());
        
        blueSquare bs = new blueSquare();
        while(bs.pos().isEqual(p) || bs.pos().isEqual(ep.pos()) || bs.pos().isEqual(rs.pos()) )
            bs = new blueSquare();
        c.drawImage(bs.getRect());
        
        wall w = new wall();
        while(w.pos().isEqual(p) || w.pos().isEqual(ep.pos()) || w.pos().isEqual(rs.pos()) || w.pos().isEqual(bs.pos()) )
            w = new wall();
        c.drawImage(w.getRect());
        
        new grid(c);
    }
}
    
class Obstacles {
    private final static int blockSize = 120;
    private final static int canvasWidth = 720;
    private final static int canvasHeight = 720;
    private final static int MAXLEVEL = 5;
    private final static int numberBlocks = canvasHeight/blockSize;    
    
    public int getBlockSize()    { return blockSize; }
    public int getCanvasWidth()  { return canvasWidth; }
    public int getCanvasHeight() { return canvasHeight; }
    public int getNumberBlocks() { return numberBlocks; }
    
    
    WorldCanvas world = new WorldCanvas(canvasWidth, canvasHeight);
    public WorldCanvas getWorld() { return world; }
    
    public int randomInt() {
        int rn = new Random().nextInt(numberBlocks);
        return (blockSize*rn + (blockSize/2));
    }
    
    public Posn randomPos() {
        return new Posn(randomInt(),randomInt());
    }
    
}

class grid extends Obstacles {  
    grid(WorldCanvas c) {
        for(int i=(getBlockSize()/2); i<getCanvasWidth(); i+=getBlockSize()) {
            for(int j=(getBlockSize()/2); j<getCanvasHeight(); j+=getBlockSize()) {
                WorldImage frame = new FrameImage(new Posn(i, j), getBlockSize(), getBlockSize(), Color.black);
                c.drawImage(frame);
            }
        }
    }
}

class redSquare extends Obstacles implements Block {
    private final Posn pos;
    private final Color color;
    private final RectangleImage rect;
    
    public redSquare() {
        this.pos = randomPos();
        this.color = Color.red;
        rect = new RectangleImage(pos, getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public Posn pos() {
        return this.pos;
    }

    @Override
    public String type() {
        return "redSquare";
    }

    @Override
    public WorldImage getRect() {
        return this.rect;
    }
}

class blueSquare extends Obstacles implements Block {
    private final Posn pos;
    private final Color color;
    private final RectangleImage rect;
    
    blueSquare() {
        this.pos = randomPos();
        this.color = Color.blue;
        rect = new RectangleImage(pos, getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public Posn pos() {
        return this.pos;
    }

    @Override
    public String type() {
        return "blueSquare";
    }

    @Override
    public WorldImage getRect() {
        return this.rect;
    }
}

class wall extends Obstacles implements Block {
    private final Posn pos;
    private final Color color;
    private final RectangleImage rect;
    
    wall() {
        this.pos = randomPos();
        this.color = Color.green;
        rect = new RectangleImage(pos, getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public Posn pos() {
        return this.pos;
    }

    @Override
    public String type() {
        return "wall";
    }

    @Override
    public WorldImage getRect() {
        return this.rect;
    }
}

class endPoint extends Obstacles implements Block {
    private final Posn pos;
    private final Color color;
    private final RectangleImage rect;
    
    endPoint() {
        int x = getCanvasWidth()-(getBlockSize()/2); //last row
        this.pos = new Posn(x, randomInt());
        this.color = Color.black;
        rect = new RectangleImage(pos, getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public Posn pos() {
        return this.pos;
    }

    @Override
    public String type() {
        return "wall";
    }

    @Override
    public WorldImage getRect() {
        return this.rect;
    }
}