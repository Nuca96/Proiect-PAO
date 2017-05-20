
package test;

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
    
    private Statement statement;
    private Scanner sc;
    private String currentUser;
    private SqlCommands sql;
    public Application(){
        
        sql=new SqlCommands();
        statement = sql.conexion();
        
        sc= new Scanner(System.in);
    }
    
    public void setCurrentuser(String currentUser){
        this.currentUser=currentUser;
    }
    
    public String getCurrentuser(){
        return currentUser;
    }
    
    public boolean search(String userName,String password) {
        
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

    public void createAccount() {
        String username, password, email, firstName, lastName;
        boolean ok = false;
        while (!ok){
            ok=true;
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
            
            Validation valid= new Validation(firstName,lastName,username,password,email);
            ok=valid.validData();

            if (!ok)
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
                        
                        UserProfile user= new UserProfile(currentUser);
                        user.profile();
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
                    UserProfile user=new UserProfile(currentUser);
                    user.profile();
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
