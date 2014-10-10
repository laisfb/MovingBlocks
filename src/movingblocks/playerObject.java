package movingblocks;

import java.awt.Color;
import java.util.Random;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;

public class playerObject {
    private final static int blockSize = 120;
    private final int size = 720;
    private final int numberBlocks = size/blockSize;
    
    private final Color color;
    private final RectangleImage rect;
    
    public int randomInt() {
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
        Posn oldPos = convertPos(this.rect.pinhole);
        if(move.x <= size - blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved to the right");
            world[oldPos.x][oldPos.y] = new empty(oldPos).getRect();
            this.rect.pinhole.x += blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            System.out.println("Invalid move");
            return 0;
        }
    }
    
    public int moveLeft(RectangleImage[][] world) {
        Posn move = new Posn(this.rect.pinhole.x - blockSize, this.rect.pinhole.y);
        Posn oldPos = convertPos(this.rect.pinhole);
        if(move.x >= blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved to the left");
            world[oldPos.x][oldPos.y] = new empty(oldPos).getRect();
            this.rect.pinhole.x -= blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            System.out.println("Invalid move");
            return 0;
        }
    }
    
    public int moveUp(RectangleImage[][] world) {
        Posn move = new Posn(this.rect.pinhole.x, this.rect.pinhole.y - blockSize);
        Posn oldPos = convertPos(this.rect.pinhole);
        if(move.y >= blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved up");
            world[oldPos.x][oldPos.y] = new empty(oldPos).getRect();
            this.rect.pinhole.y -= blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            System.out.println("Invalid move");
            return 0;
        }
    }
    
    public int moveDown(RectangleImage[][] world) {
        Posn move = new Posn(this.rect.pinhole.x, this.rect.pinhole.y + blockSize);
        Posn oldPos = convertPos(this.rect.pinhole);
        if(move.y <= size - blockSize/2 && !isWall(move,world)) {
            //System.out.println("Moved down");
            world[oldPos.x][oldPos.y] = new empty(oldPos).getRect();
            this.rect.pinhole.y += blockSize;
            if(isRed(move,world))
                return 2;
            else if(isBlue(move,world))
                return 0;
            else
                return 1;
        }
        else {
            System.out.println("Invalid move");
            return 0;
        }
    }
    
    // Convert (720,720) into (5,5) for example
    public Posn convertPos(Posn pos) {
        return new Posn(  (pos.x - blockSize/2)/blockSize  ,  (pos.y - blockSize/2)/blockSize  );
    }
    
    public boolean isWall(Posn pos, RectangleImage[][] world) {
        Posn p = convertPos(pos);
        return world[p.x][p.y].color == Color.GREEN;
    }
    
    public boolean isRed(Posn pos, RectangleImage[][] world) {
        Posn p = convertPos(pos);
        return world[p.x][p.y].color == Color.RED;
    }
    
    public boolean isBlue(Posn pos, RectangleImage[][] world) {
        Posn p = convertPos(pos);
        return world[p.x][p.y].color == Color.BLUE;
    }
}