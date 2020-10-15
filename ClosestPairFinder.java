/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toaproject1;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;


/**
 *
 * @author Ricky_W
 */
public class ClosestPairFinder {
    
    //array of ordered pairs
    private OrderedPair[] points = new OrderedPair[10000];
    
    public ClosestPairFinder(){
        
        //run generate pairs function
        this.generatePairs();
    }
    /**
     * uses random.org to generate random numbers, which are then used to make ordered pairs
     */
    private void generatePairs(){
        try{
            //gets 2n random numbers from random.org
            //MODIFY TO GET MORE RANDOM NUMBERS
            URL numbers = new URL("https://www.random.org/integers/?num=10000&min=1&max=1200&col=1&base=10&format=plain&rnd=new");
            //loops through all numbers to create n ordered pairs and puts them in array
            Scanner scan = new Scanner(numbers.openStream());
            int x = 0;
            while(scan.hasNext()){
                int num1 = Integer.parseInt(scan.next());
                int num2 = Integer.parseInt(scan.next());
                OrderedPair tempOP = new OrderedPair(num1, num2);
                points[x] = tempOP;
                x++;
            }
            //this needs to happen twice because for 10k points you need 20k random numbers
            //and random.org only allows 10k at a time
            URL nums2 = new URL("https://www.random.org/integers/?num=10000&min=1&max=1200&col=1&base=10&format=plain&rnd=new");
            Scanner scan2 = new Scanner(nums2.openStream());
            while(scan2.hasNext()){
                int num1 = Integer.parseInt(scan2.next());
                int num2 = Integer.parseInt(scan2.next());
                OrderedPair tempOP = new OrderedPair(num1, num2);
                points[x] = tempOP;
                x++;
            }
            
        }
        catch(MalformedURLException e){
            System.out.println("error in generating pairs for closest pairs finder due to malformed url");
        }
        catch(IOException e){
            System.out.println("error in opening the stream");
        }
    }
    
    // 
    /**
     * begins the divide and conquer
     *returns array of size 2 because the answer is a pair of ordered pairs
     * @return 
     */
    public OrderedPair[] findClosestPair(){
        
        //this starts d-c but calls helper which does recursion
        //sorts the ordered pairs by x value
        Arrays.sort(points, new SortByX());
        return findPairHelper(points);
    }
    
    
    /**
     * recursively finds the closest pair of points in a half and across halves
     * @param pairs
     * @return 
     */
    private OrderedPair[] findPairHelper(OrderedPair[] pairs){
        //this does the d-c recursion
        if(pairs.length == 2){
            //base case 1
            //there are only two pairs in the half, so they are the closest in the half, return them
            return pairs;
        }
        else if(pairs.length == 3){
            //base case 2
            //check distance pairwise, return the PAIR with the min distance
            OrderedPair[] out = new OrderedPair[2];
            OrderedPair p1 = pairs[0];
            OrderedPair p2 = pairs[1];
            OrderedPair p3 = pairs[2];
            double p1p2 = getDistance(p1, p2);
            double p1p3 = getDistance(p1, p3);
            double p2p3 = getDistance(p2, p3);
            
            if(p1p2 <= p1p3 && p1p2 <= p2p3){
                out[0] = p1;
                out[1] = p2;
            }
            else if(p1p3 <= p1p2 && p1p3 <= p2p3){ 
                out[0] = p1;
                out[1] = p3;
            }
            else{
                //p2p3 is smallest
                out[0] = p2;
                out[1] = p3;
            }
            return out;
        }
        else{
            //divide it and do recursive calls
            int len = pairs.length;
            int left = Math.floorDiv(len, 2);
            int right = len - left;
            OrderedPair[] leftHalf = Arrays.copyOfRange(points, 0, left);
            OrderedPair[] rightHalf = Arrays.copyOfRange(points, right, len);
            OrderedPair[] closestRight = findPairHelper(rightHalf);
            OrderedPair[] closestLeft = findPairHelper(leftHalf);
            
            //find the delta to check across midline
            double rightDist = getDistance(closestRight[0], closestRight[1]);
            double leftDist = getDistance(closestLeft[0], closestLeft[1]);
            double delta = Math.min(leftDist, rightDist);
            
            //finds the mid x line
            OrderedPair mid1 = leftHalf[leftHalf.length - 1];
            OrderedPair mid2 = rightHalf[0];
            double midX = (mid1.getX() + mid2.getX()) / 2; //value of the middle x line
            
            //find pairs within this delta/2 distance of midline in pairs array
            for(int i = 0; i < rightHalf.length; i++){
                OrderedPair temp = rightHalf[i];
                if(temp.getX() > (midX + (delta/2))){
                    if( i > 1){
                        //this means there will be data in the array due to the 
                        //nummber of times the loop has gone through
                        rightHalf = Arrays.copyOfRange(rightHalf, 0, i-1);
                    }
                    else{
                        //the loop ended on its first iteration so there are no
                        //points within delta/2
                        rightHalf = new OrderedPair[0];
                    }
                    
                    break;
                }
            }
            for(int i = leftHalf.length - 1; i >= 0; i--){
                OrderedPair temp = leftHalf[i];
                if(temp.getX() < (midX - (delta/2))){
                    if(i < leftHalf.length - 2){
                        //this means there will be data in the array due to the number
                        //of times the loop has gone through
                        leftHalf = Arrays.copyOfRange(leftHalf, i + 1, leftHalf.length - 1);
                    }
                    else{
                        //the loop ended on its first iteration so there are 
                        //no points within delta/2
                        leftHalf = new OrderedPair[0];
                    }
                    
                    break;
                }
            }
            
            //creates new array of only points within delta of midline
            int newTotalLength = rightHalf.length + leftHalf.length;
            OrderedPair[] withinDelta = new OrderedPair[newTotalLength];
            for(int i = 0; i < rightHalf.length; i++){
                withinDelta[i] = rightHalf[i];
            }
            for(int i = 0; i < leftHalf.length; i++){
                withinDelta[i + rightHalf.length] = leftHalf[i];
            }
            
            //sort remaining points by Y values
            Arrays.sort(withinDelta, new SortByY());
            OrderedPair[] possibleClosest = null;
            
            //compare each point to its next 11 neighbors
            int limit = withinDelta.length - 1;
            for(int i = 0; i < limit; i++){
                OrderedPair temp1 = withinDelta[i];
                if((limit - i) > 11){
                    for(int y = 1; y < 12; y++){
                        OrderedPair temp2 = withinDelta[i + y];
                        double distance = getDistance(temp1, temp2);
                        if(distance < delta){
                            possibleClosest = new OrderedPair[2];
                            delta = distance;
                            possibleClosest[0] = temp1;
                            possibleClosest[1] = temp2;
                        }
                    }
                }
                else{
                    for(int y = i+1; y < limit + 1; y++){
                        OrderedPair temp2 = withinDelta[y];
                        double distance = getDistance(temp1, temp2);
                        if(distance < delta){
                            possibleClosest = new OrderedPair[2];
                            delta = distance;
                            possibleClosest[0] = temp1;
                            possibleClosest[1] = temp2;
                        }
                    }
                }
            }
            

            //compare the three closest pairs and return them
            double possibleClosestDistance;
            if(possibleClosest == null){
                //this can occur if no points are within delta of the midline
                //so make the value arbitrarily high so it is never picked
                possibleClosestDistance = 999999999;
            }
            else{
                possibleClosestDistance = getDistance(possibleClosest[0] , possibleClosest[1]);
            }
            
            if(rightDist <= possibleClosestDistance && rightDist <= leftDist){
                return closestRight;
            }
            else if(leftDist <= rightDist && leftDist <= possibleClosestDistance){
                return closestLeft;
            }
            else{
                return possibleClosest;
            }
        }
        
        
        
    }
    
    
    /**
     * finds distance between 2 ordered pairs
     * @param op1
     * @param op2
     * @return 
     */
    private double getDistance(OrderedPair op1, OrderedPair op2){
        //uses basic distance formula
        int x1 = op1.getX();
        int x2 = op2.getX();
        int y1 = op1.getY();
        int y2 = op2.getY();
        int xDif = x1 - x2;
        int yDif = y1 - y2;
        double distance = Math.sqrt((Math.pow(xDif, 2)) + (Math.pow(yDif, 2)));
        
        return distance;
    }
}
