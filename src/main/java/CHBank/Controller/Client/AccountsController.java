package CHBank.Controller.Client;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
