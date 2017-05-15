
package test;

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
    private Scanner sc;
    private String currentUser;
    
    
    public Application(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao", "root", "ciscoconpass");
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
    
    public void insert(){
        try {
            int n = statement.executeUpdate("insert into users (firstname, lastname, email, username, password) values ('prenume', 'nume', 'email@ceva.com', 'user3', 'altaparola');");
            if (n==1) {
                System.out.println("inserted");
            }
            else{
                System.out.println("not inserted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private boolean search(String userName,String password) {
        
        boolean validData=false;
         try {
            ResultSet res =statement.executeQuery("select * from users;");
            
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
    
    private boolean login() {
        
        String userName;
        String password;
        
        System.out.println("Enter your User Name: ");
        userName=sc.next();
        System.out.println("Enter your Password: ");
        password= sc.next();
        
        currentUser=userName;
        return search(userName,password);//search data for login
    }
    
    private void showProfile(String thisUser){
        //prints all user's info for user thisUser (except the comments)
    }

    private void createAccount() {
        
    }
    
    private String searchSpecificUser(String partOfName){
        
        return "";
    }
    
    private void printComments(){
        //prints the comments for currentUser
    }
    
    private void leaveComment(String user){
        //current user wants to leave a comment to user user       
    }
    
    private void friendsProfile(String user){
        showProfile(user);
        System.out.println("Press 1 for a comment");
        System.out.println("Press 2 for return to your profile");
        int state=sc.nextInt();
        switch(state){
            case(1):{
                leaveComment(user);
            }
            default:{
                userProfile();
            }
        }
    }
    
    private void searchForAnother(){
        System.out.println("Enter a username, an email or an name of a friend");
        String forSearch;
        forSearch=sc.next();
        String friendsUsername=searchSpecificUser(forSearch);
        if (friendsUsername.isEmpty()){
            System.out.println("There is no user like that!");
            userProfile();
        }
        else{
            friendsProfile(friendsUsername);
        }
        
    }
    
    private void changeDescription(){
        
    }
    
    private void userProfile(){
        int state;
        showProfile(currentUser);
        printComments();
        System.out.println("Press 1 for search for an account");
        System.out.println("Press 2 for change your description");
        System.out.println("Press 3 for logout");
        state = sc.nextInt();
        switch(state){
            case(1):{
                searchForAnother();
            }
            case(2):{
                changeDescription();
            }
            case(3):{
                startApplication();
            }
            default:{
                userProfile();
            }
        }
    }
    
    public void startApplication(){
        
        int state;
       
        System.out.println("Press 1 for login");
        System.out.println("Press 2 for create new account");
        state=sc.nextInt();
        
        switch(state){// user wants to login 
            case(1):{
                if (login()==true)
                {
                    System.out.println("Bun venit!");
                    userProfile();
                }

                else {
                    System.out.println("User sau parola incorecta!");
                    startApplication();
                }
            } 
            case(2):{
                createAccount();
            }
            default:{
                startApplication();
            }
        }
    }
    
    public static void main(String[] args) {
    
        Application app = new Application();
        app.startApplication();
        
    }
    
}
