package inc;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Clique {
    
    private ArrayList<Bid> cliqueBids ;
    private int weight ;

    public Clique() {
        
        this.cliqueBids = new ArrayList<>();
        this.weight = 0 ;      
    }

    public ArrayList<Bid> getCliqueBids() {
        return cliqueBids;
    }

    public void setCliqueBids(ArrayList<Bid> cliqueBids) {
        this.cliqueBids = cliqueBids;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }  
    
}
