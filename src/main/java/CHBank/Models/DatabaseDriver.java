package CHBank.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private static final String DATABASE_URL = "jdbc:sqlite:bank.db";
    private Connection conn;

    public DatabaseDriver() {openConnection();}

    private void openConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DATABASE_URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open connection to database",e);
        }
    }

    private void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e){
            throw new RuntimeException("Failed to close connection to database",e);
        }
    }

    // CLIENT
    public ResultSet getClientData(String pAddress, String pPassword){
        PreparedStatement ps ;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ? ";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            ps.setString(2, pPassword);
            rs = ps.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getTransactions(String pAddress, int limit){
        PreparedStatement ps ;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM Transactions WHERE Sender = ? OR Receiver = ? OrDER BY ID DESC LIMIT ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress );
            ps.setString(2, pAddress);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public double getSavingsBalance(String pAddress){
        PreparedStatement ps ;
        ResultSet rs ;
        double result = 0;
        try {
            String query = "SELECT Balance FROM SavingsAccounts WHERE OWNER = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getDouble("Balance");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public double getCheckingBalance(String pAddress){
        PreparedStatement ps;
        ResultSet rs;
        double result = 0;
        try {
            String query = "SELECT Balance FROM CheckingAccounts WHERE OWNER = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getDouble("Balance");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public void updateCheckingBalance(String pAddress, double amount, String operation){
        PreparedStatement ps ;
        PreparedStatement psUpdate ;
        ResultSet rs ;
        try {
            String query = "SELECT Balance FROM CheckingAccounts WHERE OWNER = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            rs = ps.executeQuery();
            if(rs.next()){
                double currBalance = rs.getDouble("Balance");
                double newBalance ;
                if(operation.equals("ADD")){
                    newBalance = currBalance + amount;
                    String updateQuery = "UPDATE CheckingAccounts SET Balance = ? WHERE OWNER = ?";
                    psUpdate= this.conn.prepareStatement(updateQuery);
                    psUpdate.setDouble(1, newBalance);
                    psUpdate.setString(2, pAddress);
                    psUpdate.executeUpdate();
                }
                else {
                    if (currBalance >= amount){
                        newBalance = currBalance - amount;
                        String updateQuery = "UPDATE CheckingAccounts SET Balance = ? WHERE OWNER = ?";
                        psUpdate= this.conn.prepareStatement(updateQuery);
                        psUpdate.setDouble(1, newBalance);
                        psUpdate.setString(2, pAddress);
                        psUpdate.executeUpdate();
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateSavingsBalance(String pAddress, double amount, String operation){
        PreparedStatement ps;
        PreparedStatement psUpdate ;
        ResultSet rs ;
        try {
            String query = "SELECT Balance FROM SavingsAccounts WHERE OWNER = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            rs = ps.executeQuery();
            if(rs.next()) {
                double currBalance = rs.getDouble("Balance");
                double newBalance;
                if (operation.equals("ADD")) {
                    newBalance = currBalance + amount;
                    String updateQuery = "UPDATE SavingsAccounts SET Balance = ? WHERE OWNER = ?";
                    psUpdate = this.conn.prepareStatement(updateQuery);
                    psUpdate.setDouble(1, newBalance);
                    psUpdate.setString(2, pAddress);
                    psUpdate.executeUpdate();
                } else {
                    if (currBalance >= amount) {
                        newBalance = currBalance - amount;
                        String updateQuery = "UPDATE SavingsAccounts SET Balance = ? WHERE OWNER = ?";
                        psUpdate = this.conn.prepareStatement(updateQuery);
                        psUpdate.setDouble(1, newBalance);
                        psUpdate.setString(2, pAddress);
                        psUpdate.executeUpdate();
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Create and record new Transaction
    public void newTransaction(String sender, String receiver, double amount, String message) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
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
            statement = this.conn.createStatement();
            String query = "SELECT * FROM Transactions WHERE Date BETWEEN '" +
                    startDate.toString() + "' AND '" + endDate.toString() + "'";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }


    // ADMIN
    public ResultSet getAdminData(String username, String password){
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM Admins WHERE Username = ? AND Password = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public void creteClient(String firstName, String lastName, String address, String password, LocalDate date) {
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Clients(FirstName, LastName, PayeeAddress, Password, Date)" +
                    "VALUES ('"+firstName+"', '"+lastName+"', '"+address+"', '"+password+"', '"+date.toString()+"');");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        try {
            PreparedStatement statement = this.conn.prepareStatement("INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, tLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {
        try {
            PreparedStatement statement = this.conn.prepareStatement("INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, wLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllClientsData(){
        PreparedStatement ps ;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM Clients";
            ps = this.conn.prepareStatement(query);
            rs = ps.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public void depositSavings(String pAddress, double amount) {
        PreparedStatement ps ;
        try {
            String query = "UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?";
            ps = this.conn.prepareStatement(query);
            ps.setDouble(1, amount);
            ps.setString(2, pAddress);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(String pAddress) {
        try {

            PreparedStatement deleteClientStatement = conn.prepareStatement("DELETE FROM Clients WHERE PayeeAddress = ?");
            deleteClientStatement.setString(1, pAddress);
            deleteClientStatement.executeUpdate();

            PreparedStatement deleteSavingsStatement = conn.prepareStatement("DELETE FROM SavingsAccounts WHERE Owner = ?");
            deleteSavingsStatement.setString(1, pAddress);
            deleteSavingsStatement.executeUpdate();

            PreparedStatement deleteCheckingStatement = conn.prepareStatement("DELETE FROM CheckingAccounts WHERE Owner = ?");
            deleteCheckingStatement.setString(1, pAddress);
            deleteCheckingStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Others
    public ResultSet searchClient(String pAddress) {
        PreparedStatement ps ;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM Clients WHERE PayeeAddress = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int getLastClientId() {
        Statement statement ;
        ResultSet resultSet ;
        int id = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'Clients';");
            if (resultSet.next()) {
                id = resultSet.getInt("seq");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public ResultSet getCheckingAccountData(String pAddress) {
        PreparedStatement ps ;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM CheckingAccounts WHERE Owner = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getSavingsAccountData(String pAddress) {
        PreparedStatement ps ;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM SavingsAccounts WHERE Owner = ?";
            ps = this.conn.prepareStatement(query);
            ps.setString(1, pAddress);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int getTransactionLimit(String pAddress) {
        int tLimit = 0;
        try {
            PreparedStatement statement = this.conn.prepareStatement("SELECT TransactionLimit FROM CheckingAccounts WHERE Owner = ?");
            statement.setString(1, pAddress);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                tLimit = resultSet.getInt("TransactionLimit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tLimit;
    }

    public double getWithdrawalLimit(String pAddress) {
        double withdrawalLimit = 0;
        try {
            PreparedStatement statement = this.conn.prepareStatement("SELECT WithdrawalLimit FROM SavingsAccounts WHERE Owner = ?");
            statement.setString(1, pAddress);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                withdrawalLimit = resultSet.getDouble("WithdrawalLimit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return withdrawalLimit;
    }

    public void updateTransactionLimit(String pAddress, int newLimit) {
        try {
            PreparedStatement statement = this.conn.prepareStatement("UPDATE CheckingAccounts SET TransactionLimit = ? WHERE Owner = ?");
            statement.setInt(1, newLimit);
            statement.setString(2, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTransactionLimitAsDefault(){
        try {
            PreparedStatement ps = this.conn.prepareStatement("UPDATE CheckingAccounts SET TransactionLimit = ?");
            ps.setInt(1, 10);
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateWithdrawalLimit(String pAddress, double newLimit) {
        try {
            PreparedStatement statement = this.conn.prepareStatement("UPDATE SavingsAccounts SET WithdrawalLimit = ? WHERE Owner = ?");
            statement.setDouble(1, newLimit);
            statement.setString(2, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setWithdrawalLimitAsDefault(){
        try {
            PreparedStatement ps = this.conn.prepareStatement("UPDATE SavingsAccounts SET WithdrawalLimit = ?");
            ps.setDouble(1, 2000);
            ps.executeUpdate();
        }   catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getCheckingAccountDataByAccountNumber(String accountNumber)  {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM CheckingAccounts WHERE AccountNumber = ?";
            statement = this.conn.prepareStatement(query);
            statement.setString(1, accountNumber);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccountDataByAccountNumber(String accountNumber)  {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM SavingsAccounts WHERE AccountNumber = ?";
            statement = this.conn.prepareStatement(query);
            statement.setString(1, accountNumber);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public boolean isAccountExists(String accountNumber, String accountType) {
        boolean exists = false;
        try {
            ResultSet resultSet = null;
            if (accountType.equals("Checking")) {
                resultSet = getCheckingAccountDataByAccountNumber(accountNumber);
            } else if (accountType.equals("Savings")) {
                resultSet = getSavingsAccountDataByAccountNumber(accountNumber);
            }
            if (resultSet != null && resultSet.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

}
