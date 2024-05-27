package Bouldering_app.services;

import Bouldering_app.databaseConnections.DatabaseAscent;
import Bouldering_app.databaseConnections.DatabaseRoute;
import Bouldering_app.databaseConnections.DatabaseStats;
import Bouldering_app.databaseConnections.DatabaseUser;
import Bouldering_app.domain.*;

import javax.swing.*;
import java.nio.file.Path;
import java.util.*;

public class RouteService {
    //private static List<Route> routes;

    //the key is the index from the router, the values are all avg grades of the users in that moment in time
    private static Map<Integer, List<Grade>> routesGradeUsers = new HashMap<Integer, List<Grade>>();


    //private static List<Route> archiveRoutes;

    private static Scanner myObj;

    private DatabaseRoute databaseRoute = DatabaseRoute.getDatabaseRoute();
    private DatabaseUser databaseUser = DatabaseUser.getDatabaseUser();

    private DatabaseAscent databaseAscent = DatabaseAscent.getDatabaseAscent();
    private DatabaseStats databaseStats = DatabaseStats.DatabaseStats();

    public RouteService() {
        //this.archiveRoutes = new ArrayList<>();
        myObj = new Scanner(System.in);
    }

    public void addRouteSetter(User setter){
        if (setter instanceof Setter) {
            addRoute(setter);
        } else {
            throw new IllegalArgumentException("Only Setter can add routes");
        }
    }
    private  void addRoute(User setter){
        System.out.println("---------Add Route---------");

        System.out.print("Grade of the route (eg. : 4 or 5PLUS): ");
        String input = myObj.nextLine();
        Grade grade = Grade.valueOf("_" + input); //TODO try except method
        int ID = databaseRoute.getLastId();
        AddImageApp image = new AddImageApp(ID); //image.destinationPath has the full path

        System.out.print("Do you want to specify on the image the route? [Y/N]: ");
        input = myObj.nextLine().toUpperCase();
        if(input.equals("Y") || input.equals("YES")){
            SwingUtilities.invokeLater(() -> {
            editImage app = new editImage(image.destinationPath);
            app.setVisible(true);
            });
        }
        System.out.print("Stats (real number, eg. write 90.1 for 90,1%):\nStrength: ");
        double strength = Double.parseDouble(myObj.nextLine());
        System.out.print("Technique: ");
        double technique = Double.parseDouble(myObj.nextLine());
        System.out.print("Endurance: ");
        double endurance = Double.parseDouble(myObj.nextLine());
        System.out.print("flexibility: ");
        double flexibility = Double.parseDouble(myObj.nextLine());

        Stats stats = new Stats(strength, technique, endurance, flexibility);
        Route newRoute = new Route(grade, image.destinationPath, stats);

        //routes.add(newRoute);
        databaseRoute.insertRoute(newRoute, databaseUser.getIdSetter(setter));

    }
    //get the index from a sorted array of Routes by date

    public void DeleteRouteSetter(User setter){
        if (setter instanceof Setter) {
            DeleteRoute();
        } else {
            throw new IllegalArgumentException("Only Setter can archive routes");
        }
    }
    private void DeleteRoute(){
        int routeIndex = chooseRoute("Choose the route you want to archive");

        databaseRoute.removeRoute(routeIndex);
        //archiveRoutes.add(routes.remove(routeIndex));
    }

    public static int chooseRoute(String printValue){
        List<Route> routes = DatabaseRoute.getDatabaseRoute().getRoutes();

        List<Tuple<Route, Integer>> routesSorted = new ArrayList<>();
        for(int i = 0; i < routes.size(); i ++){
            routesSorted.add(new Tuple<>(new Route(routes.get(i)), i));
        }
        routesSorted.sort(new RouteDateComparator());
        for (int i = 0; i < routes.size(); i ++){
            System.out.println("Route " + (i + 1) + ": ");
            System.out.print(routesSorted.get(i).getRoute().toString() + "\n\n");
        }
        System.out.print(printValue + ", or write -1 to exit: ");
        int result = Integer.parseInt(myObj.nextLine());

        if (result > routesSorted.size() || result == -1){
            return -1;
        }

        return routesSorted.get(result).getInteger() + 1;
    }

    public void showImage(int index){
        Path path = databaseRoute.getRouteById(index).getNamePicture();
        SwingUtilities.invokeLater(() -> {
            ImageViewer app = null;
            app = new ImageViewer(path);
            app.setVisible(true);
        });

    }

    public void addAscentClimber(User climber, int id_user){
        if(climber instanceof Climber){
            addAscent(climber,id_user);
        }else {
            throw new IllegalArgumentException("Only Climbers can add Ascents");
        }
    }

    //auto increment route attempts with attempts from the user ascent
    //Create a function for live grade (get the average of all users that added this route to their ascents) (maybe need a class)
    private void addAscent(User climber, int id_user){
        int routeIndex = chooseRoute("Choose a route that you have done");
        if(routeIndex == -1){return;} //TODO throw exception

        System.out.print("Attempts: ");
        String attempts = myObj.nextLine();


        //((Climber)climber).addAscent(new Ascent(databaseRoute.getRouteById(routeIndex), Integer.parseInt(attempts)));
        //update stats

        databaseAscent.addAscent(databaseUser.getIdClimber(climber), routeIndex, Integer.parseInt(attempts));
        int id_ascent = databaseAscent.getById(databaseUser.getIdClimber(climber), routeIndex);
        //create a new stat and change the original with the new one

        //change the stats using functions from the stats class
        ((Climber) climber).getUserStats().Update(databaseAscent.getAscent(id_ascent));

        databaseStats.updateStats(databaseUser.getIdClimber(climber), ((Climber) climber).getUserStats());

        int sum = 0;
        List<Grade> userPersonalGrades = databaseUser.getAllClimberGrades(databaseUser.getIdClimber(climber));
        for(Grade element : userPersonalGrades){
            sum += element.ordinal();
        }
        int size_userPersonalGrades = userPersonalGrades.size();
        databaseUser.setAvgGrade(databaseUser.getIdClimber(climber), Grade.values()[(int)(sum/size_userPersonalGrades)]);


        //routes.get(routeIndex).addAttempts(Integer.parseInt(attempts));
        databaseRoute.addAttempts(routeIndex, Integer.parseInt(attempts));

        //generate the live grade
        //get all the ascents with the index id_route, then take all the grades of the users that have done that route
        //and calculate the average
        sum = 0;
        List<Grade> userGrades = databaseRoute.getUserGrades(routeIndex);
        for(Grade element : userGrades){
            sum += element.ordinal();
        }
        int size_route_userGrades = userGrades.size();

        //routes.get(routeIndex).setLiveGrade(Grade.values()[(int)(sum/size_route_userGrades)]);
        databaseRoute.setLiveGrade(routeIndex, Grade.values()[(int)(sum/size_route_userGrades)]);
    }
//    public void archiveRoute(int index){
//        archiveRoutes.add(routes.remove(index));
//    }
//    public void showArchiveRoutes(){
//         for (Route archiveRoute : archiveRoutes) {
//             archiveRoute.toString();
//         }
//    }
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
