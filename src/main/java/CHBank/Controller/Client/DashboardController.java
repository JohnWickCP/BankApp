package CHBank.Controller.Client;

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
        initLatestTransactionsLists();;
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        send_money_but.setOnAction(e -> onSendMoney());
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
        if (Model.getInstance().getLatestTransactions().isEmpty()){
            Model.getInstance().setLatestTransactions();
        }
    }

    private void onSendMoney(){
        String  receiver = payee_field.getText();
        double amount = Double.parseDouble(amount_filed.getText());
        String message = message_field.getText();
        String sender = Model.getInstance().getClient().pAddress().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()){
                Model.getInstance().getDatabaseDriver().updateBalance(receiver, amount, "ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tru tien cua nguoi chuyen
        Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "SUB");
        // Cap nhat so du cau doi tuong khach hang
        Model.getInstance().getClient().SavingAccount().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsBalance(sender));
        // ghi nhan
        Model.getInstance().getDatabaseDriver().newTransactions(sender, receiver, amount, message);
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
}
