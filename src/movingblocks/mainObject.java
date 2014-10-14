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
        
    private int steps = 0;
    private int best = 0;
    private int score = 0;
    private final int accScore;
    
    //***** Variables used for testing *****
    private final boolean testingMode;
    private final Posn endPoint;
    private String move = "";
    private Posn oldPos;
    private int stepsBeforeMove = 0;
    private int done = 0;
    //**************************************
    
    public mainObject(playerObject player, int level, boolean mode, int acc) {
        
        this.player = player;
        this.LEVEL = level;
        this.accScore = acc;
        
        //**************************************
        this.testingMode = mode;
        //**************************************
        
        System.out.println("\n\n------------- LEVEL " + level + "-------------");
        //System.out.println("Is this a testing mode? " + this.testingMode);
        
        // Clear the world
        for(int i=0; i<numberBlocks; i++)
            for(int j=0; j<numberBlocks; j++) {
                Posn pos = new Posn(i,j);
                world[i][j] = new RectangleImage(convertFromIndex(pos), blockSize, blockSize, Color.LIGHT_GRAY);
            }
        
        // Put the static objects on the array
        staticObjects staticWorld = new staticObjects(world, player, level);
        
        //**************************************
        this.endPoint = convertToIndex(staticWorld.getEndPoint().pinhole);
        //**************************************
        
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
            }
        }
        
        return img;
    }
    
    @Override
    public World onKeyEvent(String str) {
        this.stepsBeforeMove = this.steps;
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
            if(testingMode)
                testing();
            
            if(end) {
                this.score = 1000 - 10*(steps-best);
                if(this.score < 0)
                    this.score = 0;
                gameOver = true;
                LEVEL++;
                if(!testingMode) {
                    System.out.println("\nYou reached the goal! Steps taken: " + steps);
                    System.out.println("Jsyk, steps taken on the best solution: " + best);
                    System.out.println("Your score on this level was " + this.score);
                    System.out.println("Press any key to go to the next level...");
                }
            }
        }
        else {
            gameOver = false;
            if(LEVEL < 8) {
                // run the next level
                return new mainObject(new playerObject(), LEVEL, this.testingMode, this.accScore + this.score);
            }
            else {
                if(!testingMode) {
                    System.out.println("The game is over.");
                    System.out.println("Your overall score is: " + this.accScore);
                }
                done++;
                if(!testingMode || done == 10)
                    this.endOfWorld("");
                else {
                    System.out.println("\n***** STARTING OVER *****\n");
                    
                    return new mainObject(new playerObject(), 3, this.testingMode, 0);
                }
            }
        }
        
        return this;        
    }
    
    @Override
    public World onTick() {
        if(!testingMode)
            return this;
        else {
            this.oldPos = convertToIndex(player.getPos());
            this.move = randomMove();
            return this.onKeyEvent(move);
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
    
    public void movePlayer(playerObject player) {    
        Posn playerPos = convertToIndex(player.getPos());
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
    
    public void testing() {
        Posn pos = convertToIndex(player.getPos());
        
        //The game must end when
        //  - If the end point is one step below the player
        //    And the player move down
        //  - If the end point is one step above the player
        //    And the player move up
        //  - If the end point is one step to the right of the player
        //    And the player move right
        // Also, the number of steps must be increased by one
        
        if( (new Posn(oldPos.x + 1, oldPos.y).isEqual(endPoint) && this.move.equals("right")) ||
            (new Posn(oldPos.x, oldPos.y - 1).isEqual(endPoint) && this.move.equals("up"))    ||
            (new Posn(oldPos.x, oldPos.y + 1).isEqual(endPoint) && this.move.equals("down"))  ) {
                //System.out.println("\nFound the end point!");
                System.out.println((steps == (this.stepsBeforeMove+1)) + " should be true (end point 1)");
                System.out.println(end + " should be true (end point 2)");
        }                  
        
        //If the player is on the border and it makes an invalid move 
        // (aka trying to "get out" of the board)
        //  the position must stay the same,
        //  and the number of steps can't increase
        if( ( (oldPos.x == 0 && this.move.equals("left"))               )   ||
            ( (oldPos.x == numberBlocks-1 && this.move.equals("right")) )   ||
            ( (oldPos.y == 0 && this.move.equals("up"))                 )   ||
            ( (oldPos.y == numberBlocks-1 && this.move.equals("down"))  )   ) {
                //System.out.println("Hit the border!");
                System.out.println(oldPos.isEqual(pos) + " should be true (border 1)");
                //System.out.println("oldPos: (" + oldPos.x + "," + oldPos.y + ")   |   pos: (" + pos.x + "," + pos.y + ")");
                System.out.println((this.stepsBeforeMove == steps) + " should be true (border 2)");
        }
        
        //If the player hits a wall, the same conditions as above must be true
        if( ( oldPos.x+1 < numberBlocks && (world[oldPos.x+1][oldPos.y].color == Color.GREEN) && this.move.equals("right") )   ||
            ( oldPos.x-1 > 0            && (world[oldPos.x-1][oldPos.y].color == Color.GREEN) && this.move.equals("left")  )   ||
            ( oldPos.y-1 > 0            && (world[oldPos.x][oldPos.y-1].color == Color.GREEN) && this.move.equals("up")    )   ||
            ( oldPos.y+1 < numberBlocks && (world[oldPos.x][oldPos.y+1].color == Color.GREEN) && this.move.equals("down")  )   ) {
                //System.out.println("Hit a wall!");
                System.out.println(oldPos.isEqual(pos) + " should be true (green 1)");
                System.out.println((this.stepsBeforeMove == steps) + " should be true (green 2)");
        }
        
        //If the player steps on a red square, the number of steps must increase by two
        if( ( oldPos.x+1 < numberBlocks && (world[oldPos.x+1][oldPos.y].color == Color.RED) && this.move.equals("right") )   ||
            ( oldPos.x-1 > 0            && (world[oldPos.x-1][oldPos.y].color == Color.RED) && this.move.equals("left")  )   ||
            ( oldPos.y-1 > 0            && (world[oldPos.x][oldPos.y-1].color == Color.RED) && this.move.equals("up")    )   ||
            ( oldPos.y+1 < numberBlocks && (world[oldPos.x][oldPos.y+1].color == Color.RED) && this.move.equals("down")  )   ) {
                //System.out.println("\nRed square!");
                System.out.println((steps == (this.stepsBeforeMove+2)) + " should be true (red)");
        }
        
        //If the player steps on a blue square, the number of steps must stay the same
        if( ( oldPos.x+1 < numberBlocks && (world[oldPos.x+1][oldPos.y].color == Color.BLUE) && this.move.equals("right") )   ||
            ( oldPos.x-1 > 0            && (world[oldPos.x-1][oldPos.y].color == Color.BLUE) && this.move.equals("left")  )   ||
            ( oldPos.y-1 > 0            && (world[oldPos.x][oldPos.y-1].color == Color.BLUE) && this.move.equals("up")    )   ||
            ( oldPos.y+1 < numberBlocks && (world[oldPos.x][oldPos.y+1].color == Color.BLUE) && this.move.equals("down")  )   ) {
                //System.out.println("\nBlue square!");
                System.out.println((steps == this.stepsBeforeMove) + " should be true (blue)");
        }
        
        //If the player steps on a gray square, the number of steps must stay the same
        if( ( oldPos.x+1 < numberBlocks && (world[oldPos.x+1][oldPos.y].color == Color.LIGHT_GRAY) && this.move.equals("right") )   ||
            ( oldPos.x-1 > 0            && (world[oldPos.x-1][oldPos.y].color == Color.LIGHT_GRAY) && this.move.equals("left")  )   ||
            ( oldPos.y-1 > 0            && (world[oldPos.x][oldPos.y-1].color == Color.LIGHT_GRAY) && this.move.equals("up")    )   ||
            ( oldPos.y+1 < numberBlocks && (world[oldPos.x][oldPos.y+1].color == Color.LIGHT_GRAY) && this.move.equals("down")  )   ) {
                //System.out.println("\Empty square!");
                System.out.println((steps == (this.stepsBeforeMove+1)) + " should be true (gray)");
        }
        
        if(end) {
            //System.out.println("\nEnd of the game!");
            System.out.println( (this.best <= this.steps) + " should be true (end of game 1)");
            System.out.println( (this.score <= 1000) + " should be true (end of game 2)");
            System.out.println( (this.accScore <= LEVEL*1000) + " should be true (end of game 3)");
        }
    }
    
}