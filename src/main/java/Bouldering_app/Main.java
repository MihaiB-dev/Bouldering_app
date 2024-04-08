package Bouldering_app;
import Bouldering_app.domain.User;
import Bouldering_app.domain.Password_hashing;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);

        Password_hashing p = new Password_hashing(16);


        User u = new User("andrei", p.hash(myObj.nextLine().toCharArray()));

        if (p.authenticate("andrei".toCharArray(), u.getHashPassword())) System.out.println("verfiicare cu succes");

        System.out.println(u.getHashPassword());
    }


}
