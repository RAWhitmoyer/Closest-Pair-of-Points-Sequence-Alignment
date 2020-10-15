/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toaproject1;

import java.util.Comparator;

/**
 *
 * @author Ricky_W
 */
public class SortByX implements Comparator<OrderedPair>{
    
    //sorts in ascending order of x values
    public int compare(OrderedPair p1, OrderedPair p2){
        return p1.getX() - p2.getX();
    }
}
