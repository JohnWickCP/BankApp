package CHBank.Controller.Client;

import CHBank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;



public class ClientController implements Initializable {

    public BorderPane client_parent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getView().getClientSelectedOptions().addListener((_, _, newValue) -> {
            switch (newValue) {
                case TRANSACTION -> client_parent.setCenter(Model.getInstance().getView().getTransactionPane());
                case ACCOUNTS-> client_parent.setCenter(Model.getInstance().getView().getAccountPane());
                default -> client_parent.setCenter(Model.getInstance().getView().getDashboardPane());
            }
        });
    }
}
