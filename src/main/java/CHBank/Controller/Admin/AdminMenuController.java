package CHBank.Controller.Admin;

import CHBank.Models.Model;
import CHBank.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_button;
    public Button clients_list_button;
    public Button deposit_button;
    public Button logout_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }
    private void addListener(){
        create_client_button.setOnAction(e -> onCreateClient());
        clients_list_button.setOnAction(e -> onClients());
        deposit_button.setOnAction(e -> onDeposit());
        logout_button.setOnAction(e -> onLogout());
    }
    private void onCreateClient(){
        Model.getInstance().getView().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENTS);
    }
    private void onClients(){
        Model.getInstance().getView().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }
    private void onDeposit() {
        Model.getInstance().getView().getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
    }
    private void onLogout() {
        // Get Stage
        Stage stage = (Stage) clients_list_button.getScene().getWindow();
        // Close the Admin window
        Model.getInstance().getView().closeStage(stage);
        // Show login window
        Model.getInstance().getView().showLoginWindow();
        // Set Amin login Success Flag to false
        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
