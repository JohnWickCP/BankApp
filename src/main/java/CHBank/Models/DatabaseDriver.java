package CHBank.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver() {
        try{
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void openConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                //System.out.println("Connection is already open.");
            } else {
                this.connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                //System.out.println("Connection opened successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                //System.out.println("Connection closed successfully.");
            } else {
                //System.out.println("Connection is already closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* Client Section */

    public ResultSet getClientData(String pAddress, String pPassword) {
        openConnection();
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress = '"+pAddress+"' AND Password ='"+pPassword+"';");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return resultSet;
    }



    /* Admin Section */

    public ResultSet getAdminData(String username, String pPassword) {
        openConnection();
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Admins WHERE Username = '"+username+"' AND Password ='"+pPassword+"';");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return resultSet;
    }

    public void creteClient(String firstName, String lastName, String address, String password, LocalDate date) {
        openConnection();
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Clients(FirstName, LastName, PayeeAddress, Password, Date)" +
                    "VALUES ('"+firstName+"', '"+lastName+"', '"+address+"', '"+password+"', '"+date.toString()+"');");
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance){
        openConnection();
        try{
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, tLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance){
        openConnection();
        try{
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, wLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    /* Utility Methods */
    public int getLastClientId(){
        openConnection();
        Statement statement;
        ResultSet resultSet ;
        int id = 0 ;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM sqlite_sequence WHERE name = 'Clients';");
            id = resultSet.getInt("seq");
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return id;
    }
}
