package CHBank.Controller.Client;

import CHBank.Models.CheckingAccount;
import CHBank.Models.Model;
import CHBank.Models.Transaction;
import CHBank.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label income_label;
    public Label expense_label;
    public ListView<Transaction> transaction_listview;
    public TextField payee_field;
    public TextField amount_filed;
    public TextArea message_field;
    public Label saving_acc_num;
    public Button send_money_but;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        updateSomething();
        send_money_but.setOnAction(_ -> onSendMoney());
        accountSummary();
    }

    private void bindData(){
        user_name.textProperty().bind(Bindings.concat("Hi, ").concat(Model.getInstance().getClient().FirstName()));
        login_date.setText("Today, " + LocalDate.now());
        checking_bal.textProperty().bind(Model.getInstance().getClient().CheckingAccount().get().balanceProperty().asString());
        checking_acc_num.textProperty().bind(Model.getInstance().getClient().CheckingAccount().get().accountNumberProperty());
        savings_bal.textProperty().bind(Model.getInstance().getClient().SavingAccount().get().balanceProperty().asString());
        saving_acc_num.textProperty().bind(Model.getInstance().getClient().SavingAccount().get().accountNumberProperty());
    }

    private void initLatestTransactionsLists(){
            Model.getInstance().setLatestTransactions();
    }

    private void updateSomething(){
        initLatestTransactionsLists();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(_ -> new TransactionCellFactory());
    }

    private void onSendMoney(){
        String  receiver = payee_field.getText().trim();
        String amountText = amount_filed.getText().trim();
        String message = message_field.getText();
        String sender = Model.getInstance().getClient().pAddress().get();
        double amount = 0;
        if (receiver.isEmpty()) {
            showAlert("Please enter the recipient's name.");
            return;
        }

        if (amountText.isEmpty()) {
            showAlert("Please enter the amount to send.");
            return;
        }

        if(sender.equals(receiver)){
            showAlert("U can not transfer to yourself");
            return;
        }

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for the amount.");
            return;
        }

        if (amount <= 0) {
            showAlert("Please enter an amount greater than zero.");
            return;
        }

        int tLim = Model.getInstance().getDatabaseDriver().getTransactionLimit(sender);


        ResultSet resultSet = Model.getInstance().searchClient(receiver);
        if (resultSet == null) {
            showAlert("No client found with the given address.");
            return;
        }
        try {
            if (resultSet.isBeforeFirst()){
                Model.getInstance().getDatabaseDriver().updateCheckingBalance(receiver, amount, "ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tLim <= 0) {
            showAlert("Transaction limit exceeded");
            return;
        } else {
            tLim -=1;
            Model.getInstance().getDatabaseDriver().updateTransactionLimit(sender, tLim);
            ((CheckingAccount) Model.getInstance().getClient().CheckingAccount().get()).transactionLimitProperty().set(tLim);
        }

        Model.getInstance().getDatabaseDriver().updateCheckingBalance(sender, amount, "SUB");

        Model.getInstance().getClient().CheckingAccount().get().setBalance(Model.getInstance().getDatabaseDriver().getCheckingBalance(sender));

        Model.getInstance().getDatabaseDriver().newTransactions(sender, receiver, amount, message);

        updateSomething();
        showAlertReal(Alert.AlertType.INFORMATION, "Transfer Successful", "You have transferred " + amount + " to " + receiver);

        // Clear fields
        payee_field.clear();
        amount_filed.clear();
        message_field.clear();
    }

    private void accountSummary(){
        double income = 0;
        double expense = 0;
        if (Model.getInstance().getAllTransactions().isEmpty()){
            Model.getInstance().setAllTransactions();
        }
        for (Transaction transaction : Model.getInstance().getAllTransactions()){
            if (transaction.senderProperty().get().equals(Model.getInstance().getClient().pAddress().get())){
                expense = expense + transaction.amountProperty().get();
            } else {
                income = income + transaction.amountProperty().get();
            }
        }
        income_label.setText("+ $"+ income);
        expense_label.setText("+ $"+ expense);
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlertReal(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
