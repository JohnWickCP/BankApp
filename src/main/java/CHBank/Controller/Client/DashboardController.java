package CHBank.Controller.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label income_label;
    public Label expense_label;
    public ListView transaction_listview;
    public TextField payee_field;
    public TextField amount_filed;
    public TextArea message_field;
    public Label saving_acc_num;
    public Button send_money_but;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
