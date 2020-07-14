/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wdp;

import Include.SpecialAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import Modal.Bid;
import Modal.Init;
import Modal.MNTSearch;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 *
 * @author user
 */
public class MainController implements Initializable, Init {
    
    @FXML private JFXComboBox<String> instanceBox ;
    @FXML private JFXButton findBtn;
    @FXML private JFXTextArea resultArea ;
    @FXML private JFXToggleButton bidPoll;
    
    SpecialAlert alert = new SpecialAlert();
    Console console = new Console(resultArea);
    WDPInstances instances = new WDPInstances();
    MNTSearch search = new MNTSearch();
    ObservableList instNames = FXCollections.observableArrayList();
    Instant t1,t2;
    PrintStream ps = new PrintStream(console, true);   
    long elapsedTime;
    
    
    public static class Console extends OutputStream {

        private JFXTextArea output;

        public Console(JFXTextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }    
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        System.setOut(ps);
        System.setErr(ps); 
        
        try {
            
            try {
                instNames = WDPInstances.getAllFileNames(new File("C:\\Users\\user\\Documents\\NetBeansProjects\\WDP\\src\\instance"));                
            } catch (IOException ex) {
                alert.show(INOUT_EXCEPTION, ex.getMessage(), Alert.AlertType.ERROR, true);
            }
            
            instanceBox.setItems(instNames);
            instanceBox.getSelectionModel().selectFirst();
            search.setGraph(instances.getGraph(INSTANCE_FOLDER + instanceBox.getSelectionModel().getSelectedItem()));
            
            instanceBox.setOnAction((ActionEvent Action) -> {
                
                try {
                    search.setGraph(instances.getGraph(INSTANCE_FOLDER + instanceBox.getSelectionModel().getSelectedItem()));
                    search.starClique.getCliqueBids().clear();
                    search.starClique.setWeight(0);
                    int i = 0 ;
                    
                    resultArea.appendText("Matrix dim = " + instances.getNbrBids() + " x " + instances.getNbrObjects());
                    
                    resultArea.appendText("\n");
                    
                    if(bidPoll.isSelected()){
                        
                        for(Bid bid : search.getGraph()){
                            resultArea.appendText("Bid n = " + (i+1));
                            resultArea.appendText("Price = " + bid.getPrice());
                            resultArea.appendText("Concerned object :");
                            bid.getBidObjects().forEach((integer) -> {
                                resultArea.appendText(integer + " ");
                            });
                            i++;
                            resultArea.appendText("\n");
                        }
                    }
                } catch (IOException ex) {
                    alert.show(INOUT_EXCEPTION, ex.getMessage(), Alert.AlertType.ERROR, true);
                }
                
            });
            
            findBtn.setOnAction(Action -> {
                
                resultArea.clear();
                
                t1 = Instant.now();
                search.MNTAlgorithm();
                t2 = Instant.now();
                
                elapsedTime = Duration.between(t1, t2).toMillis();
                
                String solution = "";
                
                solution += "Wining bids are : \n";
                
                int cpt = 1;
                
                for(Bid bid : search.starClique.getCliqueBids()){
                    solution += "\nBid num = " + cpt + "\n" ;
                    solution += bid.toString();
                    cpt++;
                }
                
                solution += "\n\nTotal gain = " + search.starClique.getWeight();
                
                resultArea.appendText(solution);
                
                alert.show("Solution was found", "> Elapsed time = " + elapsedTime + " ms" + "\n> Total gain = " + search.starClique.getWeight(), Alert.AlertType.ERROR, false);
                
                
            });        
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }    
    
}
