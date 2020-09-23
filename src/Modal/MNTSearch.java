/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class MNTSearch implements Init{
    
    public ArrayList<Bid> graph = new ArrayList<>();
    public Clique starClique = new Clique();
    public Clique global = new Clique();
    public Clique locale = new Clique();
    public ArrayList<Bid> tabouList = new ArrayList<>();
    public ArrayList<Bid> omList = new ArrayList<>();
    public ArrayList<Bid> paList = new ArrayList<>();
    public int tSwap, currIter, noImprovement ;
    private final Random myrand = new Random();
    
    public ArrayList<Bid> getGraph() {
        return graph;
    }

    public void setGraph(ArrayList<Bid> graph) {
        this.graph = graph;
    }    
    
    public int getSwapRario(){
        
        Random rand = new Random();
        
        return rand.nextInt(omList.size()) + 7 ;   
    }
    
    public void OMList(){
        
        omList.clear();

        graph.stream().filter((bid) -> (allButOne(bid) != -1 && !locale.getCliqueBids().contains(bid) && !tabouList.contains(bid))).forEachOrdered((bid) -> {
            omList.add(bid);
        });
    }
    
    public int getNotConflictSize(){
        
        int count = 0, i = 1;
        count = this.starClique.getCliqueBids().stream().map((starBid) -> {
            System.out.println("Bid " + i + " = " + starBid.getNotConflict().size());
            return starBid;
        }).map((starBid) -> starBid.getNotConflict().size()).reduce(count, Integer::sum);
        
        return count;
    }
    
    public void PAList(){
        
        paList.clear();
        
        graph.stream().filter((bid) -> (!bid.inConflictWith(locale.getCliqueBids())) && !locale.getCliqueBids().contains(bid) && !tabouList.contains(bid)).forEachOrdered((bid) -> {
            paList.add(bid);
        });

        
    }
    
    public int allButOne(Bid b){
        
        int cpt = 0, index = 0, i = 0 ;
        
        for(Bid bid : locale.getCliqueBids()){
            if(bid.inConflictWith(b)){
                index = i;
                cpt++;
                if(cpt > 1)
                    return -1;
            }
            i++;
        }
        
        return index;
        
    }
    
    
    public void ADD(Bid b, Clique clique){
                
                clique.getCliqueBids().add(b);
                clique.setWeight(clique.getWeight() + b.getPrice());
                
    }
    
    public void SWAP(Bid b, Clique clique){
        
        int target = allButOne(b);
        
        if(target != -1){
            
            clique.setWeight(clique.getWeight() - clique.getCliqueBids().get(target).getPrice());
            clique.getCliqueBids().get(target).setTt(getSwapRario());
            tabouList.add(clique.getCliqueBids().get(target));
            clique.getCliqueBids().remove(target);
            clique.getCliqueBids().add(b);
            clique.setWeight(clique.getWeight() + b.getPrice());
        }
    }
    
    public void DROP(Bid b, Clique clique){

            tabouList.add(b);
            b.setTt(7);
            clique.getCliqueBids().remove(b);
            clique.setWeight(clique.getWeight() - b.getPrice());
        
    }
    
    public Clique selectRandomSolution(){
        
        clearTabouList();
        
        int vertice = myrand.nextInt(graph.size()) ;
        
        Clique thisClique = new Clique() ;
        
        thisClique.getCliqueBids().add(graph.get(vertice));
        thisClique.setWeight(graph.get(vertice).getPrice());
        
        
        graph.stream().filter((b) -> (!b.inConflictWith(thisClique.getCliqueBids()) && !thisClique.getCliqueBids().contains(b))).map((b) -> {
            thisClique.setWeight(thisClique.getWeight() + b.getPrice());
            return b;
        }).forEachOrdered((b) -> {
            thisClique.getCliqueBids().add(b);
        });     
        
        return thisClique ;
        
    }
    
    public Clique explorePAList(Clique clique) throws CloneNotSupportedException{

        ADD(Collections.max(paList), clique);
        
        return clique ;
        
    }
    
    public Clique exploreOMList(Clique clique) throws CloneNotSupportedException{

        SWAP(Collections.max(omList), clique);     
        
        return clique ;
        
    }    
    
    public void ExploreNeighborhood(){
        
        try {
            Clique pa = locale.clone();
            Clique om = locale.clone();
            Clique drop = locale.clone();
            
            if(!paList.isEmpty() && !omList.isEmpty()){
                
                pa = explorePAList(pa);
                om = exploreOMList(om);
                
                if(pa.getWeight() >= om.getWeight()){
                    locale = pa;
                }
                else{
                    locale = om;
                }
            }
            else if(!paList.isEmpty()){
                pa = explorePAList(pa);
                locale = pa;
            }
            else if(!omList.isEmpty()){
                om = exploreOMList(om);
                DROP(drop.getCliqueBids().get(myrand.nextInt(drop.getCliqueBids().size())), drop);
                
                if(om.getWeight() >= drop.getWeight()){
                    locale = om;
                }
                else{
                    locale = drop;
                }
            }            
            else{
                DROP(drop.getCliqueBids().get(myrand.nextInt(drop.getCliqueBids().size())), drop);
                //System.out.println("DROP");
                locale = drop;                
            }

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void clearTabouList(){
        
        if(!tabouList.isEmpty()){
                
            tabouList.forEach((bid) -> {
                bid.setTt(0);
            });
        
        }
        
        tabouList.clear();
        
    }
    
    
    public void updateTabouList(){
        
        ArrayList<Bid> tempo = new ArrayList<>();
        
        tabouList.forEach((bid) -> {
            bid.setTt(bid.getTt()-1);
            if(bid.getTt() != 0)
                tempo.add(bid);
        });
        
        tabouList = tempo;
        
    }
    
    public void MNTAlgorithm(){
        
        currIter = 0 ;
        
        starClique = new Clique();
        global = new Clique();
        
        while(currIter < 4000){
            
            locale = selectRandomSolution();
                        
            noImprovement = 0 ;
            
            try {
                global = locale.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while(noImprovement < 100){

                try {                    
                    
                    PAList();
                    OMList();

                    ExploreNeighborhood();
                    
                    noImprovement++ ;
                    currIter++ ;
                    
                    updateTabouList();
                    
                    if(locale.getWeight() > global.getWeight()){
                        noImprovement = 0 ;
                        global = locale.clone();
                    }
                                        
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if(global.getWeight() > starClique.getWeight())
                try {
                    starClique = global.clone() ;
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Current max revenue = " + starClique.getWeight());
            
        }
        
    }
 
}
