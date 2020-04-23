/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wdp;

import inc.Bid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author user
 */
public class WDPInstances {
    
    private int nbrBids;
    private int nbrObjects;

    public WDPInstances(int nbrBids, int nbrObjects) {
        this.nbrBids = nbrBids;
        this.nbrObjects = nbrObjects;
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
   
        
        public static ArrayList<Bid> getGraph(String filePath) throws FileNotFoundException, IOException{
            
            // read in the data
            ArrayList<Bid> graph  = new ArrayList<>();
            Scanner input = new Scanner(new File(filePath));
            while(input.hasNextLine())
            {
                Scanner colReader = new Scanner(input.nextLine());
                Bid bid = new Bid();
                int i = 0 ;
                while(colReader.hasNext())
                {
                    if(i == 0)
                        bid.setPrice(colReader.nextDouble());
                    else
                        bid.getBidObjects().add(colReader.nextInt());
                    i++;
                }
                
                graph.add(bid);
            }
            
            return graph ;
            
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
