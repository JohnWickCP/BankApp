package CHBank.Controller.Client;

import CHBank.Models.Account;
import CHBank.Models.Model;
import CHBank.Models.Transaction;
import CHBank.Views.AlertMessage;
import CHBank.Views.TransactionCellFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    Model model = Model.getInstance();
    AlertMessage alertMessage = model.getView().getAlertMessage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        updateData();
        send_money_but.setOnAction(_ -> onSendMoney());
        accountSummary();
    }

    private void updateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
           String currentTime = "Today, " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
           login_date.setText(currentTime);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void bindData(){
        user_name.textProperty().bind(Bindings.concat("Hi, ").concat(model.getClient().fNameProperty()));
        updateTime();
        checking_bal.textProperty().bind(model.getClient().checkingAccountProperty().get().balanceProperty().asString());
        checking_acc_num.textProperty().bind(model.getClient().checkingAccountProperty().get().accountNumberProperty());
        savings_bal.textProperty().bind(model.getClient().savingsAccountProperty().get().balanceProperty().asString());
        saving_acc_num.textProperty().bind(model.getClient().savingsAccountProperty().get().accountNumberProperty());
    }

    private void initLatestTransactionsLists(){
            model.setLatestTransactions();
    }

    private void updateData(){
        initLatestTransactionsLists();
        transaction_listview.setItems(model.getLatestTransactions());
        transaction_listview.setCellFactory(_ -> new TransactionCellFactory());
    }

    private void onSendMoney(){
        String receiver = payee_field.getText().trim();
        String amountText = amount_filed.getText().trim();
        String message = message_field.getText();
        String sender = Model.getInstance().getClient().pAddressProperty().get();
        String senderName =  Model.getInstance().getName(sender);
        String receiverAddress = Model.getInstance().getName(receiver);
        String mess = "Sender: " + sender +" "+ senderName + "\nReceiver: " + receiver +" "+ receiverAddress+"\nAmount: " + amountText +"\nDo you want to send money?";


            double amount ;
            double balance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().get();
            if (receiver.isEmpty()) {
                alertMessage.errorMessage("Please enter the recipient's name.");
                return;
            }

            if (amountText.isEmpty()) {
                alertMessage.errorMessage("Please enter the amount to send.");
                return;
            }

            if(sender.equals(receiver)){
                alertMessage.errorMessage("You can not transfer to yourself");
                return;
            }

            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                alertMessage.errorMessage("Please enter a valid number for the amount.");
                return;
            }

            if (amount <= 0) {
                alertMessage.errorMessage("Please enter an amount greater than zero.");
                return;
            }

            if (amount > balance){
                alertMessage.errorMessage("You can not transfer more than balance.");
                return;
            }
            int tLim = Model.getInstance().getDatabaseDriver().getTransactionLimit(sender);


            ResultSet resultSet = Model.getInstance().searchClient(receiver);
            if (resultSet == null) {
                alertMessage.errorMessage("No client found with the given address.");
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
                alertMessage.errorMessage("Transaction limit exceeded");
                return;
            } else {
                tLim -=1;
                model.getDatabaseDriver().updateTransactionLimit(sender, tLim);
                (model.getClient().checkingAccountProperty().get()).transactionLimitProperty().set(tLim);
            }
        boolean confirmed = alertMessage.confirmMessage(mess);
            if (confirmed) {

                model.getDatabaseDriver().updateCheckingBalance(sender, amount, "SUB");
                model.getClient().checkingAccountProperty().get().setBalance(model.getDatabaseDriver().getCheckingBalance(sender));
                model.getDatabaseDriver().newTransaction(sender, receiver, amount, message);
                updateData();
                alertMessage.successMessage("Transfer Successful " + "You have transferred " + amount + " to " + receiver);

                // Clear fields
                payee_field.clear();
                amount_filed.clear();
                message_field.clear();
            }

    }

    private void accountSummary(){
        double income = 0;
        double expense = 0;
        if (model.getAllTransactions().isEmpty()){
            model.setAllTransactions();
        }
        for (Transaction transaction : model.getAllTransactions()){
            if (transaction.senderProperty().get().equals(model.getClient().pAddressProperty().get())){
                expense = expense + transaction.amountProperty().get();
            } else {
                income = income + transaction.amountProperty().get();
            }
        }
        income_label.setText("+ $"+ income);
        expense_label.setText("+ $"+ expense);
    }

}
