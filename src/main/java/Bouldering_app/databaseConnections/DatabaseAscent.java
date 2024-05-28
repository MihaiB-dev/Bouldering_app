package Bouldering_app.databaseConnections;

import Bouldering_app.domain.Ascent;
import Bouldering_app.domain.Climber;
import Bouldering_app.domain.Route;
import Bouldering_app.services.Tuple;
import config.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAscent {
    //make it singleton
    private static DatabaseAscent databaseAscent = null;

    private DatabaseRoute databaseRoute = DatabaseRoute.getDatabaseRoute();
    private DatabaseAscent() {
    }

    public static DatabaseAscent getDatabaseAscent() {
        if (databaseAscent == null) {
            databaseAscent = new DatabaseAscent();
        }
        return databaseAscent;
    }


    public void addAscent(int idClimber, int id_route, int attempts) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String insertAscentSql = "INSERT INTO ascent (id_climber, id_route, date,  attempts) VALUES (?, ?, ?, ?)";
        try {
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(insertAscentSql);
            preparedStatement.setInt(1, idClimber);
            preparedStatement.setInt(2, id_route);
            preparedStatement.setDate(3, date);
            preparedStatement.setInt(4, attempts);
            preparedStatement.executeUpdate();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public int getById(int idClimber, int routeIndex) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String selectAscentSql = "SELECT id FROM ascent WHERE id_climber = ? AND id_route = ?";
        try {
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(selectAscentSql);
            preparedStatement.setInt(1, idClimber);
            preparedStatement.setInt(2, routeIndex);
            preparedStatement.executeQuery();
            preparedStatement.getResultSet().next();
            return preparedStatement.getResultSet().getInt("id");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Ascent getAscent(int idAscent) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String selectAscentSql = "SELECT * FROM ascent WHERE id = ?";
        try {
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(selectAscentSql);
            preparedStatement.setInt(1, idAscent);
            preparedStatement.executeQuery();
            preparedStatement.getResultSet().next();
            return new Ascent(databaseRoute.getRouteById(preparedStatement.getResultSet().getInt("id_route")),
                    preparedStatement.getResultSet().getInt("attempts"),
                    preparedStatement.getResultSet().getDate("date").toLocalDate());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Ascent> getAscents(int idClimber) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String selectAscentSql = "SELECT * FROM ascent WHERE id_climber = ?";
        try {
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(selectAscentSql);
            preparedStatement.setInt(1, idClimber);
            preparedStatement.executeQuery();
            List<Ascent> ascents = new ArrayList<>();
            while (preparedStatement.getResultSet().next()) {
                ascents.add(new Ascent(databaseRoute.getRouteById(preparedStatement.getResultSet().getInt("id_route")),
                        preparedStatement.getResultSet().getInt("attempts"),
                        preparedStatement.getResultSet().getDate("date").toLocalDate()));
            }
            return ascents;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Tuple<Ascent, String>> getAllAscents() {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        //join ascent to route and user to get the full name and
        String selectAscentSql = "SELECT user.fullName, ascent.id_route, ascent.attempts, ascent.date " +
                                 "FROM ascent " +
                                 "JOIN climber ON ascent.id_climber = climber.id " +
                                 "JOIN user ON climber.id_user = user.id ";
        try {
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(selectAscentSql);
            preparedStatement.executeQuery();
            List<Tuple<Ascent, String>> ascents = new ArrayList<>();
            while (preparedStatement.getResultSet().next()) {
                ResultSet result = preparedStatement.getResultSet();
                Route route = databaseRoute.getRouteById(result.getInt("id_route"));
                Ascent ascent = new Ascent(route, result.getInt("attempts"),
                        result.getDate("date").toLocalDate());
                ascents.add(new Tuple<>(ascent, result.getString("fullName")));
            }
            return ascents;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteAscent(int indexAscent) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String deleteAscentSql = "DELETE FROM ascent WHERE id = ?";
        try {
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(deleteAscentSql);
            preparedStatement.setInt(1, indexAscent);
            preparedStatement.executeUpdate();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
