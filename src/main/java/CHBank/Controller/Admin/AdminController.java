package CHBank.Controller.Admin;

import CHBank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getView().getAdminSelectedOptions().addListener((_, _, newValue) -> {
            switch (newValue) {
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getView().getClientsView());
                case DEPOSIT -> admin_parent.setCenter(Model.getInstance().getView().getDepositsView());
                default -> admin_parent.setCenter(Model.getInstance().getView().getCreateClientsView());
            }
        });
    }
}
