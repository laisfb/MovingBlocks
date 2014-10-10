package movingblocks;

import java.awt.Color;
import static java.lang.Math.*;
import java.util.*;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;


class mySortedList {

    private LinkedList<myNode> internalList  = new LinkedList<>();
    private int size;
    
    public mySortedList() {
        this.size = internalList.size();
    }
    
    public boolean isEmpty() {
        return internalList.isEmpty();
    }
    
    public int getSize() {
        return this.size;
    }
    
    public myNode getAt(int i) {
        return internalList.get(i);
    }
    
    public myNode getFirst() {
        return internalList.getFirst();
    }
    
    public void removeFirst() {
        internalList.removeFirst();
        this.size = internalList.size();
    }
    
    public void addFirst(myNode n) {
        internalList.addFirst(n);
        this.size = internalList.size();
    }
    
    public void addLast(myNode n) {
        internalList.addLast(n);
        this.size = internalList.size();
    }
    
    public void addAt(myNode n, int i) {
        internalList.add(i, n);
        this.size = internalList.size();
    }
    
    public void addFrom(myNode n, int start) {
        if(start==size)
            this.addLast(n);
        else
            this.add(n, start, this.size-1);
    }
    
    public void add(myNode n) {
        if(!contains(n))
            add(n, 0, this.size-1);
    }
    
    public void add(myNode n, int min, int max) {
        if(max==min) {      //it has only one element
            myNode m = internalList.get(max);
            if(n.getF() < m.getF()) {
                addAt(n, max);
            }
            else {
                addAt(n, max+1);
            }
        }
        else if(max-min == 1) {     //it has two elements
            myNode m1 = internalList.get(min);
            myNode m2 = internalList.get(max);
            if(n.getF() < m1.getF())
                addAt(n, min);
            else {
                if(n.getF() < m2.getF())
                    addAt(n, max);
                else
                    addAt(n, max+1);               
            }
        }
        else {      //more than two elements
            int pos = min + (max-min)/2;
            myNode half = internalList.get(pos);
            if (n.getF() < half.getF()) {           
                add(n, min, pos - 1);
            }
            else { 
                add(n, pos + 1, max);
            }
        }
    }
    
    public boolean contains(myNode n) {
        for(int i=0; i<size; i++) {
            if(n.isEqual(internalList.get(i)))
                return true;
        }
        return false;
    }
}

class myNode {
    int i, j; //position in world
    int g, h, f; //g: steps taken so far
                 //h: estimate number of steps to goal
                 //f: = g + h;
    
    myNode(Posn pos) {
        this.i = pos.x;
        this.j = pos.y;
        
        this.g = this.h = this.f = 0;
    }
    
    myNode(Posn pos, int g, int h) {
        this.i = pos.x;
        this.j = pos.y;
        
        this.g = g;
        this.h = h;
        this.f = g + h;
    }
    
    public void setG(int g) { this.g = g; }    
    public void setH(int h) { this.h = h; }
    public void setF() { this.f = this.g + this.h; }
    
    public int getI() { return this.i; }
    public int getJ() { return this.j; }
    public int getG() { return this.g; }
    public int getH() { return this.h; }
    public int getF() { return this.f; }
    
    public boolean isEqual(myNode n) {
        return (this.i == n.getI() && this.j==n.getJ());
    }
    
    public myNode moveRight() {
        Posn pos = new Posn(i+1, j);
        return new myNode(pos);
    }
    
    public myNode moveLeft() {
        Posn pos = new Posn(i-1, j);
        return new myNode(pos);
    }
    
    public myNode moveUp() {
        Posn pos = new Posn(i, j-1);
        return new myNode(pos);
    }
    
    public myNode moveDown() {
        Posn pos = new Posn(i, j+1);
        return new myNode(pos);
    }
    
}

public class ASearch {
    private final static int blockSize = 120;
    private final int size = 720;
    private final int numberBlocks = size/blockSize;
    private final RectangleImage[][] world;
    
    private myNode root;
    private myNode goal;
    private final mySortedList myNodeList = new mySortedList();
    private int solution = -1;
    
    public ASearch(RectangleImage[][] world, playerObject player) {
        this.world = world;
        makeGoal(world);
        makeRoot(player);
        
        //add root to list
        myNodeList.addLast(root);
        int i = 0;
        
        //did it checked everyone in the list?
        while(i < size) {
            //System.out.println("Keep going! Size: " + myNodeList.getSize());
            //check if first node is goal
            if ( goal.isEqual(myNodeList.getAt(i)) ) {
                //if yes, done
                this.solution = (myNodeList.getAt(i)).getF();
                System.out.println("Yay! I found a solution!");
                return;
            }
            else {
                //System.out.println("Not goal. Expand!");
                expand(myNodeList.getAt(i), i+1);       //expand node and add children to list
                //System.out.println("Skiped already explored node");
                i++;
            }
        }
        
        if(i==size)
            System.out.println("Error! Solution not found!");
        
    }
    
    public int getSolution() {
        return this.solution;
    }
    
    private void makeRoot(playerObject player) {
        this.root = new myNode(convertPos(player.getPos()));
        this.root.setG(0);
        estimateH(root);
        this.root.setF();
    }
    
    private void makeGoal(RectangleImage[][] world) {
        Posn pos = new Posn(0,0);
        for(int y=0; y<numberBlocks; y++)
            if( (world[numberBlocks-1][y]).color == Color.BLACK )
                pos = new Posn(numberBlocks-1,y); //get position of endPoint
        //System.out.println("Goal: (" + pos.x + "," + pos.y + ")");
        this.goal = new myNode(pos);
    }
    
    // Convert (720,720) into (5,5) for example
    private Posn convertPos(Posn pos) {
        return new Posn(  (pos.x - blockSize/2)/blockSize  ,  (pos.y - blockSize/2)/blockSize  );
    }
    
    private void expand(myNode n, int x) {
        
        //DON'T add child if it is off board or a wall
        
        myNode child = n.moveRight();
        if(onBoard(child) && !isWall(child)) {
            estimateH(child);
            setG(n, child);        
            child.setF();
            myNodeList.addFrom(child,x);
        }
        
        child = n.moveLeft();
        if(onBoard(child) && !isWall(child)) {
            estimateH(child);
            setG(n, child);
            child.setF();
            myNodeList.addFrom(child,x);
        }
        
        child = n.moveUp();
        if(onBoard(child) && !isWall(child)) {
            estimateH(child);
            setG(n, child);
            child.setF();    
            myNodeList.addFrom(child,x);
        }
        
        child = n.moveDown();
        if(onBoard(child) && !isWall(child)) {
            estimateH(child);
            setG(n, child);            
            child.setF();
            myNodeList.addFrom(child,x);
        }
    }
    
    private boolean onBoard(myNode n) {
        return (n.getI() >= 0 && n.getI() < numberBlocks && 
                n.getJ() >= 0 && n.getJ() < numberBlocks);
    }
    
    private boolean isWall(myNode n) { 
        return world[n.getI()][n.getJ()].color == Color.GREEN;
    }
    
    private boolean isRed(myNode n) {
        return world[n.getI()][n.getJ()].color == Color.RED;
    }
    
    private boolean isBlue(myNode n) {
        return world[n.getI()][n.getJ()].color == Color.BLUE;
    }    
    
    private void estimateH(myNode n) {
        n.setH((goal.getI() - n.getI()) + abs(goal.getJ() - n.getJ()));
    }
    
    private void setG(myNode parent, myNode child) {
        if(isRed(child))
            child.setG(parent.getG() + 2);
         else if(isBlue(child))
            child.setG(parent.getG() + 0); //added zero just to understand what's happening
         else
             child.setG(parent.getG() + 1);
    }
    
    
}
