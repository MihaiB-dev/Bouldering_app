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
            System.out.println("---------Setter Main Page " + UserService.getUser(loggedUser).getFull_name() + "---------");
            System.out.print("My profile = 1\nAdd Routes = 2\nArchive Routes = 3\nShow Routes = 4\nLog Out = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    UserService.profile(loggedUser);
                    break;
                case "2":
                    //we add the user as argument to verify if the user is actually a setter
                    RouteService.addRouteSetter(UserService.getUser(loggedUser));
                    break;
                case "3":
                    RouteService.ArchiveRouteSetter(UserService.getUser(loggedUser));
                    System.out.println("This route was successfully archived!");
                    break;
                case "4":
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

    public static void climberMainPage(){
        while(loggedUser != -1){
            System.out.println("---------Climber Main Page : " + UserService.getUser(loggedUser).getFull_name() + "---------");
            System.out.print("My profile = 1\nAdd Ascents = 2\nShow Routes = 3\nShow your Ascents = 4\nLog Out = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    UserService.profile(loggedUser);
                    break;
                case "2":
                    //we add the user as argument to verify if the user is actually a setter
                    RouteService.addAscentClimber(UserService.getUser(loggedUser));
                    break;
                case "3":
                    int index = RouteService.chooseRoute(); // if index is -1 then we have an error
                    if(index != -1){
                        RouteService.showImage(index);
                    }
                    break;
                case "4":
                    UserService.showAscents(loggedUser);
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
            else if(UserService.isClimber(loggedUser)){

                Main.climberMainPage();
            }
            else if(UserService.isSetter(loggedUser)){
                Main.setterMainPage();
            }
        }
    }
}
