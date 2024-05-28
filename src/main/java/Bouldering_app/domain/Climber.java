package Bouldering_app.domain;

import Bouldering_app.databaseConnections.DatabaseAscent;
import Bouldering_app.databaseConnections.DatabaseStats;
import Bouldering_app.services.UserInteractionService;

import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class Climber extends User implements UserInteractionService {
    private Grade avgGrade;
    private Stats userStats;
    private List<Ascent> ascents;

    DatabaseAscent databaseAscent = DatabaseAscent.getDatabaseAscent();

    public Climber(String fullName, String hashPassword, String avgGrade, int userStats, int idClimber) {
        super(fullName, hashPassword);
        DatabaseStats databaseStats = DatabaseStats.DatabaseStats();

        this.avgGrade = Grade.fromString(avgGrade);
        this.userStats = DatabaseStats.getById(userStats);
        this.ascents = databaseAscent.getAscents(idClimber);
    }

    public Grade getAvgGrade() {
        return avgGrade;
    }

    public Stats getUserStats() {
        return userStats;
    }

    public List<Ascent> getAscents() {
        return ascents;
    }

    public void showAscents_sortByDifficulty(){
        List<Tuple<Ascent, Integer>> ascentsSorted = new ArrayList<>();
        for(int i = 0; i < ascents.size(); i ++){
            ascentsSorted.add(new Tuple<>(new Ascent(ascents.get(i)), i));
        }
        ascentsSorted.sort(new AscentDifficultyComparator());
        for (int i = 0; i < ascents.size(); i ++){
            System.out.println("Ascent " + i + ": ");
            System.out.print(ascentsSorted.get(i).getRoute().toString() + "\n\n");
        }

        System.out.print("Choose an ascent by writing the index, or write -1 to exit: ");
        Scanner myObj = new Scanner(System.in);
        int result = Integer.parseInt(myObj.nextLine());
        if (result != -1) {
            Path path = ascents.get(result).getRoute().getNamePicture();
            SwingUtilities.invokeLater(() -> {
                ImageViewer app = null;
                app = new ImageViewer(path);
                app.setVisible(true);
            });
        }
    }

    @Override
    public String printProfile() {
        return "name: " + this.getFullName()
                         + "\navg_grade: " + this.avgGrade + " Color: " + this.avgGrade.getColor()
                         + "\nStats:\n" + userStats ;
//                         + "\n" + showAscents());
    }


    //this will show the routes in descending order by difficulty


    public void updateAscents(int idClimber) {
        ascents = databaseAscent.getAscents(idClimber);
    }
}

class Tuple<A,B>{
    public A route; public B integer;

    public Tuple(A route, B integer) {
        this.route = route;
        this.integer = integer;
    }
    public A getRoute() {return route;}
    public B getInteger(){return integer;}
}

class AscentDifficultyComparator implements Comparator<Tuple<Ascent, Integer>> {

    @Override
    public int compare(Tuple<Ascent, Integer> route1, Tuple<Ascent, Integer> route2) {
        return route2.getRoute().getRoute().getOriginalGrade().compareTo(route1.getRoute().getRoute().getOriginalGrade()); // Sort by date in descending order
    }
}