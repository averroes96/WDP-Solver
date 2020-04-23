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
public class MNTSearch implements Init{
    
    public ArrayList<Bid> graph = new ArrayList<>();
    public Clique starClique = new Clique();
    public ArrayList<Bid> tabouList = new ArrayList<>();
    public ArrayList<Bid> omList = new ArrayList<>();
    public ArrayList<Bid> paList = new ArrayList<>();
    private int tSwap, currIter, noImprovement ;
    private final int tabu = 7 ;
    private Random myrand = new Random();
    
    public int getSwapRario(){
        
        Random rand = new Random();
        
        return rand.nextInt(omList.size()) + 7 ;
        
    }
    
    public void OMList(Clique clique){
        
        graph.stream().filter((bid) -> (allButOne(bid, clique) != 0 && !tabouList.contains(bid))).forEachOrdered((bid) -> {
            omList.add(bid);
        });
    }
    
    public void PAList(Clique clique){
        
        graph.stream().filter((bid) -> (!bid.inConflictWith(clique.getCliqueBids())) && !clique.getCliqueBids().contains(bid) && !tabouList.contains(bid)).forEachOrdered((bid) -> {
            paList.add(bid);
        });
        
    }
    
    public int allButOne(Bid b, Clique clique){
        
        int cpt = 0, index = 0 ;
        
        for(Bid bid : clique.getCliqueBids()){
            if(bid.inConflictWith(b)){
                index = cpt;
                cpt++;
            }
        }
        
        return cpt == 1 ? index : 0 ;
        
    }
    
    
    public void ADD(Bid b, Clique clique){
        
        if (!clique.getCliqueBids().contains(b)) {

            if (!b.inConflictWith(clique.getCliqueBids())) {
                clique.getCliqueBids().add(b);
                clique.setWeight(clique.getWeight() + b.getPrice());
            }

        }
    }
    
    public void SWAP(Bid b, Clique clique){
        
        int target = allButOne(b, clique);
        if(target != 0){
            
            clique.setWeight(clique.getWeight() - clique.getCliqueBids().get(target).getPrice());
            tabouList.add(clique.getCliqueBids().get(target));
            clique.getCliqueBids().get(target).setTt(getSwapRario());
            clique.getCliqueBids().remove(target);
            clique.getCliqueBids().add(b);
            clique.setWeight(clique.getWeight() + b.getPrice());
            
        }
        
    }
    
    public void DROP(Bid b, Clique clique){
        
        if(clique.getCliqueBids().contains(b)){
            tabouList.add(b);
            b.setTt(7);
            clique.getCliqueBids().remove(b);
            clique.setWeight(clique.getWeight() - b.getPrice());
        }
        
    }
    
    public Clique selectRandomSolution(){
        
        int vertice = myrand.nextInt(graph.size()) ;
        
        Clique thisClique = new Clique() ;
        
        thisClique.getCliqueBids().add(graph.get(vertice));
        
        return thisClique ;
        
    }
    
    public void explorePAList(Clique clique){
        
        if(!paList.isEmpty()){
        
            int index = -1 ;

            while(paList.get(index + 1).getTt() != 0){
                index++;
            }
            
            if(index != -1)
                ADD(paList.get(index + 1), clique);
            else
                DROP(clique.getCliqueBids().get(myrand.nextInt(clique.getCliqueBids().size())), clique);
        
        }
        else
            DROP(clique.getCliqueBids().get(myrand.nextInt(clique.getCliqueBids().size())), clique);
        
    }
    
    public void exploreOMList(Clique clique){
        
        if(!omList.isEmpty()){
        
            int index = -1 ;

            while(omList.get(index + 1).getTt() != 0){
                index++;
            }
            
            if(index != -1)
                SWAP(omList.get(index + 1), clique);
            else
                DROP(clique.getCliqueBids().get(myrand.nextInt(clique.getCliqueBids().size())), clique);
        
        }
        else
            DROP(clique.getCliqueBids().get(myrand.nextInt(clique.getCliqueBids().size())), clique);
        
    }    
    
    public void ExploreNeighborhood(Clique clique){
                
        int randVal = myrand.nextInt(2);
        
        switch(randVal){
            
            case 0 :
                explorePAList(clique);
                exploreOMList(clique);
                break;
            case 1 :
                exploreOMList(clique);
                explorePAList(clique);
  
        }
        
    }
    
    public void updateTabouList(){
        
        tabouList.forEach((bid) -> {
            bid.setTt(bid.getTt()-1);
        });
    }
    
    public void MNTAlgorithm(){
        
        currIter = 0 ;
        
        while(currIter < MAX_ITER){
            
            Clique global = selectRandomSolution();
            
            noImprovement = 0 ;
            
            Clique local = global ;
            
            while(noImprovement < DEPTH_SEARCH){

                ExploreNeighborhood(global);
                
                noImprovement++ ;
                currIter++ ;
                
                updateTabouList();
                
                if(global.getWeight() > local.getWeight()){
                    noImprovement = 0 ;
                    local = global;
                }
                
            }
            
            if(local.getWeight() > starClique.getWeight())
                starClique = local ;
            
        }
        
    }
    
}
