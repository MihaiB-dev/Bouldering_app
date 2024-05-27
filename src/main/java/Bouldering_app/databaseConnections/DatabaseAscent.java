package Bouldering_app.databaseConnections;

import Bouldering_app.domain.Ascent;
import Bouldering_app.domain.Route;
import config.DatabaseConfiguration;

import java.sql.Connection;

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
}
