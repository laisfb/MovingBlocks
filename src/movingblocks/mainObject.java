package movingBlocks;

import java.awt.Color;

import javalib.funworld.*;
import javalib.worldimages.*;

public class mainObject extends World {
    
    private final static int size = 720;
    private final static int blockSize = 120;
    private final static int numberBlocks = size/blockSize;
    private int LEVEL;
    
    private static final RectangleImage[][] world = new RectangleImage[numberBlocks][numberBlocks];
    private final playerObject player;
    private boolean end = false;
    private int steps = 0;
    private int best = 0;
    
    public mainObject(playerObject player, int level) {
        this.player = player;
        this.LEVEL = level;
        
        System.out.println("\n------------- LEVEL " + level + "-------------");
        // Clear the world
        for(int i=0; i<numberBlocks; i++)
            for(int j=0; j<numberBlocks; j++) {
                Posn pos = new Posn(i,j);
                empty emp = new empty(pos);
                world[i][j] = emp.getRect();
            }
        
        // Put the static objects on the array
        staticObjects staticWorld = new staticObjects(world, player, level);
        
        // Add the player to the world
        int j = (player.getPos().y - (blockSize/2))/blockSize;
        world[0][j] = player.getRect();
        
        this.best = bestPath();
    }

    @Override
    public WorldImage makeImage() {
        
        OverlayImages img = new OverlayImages(world[0][0], world[0][0]);
        
        // Draw the static objects
        for(int i=0; i<numberBlocks; i++)
            for(int j=0; j<numberBlocks; j++)
                img = new OverlayImages(img, world[i][j]);
        
        // Draw the black lines and return
        return makeGrid(img);
    }
    
    public OverlayImages makeGrid(OverlayImages img) {
        for(int i=(blockSize/2); i<size; i+=blockSize) {
            for(int j=(blockSize/2); j<size; j+=blockSize) {
                WorldImage frame = new FrameImage(new Posn(i, j), blockSize, blockSize, Color.black);
                img = new OverlayImages(img, frame);
                //c.drawImage(frame);
            }
        }
        
        return img;
    }
    
    @Override
    public World onKeyEvent(String str) {
        //Posn oldPos = player.getPos();
        switch(str) {
            case "right":
                steps += player.moveRight(world);
                break;
            case "left":
                steps += player.moveLeft(world);
                break;
            case "up":
                steps += player.moveUp(world);
                break;
            case "down":
                steps += player.moveDown(world);
                break;
            default:
                System.out.println("default");
                break;
        }
        
        //System.out.println("You have taken " + steps + " steps so far.");
        movePlayer(player);
        
        if(end) {
            System.out.println("You reached the goal! Steps taken: " + steps);
            System.out.println("Jsyk, steps taken on the best solution: " + best);
            LEVEL++;
            
            if(LEVEL == 8) {
                this.endOfWorld("Game Over!");
            }
            else {
                this.stopWorld();
                playerObject p = new playerObject();
                mainObject obj = new mainObject(p, LEVEL);
        
                // run the next level
                obj.bigBang(size, size, 1);
            }
            
        }
        
        return this;
    }
    
    // Convert (720,720) into (5,5) for example
    public Posn convertPos(Posn pos) {
        return new Posn(  (pos.x - blockSize/2)/blockSize  ,  (pos.y - blockSize/2)/blockSize  );
    }
    
    public void movePlayer(playerObject player) {    
        Posn playerPos = convertPos(player.getPos());
        end = world[playerPos.x][playerPos.y].color == Color.BLACK;
        world[playerPos.x][playerPos.y] = player.getRect();
    }
    
    public int bestPath() {
        return new ASearch(world, player).getSolution();
    }
}