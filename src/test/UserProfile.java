
package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mpop
 */
public class UserProfile {
    
    private Connection connection;
    private Statement statement;
    private Scanner sc;
    private String currentUser;
    private SqlCommands sql;
    
    public UserProfile(String currentUser){
        
        sql=new SqlCommands();
        statement=sql.conexion();
        sc=new Scanner(System.in);
        this.currentUser=currentUser;
    
    }
    
    public void profile(){
            int state;
            //showProfile(currentUser);
            printComments();
            
            System.out.println("Press 1 for search for an account");
            System.out.println("Press 2 for change description");
            System.out.println("Press 3 for change password");
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
                    Application app= new Application();
                    app.startApplication();
                    break;
                }
                default:{
                    profile();
                }
            }
    }
    
    public void showProfile(String thisUser){
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
    
    public String searchSpecificUser(String partOfName){
    
        boolean OK=true;
        //ResultSet res;
        ArrayList<String> users = new ArrayList<>();
        int index=0; // Index of ArrayList with strings
        try {
            
            ResultSet res =statement.executeQuery("select * from users where username = '"+partOfName+"' ;");
            while (res.next()){
                if(partOfName.equals(res.getString("username"))){
                    return partOfName;
                }
            }
            
            res = statement.executeQuery("select * from users where firstname = '"+partOfName+"' ;");
      
            while (res.next()){
                if(partOfName.equals(res.getString("firstname"))){
                    if(OK){
                        OK=false;
                        System.out.println("There are one ore more results for your search\nPlease insert one of the following usernames:\n");
                    }
                    System.out.print(res.getString("firstname")+" is first name for account : "+res.getString("username"));
                    System.out.println("\nPress "+index +" for this user\n\n ");
                    users.add(index,res.getString("username"));
                    index++;
                    
                }
                
            }
            
            res = statement.executeQuery("select * from users where lastname = '"+partOfName+"' ;");
            while (res.next()){
                if(partOfName.equals(res.getString("lastname"))){
                    if(OK){
                        OK=false;
                        System.out.println("There are one ore more results for your search\nPlease insert one of the following usernames:\n");
                    }
                    
                    System.out.print(res.getString("lastname")+" is last name for account : "+res.getString("username"));
                    System.out.println("\nPress "+index +" for this user\n\n " );
                    users.add(index,res.getString("username"));
                    index++;
                }
                
            }
            
            if (OK){
                return "";
            }
            else{
                String username=users.get(sc.nextInt());
                return searchSpecificUser(username);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public void searchForAnother(){
        System.out.println("Enter a username, an email or an name of a friend");
        System.out.println("or type 'mine' for returning to your profile");
        String forSearch;
        forSearch=sc.next();
        String friendsUsername=searchSpecificUser(forSearch);
        
        
        if (friendsUsername.equals("mine")){
            profile();
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
   
    public void printComments(){
        
        
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

    
    public void leaveComment(String user){
        
        Scanner ss = new Scanner(System.in);
        //current user wants to leave a comment to user user     
        System.out.println("please insert in single line your comment for "+user);
        String comm = ss.nextLine();
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
    
    public void friendsProfile(String user){
        //showProfile(user);
        System.out.println("Press 1 for a comment");
        System.out.println("Press 2 for return to your profile");
        int state=sc.nextInt();
        switch(state){
            case(1):{
                leaveComment(user);
                break;
            }
            case(2):{
                profile();
            }
            default:{
                friendsProfile(user);
            }
        }
    }
    
    
    public void changeDescription(){
        System.out.println("Please insert in a line your new description");
        Scanner ss= new Scanner(System.in);
        String newDescription;
        newDescription= ss.nextLine();
        
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
        profile();
    }
    
    public void changePassword(){
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
        profile();
    }
    
    
}
