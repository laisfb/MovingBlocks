package movingBlocks;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import javalib.worldcanvas.WorldCanvas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author laisfb
 */
public class mainObject implements KeyListener {
    private final static int blockSize = 80;
    private final static int canvasWidth = 800;
    private final static int canvasHeight = 800;
    
    private final RectangleImage rect;
    private final WorldCanvas world;
    
    mainObject(WorldCanvas c) {
        rect = new RectangleImage(startPoint(c), blockSize, blockSize, Color.orange);
        world = c;
        world.drawImage(rect);
    }
    
    public Posn startPoint(WorldCanvas c) {
        Random rn = new Random();
        int range = (canvasHeight / blockSize);
        int randomNum = rn.nextInt(range) + 1;
        
        int y = blockSize*randomNum + (blockSize/2);
        Posn pos = new Posn((blockSize/2), y);
        
        return pos;
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
        world.drawImage(rect);
    }
    
    public void moveLeft() {
        Posn p;
        int x = rect.pinhole.x - blockSize;
        if(x < (canvasWidth - blockSize/2))
            p = new Posn(x, rect.pinhole.y);
        else
            p = rect.pinhole;
        
        rect.getMovedTo(p);          
        world.drawImage(rect);    
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}