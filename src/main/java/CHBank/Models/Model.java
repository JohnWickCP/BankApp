package CHBank.Models;

import CHBank.Views.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private final View view;
    private final DatabaseDriver databaseDriver;


    // Client Data Section
    private final Client client;
    private boolean clientLoginSuccessFlag;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;

    // Admin Data section
    private boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;


    private Model(){
        this.databaseDriver = new DatabaseDriver();
        this.view = new View();

        // Client Data Section

        this.clientLoginSuccessFlag = false;
        this.client =  new Client("", "", "", null, null, null);
        this.latestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();

        // Admin Data Section

        this.adminLoginSuccessFlag = false;
        this.clients = FXCollections.observableArrayList();
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public View getView(){
        return view;
    }
    public DatabaseDriver getDatabaseDriver() {return databaseDriver;}


    /* Client Method Section */
    public boolean getClientLoginSuccessFlag() {return clientLoginSuccessFlag;}

    public void setClientLoginSuccessFlag(boolean flag) {this.clientLoginSuccessFlag = flag;}

    public Client getClient() {return client;}

    public void evaluatedClientCred(String pAddress, String pPassword){
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet = databaseDriver.getClientData(pAddress, pPassword);
        try {
            if(resultSet.isBeforeFirst()){
                this.client.FirstName().set(resultSet.getString("FirstName"));
                this.client.LastName().set(resultSet.getString("LastName"));
                this.client.pAddress().set(resultSet.getString("PayeeAddress"));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.client.DateCreated().set(date);
                checkingAccount = getCheckingAccount(pAddress);
                savingAccount = getSavingsAccount(pAddress);
                this.client.CheckingAccount().set(checkingAccount);
                this.client.SavingAccount().set(savingAccount);
                this.clientLoginSuccessFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareTransactions(ObservableList<Transaction> transactions, int limit){
        ResultSet resultSet = databaseDriver.getTransactions(this.client.pAddress().get(), limit);
        try {
            while (resultSet.next()){
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                double amount = resultSet.getDouble("Amount");
                String[] dateParts  =resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String message = resultSet.getString("Message");
                transactions.add(new Transaction(sender, receiver, amount, date, message));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setLatestTransactions(){
        prepareTransactions(this.latestTransactions, 4);
    }

    public ObservableList<Transaction> getLatestTransactions(){
        return latestTransactions;
    }

    public void setAllTransactions(){
        prepareTransactions(this.allTransactions, -1);
    }

    public ObservableList<Transaction> getAllTransactions(){
        return allTransactions;
    }

    public ObservableList<Transaction> searchTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        ObservableList<Transaction> searchResults = FXCollections.observableArrayList();
        ResultSet resultSet = databaseDriver.searchTransactionsBetweenDates(startDate, endDate);
        try {
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

    // Admin method section
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
        CheckingAccount checkingAccount;
        SavingAccount savingAccount;
        ResultSet resultSet = databaseDriver.getAllClientsData();

        try {
            while (resultSet.next()){
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("lastname");
                String pAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                checkingAccount = getCheckingAccount(pAddress);
                savingAccount = getSavingsAccount(pAddress);
                clients.add(new Client(fName, lName, pAddress, checkingAccount, savingAccount, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public SavingAccount getSavingsAccount(String pAddress) {
        SavingAccount account = null;
        ResultSet resultSet = databaseDriver.getSavingsAccountData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            double wLimit = resultSet.getDouble("WithdrawalLimit");
            double balance = resultSet.getDouble("Balance");
            account = new SavingAccount(pAddress, num, balance, wLimit );
        } catch (Exception e){
            e.printStackTrace();
        }
        return account;
    }

    public ObservableList<Client> searchClients(String pAddress) {
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        ResultSet resultSet = databaseDriver.searchClient(pAddress);
        try {
            if (!resultSet.next()) {
              return searchResults;
            }
            CheckingAccount checkingAccount = getCheckingAccount(pAddress);
            SavingAccount savingAccount = getSavingsAccount(pAddress);
            String fName = resultSet.getString("Firstname");
            String lName = resultSet.getString("lastname");
            String[] dateParts = resultSet.getString("Date").split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
            searchResults.add(new Client(fName, lName, pAddress, checkingAccount, savingAccount, date));
        } catch (Exception e){
            e.printStackTrace();
        }
        return searchResults;
    }

    public ResultSet searchClient(String pAddress) {
        ResultSet resultSet = databaseDriver.searchClient(pAddress);
        try {
            if (!resultSet.next()) {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void refreshLimitations(String pAddress) {
        databaseDriver.updateTransactionLimit(pAddress, 10);
        databaseDriver.updateWithdrawalLimit(pAddress, 2000);
    }
}
