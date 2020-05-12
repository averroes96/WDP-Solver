/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc;

import java.util.ArrayList;
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
    public ArrayList<Bid> tabouList = new ArrayList<>();
    public ArrayList<Bid> omList = new ArrayList<>();
    public ArrayList<Bid> paList = new ArrayList<>();
    private int tSwap, currIter, noImprovement ;
    private final int tabu = 7 ;
    private Random myrand = new Random();
    
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
    
    public void OMList(Clique clique){
        
        omList.clear();
        
        graph.stream().filter((bid) -> (allButOne(bid, clique) != -1 && !clique.getCliqueBids().contains(bid) && !tabouList.contains(bid))).forEachOrdered((bid) -> {
            omList.add(bid);
        });
    }
    
    public void PAList(Clique clique){
        
        paList.clear();
        
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
        
        return cpt == 1 ? index : -1 ;
        
    }
    
    
    public void ADD(Bid b, Clique clique){
        
        if (!clique.getCliqueBids().contains(b)) {
                
                clique.getCliqueBids().add(b);
                clique.setWeight(clique.getWeight() + b.getPrice());

        }
    }
    
    public void SWAP(Bid b, Clique clique){
        
        int target = allButOne(b, clique);
        if(target != -1){
            
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
        thisClique.setWeight(graph.get(vertice).getPrice());
        
        graph.stream().filter((b) -> (!b.inConflictWith(thisClique.getCliqueBids()) && !thisClique.getCliqueBids().contains(b))).map((b) -> {
            thisClique.setWeight(thisClique.getWeight() + b.getPrice());
            return b;
        }).forEachOrdered((b) -> {
            thisClique.getCliqueBids().add(b);
        });
        
        return thisClique ;
        
    }
    
    public Clique explorePAList(Clique clique){
        
        
        if(!paList.isEmpty()){
        
            int index = myrand.nextInt(paList.size()) ;

                ADD(paList.get(index), clique);
               // System.out.println("ADD");
        }
        else{
            DROP(clique.getCliqueBids().get(myrand.nextInt(clique.getCliqueBids().size())), clique);
            //System.out.println("ADD DROP");
        }
        
        return clique ;
        
    }
    
    public Clique exploreOMList(Clique clique){
        
        if(!omList.isEmpty()){
        
            int index = myrand.nextInt(omList.size()) ;

                SWAP(omList.get(index), clique);
               // System.out.println("SWAP");
        
        }
        else{
                DROP(clique.getCliqueBids().get(myrand.nextInt(clique.getCliqueBids().size())), clique);
                //System.out.println("SWAP DROP");
        }
        
        return clique ;
        
    }    
    
    public Clique ExploreNeighborhood(Clique clique){
                
        Clique N1 = clique;
        Clique N2 = clique;
        
        N1 = explorePAList(N1);
        N2 = exploreOMList(N2);
        
        if(N1.getWeight() > N2.getWeight()){
            //System.out.println("ADD");
            return N1;
        }
        else{
            //System.out.println("SWAP");
            return N2;
        }
        
    }
    
    public void updateTabouList(){
        
        ArrayList<Bid> temp = new ArrayList<>();
        
        tabouList.forEach((bid) -> {
            bid.setTt(bid.getTt()-1);
            if(bid.getTt() != 0)
                temp.add(bid);
        });
        
        tabouList = temp;
        
    }
    
    public void MNTAlgorithm(){
        
        currIter = 0 ;
        
        while(currIter < MAX_ITER){
            
            Clique global = selectRandomSolution();
            
            noImprovement = 0 ;
            
            Clique local = null ;
            try {
                local = global.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while(noImprovement < DEPTH_SEARCH){
                
               // System.out.println("OM list size == " + omList.size());
               // System.out.println("PA list size == " + paList.size());
               // System.out.println("TABOU list size == " + tabouList.size());
                
                PAList(global);
                OMList(global);
                global = ExploreNeighborhood(global);
                
                noImprovement++ ;
                currIter++ ;
                
                updateTabouList();
                
                //System.out.println("Imp = " + noImprovement);
                //System.out.println("Global = " + global.getWeight());
                //System.out.println("Local = " + local.getWeight());
                
                if(global.getWeight() > local.getWeight()){
                    noImprovement = 0 ;
                    try {
                        local = global.clone();
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
            
            if(local.getWeight() > starClique.getWeight())
                try {
                    starClique = local.clone() ;
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    
    
}
