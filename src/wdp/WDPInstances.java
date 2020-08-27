/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wdp;

import Modal.Bid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author user
 */
public class WDPInstances {
    
    private int nbrBids;
    private int nbrObjects;

    public WDPInstances() {
        this.nbrBids = 0;
        this.nbrObjects = 0;
    }

    public int getNbrBids() {
        return nbrBids;
    }

    public void setNbrBids(int nbrBids) {
        this.nbrBids = nbrBids;
    }

    public int getNbrObjects() {
        return nbrObjects;
    }

    public void setNbrObjects(int nbrObjects) {
        this.nbrObjects = nbrObjects;
    }
   
        
        public ArrayList<Bid> getGraph(String filePath) throws FileNotFoundException, IOException{
            
            FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader);
            ArrayList<Bid> bids = new ArrayList<>();

            // read line by line
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                
                String[] arr = line.split(" ");
                Bid bid = new Bid();
                
                if(i == 0){
                    this.nbrBids = Integer.parseInt(arr[0]);
                    this.nbrObjects = Integer.parseInt(arr[1]);
                }
                else{
                    for(int cpt = 0; cpt < arr.length; cpt++){
                        if(cpt == 0)
                            bid.setPrice(Double.parseDouble(arr[cpt]));
                        else
                            bid.getBidObjects().add(Integer.parseInt(arr[cpt]));
                    }
                    
                    if(!bids.isEmpty()){
                    for(Bid currentBids : bids){
                        if(currentBids.inConflictWith(bid)){
                            currentBids.addToConflicts(bid);
                        }
                        else{
                            currentBids.addToNot(bid);
                        }
                    }
                    }
                    bids.add(bid);
                }
                i++;
                    
            }
            
            return bids;

        }   
        public static ObservableList<String> getAllFileNames(final File folder) throws IOException{
            
            ObservableList<String> filesList = FXCollections.observableArrayList();
            
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    getAllFileNames(fileEntry);
                } else {
                    filesList.add(fileEntry.getName());
                }
            }
            
            return filesList ;
            
        }
        
        
    
}
