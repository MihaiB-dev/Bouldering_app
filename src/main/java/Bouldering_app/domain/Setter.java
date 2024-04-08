package Bouldering_app.domain;
import java.time.LocalDate; // import the LocalDate class
import java.util.ArrayList;
import java.util.List;

public class Setter extends User{
    private List<Route> addedRoutes;
    private LocalDate dateEmployee;

    public Setter(String full_name, String hashPassword) {
        super(full_name, hashPassword);
        this.addedRoutes  = new ArrayList<>();
        this.dateEmployee = LocalDate.now();
    }

    public List<Route> getAddedRoutes() {
        return addedRoutes;
    }

    public LocalDate getDateEmployee() {
        return dateEmployee;
    }
}
