/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author user
 */
public class MNTSearch {
    
    public ArrayList<Bid> graph = new ArrayList<>();
    public Clique clique = new Clique();
    public ArrayList<Bid> tabouList = new ArrayList<>();
    public ArrayList<Bid> omList = new ArrayList<>();
    private int tSwap ;
    private final int tabu = 7 ;
    
    public boolean isPA(ArrayList<Bid> excluded){
        
        return excluded.stream().noneMatch((exc) -> (!clique.getCliqueBids().stream().noneMatch((bid) -> (exc.inConflictWith(bid)))));
        
    }
    
    public void OMList(){
        
        graph.stream().filter((bid) -> (allButOne(bid) != 0)).forEachOrdered((bid) -> {
            omList.add(bid);
        });
    }
    
    public int allButOne(Bid b){
        
        int cpt = 0, index = 0 ;
        
        for(Bid bid : clique.getCliqueBids()){
            if(bid.inConflictWith(b)){
                index = cpt;
                cpt++;
            }
        }
        
        return cpt == 1 ? index : 0 ;
        
    }
    
    
    public void ADD(Bid b){
        
        if (!clique.getCliqueBids().contains(b)) {

            if (!b.inConflictWith(clique.getCliqueBids())) {
                clique.getCliqueBids().add(b);
                clique.setWeight(clique.getWeight() + b.getPrice());
            }

        }
    }
    
    public void SWAP(Bid b){
        
        int target = allButOne(b);
        if(target != 0){
            
            clique.setWeight(clique.getWeight() - clique.getCliqueBids().get(target).getPrice());
            tabouList.add(clique.getCliqueBids().get(target));
            clique.getCliqueBids().remove(target);
            clique.getCliqueBids().add(b);
            clique.setWeight(clique.getWeight() + b.getPrice());
            
        }
        
    }
    
    public void DROP(Bid b){
        
        if(clique.getCliqueBids().contains(b)){
            tabouList.add(b);
            clique.getCliqueBids().remove(b);
            clique.setWeight(clique.getWeight() - b.getPrice());
        }
        
    }
    
    public void selectRandomSolution(){
        
        Random rand = new Random();
        
        int vertice = rand.nextInt(graph.size());
        
        clique.getCliqueBids().add(graph.get(vertice));
        
        int v = rand.nextInt(graph.size());
        
        clique.getCliqueBids().add(graph.get(v));
        
        graph.forEach((b) -> {
            ADD(b);
        });
        
    }
    
}
