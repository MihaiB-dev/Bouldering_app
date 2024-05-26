package Bouldering_app.databaseConnections;

import Bouldering_app.domain.Climber;
import Bouldering_app.domain.Password_hashing;
import Bouldering_app.domain.Setter;
import Bouldering_app.domain.User;
import config.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseUser {
    private static DatabaseUser databaseUser = null;

    private DatabaseUser(){

    }
    public static DatabaseUser DatabaseUser(){
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
        //firstly create a new Stats row
        String insertStats = "INSERT INTO stats (strength, techinque, endurance, flexibility) VALUES (0,0,0,0)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertStats);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //add a basic stats in the database
        String selectStats = "SELECT id FROM stats ORDER BY id DESC LIMIT 1";
        int id_stats = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectStats);
            preparedStatement.executeQuery();
            preparedStatement.getResultSet().next();
            id_stats = preparedStatement.getResultSet().getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //finaly add the Climber
        String insertClimber = "INSERT INTO climber (id_user, avgGrade, userStats) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertClimber);
            preparedStatement.setInt(1, id_user);
            preparedStatement.setString(2, "4");
            preparedStatement.setInt(3, id_stats);
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
                    String selectClimber = "SELECT user.fullName, user.hashPassword, climber.avgGrade, climber.userStats " +
                            "FROM climber JOIN user ON climber.id_user = user.id " +
                            "WHERE climber.id_user = ?";
                    preparedStatement = conn.prepareStatement(selectClimber);
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeQuery();

                    java.sql.ResultSet resultSet = preparedStatement.getResultSet();

                    if (resultSet.next()){
                        return new Climber(resultSet.getString("fullName"), resultSet.getString("hashPassword"), resultSet.getString("avgGrade"), resultSet.getInt("userStats"));
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
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

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
}
