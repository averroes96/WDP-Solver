/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

/**
 *
 * @author user
 */
public interface Init {
    
    String DB_NAME = "jdbc:mysql://127.0.0.1/wdp";
    
    String ENCODING = "?useUnicode=yes&characterEncoding=UTF-8";
 
    String DB_NAME_WITH_ENCODING = DB_NAME + ENCODING;
 
    String USER = "root";
 
    String PASSWORD = "";
    
    String FXMLS_PATH = "/Fxmls/" ;
    
    String CONNECTION_ERROR = "Connection error";
    
    String CONNECTION_ERROR_MESSAGE = "Failed to connect to the database !";

    String UNKNOWN_ERROR = "Unknown error";
    
    String LAYOUT_PATH = "/Layout/";
    
    String USER_EXIST = "User doesn't exist";
    
    String USER_EXIST_MESSAGE = "This user does not exist or was already deleted !";
    
    String WRONG_PATH = "Wrong path";
    
    String SUCCESS = "Success!";
    
    String SIGNUP_SUCCESS_MSG = "You were successfully signed up !";
    
    String USERNAME_ERROR = "Username exist!";
    
    String USERNAME_ERROR_MESSAGE = "This username is already taken!";
        
}
