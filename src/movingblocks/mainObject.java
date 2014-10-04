package movingBlocks;

import java.awt.Color;
import java.util.Random;

import javalib.funworld.*;
import javalib.colors.*;
import javalib.worldcanvas.*;
import javalib.worldimages.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author laisfb
 */
public class mainObject extends World {
    private final static int blockSize = 120;
    private final static int canvasWidth = 720;
    private final static int canvasHeight = 720;
    private final static int numberBlocks = canvasHeight/blockSize;
    
    private final Posn pos;
    private final Color color;
    private final RectangleImage rect;
    
    public int randomInt() {
        int rn = new Random().nextInt(numberBlocks);
        return (blockSize*rn + (blockSize/2));
    }
        
    public mainObject(WorldCanvas c) {
        super();
        int x = (blockSize/2); //first row
        this.pos = new Posn(x, randomInt());
        this.color = Color.orange;
        rect = new RectangleImage(pos, blockSize, blockSize, color);
        c.drawImage(rect);        
    }
    
    public Posn getPos() {
        return this.rect.pinhole;
    }
    
    public RectangleImage getRect() {
        return this.rect;
    }    
    
    public void moveRight() {
        Posn p;
        int x = rect.pinhole.x - blockSize;
        if(x > (blockSize/2))
            p = new Posn(x, rect.pinhole.y);
        else
            p = rect.pinhole;
        
        rect.getMovedTo(p);
    }
    
    public void moveLeft() {
        Posn p;
        int x = rect.pinhole.x - blockSize;
        if(x < (canvasWidth - blockSize/2))
            p = new Posn(x, rect.pinhole.y);
        else
            p = rect.pinhole;
        
        rect.getMovedTo(p);  
    }
    
    
    @Override
    public WorldImage makeImage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public World onKeyEvent(String str) {
	  if (str.equals("x"))
	    System.out.println("Yup!");
	  else
            System.out.println("Nope!");
          
          return this;
    }
    
    @Override
    public World onMouseClicked(Posn p) {
        System.out.println("Click: (" + p.x + "," + p.y + ")");
          
        return this;
    }
    
}