
package application;

import java.io.InputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author mpop
 */
public class Application {
    
    private Connection connection;
    private Statement statement;
    private String db="pao";
    private String tableUsers="users";//store users
    private String tableComments="commets";//store all the comments 
    
    
    public Application(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db, "root", "ciscoconpass");
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            statement=connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void insert(){
        try {
            int n = statement.executeUpdate("insert into"+tableUsers +" values ('prenume', 'nume', 'email@ceva.com', 'user3', 'altaparola');");
            if (n==1) {
                System.out.println("rand inserat");
            }
            else{
                System.out.println("not inserted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean search(String userName,String password) {
        
        boolean validData=false;
         try {
            ResultSet res =statement.executeQuery("select * from user;");
            
            String currUserName;
            String currPassword;
            
            while (res.next()){
                currUserName=res.getString("username");
                currPassword=res.getString("password");
                
                if((currUserName.equals(userName))&&(currPassword.equals(password)))
                    validData=true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        return validData;
    }
     
    public static void main(String[] args) {
    
        Application app = new Application();
        int state;
       
        Scanner sc= new Scanner(System.in);
        
        System.out.println("Press 1 for login");
        System.out.println("Press 2 for create new account");
        state=sc.nextInt();
        
        if(state==1){// user wants to login 
            boolean ok;
            String userName;
            String password;
            
            System.out.println("Enter your User Name: ");
            userName=sc.next();
            password= sc.next();
            ok=search(userName,password);//search data for login
            
        }
        
    }
    
}
