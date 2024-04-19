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
        Model.getInstance().getView().getClientSelectedMenuItem().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Transaction" -> client_parent.setCenter(Model.getInstance().getView().getTransactionsView());
                case "Accounts" -> client_parent.setCenter(Model.getInstance().getView().getAccountsView());
                default -> client_parent.setCenter(Model.getInstance().getView().getDashboardView());
            }
        });
    }
}
