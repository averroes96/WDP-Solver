/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class MNTSearch {
    
    HashMap<Bid, Integer> graph = new HashMap<>();
    Clique clique = new Clique();
    
    
    public boolean isPA(ArrayList<Bid> excluded){
        
        return excluded.stream().noneMatch((exc) -> (!clique.getCliqueBids().stream().noneMatch((bid) -> (exc.inConflictWith(bid)))));
        
    }
    
    public int AllButOne(Bid b){
        
        int cpt = 0, index = 0 ;
        
        for(Bid bid : clique.getCliqueBids()){
            if(bid.inConflictWith(b)){
                index = cpt;
                cpt++;
            }
        }
        
        
        return cpt == 1 ? index : 0 ;
        
    }
    
    public boolean isOM(ArrayList<Bid> excluded){
        
        return excluded.stream().noneMatch((exc) -> (AllButOne(exc) != 0));
        
    }
    
    public void ADD(ArrayList<Bid> excuded){
        
        if(!excuded.isEmpty()){
            if(isPA(excuded)){
                excuded.forEach((bid) -> {
                    clique.getCliqueBids().add(bid);
                    clique.setWeight(clique.getWeight() + bid.getPrice()); 
                });
            }
        }
    }
    
    public void SWAP(ArrayList<Bid> excuded){
        
        if(!excuded.isEmpty() && isOM(excuded)){
            
            
            
        }
        
    }
    
    public void DROP(Bid b){
        
        if(clique.getCliqueBids().contains(b)){
            clique.getCliqueBids().remove(b);
            clique.setWeight(clique.getWeight() - b.getPrice());
        }
        
    }
    
}
