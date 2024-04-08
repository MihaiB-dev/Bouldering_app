package Bouldering_app;
import Bouldering_app.domain.User;
import Bouldering_app.domain.Password_hashing;
import Bouldering_app.services.UserService;

import java.util.Scanner;

public class Main {
    private static int loggedUser = -1;
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        UserService userService = new UserService();
        System.out.println("Welocome to the Bouldering app:");

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
                    //TODO  show routes
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Alege una dintre cele 4 variante");
            }
        }


    }


}
