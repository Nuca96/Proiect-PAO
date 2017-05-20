
package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mpop
 */
public class SqlCommands {
    
    private Connection connection;
    private Statement statement;
    
    
    SqlCommands(){
        
    }
    
    public Statement conexion() {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao", "root", "");
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            statement=connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return statement;
    
    /*
        public ResultSet selectStar() {
            
            return 
        }
    }
    */
    
}
}