/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import static Include.Common.setDraggable;
import static Include.Database.userExists;
import Include.Init;
import Include.SpecialAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

public class AuthentificationController implements Initializable, Init {
    @FXML private StackPane rootSign;
    @FXML private VBox paneSignIn, paneSignUp;

    /* Sign In Attribute */
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;

    /* Sign Up Attribute */
    @FXML private JFXTextField fieldFullNameSignUp, fieldEmailSignUp;
    @FXML private JFXPasswordField fieldPasswordSignUp;
    @FXML private JFXButton loginBtn;

    // for animation
    TranslateTransition translateAnimation;

    // For Make Stage Drageable
    private double xOffset = 0;
    private double yOffset = 0;
    
    SpecialAlert alert = new SpecialAlert();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        translateAnimation = new TranslateTransition();
        loginBtn.setOnAction(Action -> {
            try {
                onSignIn(Action);
            } catch (IOException ex) {
                alert.show(WRONG_PATH, ex.getMessage(), Alert.AlertType.ERROR,true);
            }
        });
    }

    /* Change Pane (between Sign in and Sign up */
    @FXML
    private void goToSignIn() {
        translateAnimation.setToX(0);
        translateAnimation.setNode(rootSign);
        translateAnimation.setDuration(Duration.seconds(0.2));
        translateAnimation.play();
        translateAnimation.setOnFinished(e -> {
            paneSignIn.setVisible(true);
            paneSignUp.setVisible(false);
        });
    }
    @FXML
    private void goToSignUp() {
        translateAnimation.setToX(-500);
        translateAnimation.setNode(rootSign);
        translateAnimation.setDuration(Duration.seconds(0.2));
        translateAnimation.play();
        translateAnimation.setOnFinished(e -> {
            paneSignIn.setVisible(false);
            paneSignUp.setVisible(true);
        });
    }

    /* Sign In Action */
    @FXML
    private void onSignIn(ActionEvent event) throws IOException {

        try {
            if(userExists(username.getText().trim(),password.getText().trim())){            
                ((Node)event.getSource()).getScene().getWindow().hide();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLS_PATH + "Dashboard.fxml"));
                HBox root = (HBox)loader.load();
                DashboardController mControl = (DashboardController)loader.getController();
                //mControl.getEmployer(getUser(username.getText(), password.getText()));
                Scene scene = new Scene(root);
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                //stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
                setDraggable(root,stage);
                
            }
            else{
                
                alert.show(USER_EXIST, USER_EXIST_MESSAGE, Alert.AlertType.ERROR,false);
                        
            }
        } catch (SQLException ex) {
            alert.show(CONNECTION_ERROR, CONNECTION_ERROR_MESSAGE, Alert.AlertType.ERROR,true);
            
        }        
        
    }

    /* Sign Up Action */
    @FXML
    private void onSignUp() {

    }

    /* Control Stage action */
    @FXML
    private void onClose() {
        Platform.exit();
    }
    @FXML
    private void onHide() {
        ((Stage) username.getScene().getWindow()).setIconified(true);
    }
}