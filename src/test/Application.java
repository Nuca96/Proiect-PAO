
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
    
    
    private boolean search(String userName,String password) {
        
        boolean validData=false;
         try {
            ResultSet res =statement.executeQuery("select * from users;");
            
            String currUserName;
            String currPassword;
            
            while (res.next()){
                currUserName=res.getString("username");
                currPassword=res.getString("password");
                
                if((currUserName.equals(userName))&&(currPassword.equals(password))){
                    currentUser=userName;
                    validData=true;
                    break;
                }
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
        
        return search(userName,password);//search data for login
    }
    
    private void showProfile(String thisUser){
            //prints user's info for thisUser (except the password and the comments)
        try {
            ResultSet res =statement.executeQuery("select * from users where username = '" +thisUser+"';");
            if (res.next()){
                System.out.println("Username: " + res.getString("username"));
                System.out.println("Name: " + res.getString("firstname")+" "+res.getString("lastname"));
                System.out.println("Email: " + res.getString("email"));
                System.out.println("Description: " + res.getString("description"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void createAccount() {
        String username, password, email, firstName, lastName;
        boolean ok = true;
        while (ok){
            ok=false;
            System.out.println("first name: ");
            firstName=sc.next();
            System.out.println("last name: ");
            lastName=sc.next();
            System.out.println("Username: ");
            username=sc.next();
            System.out.println("password: ");
            password=sc.next();
            System.out.println("email: ");
            email=sc.next();
            try {
                ResultSet res =statement.executeQuery("select * from users;");
                while (res.next()){
                    if(username.equals( res.getString("username"))) 
                        ok = true;
                    if(email.equals(res.getString("email"))) 
                        ok = true;
                }

            } catch (SQLException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (ok)
            {
                System.out.println("Username or email not available");
            }
            else{
                String sql="insert into users (username, email, lastname, firstname, password) values ('" + username + "', '" + email + "', '" + lastName + "', '" + firstName + "', '" + password+"');";
                try {
                    int n = statement.executeUpdate(sql);
                    if (n==1){
                        System.out.println("Accound created ");
                        currentUser=username;
                        userProfile();
                    }
                    else{
                        System.out.println("error");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
        }
    }
    
    private String searchSpecificUser(String partOfName){
        try {
            ResultSet res =statement.executeQuery("select * from users;");
            while (res.next()){
                if(partOfName.equals(res.getString("username"))){
                    return partOfName;
                }
            }
            res = statement.executeQuery("select * from users;");
            boolean OK=true;
            while (res.next()){
                if(partOfName.equals(res.getString("firstname")) || partOfName.equals(res.getString("email")) || partOfName.equals(res.getString("lastname"))){
                    if(OK){
                        OK=false;
                        System.out.println("There are one ore more results for your search\nPlease insert one of the following usernames:\n");
                    }
                    System.out.println(res.getString("username"));
                }
                
            }
            if (OK){
                return "";
            }
            else{
                String username=sc.next();
                return searchSpecificUser(username);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    private void printComments(){
        //prints the comments for currentUser
        System.out.println("Comments: ");
        try {
            ResultSet res =statement.executeQuery("select * from comments where recever = '"+ currentUser +"' ;");
            while(res.next()){
                System.out.println(res.getString("sender") + ":\n" + res.getString("comment"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void leaveComment(String user){
        //current user wants to leave a comment to user user     
        System.out.println("please insert in single line your comment for "+user);
        String comm = sc.nextLine();
        try {
            int n = statement.executeUpdate("insert into comments(sender, recever, comment) values ('"+currentUser+"', '"+user+"', '"+comm+"');");
            if (n==1) {
                System.out.println("comment inserted");
            }
            else{
                System.out.println("error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        friendsProfile(user);
    }
    
    private void friendsProfile(String user){
        showProfile(user);
        System.out.println("Press 1 for a comment");
        System.out.println("Press 2 for return to your profile");
        int state=sc.nextInt();
        switch(state){
            case(1):{
                leaveComment(user);
                break;
            }
            case(2):{
                userProfile();
            }
            default:{
                friendsProfile(user);
            }
        }
    }
    
    private void searchForAnother(){
        System.out.println("Enter a username, an email or an name of a friend");
        System.out.println("or type 'mine' for returning to your profile");
        String forSearch;
        forSearch=sc.next();
        String friendsUsername=searchSpecificUser(forSearch);
        if (friendsUsername.equals("mine")){
            userProfile();
        }
        else{
            if (friendsUsername.isEmpty()){
                System.out.println("There is no such user!");
                searchForAnother();
            }
            else{
                friendsProfile(friendsUsername);
            }
            
        }
        
    }
    
    private void changeDescription(){
        System.out.println("Please insert in a line your new description");
        String newDescription = sc.nextLine();
        String sql="update users set description = '"+ newDescription + "' where username = '" + currentUser+"';";
        try {
            int n = statement.executeUpdate(sql);
            if (n==1){
                System.out.println("Description changed");
            }
            else{
                System.out.println("error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        userProfile();
    }
    
    private void changePassword(){
        System.out.println("please reenter your password in order to change it");
        String password, newPassword;
        String sql;
        password = sc.next();
        try {
            sql = "select * from comments where username = '" + currentUser+ "' ;";
            ResultSet res =statement.executeQuery(sql);
            res.next();
            if (password.equals(res.getString("password"))){
                System.out.println("insert your new password");
                newPassword = sc.next();
                sql="update users set password = '"+ newPassword + "' where username = '" + currentUser+"';";
                int n = statement.executeUpdate(sql);
                if (n==1){
                    System.out.println("Password changed");
                }
                else{
                    System.out.println("error");
                }
            }
            else{
                System.out.println("Wrong password!");
                changePassword();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        userProfile();
    }
    
    private void userProfile(){
        int state;
        showProfile(currentUser);
        printComments();
        System.out.println("Press 1 for search for an account");
        System.out.println("Press 2 for change description");
        System.out.println("Press 2 for change password");
        System.out.println("Press 4 for logout");
        state = sc.nextInt();
        switch(state){
            case(1):{
                searchForAnother();
                break;
            }
            case(2):{
                changeDescription();
                break;
            }
            case(3):{
                changePassword();
                break;
            }
            case(4):{
                startApplication();
                break;
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
        System.out.println("Press 3 for exit the aplication");
        state=sc.nextInt();
        
        switch(state){// user wants to login 
            case(1):{
                if (login()==true)
                {
                    System.out.println("Welcome!");
                    userProfile();
                }

                else {
                    System.out.println("User or password incorrect");
                    startApplication();
                }
                break;
            } 
            case(2):{
                createAccount();
                break;
            }
            case(3):{
                System.out.println("Thank you! Good bye!");
                break;
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
