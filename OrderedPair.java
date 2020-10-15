/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toaproject1;

/**
 *
 * @author Ricky_W
 */
public class OrderedPair {
    
    //x and y coordinates
    private int x;
    private int y;
    
    /**
     * takes integer values for x and y coordinates of ordered pairs
     * @param x
     * @param y 
     */
    public OrderedPair(int x, int y){
        this.x = x;
        this.y = y;
        
    }
    /**
     * returns the x value
     * @return 
     */
    public int getX(){
        return x;
    }
    
    /**
     * returns the y value
     * @return 
     */
    public int getY(){
        return y;
    }
    /**
     * returns String of ordered pair
     * output format is [x,y]
     * @return 
     */
    public String toString(){
        String out = "[" + this.getX() + "," + this.getY() + "]";
        return out;
    }
    
}
