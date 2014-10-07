package movingBlocks;

import java.awt.Color;
import java.util.Random;

import javalib.funworld.*;
import javalib.worldimages.*;

public class mainObject extends World {
    
    private final static int blockSize = 120;
    private final static int width = 720;
    private final static int height = 720;
    private final static int numberBlocks = height/blockSize;
    
    private final static RectangleImage[][] world = new RectangleImage[numberBlocks][numberBlocks];
    private final playerObject player;
    private boolean end = false;
    private int steps = 0;
    
    public mainObject(playerObject player) {        
        this.player = player;
        
        // Clear the world
        for(int i=0; i<numberBlocks; i++)
            for(int j=0; j<numberBlocks; j++) {
                Posn pos = new Posn(i,j);
                empty emp = new empty(pos);
                world[i][j] = emp.getRect();
            }
        
        // Put the static objects on the array
        staticObjects staticWorld = new staticObjects(world, player);
        
        // Add the player to the world
        int j = (player.getPos().y - (blockSize/2))/blockSize;
        world[0][j] = player.getRect();
    }

    @Override
    public WorldImage makeImage() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        OverlayImages img = new OverlayImages(world[0][0], world[0][0]);
        
        // Draw the static objects
        for(int i=0; i<numberBlocks; i++)
            for(int j=0; j<numberBlocks; j++)
                img = new OverlayImages(img, world[i][j]);
        
        // Draw the black lines and return
        return makeGrid(img);
    }
    
    public OverlayImages makeGrid(OverlayImages img) {
        for(int i=(blockSize/2); i<width; i+=blockSize) {
            for(int j=(blockSize/2); j<height; j+=blockSize) {
                WorldImage frame = new FrameImage(new Posn(i, j), blockSize, blockSize, Color.black);
                img = new OverlayImages(img, frame);
                //c.drawImage(frame);
            }
        }
        
        return img;
    }
    
    @Override
    public World onKeyEvent(String str) {
        Posn oldPos = new Posn(0,0);
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
        
        System.out.println("You have taken " + steps + " steps so far.");
        movePlayer(player);
        
        if(end) {
            this.endOfWorld("You reached the goal! Steps taken: " + steps);
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
    
}