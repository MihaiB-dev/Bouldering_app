package Bouldering_app.services;

import Bouldering_app.domain.*;

import javax.swing.*;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.*;

public class RouteService {
    private static List<Route> routes;

    private static int ID = 0;

    private List<Route> archiveRoutes;

    private static Scanner myObj;

    public RouteService() {
        this.routes = new ArrayList<>();
        this.archiveRoutes = new ArrayList<>();
        myObj = new Scanner(System.in);
    }

    public Route getRoute(int index){
        return routes.get(index);
    }
    public static void addRouteSetter(User setter){
        if (setter instanceof Setter) {
            addRoute(setter);
        } else {
            throw new IllegalArgumentException("Only Setter can add routes");
        }
    }
    private static void addRoute(User setter){
        System.out.println("---------Add Route---------");

        System.out.print("Grade of the route (eg. : 4 or 5PLUS): ");
        String input = myObj.nextLine();
        Grade grade = Grade.valueOf("_" + input); //TODO try except method

        AddImageApp image = new AddImageApp(ID); //image.destinationPath has the full path
        ID ++;

        System.out.print("Do you want to specify on the image the route? [Y/N]: ");
        input = myObj.nextLine().toUpperCase();
        if(input.equals("Y") || input.equals("YES")){
            SwingUtilities.invokeLater(() -> {
            editImage app = new editImage(image.destinationPath);
            app.setVisible(true);
            });
        }

        Stats stats = new Stats();
        Route newRoute = new Route(grade, image.destinationPath, stats);
        routes.add(newRoute);

        ((Setter)setter).addRoutes(newRoute);

    }
    //get the index from a sorted array of Routes by date

    public static int chooseRoute(){
        List<Tuple<Route, Integer>> routes_sorted = new ArrayList<>();
        for(int i = 0; i < routes.size(); i ++){
            routes_sorted.add(new Tuple<>(new Route(routes.get(i)), i));
        }
        routes_sorted.sort(new RouteDateComparator());
        for (int i = 0; i < routes.size(); i ++){
            System.out.println("Index: " + i);
            System.out.print(routes.get(i).toString() + "\n\n");
        }
        System.out.println("Choose a route to see the image by writing the index, or write -1 to exit");
        int result = Integer.parseInt(myObj.nextLine());

        if (result > routes_sorted.size() || result == -1){
            return -1;
        }

        return routes_sorted.get(result).getInteger();
    }

    public static void showImage(int index){
        Path path = routes.get(index).getNamePicture();
        SwingUtilities.invokeLater(() -> {
            ImageViewer app = null;
            app = new ImageViewer(path);
            app.setVisible(true);
        });

    }

    public static void addAscentClimber(User climber){
        if(climber instanceof Climber){
            addAscent(climber);
        }else {
            throw new IllegalArgumentException("Only Climbers can add Ascents");
        }
    }

    //auto increment route attempts with attempts from the user ascent
    //Create a function for live grade (get the average of all users that added this route to their ascents) (maybe need a class)
    private static void addAscent(User climber){
        //TODO
    }
    public void archiveRoute(int index){
        archiveRoutes.add(routes.remove(index));
    }
    public void showArchiveRoutes(){
         for (Route archiveRoute : archiveRoutes) {
             archiveRoute.toString();
         }
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
class RouteDateComparator implements Comparator<Tuple<Route, Integer>> {
    @Override
    public int compare(Tuple<Route, Integer> route1, Tuple<Route, Integer> route2) {
        return route2.getRoute().getDateAdded().compareTo(route1.getRoute().getDateAdded()); // Sort by date in descending order
    }
}
