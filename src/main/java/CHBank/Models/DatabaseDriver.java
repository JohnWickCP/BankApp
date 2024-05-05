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

    /* Client Section */

    public ResultSet getClientData(String pAddress, String pPassword) {
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

    public ResultSet getTransactions(String pAddress, int limit){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Transactions WHERE Sender = '"+pAddress+"' OR Receiver = '"+pAddress+"' LIMIT "+limit+";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public double getSavingsBalance(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        double balance = 0;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = '"+pAddress+"';");
            balance = resultSet.getDouble("Balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public double getCheckingBalance(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        double balance = 0;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner = '"+pAddress+"';");
            balance= resultSet.getDouble("Balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    // Phương thức + or - theo dấu
    public void updateCheckingBalance(String pAddress, double amount, String operation) {
        Statement statement;
        ResultSet resultSet;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner = '"+pAddress+"';");
            double newBalance;
            if (operation.equals("ADD")) {
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE CheckingAccounts SET Balance = "+newBalance+" WHERE Owner = '"+pAddress+"';");
            } else {
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE CheckingAccounts SET Balance = "+newBalance+" WHERE Owner = '"+pAddress+"';");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSavingsBalance(String pAddress, double amount, String operation) {
        Statement statement;
        ResultSet resultSet;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = '"+pAddress+"';");
            double newBalance;
            if (operation.equals("ADD")) {
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE SavingsAccounts SET Balance = "+newBalance+" WHERE Owner = '"+pAddress+"';");
            } else {
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE SavingsAccounts SET Balance = "+newBalance+" WHERE Owner = '"+pAddress+"';");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create and record new trans
    public void newTransactions(String sender, String receiver, double amount, String message) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            LocalDate date = LocalDate.now();
            statement.executeUpdate("INSERT INTO " +
                    "Transactions(Sender, Receiver, Amount, Date, Message)"+
                    "VALUES ('"+sender+"', '"+receiver+"', "+amount+", '"+date+"', '"+message+"');");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet searchTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            String query = "SELECT * FROM Transactions WHERE Date BETWEEN '" +
                    startDate.toString() + "' AND '" + endDate.toString() + "'";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e){
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

        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Clients(FirstName, LastName, PayeeAddress, Password, Date)" +
                    "VALUES ('"+firstName+"', '"+lastName+"', '"+address+"', '"+password+"', '"+date.toString()+"');");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void createCheckingAccount(String owner, String number, double tLimit, double balance){

        try{
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, tLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance){

        try{
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, wLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
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


    public void depositSavings(String pAddress, double amount){
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("UPDATE SavingsAccounts SET Balance = "+amount+" WHERE Owner = '"+pAddress+"';");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteClient(String pAddress) {
        try {

            PreparedStatement deleteClientStatement = connection.prepareStatement("DELETE FROM Clients WHERE PayeeAddress = ?");
            deleteClientStatement.setString(1, pAddress);
            deleteClientStatement.executeUpdate();

            PreparedStatement deleteSavingsStatement = connection.prepareStatement("DELETE FROM SavingsAccounts WHERE Owner = ?");
            deleteSavingsStatement.setString(1, pAddress);
            deleteSavingsStatement.executeUpdate();

            PreparedStatement deleteCheckingStatement = connection.prepareStatement("DELETE FROM CheckingAccounts WHERE Owner = ?");
            deleteCheckingStatement.setString(1, pAddress);
            deleteCheckingStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* Utility Methods */
    public ResultSet searchClient(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress = '"+pAddress+"' ;");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

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
