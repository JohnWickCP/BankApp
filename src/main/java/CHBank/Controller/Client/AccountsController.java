package CHBank.Controller.Client;

import CHBank.Models.Model;
import CHBank.Views.AlertMessage;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label saving_acc_num;
    public Label withdrawal_limit;
    public Label saving_acc_date;
    public Label saving_acc_bal;
    public TextField amount_to_save;
    public Button trans_to_save_button;
    public Button trans_to_cv_button;
    public TextField amount_to_ch;

    Model mInstance = Model.getInstance();
    AlertMessage alertMessage = mInstance.getView().getAlertMessage();
    String pAddress = mInstance.getClient().pAddressProperty().get();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        trans_to_save_button.setOnAction(_ -> setTrans_to_save_button());
        trans_to_cv_button.setOnAction(_ -> setTrans_to_cv_button());
    }

    private void bindData(){
        ch_acc_bal.textProperty().bind(mInstance.getClient().checkingAccountProperty().get().balanceProperty().asString());
        ch_acc_num.textProperty().bind(mInstance.getClient().checkingAccountProperty().get().accountNumberProperty());
        transaction_limit.textProperty().bind(mInstance.getClient().checkingAccountProperty().get().transactionLimitProperty().asString());

        String dateCre = mInstance.getClient().dateCreatedProperty().get().toString();
        ch_acc_date.setText(dateCre);

        saving_acc_bal.textProperty().bind(mInstance.getClient().savingsAccountProperty().get().balanceProperty().asString());
        saving_acc_num.textProperty().bind(mInstance.getClient().savingsAccountProperty().get().accountNumberProperty());
        withdrawal_limit.textProperty().bind(mInstance.getClient().savingsAccountProperty().get().withdrawalLimitProperty().asString());

        saving_acc_date.setText(dateCre);
    }

    private void setTrans_to_save_button(){
        String amountText = amount_to_save.getText().trim();
        double amount;
        if(amountText.isEmpty()){
            alertMessage.errorMessage("Amount cannot be empty");
            return;
        }

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            alertMessage.errorMessage("Invalid amount format");
            return;
        }
        double checkingBalance = mInstance.getClient().checkingAccountProperty().get().balanceProperty().get();

        if (amount > checkingBalance){
            alertMessage.errorMessage("Insufficient balance in checking account");
            return;
        }

        int tLim = mInstance.getClient().checkingAccountProperty().get().transactionLimitProperty().get();

        if (tLim <=0){
            alertMessage.errorMessage("Transaction limit exceeded.");
            return;
        } else {
            tLim -= 1;
            mInstance.getDatabaseDriver().updateTransactionLimit(pAddress, tLim);
            mInstance.getClient().checkingAccountProperty().get().transactionLimitProperty().set(tLim);
        }

        mInstance.getDatabaseDriver().updateSavingsBalance(pAddress, amount, "ADD");
        mInstance.getDatabaseDriver().updateCheckingBalance(pAddress, amount, "SUBTRACT");
        alertMessage.successMessage("Transfer Successfully");
        mInstance.getClient().savingsAccountProperty().get().setBalance(mInstance.getDatabaseDriver().getSavingsBalance(pAddress));
        mInstance.getClient().checkingAccountProperty().get().setBalance(mInstance.getDatabaseDriver().getCheckingBalance(pAddress));

        amount_to_save.clear();
    }

    private void setTrans_to_cv_button(){
        String amountText = amount_to_ch.getText().trim();
        double amount;

        if(amountText.isEmpty()){
            alertMessage.errorMessage("Amount cannot be empty");
            return;
        }

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            alertMessage.errorMessage("Invalid amount format");
            return;
        }

        double savingsBalance = mInstance.getClient().savingsAccountProperty().get().balanceProperty().get();
        if (amount > savingsBalance){
            alertMessage.errorMessage("Insufficient balance in savings account");
            return;
        }

        double wLim = mInstance.getClient().savingsAccountProperty().get().withdrawalLimitProperty().get();
        if (wLim >= savingsBalance){
            alertMessage.errorMessage("withdrawal limit exceeded");
            return;
        } else {
            wLim -= amount;
            mInstance.getDatabaseDriver().updateWithdrawalLimit(pAddress, wLim);
            mInstance.getClient().savingsAccountProperty().get().withdrawalLimitProperty().set(wLim);
        }

        mInstance.getDatabaseDriver().updateCheckingBalance(pAddress, amount, "ADD");
        mInstance.getDatabaseDriver().updateSavingsBalance(pAddress, amount, "SUBTRACT");
        alertMessage.successMessage("Transfer Successfully");

        mInstance.getClient().savingsAccountProperty().get().setBalance(mInstance.getDatabaseDriver().getSavingsBalance(pAddress));
        mInstance.getClient().checkingAccountProperty().get().setBalance(mInstance.getDatabaseDriver().getCheckingBalance(pAddress));

        amount_to_ch.clear();
    }
}
