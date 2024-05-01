package CHBank.Controller.Client;

import CHBank.Models.Model;
import CHBank.Models.Transaction;
import CHBank.Views.TransactionCellFactory;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
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
}
