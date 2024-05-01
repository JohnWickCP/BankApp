package CHBank.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private static final String DATABASE_URL = "jdbc:sqlite:bank.db";
    private Connection connection;

    public DatabaseDriver() {
        openConnection();
    }

    private void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open connection to database.", e);
        }
    }

    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close connection to database.", e);
        }
    }

    private void executeStatement(String sql) throws SQLException {
        openConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
    /* Client Section */

    public ResultSet getClientData(String pAddress, String pPassword) {
      //  openConnection();
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress = '"+pAddress+"' AND Password ='"+pPassword+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }



    /* Admin Section */

    public ResultSet getAdminData(String username, String pPassword) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Admins WHERE Username = '"+username+"' AND Password ='"+pPassword+"';");
        } catch (Exception e){
            e.printStackTrace();
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

    public ResultSet getAllClientsData(){

        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /* Utility Methods */
    public int getLastClientId(){
        Statement statement;
        ResultSet resultSet ;
        int id = 0 ;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM sqlite_sequence WHERE name = 'Clients';");
            id = resultSet.getInt("seq");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public ResultSet getCheckingAccountData(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT  * FROM CheckingAccounts WHERE Owner = '"+pAddress+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccountData(String pAddress){

        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT  * FROM SavingsAccounts WHERE Owner = '"+pAddress+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
