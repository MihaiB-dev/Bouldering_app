package Bouldering_app.databaseConnections;

import Bouldering_app.domain.*;
import config.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUser {
    private static DatabaseUser databaseUser = null;

    private DatabaseUser(){

    }
    public static DatabaseUser getDatabaseUser(){
        if (databaseUser == null){
            databaseUser = new DatabaseUser();
        }
        return databaseUser;
    }

    public void insertUser(String fullName, String hashPassword){
        String insertUser = "INSERT INTO user (fullName, hashPassword) VALUES (?,?)";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertUser);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, hashPassword);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertClimber(String fullName, String hashPassword, int id_user) {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        insertUser(fullName, hashPassword);

        int idStats = DatabaseStats.DatabaseStats().createStats();
        //finaly add the Climber
        String insertClimber = "INSERT INTO climber (id_user, avgGrade, userStats) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertClimber);
            preparedStatement.setInt(1, id_user);
            preparedStatement.setString(2, "4");
            preparedStatement.setInt(3, idStats);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSetter(String fullName, String hashPassword, int id_user){
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        insertUser(fullName, hashPassword);

        String insertClimber = "INSERT INTO setter (id_user, dateEmployee) VALUES (?,?)";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(insertClimber);
            preparedStatement.setInt(1,id_user);
            preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getById(int id){
        //get user by id
        //find if it is a climber or a setter and create the object accordingly
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectUser = "SELECT * FROM user WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
            if (preparedStatement.getResultSet().next()){

                if (verifyIfClimber(id)){
                    // Join users and climber tables on id_user
                    String selectClimber = "SELECT climber.id, user.fullName, user.hashPassword, climber.avgGrade, climber.userStats " +
                            "FROM climber JOIN user ON climber.id_user = user.id " +
                            "WHERE climber.id_user = ?";
                    preparedStatement = conn.prepareStatement(selectClimber);
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeQuery();

                    java.sql.ResultSet resultSet = preparedStatement.getResultSet();

                    if (resultSet.next()){
                        return new Climber(resultSet.getString("fullName"),
                                resultSet.getString("hashPassword"),
                                resultSet.getString("avgGrade"),
                                resultSet.getInt("userStats"),
                                resultSet.getInt("id")
                        );
                    }

                } else {
                    // Join users and setter tables on id_user
                    String selectSetter = "SELECT user.fullName, user.hashPassword, setter.dateEmployee " +
                            "FROM setter JOIN user ON setter.id_user = user.id " +
                            "WHERE setter.id_user = ?";

                    preparedStatement = conn.prepareStatement(selectSetter);
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeQuery();

                    java.sql.ResultSet resultSet = preparedStatement.getResultSet();

                    if (resultSet.next()){
                        return new Setter(resultSet.getString("fullName"), resultSet.getString("hashPassword"), resultSet.getDate("dateEmployee").toLocalDate());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    private boolean verifyIfClimber(int id){
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectClimber = "SELECT * FROM climber WHERE id_user = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectClimber);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
            return preparedStatement.getResultSet().next(); //if it finds something in database, it is a climber
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int authenticate(Password_hashing p, String fullName, char[] hashedPassword) {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectUser = "SELECT * FROM user WHERE fullName = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.setString(1, fullName);
            preparedStatement.executeQuery();
            if (preparedStatement.getResultSet().next()){
                String hashPassword = preparedStatement.getResultSet().getString("hashPassword");

                if (p.authenticate(hashedPassword, hashPassword)){
                    return preparedStatement.getResultSet().getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getLastId() {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectUser = "SELECT id FROM user ORDER BY id DESC LIMIT 1";
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

    public boolean verifyDuplicateName(String fullName) {
        //search in all users and see if the name is unique
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectUser = "SELECT * FROM user WHERE fullName = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.setString(1, fullName);
            preparedStatement.executeQuery();
            return preparedStatement.getResultSet().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void showAllUsers() {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectUser = "SELECT id, fullName FROM user";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()){
                System.out.println(resultSet.getInt("id") + " " + resultSet.getString("fullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String fullName) {
        //delete the stats of the user (we have to find if it is climber or not)(make a big query)


        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        //if it is a climber, we delete the stats of the climber, for setter we do no preprocessing
        String deleteStats = "DELETE FROM stats WHERE id = (SELECT userStats FROM climber WHERE id_user = (SELECT id FROM user WHERE fullName = ?))";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteStats);
            preparedStatement.setString(1, fullName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String deleteUser = "DELETE FROM user WHERE fullName = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteUser);
            preparedStatement.setString(1, fullName);
            int deleted = preparedStatement.executeUpdate();
            if(deleted == 0){
                System.out.println("User not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(String fullName, String hash) {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String updatePassword = "UPDATE user SET hashPassword = ? WHERE fullName = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(updatePassword);
            preparedStatement.setString(1, hash);
            preparedStatement.setString(2, fullName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getIdSetter(User setter){
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        //create a join between setter and user and chose setter id by the name of the user
        String selectUser = "SELECT setter.id FROM setter JOIN user ON setter.id_user = user.id WHERE user.fullName = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.setString(1, setter.getFullName());
            preparedStatement.executeQuery();
            if (preparedStatement.getResultSet().next()) {

                return preparedStatement.getResultSet().getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int getIdClimber(User climber) {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        //create a join between climber and user and chose climber id by the name of the user
        String selectUser = "SELECT climber.id FROM climber JOIN user ON climber.id_user = user.id WHERE user.fullName = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.setString(1, climber.getFullName());
            preparedStatement.executeQuery();
            if (preparedStatement.getResultSet().next()) {

                return preparedStatement.getResultSet().getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getIdClimber(String name){
        //get the id of the climber by the fullName user
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectUser = "SELECT climber.id FROM climber JOIN user ON climber.id_user = user.id WHERE user.fullName = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectUser);
            preparedStatement.setString(1, name);
            preparedStatement.executeQuery();
            if (preparedStatement.getResultSet().next()) {
                return preparedStatement.getResultSet().getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public List<Route> getSetterRoutes(int setter_id) {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectRoutes = "SELECT * FROM route WHERE id_setter = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectRoutes);
            preparedStatement.setInt(1, setter_id);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<Route> routes = new ArrayList<>();
            while (resultSet.next()){
                //create list of routes and return it
                Route route = new Route(resultSet.getString("originalGrade"),
                                        resultSet.getString("path"),
                                        resultSet.getInt("nrAttempts"),
                                        resultSet.getString("liveGrade"),
                                        resultSet.getDate("dateAdded").toLocalDate());

                routes.add(route);
            }
            return routes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAvgGrade(int idClimber, Grade value) {
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String updateAvgGrade = "UPDATE climber SET avgGrade = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(updateAvgGrade);
            preparedStatement.setString(1, value.toString());
            preparedStatement.setInt(2, idClimber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Grade> getAllClimberGrades(int idClimber) {
        //get all the grades from the ascents of the climber
        Connection conn = DatabaseConfiguration.getDatabaseConnection();
        String selectGrades = "SELECT route.originalGrade FROM ascent JOIN route ON ascent.id_route = route.id WHERE ascent.id_climber = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectGrades);
            preparedStatement.setInt(1, idClimber);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<Grade> grades = new ArrayList<>();
            while (resultSet.next()){
                grades.add(Grade.valueOf("_" + resultSet.getString("originalGrade")));
            }
            return grades;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
