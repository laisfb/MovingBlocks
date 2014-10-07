package movingBlocks;

import java.awt.Color;
import java.util.Random;
import javalib.appletworld.World;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;

interface Block {
    public int size(); //the size of the block
    public String type(); //what kind of block it is
    public RectangleImage getRect();
}

public class staticObjects {
    
    private final static int blockSize = 120;
    private final static int width = 720;
    private final static int height = 720;
    private final static int numberBlocks = height/blockSize;
   
    public staticObjects(RectangleImage[][] world, playerObject player) {
        Posn p = player.getPos();
        
        //There's no need to check if the position is the same as the mainObject
        //Because they will always be in different rows
        Posn epPos = new Posn(randomInt(), randomInt());
        endPoint ep = new endPoint(epPos.y);
        world[numberBlocks-1][epPos.y] = ep.getRect();
      
        Posn rsPos = new Posn(randomInt(), randomInt());
        while(rsPos.isEqual(p) || rsPos.isEqual(epPos))
            rsPos = new Posn(randomInt(), randomInt());
        redSquare rs = new redSquare(rsPos);
        world[rsPos.x][rsPos.y] = rs.getRect();
        
        Posn bsPos = new Posn(randomInt(), randomInt());
        while(bsPos.isEqual(p) || bsPos.isEqual(epPos) || bsPos.isEqual(rsPos))
            bsPos = new Posn(randomInt(), randomInt());
        blueSquare bs = new blueSquare(bsPos);
        world[bsPos.x][bsPos.y] = bs.getRect();
        
        Posn wPos = new Posn(randomInt(), randomInt());
        while(wPos.isEqual(p) || wPos.isEqual(epPos) || wPos.isEqual(rsPos) || wPos.isEqual(bsPos) )
            wPos = new Posn(randomInt(), randomInt());
        wall w = new wall(wPos);
        world[wPos.x][wPos.y] = w.getRect();
    }
    
    public static int randomInt() {
        return new Random().nextInt(numberBlocks-1);
    }
}
    
class Obstacles {
    private final static int blockSize = 120;
    private final static int width = 720;
    private final static int height = 720;
    private final static int numberBlocks = height/blockSize;
    
    public int getBlockSize()    { return blockSize; }
    public int getCanvasWidth()  { return width; }
    public int getCanvasHeight() { return height; }
    public int getNumberBlocks() { return numberBlocks; }
    
    
    WorldCanvas world = new WorldCanvas(width, height);
    public WorldCanvas getWorld() { return world; }
    
    public int randomInt() {
        int rn = new Random().nextInt(getNumberBlocks());
        return (getNumberBlocks()*rn + (getNumberBlocks()/2));
    }
    
    // Convert (5,5) into (720,720) for example
    public Posn convertPos(Posn pos) {
        return new Posn(pos.x*getBlockSize() + getBlockSize()/2, pos.y*getBlockSize() + getBlockSize()/2);
    }
    
}

class redSquare extends Obstacles implements Block {
    private final Color color;
    private final RectangleImage rect;
    
    public redSquare(Posn pos) {
        this.color = Color.RED;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public String type() {
        return "redSquare";
    }

    @Override
    public RectangleImage getRect() {
        return this.rect;
    }
}

class blueSquare extends Obstacles implements Block {
    private final Color color;
    private final RectangleImage rect;
    
    blueSquare(Posn pos) {
        this.color = Color.BLUE;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public String type() {
        return "blueSquare";
    }

    @Override
    public RectangleImage getRect() {
        return this.rect;
    }
}

class wall extends Obstacles implements Block {
    private final Color color;
    private final RectangleImage rect;
    
    wall(Posn pos) {
        this.color = Color.GREEN;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public String type() {
        return "wall";
    }

    @Override
    public RectangleImage getRect() {
        return this.rect;
    }
}

class endPoint extends Obstacles implements Block {
    private final Color color;
    private final RectangleImage rect;
    
    endPoint(int y) {
        Posn pos = new Posn(getNumberBlocks()-1, y);
        this.color = Color.BLACK;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public String type() {
        return "endPoint";
    }

    @Override
    public RectangleImage getRect() {
        return this.rect;
    }    
}

class empty extends Obstacles implements Block {
    private final Color color;
    private final RectangleImage rect;

    public empty(Posn pos) {
        this.color = Color.LIGHT_GRAY;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }

    @Override
    public int size() {
        return getBlockSize();
    }

    @Override
    public String type() {
        return "empty";
    }

    @Override
    public RectangleImage getRect() {
        return this.rect;
    }
}