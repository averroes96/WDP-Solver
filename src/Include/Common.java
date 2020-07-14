/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Common {

    private static double xOffset = 0;
    private static double yOffset = 0;
    
    public static void setDraggable(Parent root, Stage stage){
        
                        root.setOnMousePressed((MouseEvent event) -> {
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                });
                        root.setOnMouseDragged((MouseEvent event) -> {
                            stage.setX(event.getScreenX() - xOffset);
                            stage.setY(event.getScreenY() - yOffset);
                });        
        
    }    
    
}
