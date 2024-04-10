package Bouldering_app.domain;

import java.util.Scanner;

public class Stats {
    public double strength;
    public double technique;
    public double endurance;
    public double flexibility;

    public Stats(double strength, double technique, double endurance, double flexibility) {
        this.strength = strength;
        this.technique = technique;
        this.endurance = endurance;
        this.flexibility = flexibility;
    }

    //TODO to string
    public Stats(){
        System.out.print("Stats (real number, eg. write 90.1 for 90,1%):\nStrength: ");
        Scanner myObj = new Scanner(System.in);
        this.strength = Double.parseDouble(myObj.nextLine());
        System.out.print("Technique: ");
        this.technique = Double.parseDouble(myObj.nextLine());
        System.out.print("Endurance: ");
        this.endurance = Double.parseDouble(myObj.nextLine());
        System.out.print("flexibility: ");
        this.flexibility = Double.parseDouble(myObj.nextLine());
    }

    public String printForRoute(){
        return
                " strength=" + strength +
                ", technique=" + technique +
                ", endurance=" + endurance +
                ", flexibility=" + flexibility;
    }


}
