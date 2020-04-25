package inc;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Clique implements Cloneable {
    
    private ArrayList<Bid> cliqueBids ;
    private double weight ;

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    @Override
    public Clique clone() throws CloneNotSupportedException{
        return (Clique) super.clone();
    }
    
}
