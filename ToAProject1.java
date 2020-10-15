/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toaproject1;

import java.util.Scanner;

/**
 *
 * @author Ricky_W
 */
public class ToAProject1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //scanner to get user input for sequence alignment
        Scanner scan = new Scanner(System.in);
        System.out.println("enter a word:");
        String word = scan.next();
        System.out.println("Enter a mismatch penalty:");
        int mm = Integer.parseInt(scan.next());
        System.out.println("Enter a gap penalty:");
        int gap = Integer.parseInt(scan.next());
        
        //creates sequence alignment object
        SequenceAlignment seqAl = new SequenceAlignment(mm, gap, word);
        
//        //lets see the words
//        //used for testing
//        String[] dict = seqAl.outputDict();
//        for(int i = 0; i < dict.length; i++){
//            System.out.println(dict[i]);
//        }

        
        //finds and displays the word
        String closeword = seqAl.findClosestWord();
        System.out.println("closest word: " + closeword);
        
        
        //closest pair of points
        System.out.println("");
        System.out.println("closest pairs");
        ClosestPairFinder cpf = new ClosestPairFinder();
        
        //gets array of ordered pairs that contains two closest pairs
        //note these can be the same two points based on number of pairs generated
        //and range of numbers which are generated
        OrderedPair[] closestPair = cpf.findClosestPair();
        
        //outputs the pairs
        for(int i = 0; i < closestPair.length; i++){
            System.out.println(closestPair[i]);
        }
        
    }
    
}
