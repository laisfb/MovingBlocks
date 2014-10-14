package movingblocks;

import java.awt.Color;
import java.util.Random;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
public class playerObject {
    private static final int blockSize = 120;
    private static final int size = 720;
    private static final int numberBlocks = size/blockSize;
    
    private final Color color;
    private final RectangleImage rect;
    
    public static int randomInt() {
        int rn = new Random().nextInt(numberBlocks);
        return (blockSize*rn + (blockSize/2));
    }
        
    public playerObject() {        
        int x = (blockSize/2); //first row
        Posn pos = new Posn(x, randomInt());
        this.color = Color.orange;
        rect = new RectangleImage(pos, blockSize, blockSize, color);
    }
    
    public Posn getPos() {
        return this.rect.pinhole;
    }
    
    public RectangleImage getRect() {
        return this.rect;
    }
    
    public int moveRight(RectangleImage[][] world) {
        Posn move = new Posn(this.rect.pinhole.x + blockSize, this.rect.pinhole.y);
        Posn oldPos = convertToIndex(this.rect.pinhole);
        if(move.x <= size - blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved to the right");
            world[oldPos.x][oldPos.y] = new RectangleImage(convertFromIndex(oldPos), blockSize, blockSize, Color.LIGHT_GRAY);
            this.rect.pinhole.x += blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            //System.out.println("\nInvalid move");
            return 0;
        }
    }
    
    public int moveLeft(RectangleImage[][] world) {
        Posn move = new Posn(this.rect.pinhole.x - blockSize, this.rect.pinhole.y);
        Posn oldPos = convertToIndex(this.rect.pinhole);
        if(move.x >= blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved to the left");
            world[oldPos.x][oldPos.y] = new RectangleImage(convertFromIndex(oldPos), blockSize, blockSize, Color.LIGHT_GRAY);
            this.rect.pinhole.x -= blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            //System.out.println("\nInvalid move");
            return 0;
        }
    }
    
    public int moveUp(RectangleImage[][] world) {
        Posn move = new Posn(this.rect.pinhole.x, this.rect.pinhole.y - blockSize);
        Posn oldPos = convertToIndex(this.rect.pinhole);
        if(move.y >= blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved up");
            world[oldPos.x][oldPos.y] = new RectangleImage(convertFromIndex(oldPos), blockSize, blockSize, Color.LIGHT_GRAY);
            this.rect.pinhole.y -= blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            //System.out.println("\nInvalid move");
            return 0;
        }
    }
    
    public int moveDown(RectangleImage[][] world) {
        Posn move = new Posn(this.rect.pinhole.x, this.rect.pinhole.y + blockSize);
        Posn oldPos = convertToIndex(this.rect.pinhole);
        if(move.y <= size - blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved down");
            world[oldPos.x][oldPos.y] = new RectangleImage(convertFromIndex(oldPos), blockSize, blockSize, Color.LIGHT_GRAY);
            this.rect.pinhole.y += blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            //System.out.println("\nInvalid move");
            return 0;
        }
    }
    
    // Convert (720,720) into (5,5) for example
    public static Posn convertToIndex(Posn pos) {
        return new Posn(  (pos.x - blockSize/2)/blockSize  ,  (pos.y - blockSize/2)/blockSize  );
    }
    
    // Convert (5,5) into (720,720) for example
    public static Posn convertFromIndex(Posn pos) {
        return new Posn(  ((pos.x * blockSize) + blockSize/2)  ,  ((pos.y * blockSize) + blockSize/2)  );
    }
    
    public boolean isWall(Posn pos, RectangleImage[][] world) {
        Posn p = convertToIndex(pos);
        return world[p.x][p.y].color == Color.GREEN;
    }
    
    public boolean isRed(Posn pos, RectangleImage[][] world) {
        Posn p = convertToIndex(pos);
        return world[p.x][p.y].color == Color.RED;
    }
    
    public boolean isBlue(Posn pos, RectangleImage[][] world) {
        Posn p = convertToIndex(pos);
        return world[p.x][p.y].color == Color.BLUE;
    }
}