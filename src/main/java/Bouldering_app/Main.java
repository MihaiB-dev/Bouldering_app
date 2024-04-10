package Bouldering_app;
import Bouldering_app.domain.Route;
import Bouldering_app.domain.User;
import Bouldering_app.domain.Password_hashing;
import Bouldering_app.services.RouteService;
import Bouldering_app.services.UserService;

import java.util.Scanner;

public class Main {
    private static int loggedUser = -1; //if it is positive, we have the index of the user
    static UserService userService = new UserService();
    static RouteService routeService = new RouteService();
    static Scanner myObj = new Scanner(System.in);
    public static void unregisteredMainPage(){
        while(loggedUser == -1) {
            System.out.println("---------Main Page---------");
            System.out.print("Sing Up = 1\nLogIn = 2\nShow Routes = 3\nexit = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    loggedUser = userService.SignUp();
                    break;
                case "2":
                    loggedUser = userService.LogIn();
                    break;
                case "3":
                    int index = RouteService.chooseRoute(); // if index is -1 then we have an error
                    if(index != -1){RouteService.showImage(index);}else{
                        System.out.println("There isn't a route with this index");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choose from one of the 4 variants");
            }
        }
    }

    public static void setterMainPage(){
        while(loggedUser != -1){
            System.out.println("---------Setter Main Page---------");
            System.out.print("Add Routes = 1\nArchive Routes = 2\nShow Routes = 3\nLog Out = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    //we add the user as argument to verify if the user is actually a setter
                    RouteService.addRouteSetter(UserService.getUser(loggedUser));
                    break;
                case "2":
                    //TODO make an archive method for the objects and move the images to other file
                    break;
                case "3":
                    int index = RouteService.chooseRoute(); // if index is -1 then we have an error
                    if(index != -1){
                        RouteService.showImage(index);
                    }
                    break;
                case "0":
                    loggedUser = -1;
                    break;
                default:
                    System.out.println("Choose from one of the 4 variants");
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to the Bouldering app:");

        Main.unregisteredMainPage();

        while(true){
            if(loggedUser == -1){
                Main.unregisteredMainPage();
            }
            else if(userService.isClimber(loggedUser)){
                //TODO make the climber main page
            }
            else if(userService.isSetter(loggedUser)){
                Main.setterMainPage();
            }
        }
    }
}
