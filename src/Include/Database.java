/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

import java.io.File;
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
public class Database implements Init {
    
    static SpecialAlert alert = new SpecialAlert();

    static File selectedFile = null;    
    
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
    
    public static int usernameExist(String username){
       
        Connection con = getConnection();
        String query = "SELECT * FROM user WHERE username = ?";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setString(1, username);
            rs = st.executeQuery();
            int count = 0;
            
            while (rs.next()) {
                ++count;
                
            }
            con.close();
            
            return count;

        }
        catch (SQLException e) {
            alert.show(UNKNOWN_ERROR, e.getMessage(), Alert.AlertType.ERROR,true);
            return 0;
        }       
       
   }
    
    public static int getAdminCount(){
        
        try {
            int count;
            try (Connection con = getConnection()) {
                count = 0;
                String query = "SELECT count(*) FROM user WHERE admin = 1";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    
                    count = rs.getInt("count(*)");
                    
                }
            }
            
            return count;
        } catch (SQLException ex) {
            alert.show(UNKNOWN_ERROR, ex.getMessage(), Alert.AlertType.ERROR,true);
            return 0;
        }
        
    }
    
}
