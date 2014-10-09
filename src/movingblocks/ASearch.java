/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movingBlocks;

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
    
    public int getSize() {
        return this.size;
    }
    
    public void addLast(myNode n) {
        internalList.addLast(n);
        this.size = internalList.size();
    }
    
    public void addFirst(myNode n) {
        internalList.addFirst(n);
        this.size = internalList.size();
    }
    
    public void add(myNode n, int i) {
        internalList.add(i, n);
        this.size = internalList.size();
    }
    
    public myNode get(int i) {
        return internalList.get(i);
    }
    
    public boolean isEmpty() {
        return internalList.isEmpty();
    }
    
    public myNode getFirst() {
        return internalList.getFirst();
    }
    
    public void removeFirst() {
        internalList.removeFirst();
        this.size = internalList.size();
    }
    
    public void clear() {
        internalList.clear();
        this.size = internalList.size();
    }
    
    public void add(myNode n) {
        if(!contains(n))
            add(n, 0, size-1);
    }
    
    public void add(myNode n, int min, int max) {
        if(max==min) {      //it has only one element
            myNode m = internalList.get(max);
            if(n.getF() < m.getF()) {
                add(n, max);
            }
            else {
                add(n, max+1);
            }
        }
        else if(max-min == 1) {     //it has two elements
            myNode m1 = internalList.get(min);
            myNode m2 = internalList.get(max);
            if(n.getF() < m1.getF())
                add(n, min);
            else {
                if(n.getF() < m2.getF())
                    add(n, max);
                else
                    add(n, max+1);               
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
    public int getF() { return this.f; }
    
    public boolean isEqual(myNode n) {
        return (this.i == n.getI() && this.j==n.getJ());
    }
    
    public myNode moveRight() {
        Posn pos = new Posn(i+1, j);
        return new myNode(pos, g+1, 0);
    }
    
    public myNode moveLeft() {
        Posn pos = new Posn(i-1, j);
        return new myNode(pos, g+1, 0);
    }
    
    public myNode moveUp() {
        Posn pos = new Posn(i, j-1);
        return new myNode(pos, g+1, 0);
    }
    
    public myNode moveDown() {
        Posn pos = new Posn(i, j+1);
        return new myNode(pos, g+1, 0);
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
        
        //check if list is empty
        while(!myNodeList.isEmpty()) {
            //System.out.println("Keep going! Size: " + myNodeList.getSize());
            //check if first node is goal
            if ( goal.isEqual(myNodeList.get(i)) ) {
                //if yes, done
                this.solution = (myNodeList.get(i)).getF();
                System.out.println("Yay! You found a solution!");
                return;
                //
            }
            else {
                //System.out.println("Not goal. Expand!");
                expand(myNodeList.get(i));
                //System.out.println("Skiped already explored node");
                i++;
                //myNodeList.removeFirst();
                //expand node and add children to list
                //remove the first node, already checked
            }
        }
        
        if(myNodeList.isEmpty())
            System.out.println("Error! Solution not found!"); //if yes, fail
        
    }
    
    public int getSolution() {
        return this.solution;
    }
    
    private void makeRoot(playerObject player) {
        this.root = new myNode(convertPos(player.getPos()));
        this.root.setG(0);
        this.root.setH(estimateH(root,goal));
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
    
    private void expand(myNode n) {
        myNode child = n.moveRight();
        if(validMove(child)) {
            child.setH(estimateH(child,goal));
            myNodeList.add(child);
        }
        
        child = n.moveLeft();
        if(validMove(child)) {
            child.setH(estimateH(child,goal));
            myNodeList.add(child);
        }
        
        child = n.moveUp();
        if(validMove(child)) {
            child.setH(estimateH(child,goal));
            myNodeList.add(child);
        }
        
        child = n.moveDown();
        if(validMove(child)) {
            child.setH(estimateH(child,goal));
            myNodeList.add(child);
        }
    }
    
    private boolean validMove(myNode n) {
        return (n.getI() >= 0 && n.getI() < numberBlocks &&
                n.getJ() >= 0 && n.getJ() < numberBlocks); //true if on board
    }
    
    private int estimateH(myNode n, myNode goal) {
        return (goal.getI() - n.getI()) + abs(goal.getJ() - n.getJ());
    }
    
    
}
