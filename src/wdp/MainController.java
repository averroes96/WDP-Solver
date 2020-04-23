/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wdp;

import inc.Bid;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author user
 */
public class MainController implements Initializable {
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            
            ObservableList<String> list = WDPInstances.getAllFileNames(new File("C:\\Users\\user\\Documents\\NetBeansProjects\\WDP\\src\\instance"));
            
            list.forEach((str) -> {
                System.out.println(str);
            });
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            
            ArrayList<Bid> graph = WDPInstances.getGraph("C:\\Users\\user\\Documents\\NetBeansProjects\\WDP\\src\\instance\\in700");
            int i = 0 ;
            
            for(Bid bid : graph){
                System.out.println("Bid n = " + i+1);
                System.out.println("Price = " + bid.getPrice());
                System.out.println("Concerned object :");
                for (int integer : bid.getBidObjects()) {
                    System.out.print(integer + " ");
                }
                i++;
            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }    
    
}
