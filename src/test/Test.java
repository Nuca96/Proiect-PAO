
package test;


import java.io.InputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Irina
 */
public class Test {
    Connection con;
    Statement st;
    
    public Test(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "ciscoconpass");
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            st=con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insert(){
        try {
            int n = st.executeUpdate("insert into user values ('prenume', 'nume', 'email@ceva.com', 'user3', 'altaparola');");
            if (n==1) {
                System.out.println("rand inserat");
            }
            else{
                System.out.println("not inserted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void select_star(){
        try {
            ResultSet res =st.executeQuery("select * from user;");
            InputStream is;
            Array a=res.getArray(1);
            System.out.println("blablabal");
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    public static void main(String[] args) {
        Test T=new Test();
        T.insert();
        T.select_star();
    }
    
}
