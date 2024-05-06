package CHBank.Controller.Client;

import CHBank.Models.SavingAccount;
import CHBank.Models.CheckingAccount;
import CHBank.Models.Model;
import javafx.fxml.Initializable;
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
    public Label empty_transfer_warning_save;
    public Label empty_transfer_warning_ch;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        trans_to_save_button.setOnAction(e -> trans_to_save());
        trans_to_cv_button.setOnAction(e -> trans_to_check());
    }

    private void bindData(){
        ch_acc_bal.textProperty().bind(Model.getInstance().getClient().CheckingAccount().get().balanceProperty().asString());
        ch_acc_num.textProperty().bind(Model.getInstance().getClient().CheckingAccount().get().accountNumberProperty());
        transaction_limit.textProperty().bind(((CheckingAccount) Model.getInstance().getClient().CheckingAccount().get()).transactionLimitProperty().asString());
        ch_acc_date.setText(Model.getInstance().getClient().DateCreated().get().toString());

        saving_acc_bal.textProperty().bind(Model.getInstance().getClient().SavingAccount().get().balanceProperty().asString());
        saving_acc_num.textProperty().bind(Model.getInstance().getClient().SavingAccount().get().accountNumberProperty());
        withdrawal_limit.textProperty().bind(((SavingAccount) Model.getInstance().getClient().SavingAccount().get()).withdrawalLimitProperty().asString());
        saving_acc_date.setText(Model.getInstance().getClient().DateCreated().get().toString());
    }

    private void trans_to_save() {
        String pAddress = Model.getInstance().getClient().pAddress().get();
        String amountText = amount_to_save.getText().trim();
        if (amountText.isEmpty()) {
            empty_transfer_warning_save.setVisible(true);
            return;
        }
        empty_transfer_warning_save.setVisible(false);

        double amount = Double.parseDouble(amountText);
        double checkingBalance = Model.getInstance().getClient().CheckingAccount().get().balanceProperty().get();
        if (amount > checkingBalance) {
            empty_transfer_warning_save.setText("Insufficient balance in checking account.");
            empty_transfer_warning_save.setVisible(true);
            return;
        }
        int tLim = Model.getInstance().getDatabaseDriver().getTransactionLimit(pAddress);
        if (tLim <= 0) {
            empty_transfer_warning_save.setText("Transaction limit exceeded.");
            empty_transfer_warning_save.setVisible(true);
            return;
        } else {
            tLim -=1;
            Model.getInstance().getDatabaseDriver().updateTransactionLimit(pAddress, tLim);
            ((CheckingAccount) Model.getInstance().getClient().CheckingAccount().get()).transactionLimitProperty().set(tLim);
        }

        Model.getInstance().getDatabaseDriver().updateSavingsBalance(pAddress, amount, "ADD");
        Model.getInstance().getDatabaseDriver().updateCheckingBalance(pAddress, amount, "SUBTRACT");

        Model.getInstance().getClient().SavingAccount().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsBalance(pAddress));
        Model.getInstance().getClient().CheckingAccount().get().setBalance(Model.getInstance().getDatabaseDriver().getCheckingBalance(pAddress));


        amount_to_save.clear();
    }

    private void trans_to_check() {
        String pAddress = Model.getInstance().getClient().pAddress().get();
        String amountText = amount_to_ch.getText().trim();
        if (amountText.isEmpty()) {
            empty_transfer_warning_ch.setVisible(true);
            return;
        }
        empty_transfer_warning_ch.setVisible(false);

        double amount = Double.parseDouble(amountText);
        double savingsBalance = Model.getInstance().getClient().SavingAccount().get().balanceProperty().get();
        if (amount > savingsBalance) {
            empty_transfer_warning_ch.setText("Insufficient balance in savings account.");
            empty_transfer_warning_ch.setVisible(true);
            return;
        }

        double wLim = Model.getInstance().getDatabaseDriver().getWithdrawalLimit(pAddress);
        if (wLim <= amount) {
            empty_transfer_warning_ch.setText("Withdrawal limit exceeded.");
            empty_transfer_warning_ch.setVisible(true);
            return;
        } else {
            wLim -= amount;
            Model.getInstance().getDatabaseDriver().updateWithdrawalLimit(pAddress, wLim);
            ((SavingAccount) Model.getInstance().getClient().CheckingAccount().get()).withdrawalLimitProperty().set(wLim);
        }

        Model.getInstance().getDatabaseDriver().updateSavingsBalance(pAddress, amount, "SUBTRACT");
        Model.getInstance().getDatabaseDriver().updateCheckingBalance(pAddress, amount, "ADD");

        Model.getInstance().getClient().SavingAccount().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsBalance(pAddress));
        Model.getInstance().getClient().CheckingAccount().get().setBalance(Model.getInstance().getDatabaseDriver().getCheckingBalance(pAddress));

        amount_to_ch.clear();
    }

}
