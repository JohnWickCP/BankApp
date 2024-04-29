package CHBank.Models;

import CHBank.Views.AccountType;
import CHBank.Views.View;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private final View view;
    private final DatabaseDriver databaseDriver;
    private AccountType loginAccountType = AccountType.CLIENT;

    // Client Data Section
    private final Client client;
    private boolean clientLoginSuccessFlag;

    private Model(){
        this.databaseDriver = new DatabaseDriver();
        this.view = new View();
        // Client Data Section
        this.clientLoginSuccessFlag = false;
        this.client =  new Client("", "", "", null, null, null);
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

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    /* Client Method Section
     */
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
                this.clientLoginSuccessFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
