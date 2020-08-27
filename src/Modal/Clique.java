package Modal;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Clique implements Cloneable {
    
    private ArrayList<Bid> cliqueBids = new ArrayList<>() ;
    private double weight = 0 ;

    public Clique() {
        
        this.cliqueBids = new ArrayList<>();
        this.weight = 0 ;      
    }
    
    public Clique(ArrayList<Bid> bids, double weight){
        this.cliqueBids = bids;
        this.weight = weight;
    }

    public ArrayList<Bid> getCliqueBids() {
        return cliqueBids;
    }

    public void setCliqueBids(ArrayList<Bid> cliqueBids) {
        this.cliqueBids = cliqueBids;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    @Override
    public Clique clone() throws CloneNotSupportedException{
        
        Clique clique = (Clique)super.clone();
        clique.cliqueBids = new ArrayList<>();
        
        for(Bid bid : this.cliqueBids){
            clique.cliqueBids.add(bid);
        }
        
        clique.weight = this.weight;
        
        return clique;
    }
    
}
