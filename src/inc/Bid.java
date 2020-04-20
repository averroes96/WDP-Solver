/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author user
 */
public class Bid {
    
    private ArrayList<Integer> bidObjects ;
    private int price ;
    private ArrayList<Bid> conflict;

    public Bid(int pricce) {
        
        bidObjects = new ArrayList<>() ;
        this.price = pricce ;
        
    }
    

    public Bid() {
        
        bidObjects = new ArrayList<>() ;
        this.price = 0 ;
        
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
    
    public void addToConflicts(Bid b){
        
        if(this.inConflictWith(b)){
            this.conflict.add(b);
            b.conflict.add(this);
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
            str += "Selected objects :\n"; 
        }
        
        str += "\nPrice : " + price ;
        
        return str ;
        
    }

    public ArrayList<Integer> getBidObjects() {
        return bidObjects;
    }

    public void setBidObjects(ArrayList<Integer> bidObjects) {
        this.bidObjects = bidObjects;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    public static boolean validPrice(int price){
        
        return price >= 0 ; 
        
    }
    
    
    
}
