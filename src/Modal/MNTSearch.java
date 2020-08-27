/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modal;

import java.time.Duration;
import java.time.Instant;
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
    public Clique global = new Clique();
    public Clique locale = new Clique();
    public Clique temp = new Clique();
    public Clique tempTwo = new Clique();    
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
    
    public void OMList(){
        
        omList.clear();
        
        /*
        clique.getCliqueBids().forEach((bidGlobal) -> {
            bidGlobal.getNotConflict().stream().filter((bid) -> (!clique.getCliqueBids().contains(bid) && !tabouList.contains(bid) && bid.isWithOneConflict(clique.getCliqueBids()))).forEachOrdered((bid) -> {
                omList.add(bid);
            });
        });*/        
        
        
        graph.stream().filter((bid) -> (allButOne(bid) != -1 && !locale.getCliqueBids().contains(bid) && !tabouList.contains(bid))).forEachOrdered((bid) -> {
            omList.add(bid);
        });
    }
    
    public void PAList(){
        
        paList.clear();
        
        /*
        locale.getCliqueBids().forEach((bidGlobal) -> {
            bidGlobal.getNotConflict().stream().filter((bid) -> (!locale.getCliqueBids().contains(bid) && !tabouList.contains(bid) && !bid.inConflictWith(locale.getCliqueBids()))).forEachOrdered((bid) -> {
                paList.add(bid);
            });
        });*/
        
        
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
             
        
        if(!paList.isEmpty()){
        
            int index = myrand.nextInt(paList.size()) ;

                ADD(paList.get(index), clique);
                
        }
        
        return clique ;
        
    }
    
    public Clique exploreOMList(Clique clique) throws CloneNotSupportedException{
       
        
        if(!omList.isEmpty()){
        
            int index = myrand.nextInt(omList.size()) ;

                SWAP(omList.get(index), clique);
                
        }     
        
        return clique ;
        
    }    
    
    public void ExploreNeighborhood(){
        
        
        if(!paList.isEmpty()){
        
            int index = myrand.nextInt(paList.size()) ;

            ADD(paList.get(index), locale);
                
        }else if(!omList.isEmpty()){
            int index = myrand.nextInt(omList.size()) ;

                SWAP(omList.get(index), locale);            
        }
        else{
            DROP(locale.getCliqueBids().get(myrand.nextInt(locale.getCliqueBids().size())), locale);
        }
        
        /*
        
        try {
            ArrayList<Bid> initBids = new ArrayList();
            locale.getCliqueBids().forEach((bid) -> {
                initBids.add(bid);
            });
            double initWeight = locale.getWeight();
            
            Clique paClique = new Clique(initBids, initWeight);*/
            //Clique omClique = new Clique(initBids, initWeight);
            //Clique dropClique = new Clique(initBids, initWeight);
            
            //return explorePAList(paClique);
            
            /*
            exploreOMList(omClique);
            DROP(dropClique.getCliqueBids().get(myrand.nextInt(dropClique.getCliqueBids().size())), dropClique);
            
            
            if(paClique.getWeight() > omClique.getWeight()){
                if(paClique.getWeight() > dropClique.getWeight())
                    return paClique;
                else
                    return dropClique;
            }
            else{
                if(omClique.getWeight() > dropClique.getWeight())
                    return omClique;
                else
                    return dropClique;
            }*/
                    
            /*
            //explorePAList();
            //exploreOMList();
            
            System.out.println("PAList weight = " + locale.getWeight());
            System.out.println("OMList weight = " + temp.getWeight());
            
            if(locale.getWeight() > temp.getWeight()){
            //System.out.println("ADD");
            return locale;
            }
            else{
            //System.out.println("SWAP");
            return temp;
            }

            
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }*/
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
        
        while(currIter < 4000){
            
            Instant t = Instant.now();
            locale = selectRandomSolution();
            Instant t1 = Instant.now();
            
            System.out.println("Time to select a solution = " + Duration.between(t, t1).toMillis());
            
            noImprovement = 0 ;
            
            try {
                global = locale.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MNTSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while(noImprovement < 50){

                try {                    
                    
                    t = Instant.now();
                    PAList();
                    t1 = Instant.now();
                    System.out.println("Time to set PA = " + Duration.between(t, t1).toMillis());
                    
                    t = Instant.now();
                    OMList();
                    t1 = Instant.now();
                    System.out.println("Time to set OM = " + Duration.between(t, t1).toMillis());
                    
                    
                    t = Instant.now();
                    ExploreNeighborhood();
                    //System.out.println("locale = " + locale.getWeight());
                    t1 = Instant.now();
                    System.out.println("Time to explore neighborhood = " + Duration.between(t, t1).toMillis());
                    
                    //System.out.println("PAList weight = " + temp.getWeight());
                    //System.out.println("OMList weight = " + tempTwo.getWeight());
                    
                    noImprovement++ ;
                    currIter++ ;
                    
                    t = Instant.now();
                    updateTabouList();
                    t1 = Instant.now();
                    System.out.println("Time to update Tabou = " + Duration.between(t, t1).toMillis());
                    
                    
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
            
            System.out.println("Num Iter = " + currIter);
            System.out.println("Weight = " + starClique.getWeight());
            
        }
        
    }
    
    
    
}
