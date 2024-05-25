package Bouldering_app;
import Bouldering_app.services.RouteService;
import Bouldering_app.services.UserService;
import config.DatabaseConfiguration;
import config.SetupTables;

import java.sql.Connection;
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
            System.out.println("---------Setter Main Page " + userService.getUser().getFullName() + "---------");
            System.out.print("My profile = 1\nAdd Routes = 2\nArchive Routes = 3\nShow Routes = 4\nLog Out = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    userService.profile();
                    break;
                case "2":
                    //we add the user as argument to verify if the user is actually a setter
                    RouteService.addRouteSetter(userService.getUser());
                    break;
                case "3":
                    RouteService.ArchiveRouteSetter(userService.getUser());
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
            System.out.println("---------Climber Main Page : " + userService.getUser().getFullName() + "---------");
            System.out.print("My profile = 1\nAdd Ascents = 2\nShow Routes = 3\nShow your Ascents = 4\nLog Out = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    userService.profile();
                    break;
                case "2":
                    //we add the user as argument to verify if the user is actually a setter
                    RouteService.addAscentClimber(userService.getUser());
                    break;
                case "3":
                    int index = RouteService.chooseRoute(); // if index is -1 then we have an error
                    if(index != -1){
                        RouteService.showImage(index);
                    }
                    break;
                case "4":
                    //userService.showAscents();
                case "0":
                    loggedUser = -1;
                    break;
                default:
                    System.out.println("Choose from one of the 4 variants");
            }
        }
    }

    public static void createDatabase(){
        SetupTables setupTables = new SetupTables();
        setupTables.createTables();
    }
    public static void main(String[] args) {
        Main.createDatabase();
        System.out.println("Welcome to the Bouldering app:");

        Main.unregisteredMainPage();
        while(true){
            if(loggedUser == -1){
                Main.unregisteredMainPage();
            }
            else if(userService.isClimber()){

                Main.climberMainPage();
            }
            else if(userService.isSetter()){
                Main.setterMainPage();
            }
        }
    }
}
