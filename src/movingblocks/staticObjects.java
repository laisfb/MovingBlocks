package movingBlocks;

import java.awt.Color;
import java.util.Random;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;

interface Block {
    public int size(); //the size of the block
    public String type(); //what kind of block it is
    public RectangleImage getRect();
}

public class staticObjects {
    
    private final static int size = 720;
    private final static int blockSize = 120;
    private final static int numberBlocks = size/blockSize;
   
    public staticObjects(RectangleImage[][] world, playerObject player, int level) {
        Posn p = player.getPos();
        
        //There's no need to check if the position is the same as the mainObject
        //Because they will always be in different rows
        Posn epPos = new Posn(randomInt(), randomInt());
        endPoint ep = new endPoint(epPos.y);
        world[numberBlocks-1][epPos.y] = ep.getRect();
      
        int tam = 3*(level-1) + 2;
        Posn[] obst = new Posn[tam];
        obst[0] = p;
        obst[1] = epPos;
        int j=2;
        
        for(int i=1; i<level; i++) {
            
            Posn rsPos = new Posn(randomInt(), randomInt());
            while(notValidPos(rsPos, obst, j))
                rsPos = new Posn(randomInt(), randomInt());            
            obst[j] = rsPos;
            j++;
            redSquare rs = new redSquare(rsPos);
            world[rsPos.x][rsPos.y] = rs.getRect();
            
            
            Posn bsPos = new Posn(randomInt(), randomInt());
            while(notValidPos(bsPos, obst, j))
                bsPos = new Posn(randomInt(), randomInt());            
            obst[j] = bsPos;
            j++;
            blueSquare bs = new blueSquare(bsPos);
            world[bsPos.x][bsPos.y] = bs.getRect();
            
            
            Posn wPos = new Posn(randomInt(), randomInt());
            while(notValidPos(wPos, obst, j))
                wPos = new Posn(randomInt(), randomInt());            
            obst[j] = wPos;
            j++;
            wall w = new wall(wPos);
            world[wPos.x][wPos.y] = w.getRect();
            
            /*
            Posn rsPos = new Posn(randomInt(), randomInt());
            while(notValidPos(rsPos,p) || notValidPos(rsPos,epPos))
                rsPos = new Posn(randomInt(), randomInt());
            redSquare rs = new redSquare(rsPos);
            world[rsPos.x][rsPos.y] = rs.getRect();
            
            Posn bsPos = new Posn(randomInt(), randomInt());
            while(notValidPos(bsPos,p) || notValidPos(bsPos,epPos) || notValidPos(bsPos,rsPos))
                bsPos = new Posn(randomInt(), randomInt());
            blueSquare bs = new blueSquare(bsPos);
            world[bsPos.x][bsPos.y] = bs.getRect();

            Posn wPos = new Posn(randomInt(), randomInt());
            while(notValidPos(wPos,p) || notValidPos(wPos,epPos) || notValidPos(wPos,rsPos) || notValidPos(wPos,bsPos) )
                wPos = new Posn(randomInt(), randomInt());
            wall w = new wall(wPos);
            world[wPos.x][wPos.y] = w.getRect();
            */
            
        }
    }
    
    public final boolean notValidPos(Posn p, Posn[] obst, int tam) {
        //diff returns true if the difference between p and q is 1
        //if (p.isEqual(obst[i]) || p.diff(obst[i]))
        for(int i=0; i<tam; i++) {
            if (p.isEqual(obst[i]))
                return true;
        }
        return false;
    }
    
    public final boolean notValidPos(Posn p, Posn q) {
        //diff returns true if the difference between p and q is 1
        return (p.isEqual(q) || p.diff(q));
    }
    
    public static int randomInt() {
        return new Random().nextInt(numberBlocks-1);
    }
}
    
class Obstacles {
    private final static int blockSize = 120;
    private final static int size = 720;
    private final static int numberBlocks = size/blockSize;
    
    public int getBlockSize()    { return blockSize; }
    public int getCanvasSize()  { return size; }
    public int getNumberBlocks() { return numberBlocks; }
    
    WorldCanvas world = new WorldCanvas(size, size);
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