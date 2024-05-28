package Bouldering_app.databaseConnections;

import Bouldering_app.domain.Grade;
import Bouldering_app.domain.Route;
import Bouldering_app.domain.Stats;
import config.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRoute {
    //create a singleton
    private static DatabaseRoute databaseRoute = null;
    private DatabaseStats databaseStats = DatabaseStats.DatabaseStats();
    private DatabaseRoute() {
    }

    public static DatabaseRoute getDatabaseRoute() {
        if (databaseRoute == null) {
            databaseRoute = new DatabaseRoute();
        }
        return databaseRoute;
    }

    public int getLastId() {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectUser = "SELECT id FROM route ORDER BY id DESC LIMIT 1";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.executeQuery();
            if (preparedStatement.getResultSet().next()) {
                return preparedStatement.getResultSet().getInt("id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public void insertRoute(Route newRoute, int id_setter) {
        //insert the route in the database
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        //create in the database stats
        int statsId = databaseStats.createStats(newRoute.getRoutestats());
        String insertRouteSql = "INSERT INTO route (originalGrade, path, nrAttempts, liveGrade, dateAdded, routeStats, id_setter)VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertRouteSql);
            preparedStatement.setString(1, newRoute.getOriginalGrade().toString());
            preparedStatement.setString(2, newRoute.getPath());
            preparedStatement.setInt(3, newRoute.getNrAttempts());
            preparedStatement.setString(4, newRoute.getLiveGrade().toString());
            preparedStatement.setDate(5, Date.valueOf(newRoute.getDateAdded()));
            preparedStatement.setInt(6, statsId);
            preparedStatement.setInt(7, id_setter);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeRoute(int routeIndex) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String deleteRouteSql = "DELETE FROM route WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteRouteSql);
            preparedStatement.setInt(1, routeIndex);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Route> getRoutes() {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String selectSql = "SELECT * FROM route";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.executeQuery();
            java.sql.ResultSet resultSet = preparedStatement.getResultSet();
            List<Route> routes = new ArrayList<>();
            while (resultSet.next()) {
                String selectStats = "SELECT * FROM stats WHERE id = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(selectStats);
                preparedStatement1.setInt(1, resultSet.getInt("routeStats"));
                preparedStatement1.executeQuery();
                java.sql.ResultSet resultSet1 = preparedStatement1.getResultSet();
                Route route = null;
                if (resultSet1.next()) {
                    //get values from the stats and add them to the route constructor
                    route = new Route(resultSet.getString("originalGrade"),
                            resultSet.getString("path"),
                            resultSet.getInt("nrAttempts"),
                            resultSet.getString("liveGrade"),
                            resultSet.getDate("dateAdded").toLocalDate(),
                            new Stats(resultSet1.getDouble("strength"),
                                    resultSet1.getDouble("techinque"),
                                    resultSet1.getDouble("endurance"),
                                    resultSet1.getDouble("flexibility")
                            ));
                }
                routes.add(route);
            }
            return routes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Route getRouteById(int id) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String selectSql = "SELECT * FROM route WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
            java.sql.ResultSet resultSet = preparedStatement.getResultSet();



            if (resultSet.next()) {
                String selectStats = "SELECT * FROM stats WHERE id = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(selectStats);
                preparedStatement1.setInt(1, resultSet.getInt("routeStats"));
                preparedStatement1.executeQuery();
                java.sql.ResultSet resultSet1 = preparedStatement1.getResultSet();
                resultSet1.next();
                //create list of routes and return it
                Route route = new Route(resultSet.getString("originalGrade"),
                        resultSet.getString("path"),
                        resultSet.getInt("nrAttempts"),
                        resultSet.getString("liveGrade"),
                        resultSet.getDate("dateAdded").toLocalDate(),
                        new Stats(resultSet1.getDouble("strength"),
                        resultSet1.getDouble("techinque"),
                        resultSet1.getDouble("endurance"),
                        resultSet1.getDouble("flexibility")
                ));
                return route;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //find how many attempts are in the route and add on it the i value
    public void addAttempts(int routeIndex, int i) {

        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String selectSql = "SELECT nrAttempts FROM route WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, routeIndex);
            preparedStatement.executeQuery();
            java.sql.ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                int nrAttempts = resultSet.getInt("nrAttempts");
                String updateSql = "UPDATE route SET nrAttempts = ? WHERE id = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(updateSql);
                preparedStatement1.setInt(1, nrAttempts + i);
                preparedStatement1.setInt(2, routeIndex);
                preparedStatement1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLiveGrade(int routeIndex, Grade value) {
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String updateSql = "UPDATE route SET liveGrade = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, value.toString());
            preparedStatement.setInt(2, routeIndex);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Grade> getUserGrades(int routeIndex) {
        //get all the ascents with the routeIndex id_route, then take all the grades of the users that have done that route
        Connection connection = DatabaseConfiguration.getDatabaseConnection();
        String selectSql = "SELECT climber.avgGrade FROM ascent JOIN climber ON ascent.id_climber = climber.id WHERE ascent.id_route = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, routeIndex);
            preparedStatement.executeQuery();
            java.sql.ResultSet resultSet = preparedStatement.getResultSet();
            List<Grade> userGrades = new ArrayList<>();
            while (resultSet.next()) {
                userGrades.add(Grade.valueOf("_" + resultSet.getString("avgGrade")));
            }
            return userGrades;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
