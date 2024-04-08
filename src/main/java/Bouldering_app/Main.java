package Bouldering_app;
import Bouldering_app.domain.User;
import Bouldering_app.domain.Password_hashing;
import Bouldering_app.services.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        UserService userService = new UserService();
        System.out.println("Welocome to the Bouldering app:");


        boolean ok = true;
        while(ok == true) {
            System.out.print("Sing Up = 1\nLogIn = 2\nShow Routes = 3\nexit = 0\nYour choice: ");
            String chosen = myObj.nextLine();
            switch (chosen) {
                case "1":
                    userService.SignUp();
                    //TODO LogIn after
                    ok = false;
                    break;
                case "2":
                    //TODO LogIn
                    ok = false;
                    break;
                case "3":
                    //TODO  show routes
                    ok = false;
                    break;
                case "0":
                    ok = false;
                    return;
                default:
                    System.out.println("Alege una dintre cele 4 variante");
            }
        }


    }


}
