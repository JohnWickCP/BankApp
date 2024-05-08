package CHBank.Controller.Admin;

import CHBank.Models.Model;
import CHBank.Views.AdminMenuOptions;
import CHBank.Views.AlertMessage;
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

    Model model = Model.getInstance();
    AlertMessage alertMessage = model.getView().getAlertMessage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }
    private void addListener(){
        create_client_button.setOnAction(_ -> onCreateClient());
        clients_list_button.setOnAction(_ -> onClients());
        deposit_button.setOnAction(_ -> onDeposit());
        logout_button.setOnAction(_ -> onLogout());
    }
    private void onCreateClient(){
        model.getView().getAdminSelectedOptions().set(AdminMenuOptions.CREATE_CLIENTS);
    }
    private void onClients(){
        model.getView().getAdminSelectedOptions().set(AdminMenuOptions.CLIENTS);
    }
    private void onDeposit() {
        model.getView().getAdminSelectedOptions().set(AdminMenuOptions.DEPOSIT);
    }
    private void onLogout() {
        boolean confirmed = alertMessage.confirmMessage("Do you want to logout?");
        if (confirmed){
            Stage stage = (Stage) clients_list_button.getScene().getWindow();
            model.getView().closeStage(stage);
            model.getView().showLoginWindow();
            model.setAdminLoginSuccessFlag(false);
        }

    }
}
