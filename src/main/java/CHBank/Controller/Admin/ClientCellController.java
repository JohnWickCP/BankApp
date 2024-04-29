package CHBank.Controller.Admin;

import CHBank.Models.Client;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    public Label fName_label;
    public Label lName_label;
    public Label pAddress_label;
    public Label ch_acc_label;
    public Label sv_acc_label;
    public Label date_label;
    public Button delete_button;

    private final Client client;
    public ClientCellController(Client client) {
        this.client = client;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

