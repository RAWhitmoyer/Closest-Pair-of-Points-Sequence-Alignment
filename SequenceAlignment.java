/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toaproject1;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author Ricky_W
 */
public class SequenceAlignment {
    
    private String userIn;
    //input word by user
    private int mismatch;
    private int gap;
    //mismatch and gap penalties respectively
    private String dict[] = new String[1000];
    //array to store words in
    
    /**
     * constructor
     * @param mismatch
     * @param gap
     * @param in 
     */
    public SequenceAlignment(int mismatch, int gap, String in){
        this.mismatch = mismatch;
        this.gap = gap;
        this.userIn = in;
        //this.dictToArray(); //gets dictionary words from random.org and stores them in an array
        this.txtToArray();
    }
    /**
     * returns array of all random strings generated
     * @return 
     */
    public String[] outputDict(){
        return dict;
    }
    /**
     * finds min cost between user's word and another word (from the dictionary)
     * @param dictWord 
     */
    private int minPenalty (String dictWord){
        //do bottom up linear programming
        //m rows, n columns in memoization array
        int m = dictWord.length();
        int n = userIn.length();
        int memArr[][] = new int [n+1][m+1];
        
        
        //two for loops to intialize
        for(int i = 0; i <= m; i++){
            memArr[0][i] = i * gap;
        }
        for(int i = 0; i <= n; i++){
            memArr[i][0] = i * gap;
        }
        
        
        //loops to populate memoization array
        for(int x = 1; x <= n; x++){
            for(int y = 1; y <= m; y++){
                char currDictChar = dictWord.charAt(y - 1);
                char currUserInChar = userIn.charAt(x - 1);
                int poss1;
                if(currDictChar == currUserInChar){
                    poss1 = 0 + memArr[x-1][y-1]; //no mismatch and previous best alignment
                }
                else{
                    poss1 = mismatch + memArr[x-1][y-1]; //take the mismatch and previous alignment
                }
                
                int poss2 = gap + memArr[x-1][y]; //take gap
                int poss3 = gap + memArr[x][y-1]; //take gap
                int editDistance = Math.min(poss1, Math.min(poss2, poss3));
                memArr[x][y] = editDistance;
            }
        }
        
        return memArr[n][m];
    }
    
    private void txtToArray(){
        try{
            File words = new File("thousandWords.txt");
            Scanner scan = new Scanner(words);
            int x = 0;
            while(scan.hasNext()){
                dict[x] = scan.next();
                x++;
            }
            //dict is now full of words from the text file
        }
        catch(Exception e){
            System.out.println("error in txtToArray");
        }
    }
    
    /**
     * gets random strings from Random.org, puts them into an array
     */
    private void dictToArray(){
        //turns a dictionary of words into an array of strings
        //call in constructor
        //do they need to be real words?????? no
        ///////////////////////////////
        //DONT FORGET TO SWITCH TO 1000 WORDS AND CHANGE ARRAY LENGTH
        //////////////////////////////
        try{
            //tries to get strings from online source
            URL randWords = new URL("https://www.random.org/strings/?num=1000&len=10&loweralpha=on&unique=on&format=plain&rnd=new");
            Scanner scan = new Scanner(randWords.openStream());
            
            
            //adds string to array
            int x = 0;
            while(scan.hasNext()){
                dict[x] = scan.next();
                x++;
            }
            //dict is now full of words
            
        }
        catch(Exception e){
            //catch url exception
            System.out.println("exception in dictToArray");
        }
        
    }
    
    /**
     * finds the closest word in dictionary to user input based on the sequence alignment alg
     * @return 
     */
    public String findClosestWord(){
        //compares user word to all dictionary words,
        //keeps track of closest word (minPenalty) during runtime
        //loops through array of words one by one
        
        
        ////////////////////////
        //CHANGE LOOP SIZE TO 1000
        ////////////////////////
        //loops through dictionary and compares every word to the user input
        //keeps track of size of smallest penalty
        //keeps track of word with smallest penalty
        int smallestPenalty = minPenalty(dict[0]);
        String minPenaltyWord = dict[0];
        for(int x = 0; x < dict.length; x++){
            String temp = dict[x];
            int tempPen = minPenalty(temp);
            if(tempPen <= smallestPenalty){
                smallestPenalty = tempPen;
                minPenaltyWord = temp;
            }
        }
        
        return minPenaltyWord;
    }
}
