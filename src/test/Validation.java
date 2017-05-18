
package test;

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
public class Validation {
    
   
    private Connection connection;
    private Statement statement;
    private Scanner sc;
    private String firstname;
    private String lastname;
    private String userName;
    private String password;
    private String email;
    
    public Validation(String firstname , String lastname, String userName, String password, String email){
        
        this.firstname=firstname;
        this.lastname=lastname;
        this.userName=userName;
        this.password=password;
        this.email=email;

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
        sc= new Scanner(System.in);
    
    
    }
    
    public void setFirstName(String firstname){
        this.firstname=firstname;
    }
    
    public void setLastname(String lastname){
        this.lastname=lastname;
    }
    
    public void setUser(String userName){
        this.userName=userName;
    }
    
    public void setPassword(String password ){
        this.password=password;
    }
    
    public void setEmail(String email){
        this.email=email;
    }
    
    public String getFirstname(){
        return firstname;
    }
    
    public String getLastname(){
        return lastname;
    }
    public String getUser(){
        return userName;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getEmail(){
        return email;
    }
    
    public boolean validName(String name){
        
        boolean valid=true;    //I suspect taht name is corect and search incorect characters (numbers or special characters)
        for(int i=0;i<name.length();i++) {   
            if((name.charAt(i)>='A')&&(name.charAt(i)<='Z')||(name.charAt(i)>='a')&&(name.charAt(i)<='z'))
                valid=false;
        }
        return valid;   
        
    }
    
    public boolean validPassword(String password) {      

        if (password.length() < 6)
            return false;

        boolean validLitera = false;    
        for (int i = 0; i < password.length(); i++)
            if ((password.charAt(i) >= 'A') && (password.charAt(i) <= 'Z'))
                validLitera = true;

        boolean validCifra = false;
        for (int i = 0; i < password.length(); i++)
            if ((password.charAt(i) >= '0') && (password.charAt(i) <= '9'))
                validCifra = true;

        if ((validLitera == false) || (validCifra == false))
            return false;

        return true;
    }

    public boolean validEmail(String email){
    
        boolean valid=false;    //I suspect that email don't have @ and search "@" in String "email"
        
        for(int i=0;i<email.length();i++){
            if(email.charAt(i)=='@')
                valid=true;
        }
        
        return valid;
    }
    
    public boolean validData(){
        
        if(validName(firstname)==false){
            System.out.println("Insert a correct first name");
            return false;
        }
        
        if(validName(lastname)==false){
            System.out.println("Insert a correct last name");
            return false;
        }
        
        if(validPassword(password)==false){
            System.out.println("Password mast have 6 character , a number and a capital");
            return false;
        }
        
        try {
                ResultSet res =statement.executeQuery("select * from users;");
                while (res.next()){
                    if(userName.equals( res.getString("username"))) {
                        System.out.println("User already exist");
                        return false;
                    }
                    if(email.equals(res.getString("email"))){ 
                        System.out.println("This email is taken ");
                        return false;
                    }                    
                }
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return true;
        
    }
    
    
}
