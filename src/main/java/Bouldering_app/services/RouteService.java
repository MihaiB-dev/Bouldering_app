package Bouldering_app.services;

import Bouldering_app.domain.*;

import javax.swing.*;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.*;

public class RouteService {
    private static List<Route> routes;

    //the key is the index from the router, the values are all avg grades of the users in that moment in time
    private static Map<Integer, List<Grade>> routes_gradeUsers = new HashMap<Integer, List<Grade>>();

    private static int ID = 0;

    private static List<Route> archiveRoutes;

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

    public static void ArchiveRouteSetter(User setter){
        if (setter instanceof Setter) {
            archiveRoute(setter);
        } else {
            throw new IllegalArgumentException("Only Setter can archive routes");
        }
    }
    private static void archiveRoute(User setter){
        int routeIndex = chooseRoute();
        archiveRoutes.add(routes.remove(routeIndex));
    }
    public static int chooseRoute(){
        List<Tuple<Route, Integer>> routes_sorted = new ArrayList<>();
        for(int i = 0; i < routes.size(); i ++){
            routes_sorted.add(new Tuple<>(new Route(routes.get(i)), i));
        }
        routes_sorted.sort(new RouteDateComparator());
        for (int i = 0; i < routes.size(); i ++){
            System.out.println("Route " + i + ": ");
            System.out.print(routes_sorted.get(i).getRoute().toString() + "\n\n");
        }
        System.out.print("Choose a route by writing the index, or write -1 to exit: ");
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
        int routeIndex = chooseRoute();
        if(routeIndex == -1){return;} //TODO throw exception

        System.out.print("Attempts: ");
        String result = myObj.nextLine();


        ((Climber)climber).addAscent(new Ascent(routes.get(routeIndex), Integer.parseInt(result)));

        //add attempts to the route
        routes.get(routeIndex).addAttempts(Integer.parseInt(result));

        //generate the live grade
        //add users to the map to the route they have done
        if (!routes_gradeUsers.containsKey(routeIndex)){
            List<Grade> grades = new ArrayList<>();
            grades.add(((Climber) climber).getAvgGrade());
            routes_gradeUsers.put(routeIndex, grades);
        }
        else{
            List<Grade> grades = routes_gradeUsers.get(routeIndex);
            grades.add(((Climber) climber).getAvgGrade());
            routes_gradeUsers.put(routeIndex, grades);
        }

        int sum = 0;
        //calculate the arithmetic of the grades
        for(Grade element : routes_gradeUsers.get(routeIndex)){
            sum += element.ordinal();
        }
        int size_route_userGrades = routes_gradeUsers.get(routeIndex).size();

        routes.get(routeIndex).setLiveGrade(Grade.values()[(int)(sum/size_route_userGrades)]);
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
