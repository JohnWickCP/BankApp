package CHBank.Controller.Admin;

import CHBank.Models.Client;
import javafx.fxml.Initializable;
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

    private final Client client;

    public ClientCellController(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fName_label.textProperty().bind(client.fNameProperty());
        lName_label.textProperty().bind(client.lNameProperty());
        pAddress_label.textProperty().bind(client.pAddressProperty());
        ch_acc_label.textProperty().bind(client.checkingAccountProperty().asString());
        sv_acc_label.textProperty().bind(client.savingsAccountProperty().asString());
        date_label.textProperty().bind(client.dateCreatedProperty().asString());
    }

}

