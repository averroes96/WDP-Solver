/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modal;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author user
 */
public class Bid {
    
    private ArrayList<Integer> bidObjects ;
    private double price ;
    private ArrayList<Bid> conflict = new ArrayList<>();
    private ArrayList<Bid> notConflict = new ArrayList<>();
    private int tt ;

    public Bid(int pricce) {
        
        bidObjects = new ArrayList<>() ;
        this.price = pricce ;
        this.conflict = new ArrayList<>();
        this.notConflict = new ArrayList<>();
        
    }
    

    public Bid() {
        
        bidObjects = new ArrayList<>() ;
        notConflict = new ArrayList<>();
        this.price = 0 ;
        this.tt = 0;
        
    }
    
    public boolean inConflictWith(Bid b){
        
	Iterator<Integer> objects = this.bidObjects.iterator();
        
	while(objects.hasNext()){
            if(b.getBidObjects().contains(objects.next())){
                    return true;
		}
	}
        
        return false;
        
    }
        
    
    public boolean inConflictWith(ArrayList<Bid> bids){
        
        for(Bid bid : bids){
	Iterator<Integer> objects = this.bidObjects.iterator();
        
	while(objects.hasNext()){
            if(bid.getBidObjects().contains(objects.next())){
                    return true;
		}
	}
        
        }
        
        return false;
        
    }    
    
    public void addToConflicts(Bid b){
        
        if(this.inConflictWith(b)){
            this.conflict.add(b);
            b.conflict.add(this);
        }
        
    }
    
    public void addToNot(Bid b){
        
        if(!this.inConflictWith(b)){
            this.notConflict.add(b);
            b.notConflict.add(this);
        }
       
    }
    
	public boolean equals(Bid b){
		if(this.price!= b.getPrice() ||this.bidObjects.size()!=b.getBidObjects().size()){
			return false;
		}
		for(int i=0;i<this.bidObjects.size();i++){
			if(this.bidObjects.get(i) != b.getBidObjects().get(i)){
				return false;
			}
		}
		return true;
	}    

    @Override
    public String toString() {
        
        String str = "";
        
        if(!bidObjects.isEmpty()){
            str += "Selected objects :\n [ ";
            str = bidObjects.stream().map((integer) -> integer + " ").reduce(str, String::concat);
            str += "]\n" ;
        }
        
        str += "Price : " + price + "\n" ;
        
        return str ;
        
    }

    public ArrayList<Integer> getBidObjects() {
        return bidObjects;
    }

    public void setBidObjects(ArrayList<Integer> bidObjects) {
        this.bidObjects = bidObjects;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public static boolean validPrice(int price){
        
        return price >= 0 ; 
        
    }

    public ArrayList<Bid> getConflict() {
        return conflict;
    }

    public void setConflict(ArrayList<Bid> conflict) {
        this.conflict = conflict;
    }

    public int getTt() {
        return tt;
    }

    public void setTt(int tt) {
        this.tt = tt;
    }

    public ArrayList<Bid> getNotConflict() {
        return notConflict;
    }

    public void setNotConflict(ArrayList<Bid> notConflict) {
        this.notConflict = notConflict;
    }
    
    public boolean isWithOneConflict(ArrayList<Bid> bids){
        
        int cpt = 0;
        
        for(Bid bid : bids){
            if(this.inConflictWith(bid)){
                cpt++;
                if(cpt > 1)
                    return false;
            }
        }
        
        return true;
    }
    
    
}
