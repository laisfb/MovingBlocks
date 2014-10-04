package movingBlocks;

import com.sun.java.accessibility.util.AWTEventMonitor;
import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import javalib.worldcanvas.WorldCanvas;
import javax.swing.JFrame;
import javax.swing.JTextField;

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
        int x = (blockSize/2); //first row
        this.pos = new Posn(x, randomInt());
        this.color = Color.orange;
        rect = new RectangleImage(pos, blockSize, blockSize, color);
        c.drawImage(rect);
        
        addKeyListener(this);
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
    public void keyPressed(KeyEvent e) {
        System.out.println("sigh");
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Right key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Left key pressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    
}