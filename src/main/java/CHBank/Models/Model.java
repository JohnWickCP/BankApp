package CHBank.Models;

import CHBank.Views.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private final View view;
    private final DatabaseDriver databaseDriver;

    // CLIENT
    private final Client client;
    private boolean clientLoginSuccessFlag;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;

    // ADMIN
    private boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;

    private Model(){
        this.databaseDriver = new DatabaseDriver();
        this.view = new View();

        // CLIENT DATA
        this.client = new Client("", "", "", null, null, null);
        this.clientLoginSuccessFlag = false;
        this.latestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();

        // ADMIN DATA
        this.adminLoginSuccessFlag = false;
        this.clients = FXCollections.observableArrayList();
    }

    public static Model getInstance(){
        if(model == null){model = new Model();}
        return model;
    }

    public View getView() {return view;}
    public DatabaseDriver getDatabaseDriver() {return databaseDriver;}

    // CLIENT
    public Client getClient() {return client;}

    public boolean getClientLoginSuccessFlag() {return clientLoginSuccessFlag;}
    public void setClientLoginSuccessFlag(boolean clientLoginSuccessFlag) {this.clientLoginSuccessFlag = clientLoginSuccessFlag;}

    public void evaluatedClientCred(String pAddress, String pPassword){
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = databaseDriver.getClientData(pAddress, pPassword);
        try {
            if(resultSet.isBeforeFirst()){
                this.client.fNameProperty().set(resultSet.getString("FirstName"));
                this.client.lNameProperty().set(resultSet.getString("LastName"));
                this.client.pAddressProperty().set(resultSet.getString("PayeeAddress"));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.client.dateCreatedProperty().set(date);
                checkingAccount =  getCheckingAccount(pAddress);
                savingsAccount = getSavingsAccount(pAddress);
                this.client.checkingAccountProperty().set(checkingAccount);
                this.client.savingsAccountProperty().set(savingsAccount);
                this.clientLoginSuccessFlag = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void prepareTransactions(ObservableList<Transaction> transactions, int limit) {
        try (ResultSet resultSet = databaseDriver.getTransactions(this.client.pAddressProperty().get(), limit)) {
            while (resultSet.next()) {
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                double amount = resultSet.getDouble("Amount");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String message = resultSet.getString("Message");
                transactions.add(new Transaction(sender, receiver, amount, date, message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Transaction> searchTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        ObservableList<Transaction> searchResults = FXCollections.observableArrayList();
        try (ResultSet resultSet = databaseDriver.searchTransactionsBetweenDates(startDate, endDate);){
            while (resultSet.next()) {
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                double amount = resultSet.getDouble("Amount");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String message = resultSet.getString("Message");
                searchResults.add(new Transaction(sender, receiver, amount, date, message));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return searchResults;
    }

    public ObservableList<Transaction> getLatestTransactions(){return latestTransactions;}
    public void setLatestTransactions(){
        prepareTransactions(this.latestTransactions, 4);
    }

    public ObservableList<Transaction> getAllTransactions(){
        return allTransactions;
    }
    public void setAllTransactions(){
        prepareTransactions(this.allTransactions, -1);
    }

    // ADMIN
    public boolean getAdminLoginSuccessFlag() {return adminLoginSuccessFlag;}
    public void setAdminLoginSuccessFlag(boolean flag) {this.adminLoginSuccessFlag = flag;}

    public void evaluateAdminCred(String username, String password ){
        ResultSet resultSet = databaseDriver.getAdminData(username, password);
        try {
            if (resultSet.isBeforeFirst()){
                this.adminLoginSuccessFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Client> getClients() {return clients;}
    public void setClients() {
        try (ResultSet resultSet = databaseDriver.getAllClientsData()) {
            while (resultSet.next()) {
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("lastname");
                String pAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                CheckingAccount checkingAccount = getCheckingAccount(pAddress);
                SavingsAccount savingsAccount = getSavingsAccount(pAddress);
                clients.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ObservableList<Client> searchClients(String pAddress) {
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        try (ResultSet resultSet = databaseDriver.searchClient(pAddress)) {
            if (!resultSet.next()) {
                return searchResults;
            }

            CheckingAccount checkingAccount = getCheckingAccount(pAddress);
            SavingsAccount savingAccount = getSavingsAccount(pAddress);
            String fName = resultSet.getString("Firstname");
            String lName = resultSet.getString("lastname");
            String[] dateParts = resultSet.getString("Date").split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));

            searchResults.add(new Client(fName, lName, pAddress, checkingAccount, savingAccount, date));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return searchResults;
    }

    public ResultSet searchClient(String pAddress) {
        ResultSet resultSet = databaseDriver.searchClient(pAddress);
        try {
            if (!resultSet.next()) {return null;}
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }


    public CheckingAccount getCheckingAccount(String pAddress) {
        CheckingAccount account = null;
        ResultSet resultSet = databaseDriver.getCheckingAccountData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            int tLimit = (int) resultSet.getDouble("TransactionLimit");
            double balance = resultSet.getDouble("Balance");
            account = new CheckingAccount(pAddress, num, balance, tLimit );
        } catch (Exception e){
            e.printStackTrace();
        }
        return account;
    }

    public SavingsAccount getSavingsAccount(String pAddress) {
        SavingsAccount account = null;
        ResultSet resultSet = databaseDriver.getSavingsAccountData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            double wLimit = resultSet.getDouble("WithdrawalLimit");
            double balance = resultSet.getDouble("Balance");
            account = new SavingsAccount(pAddress, num, balance, wLimit );
        } catch (Exception e){
            e.printStackTrace();
        }
        return account;
    }

    public void refreshLimitations(String pAddress) {
        databaseDriver.updateTransactionLimit(pAddress, 10);
        databaseDriver.updateWithdrawalLimit(pAddress, 2000);
    }
}

