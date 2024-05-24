package Bouldering_app.domain;
import Bouldering_app.services.UserInteractionService;

import java.time.LocalDate; // import the LocalDate class
import java.util.ArrayList;
import java.util.List;

public class Setter extends User implements UserInteractionService {
    private List<Route> addedRoutes;
    private LocalDate dateEmployee;

    public Setter(String fullName, String hashPassword) {
        super(fullName, hashPassword);
        this.addedRoutes  = new ArrayList<>();
        this.dateEmployee = LocalDate.now();
    }

    public List<Route> getAddedRoutes() {
        return addedRoutes;
    }

    public LocalDate getDateEmployee() {
        return dateEmployee;
    }

    public void addRoutes(Route route){
        addedRoutes.add(route);
    }

    @Override
    public String printProfile() {
        return "name: " + this.getFullName() +
                "\naddedRoutes: " +
                //TODO create a method to show all routes (get from the routeservice)(maybe add to interface)
                "\ndate employee: " + this.dateEmployee;
    }

    //this will show the routes in descending order by date
    @Override
    public int chooseRoute() {
        return 0;
    }

    @Override
    public void showImage() {

    }
}
