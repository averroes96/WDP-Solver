/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wdp;

import Include.SpecialAlert;
import Modal.Bid;
import Modal.Init;
import Modal.MNTSearch;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author user
 */
public class MainController implements Initializable, Init {
    
    @FXML private JFXComboBox<String> instanceBox ;
    @FXML private JFXButton findBtn;
    @FXML private JFXTextArea resultArea ;
    @FXML private JFXToggleButton bidPoll;
    @FXML private StackPane stackPane,stackPane1;
    @FXML private JFXDialog dialog;
    
    SpecialAlert alert = new SpecialAlert();
    WDPInstances instances = new WDPInstances();
    MNTSearch search = new MNTSearch();
    ObservableList instNames = FXCollections.observableArrayList();
    Instant t1,t2;   
    long elapsedTime;
    
    
    @FXML
    public void loadDialog(JFXDialogLayout layout, boolean button){
        
        stackPane.setVisible(true);
        findBtn.setDefaultButton(false);
        JFXButton btn = new JFXButton("Okay");
        btn.setDefaultButton(true);
        btn.setOnAction(Action -> {
            dialog.close();
            stackPane.setVisible(false);
            btn.setDefaultButton(false);
            findBtn.setDefaultButton(true);
        });
        if(button){
            layout.setActions(btn);
        }
        dialog = new JFXDialog(stackPane, layout , JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false);
        dialog.show();
    }
    
    public void closeDialog(JFXDialog dialog, StackPane pane){
        dialog.close();
        pane.setVisible(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        stackPane.setVisible(false);
        stackPane1.setVisible(false);

        try {
            
            instNames = WDPInstances.getAllFileNames(new File(INSTANCE_FOLDER));                
            
            instanceBox.setItems(instNames);
            instanceBox.getSelectionModel().selectFirst();
            search.setGraph(instances.getGraph(INSTANCE_FOLDER + instanceBox.getSelectionModel().getSelectedItem()));
            resultArea.setText("Matrix dim = " + instances.getNbrBids() + " x " + instances.getNbrObjects());
            
            instanceBox.setOnAction((ActionEvent Action) -> {
                
                try {
                    search.setGraph(instances.getGraph(INSTANCE_FOLDER + instanceBox.getSelectionModel().getSelectedItem()));
                    search.starClique.getCliqueBids().clear();
                    search.starClique.setWeight(0);
                    
                    resultArea.setText("Matrix dim = " + instances.getNbrBids() + " x " + instances.getNbrObjects());
                    
                    resultArea.appendText("\n");
                    
                    if(bidPoll.isSelected()){
                        
                        Thread th = new Thread(() -> {
                            
                            if (Platform.isFxApplicationThread()) {
                                int i = 0 ;
                                
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
                            } else {
                                Platform.runLater(() -> {
                                int i = 0 ;
                                
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
                                });
                            }                            
                        });
                        th.start();
                    }
                } catch (IOException ex) {
                    alert.show(INOUT_EXCEPTION, ex.getMessage(), Alert.AlertType.ERROR, true);
                }
                
            });
            
            findBtn.setOnAction(Action -> {
                
                resultArea.clear();
                
                
                JFXDialogLayout layout = new JFXDialogLayout();
                layout.setHeading(new Text("Searching..."));
                layout.setBody(new Text("Searching for solution"));
                            
                loadDialog(layout, false);
                
                Thread th = new Thread(() -> {
                    t1 = Instant.now();
                    search.MNTAlgorithm();
                    t2 = Instant.now();

                    elapsedTime = (Duration.between(t1, t2).toMillis());

                    String solution = "";

                    solution += "Wining bids are : \n";

                    int cpt = 1;

                    for(Bid bid : search.starClique.getCliqueBids()){
                        solution += "\nBid num = " + cpt + "\n" ;
                        solution += bid.toString();
                        cpt++;
                    }

                    solution += "\n\nTotal gain = " + search.starClique.getWeight();
                    
                    final String finalSolution = solution;
                                      
                
                Platform.runLater(() -> {
                    resultArea.appendText(finalSolution);
                    
                    System.out.println("> Elapsed time = " + elapsedTime + " seconds" + "\n> Total gain = " + search.starClique.getWeight());
                    
                    JFXDialogLayout layout1 = new JFXDialogLayout();
                    layout1.setHeading(new Text("Optimized solution was found"));
                    layout1.setBody(new Text("> Elapsed time = " + elapsedTime + " seconds" + "\n> Total gain = " + search.starClique.getWeight()));
                    loadDialog(layout1, true);
                    ArrayList values = new ArrayList<>();
                    search.starClique.getCliqueBids().forEach((bid) -> {
                        bid.getBidObjects().stream().map((integer) -> {
                            if(values.contains(integer)){
                                System.out.println(integer + " is duplicated");
                            }
                            return integer;
                        }).forEachOrdered((integer) -> {
                            values.add(integer);
                        });
                        
                    });//To change body of generated methods, choose Tools | Templates.
                    });

                });
                th.setDaemon(true);
                th.start();
                                          
            });
            
        } catch (IOException ex) {
            alert.show(INOUT_EXCEPTION, ex.getMessage(), Alert.AlertType.ERROR, true);
        }
       
    }    
    
}
