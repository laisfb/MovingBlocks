package movingblocks;

import java.awt.Color;
import static java.lang.Math.abs;
import java.util.Random;
import javalib.worldimages.*;


public class staticObjects {
    
    private final static int size = 720;
    private final static int blockSize = 120;
    private final static int numberBlocks = size/blockSize;
    
    private final RectangleImage endPoint;
   
    public staticObjects(RectangleImage[][] world, playerObject player, int level) {
        Posn p = convertToIndex(player.getPos());
        
        //There's no need to check if the position is the same as the mainObject
        //Because they will always be in different columns
        Posn epPos = new Posn(randomInt(), randomInt());
        epPos.x = numberBlocks - 1;
        this.endPoint = new RectangleImage(convertFromIndex(epPos), blockSize, blockSize, Color.BLACK);
        
        world[numberBlocks-1][epPos.y] = this.endPoint;        
      
        //Store all occupied positions
        int tam = 3*(level-1)+2;
        Posn[] obst = new Posn[tam];
        obst[0] = p;
        obst[1] = epPos;
        int j=2;
        
        //Store the walls positions
        Posn[] spec = new Posn[2];
        spec[0] = p;
        spec[1] = epPos;
        
        for(int i=1; i<level; i++) {
            
            Posn wPos = new Posn(randomInt(), randomInt());
            while(notValidPos(wPos, obst, j) || dist(wPos, spec, 2))
                wPos = new Posn(randomInt(), randomInt());
            world[wPos.x][wPos.y] = new RectangleImage(convertFromIndex(wPos), blockSize, blockSize, Color.GREEN);
            obst[j] = wPos;
            j++;
            
            Posn rsPos = new Posn(randomInt(), randomInt());
            while(notValidPos(rsPos, obst, j))
                rsPos = new Posn(randomInt(), randomInt());
            world[rsPos.x][rsPos.y] = new RectangleImage(convertFromIndex(rsPos), blockSize, blockSize, Color.RED);
            obst[j] = rsPos;
            j++;
            
            Posn bsPos = new Posn(randomInt(), randomInt());
            while(notValidPos(bsPos, obst, j))
                bsPos = new Posn(randomInt(), randomInt());
            world[bsPos.x][bsPos.y] = new RectangleImage(convertFromIndex(bsPos), blockSize, blockSize, Color.BLUE);
            obst[j] = bsPos;
            j++;
            
        }
    }
    
    public final boolean notValidPos(Posn p, Posn[] obst, int tam) {
        for(int i=0; i<tam; i++) {
            if(p.isEqual(obst[i]))
                return true;
        }
        return false;
    }
    
    // This function was created to make the walls have a certain distance of 
    //  the player, and the end point
    // Because since the game is randomly generated it can be the case where
    //  the walls are surrounding the player or the game, and therefore the
    //  game has no solution
    // Returns true if the wall has a distance > 1 
    //  to each one of the elements in the array (in this case, the two elements)
    // The "distance" means that both w.x and w.y must be greater by more than 1
    public final boolean dist(Posn p, Posn[] walls, int tam) {
        for(int i=0; i<tam; i++) {
           if(  p.isEqual(walls[i]) ||
               (  p.y == walls[i].y   && (p.x == walls[i].x-1 || p.x == walls[i].x + 1) )  ||
               (  p.x == walls[i].x   && (p.y == walls[i].y-1 || p.y == walls[i].y + 1) )  ||
               ( (p.x == walls[i].x-1 || p.x == walls[i].x+1) && (p.y == walls[i].y-1 || p.y == walls[i].y+1) ) )
                return true;
        }
        return false;
    }
    
    public static int randomInt() {
        return new Random().nextInt(numberBlocks-1);
    }
    
    // Convert (720,720) into (5,5) for example
    public static Posn convertToIndex(Posn pos) {
        return new Posn(  (pos.x - blockSize/2)/blockSize  ,  (pos.y - blockSize/2)/blockSize  );
    }
    
    // Convert (5,5) into (720,720) for example
    public static Posn convertFromIndex(Posn pos) {
        return new Posn(  ((pos.x * blockSize) + blockSize/2)  ,  ((pos.y * blockSize) + blockSize/2)  );
    }
    
    public RectangleImage getEndPoint() {
        return this.endPoint;
    }
}


/*
public class staticObjects {
    
    private final static int size = 720;
    private final static int blockSize = 120;
    private final static int numberBlocks = size/blockSize;
    
    private final RectangleImage endPoint;
   
    public staticObjects(RectangleImage[][] world, playerObject player, int level) {
        Posn p = player.getPos();
        
        //There's no need to check if the position is the same as the mainObject
        //Because they will always be in different rows
        Posn epPos = new Posn(randomInt(), randomInt());
        endPoint ep = new endPoint(epPos.y);
        world[numberBlocks-1][epPos.y] = ep.getRect();
        
        this.endPoint = ep.getRect();
      
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
    
    public static int randomInt() {
        return new Random().nextInt(numberBlocks-1);
    }
    
    public RectangleImage getEndPoint() {
        return this.endPoint;
    }
}

class Obstacles {
    
    private final static int blockSize = 120;
    private final static int size = 720;
    private final static int numberBlocks = size/blockSize;
    
    public int getBlockSize()    { return blockSize; }
    public int getCanvasSize()   { return size; }
    public int getNumberBlocks() { return numberBlocks; }    
    
    public int randomInt() {
        int rn = new Random().nextInt(getNumberBlocks());
        return (getNumberBlocks()*rn + (getNumberBlocks()/2));
    }
    
    // Convert (5,5) into (720,720) for example
    public Posn convertPos(Posn pos) {
        return new Posn(pos.x*getBlockSize() + getBlockSize()/2, pos.y*getBlockSize() + getBlockSize()/2);
    }
    
}

class redSquare extends Obstacles {
    private final Color color;
    private final RectangleImage rect;
    
    public redSquare(Posn pos) {
        this.color = Color.RED;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }

    public RectangleImage getRect() {
        return this.rect;
    }
}

class blueSquare extends Obstacles {
    private final Color color;
    private final RectangleImage rect;
    
    blueSquare(Posn pos) {
        this.color = Color.BLUE;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    public RectangleImage getRect() {
        return this.rect;
    }
}

class wall extends Obstacles {
    private final Color color;
    private final RectangleImage rect;
    
    wall(Posn pos) {
        this.color = Color.GREEN;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    public RectangleImage getRect() {
        return this.rect;
    }
}

class endPoint extends Obstacles {
    private final Color color;
    private final RectangleImage rect;
    
    endPoint(int y) {
        Posn pos = new Posn(getNumberBlocks()-1, y);
        this.color = Color.BLACK;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    public RectangleImage getRect() {
        return this.rect;
    }    
}

class empty extends Obstacles {
    private final Color color;
    private final RectangleImage rect;

    public empty(Posn pos) {
        this.color = Color.LIGHT_GRAY;
        rect = new RectangleImage(convertPos(pos), getBlockSize(), getBlockSize(), color);
    }
    
    public RectangleImage getRect() {
        return this.rect;
    }
}*/