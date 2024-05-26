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
                    int index = RouteService.chooseRoute("Choose a route to see the image"); // if index is -1 then we have an error
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
                    int index = RouteService.chooseRoute("Choose a route to see the image"); // if index is -1 then we have an error
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
                    int index = RouteService.chooseRoute("Choose a route by writing the index"); // if index is -1 then we have an error
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

    private static void adminMainPage() {
        while(loggedUser == -2509){
            System.out.println("---------Admin Main Page---------");
            System.out.print("Delete a user = 1\nDelete a route = 2\nArchive an ascent = 3\nLog Out = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    //show all users and choose an index
                    userService.showAllUsers(userService.getUser()); //it will be shown id, full name and type of user
                    System.out.print("Full name of the user you want to delete: ");

                    String fullName = myObj.nextLine();
                    userService.deleteUser(userService.getUser(), fullName);
                    break;
                case "2":
                    System.out.println("Warning: this a destructive operation, all the user ascents will be deleted too");
                    //int index = routeService.chooseRoute("Choose the route you want to delete");
                    //if(index == -1){
                    //    System.out.println("There isn't a route with this index");
                    //    break;
                    //}
                    //routeService.DeleteRoute(userService.getUser(), index);
                    break;
                case "3":
                    System.out.print("Index of the ascent you want to delete: ");
                    int indexAscent = Integer.parseInt(myObj.nextLine());
                    //routeService.showAscents(userService.getUser());

                    //routeService.deleteAscent(userService.getUser(), indexAscent); //it will shown id, date, route grade and users Ascent, ordered by username
                    break;
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
            else if(loggedUser == -2509){
                Main.adminMainPage();
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
