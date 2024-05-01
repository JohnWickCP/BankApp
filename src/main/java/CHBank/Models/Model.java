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


    // Client Data Section
    private final Client client;
    private boolean clientLoginSuccessFlag;

    // Admin Data section
    private boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;

    private Model(){
        this.databaseDriver = new DatabaseDriver();
        this.view = new View();

        // Client Data Section

        this.clientLoginSuccessFlag = false;
        this.client =  new Client("", "", "", null, null, null);

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
}
