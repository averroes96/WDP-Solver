/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

import static Include.Database.getConnection;
import static Include.Database.usernameExist;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

/**
 *
 * @author user
 */
public class User implements Init {
    
    private SimpleIntegerProperty userID ;
    private SimpleStringProperty fullname;
    private SimpleIntegerProperty admin;
    private String username;
    private String password;
    
    private static final SpecialAlert alert = new SpecialAlert();

    public User() {

        this.userID = new SimpleIntegerProperty(0);
        this.fullname = new SimpleStringProperty("");
        this.admin = new SimpleIntegerProperty(0);
        this.password = "";
        this.username = "";
    }

    public int getUserID() {
        return userID.getValue();
    }

    public void setUserID(int userID) {
        this.userID = new SimpleIntegerProperty(userID);
    }

    public String getFullname() {
        return fullname.getValue();
    }

    public void setFullname(String fullname) {
        this.fullname = new SimpleStringProperty(fullname);
    }

    public int getAdmin() {
        return admin.getValue();
    }

    public void setAdmin(int admin) {
        this.admin = new SimpleIntegerProperty(admin);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void delete() throws SQLException{
        
                try (Connection con = getConnection()) {
                    String query = "UPDATE user SET active = 0 WHERE user_id = ?";
                    
                    PreparedStatement ps = con.prepareStatement(query);
                    
                    ps.setInt(1, this.getUserID());
                    
                    ps.executeUpdate();
                }
    }

    public void insert(){
        
            if(usernameExist(this.getUsername()) == 0){
            try {

                try (Connection con = getConnection()) {
                    if(con == null) {
                        alert.show(CONNECTION_ERROR, CONNECTION_ERROR_MESSAGE, Alert.AlertType.ERROR,true);
                    }
                    
                    PreparedStatement ps;
                    
                    
                    //if (selectedFile == null) {
                        ps = con.prepareStatement("INSERT INTO user(fullname, username, password, admin) values(?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
                    //}
                    //else {
                    //    String createImagePath = saveSelectedImage(selectedFile);
                    //    
                    //    ps = con.prepareStatement("INSERT INTO user(fullname, telephone, admin, username, password, image) values(?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
                    //    ps.setString(6, createImagePath);
                    //}
                    
                    
                    
                    ps.setString(1, this.getFullname());
                    ps.setString(2, this.getUsername());
                    ps.setString(3, this.getPassword());
                    ps.setInt(4, this.getAdmin());
                    
                    ps.executeUpdate();

                        con.close();
                    
                }
                
                alert.show(SUCCESS, SIGNUP_SUCCESS_MSG, Alert.AlertType.INFORMATION,false);

            }
            catch (NumberFormatException | SQLException e) {
                alert.show(UNKNOWN_ERROR, e.getMessage(), Alert.AlertType.ERROR,true);
            }
        }
            else{
                alert.show(USERNAME_ERROR, USERNAME_ERROR_MESSAGE, Alert.AlertType.ERROR,false);
            } 
        
    }
    
}
