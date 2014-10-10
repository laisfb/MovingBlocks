package movingblocks;

import java.awt.Color;
import java.util.Random;

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
    private boolean gameOver = false;
    
    private final boolean testingMode;
    
    private int steps = 0;
    private int best = 0;
    private int score = 0;
    
    public mainObject(playerObject player, int level, boolean mode) {
        
        this.player = player;
        this.LEVEL = level;
        this.testingMode = mode;
        
        System.out.println("\n------------- LEVEL " + level + "-------------");
        System.out.println("Is this a testing mode? " + this.testingMode);
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
        if(!gameOver) {
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
                gameOver = true;
                System.out.println("Press any key to go to the next level...");
                LEVEL++;
            }
        }
        else {
            gameOver = false;
            if(LEVEL < 8) {
                    playerObject player = new playerObject();
                    mainObject obj = new mainObject(player, LEVEL, this.testingMode);
                    // run the next level
                    return obj;
                }
                else {
                    System.out.println("The game is over.");
                    System.out.println("Your score is: " + score);
                    this.endOfWorld("");
            }
        }
        
        this.score += 100 - 10*(best-steps);
            
        return this;        
    }
    
    @Override
    public World onTick() {
        if(!testingMode)
            return this;
        else {
            return this.onKeyEvent(randomMove());
        }        
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
    
    
    // =============================== Methods used for testing ===============================
    public static int randomInt(int max) {
        return new Random().nextInt(max);
    }
    
    public String randomMove() {
        int x = randomInt(9);
        String str = "";
        switch(x) {
            case 0:
                str = "left";
                break;
            case 1:
            case 4:
            case 7:
                str = "right";
                break;
            case 2:
            case 5:
            case 8:
                str = "up";
                break;
            case 3:
            case 6:
            case 9:
                str = "down";
                break;
        }
        return str;
    }
    
    
}