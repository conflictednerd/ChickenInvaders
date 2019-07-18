package com.saeed.database;

import Base.Player;

import java.io.Closeable;
import java.sql.*;

public class Database implements Closeable {
    private String dbURL = "jdbc:mysql://localhost:3306", dbUsername = "root", dbPassword = "dbpass1234567890";
    private final String databaseName = "gameDB", playersTable = "players", rankingTable = "ranking";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String sql = "";

    public Database(){
        //check for mySQL database driver.
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

        try {
            connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            statement = connection.createStatement();
            createDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean playerIsRegistered(Player player) throws SQLException {
        sql = "SELECT * FROM " +
                playersTable +
                " WHERE name = \"" +
                player.name +
                "\";";

        resultSet = statement.executeQuery(sql);
        return resultSet.next();
    }

    public void addPlayer(Player player) throws SQLException {
        if(playerIsRegistered(player)) return;
        sql = "INSERT INTO `" +
                playersTable +
                "` (`name`, `json`) VALUES ('" + player.name + "'" +
                ", '" + player.StoJson(player) + "');";
//        System.out.println(sql);
        statement.executeUpdate(sql);
    }

    public Player selectPlayer(String name) throws SQLException {
        sql = "SELECT * FROM " +
                playersTable +
                " WHERE name = '" +
                name +
                "';";
        resultSet = statement.executeQuery(sql);
        //If player didn't exist in database return null.
        if(!resultSet.next()) return null;
        Player p = Player.SfromJson(resultSet.getString("json"));
//        System.out.println("Data read from database: ");
//        System.out.println(resultSet.getString("json"));
//        System.out.println("Player created using database info:");
//        System.out.println(Player.StoJson(p));
        p.name = name;
        p.waitingForShotCooldown = false;
        p.coolDownTimer = 0l;
        p.shootingTimer = null;
        p.timeOfLastShot = 0l;
        p.shotHeat = 0;
        p.maxHeat = 100;
        return p;
    }

    private void createDB() throws SQLException {
        sql = "SHOW DATABASES;";
        boolean exists = false;

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String name = resultSet.getString("Database");
            if(name.equals(databaseName)){
                exists = true;
                break;
            }
        }

        if(!exists){
            sql = "CREATE DATABASE " +
                    databaseName +
                    ";";
            statement.executeUpdate(sql);
        }

        sql = "USE " +
                databaseName +
                ";";
        statement.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS " +
                playersTable +
                "(id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "json JSON);";
        statement.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS " +
                rankingTable +
                "(id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "json JSON);";
        statement.executeUpdate(sql);
    }

    @Override
    public void close() {
        System.out.println("Database closed.");
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayers(Player player) throws SQLException {
        //just a safety net.
        if(!playerIsRegistered(player)){
            addPlayer(player);
            return;
        }
        sql = "UPDATE `" + playersTable + "` " +
                "SET `json` = '" + Player.StoJson(player) + "' " +
                "WHERE `name` = '" + player.name + "'" +
                ";";
        statement.executeUpdate(sql);
    }

    public void addPlayerToRanking(Player player) throws SQLException {
        sql = "INSERT INTO `" +
                rankingTable +
                "` (`name`, `json`) VALUES ('" + player.name + "'" +
                ", '" + player.StoJson(player) + "');";
//        System.out.println(sql);
        statement.executeUpdate(sql);
    }
}
