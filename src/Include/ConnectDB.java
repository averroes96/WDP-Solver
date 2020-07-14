/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author user
 */
public class ConnectDB implements Init {
    
    static SpecialAlert alert = new SpecialAlert();    
    
    public static Connection getConnection()
    {
        Connection con;
        try {

            con = DriverManager.getConnection(DB_NAME_WITH_ENCODING, USER, PASSWORD);
            return con;
        }
        catch (SQLException ex) {
            alert.show(CONNECTION_ERROR, CONNECTION_ERROR_MESSAGE, Alert.AlertType.ERROR,true);
            return null;            
        }
    }
    
    public static boolean userExists(String username, String password) throws SQLException{
        
        Connection con = getConnection();
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        PreparedStatement stmt;
        ResultSet rs;
     
        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, username);        
            stmt.setString(2, password);
            
            rs = stmt.executeQuery(); 
            
            return rs.next();
                
  
        } catch (SQLException ex) {
            try {
                Runtime.getRuntime().exec("C:\\xampp\\mysql\\bin\\mysqld.exe"); 
            } catch (IOException e) {
                alert.show(UNKNOWN_ERROR, e.getMessage(), Alert.AlertType.ERROR,true);
                
            }
            return false;
        }
    }
    
}
