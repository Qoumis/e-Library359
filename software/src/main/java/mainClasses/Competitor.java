package mainClasses;

import java.util.ArrayList;

/**This class is used for the giveaway bonus
 * This competitor class holds each user that participates in the giveaway
 * */
public class Competitor {
    String name;
    ArrayList<Integer> entries; //this will contain elements equal to the amount of entry_count
    int entry_count;            //the number of entries each user has


    public Competitor(String name, int currentEntries, int myEntries) {
        this.name = name;
        this.entry_count = myEntries;
        entries = new ArrayList<Integer>();
        for(int i = 0; i < myEntries; i++)
            entries.add(++currentEntries);
    }

    /**We use this after the first giveaway is done so that the 2nd giveaway is correct*/
    public void reAssignEntries(int currentEntries, int myEntries){
        entries.clear();
        for(int i = 0; i < myEntries; i++)
            entries.add(++currentEntries);
    }

    /**This function checks if the random number is equal to one of user's entry numbers*/
    public boolean checkForWinner(int winningNumber){
        for(int i = 0; i < entries.size(); i++){
            if(entries.get(i) == winningNumber)
                return true;
        }
        return false;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Integer> getEntries(){
        return this.entries;
    }

    public int getEntry_count(){
        return this.entry_count;
    }
}
