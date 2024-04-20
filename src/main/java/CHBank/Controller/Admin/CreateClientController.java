package CHBank.Controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField firstname_filed;
    public TextField lastname_file;
    public TextField password_filed;
    public CheckBox pAddress_box;
    public Label pAddress_label;
    public CheckBox check_acc_box;
    public TextField check_amount_field;
    public CheckBox save_acc_box;
    public TextField save_amount_filed;
    public Button create_client_button;
    public Label error_label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
