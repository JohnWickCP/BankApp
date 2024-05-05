package CHBank.Controller.Client;

import CHBank.Models.Model;
import CHBank.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_but;
    public Button transaction_but;
    public Button acc_but;
    public Button logout_but;
    public Button report_but;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }
    public void addListener(){
        dashboard_but.setOnAction(_ -> onDashboard());
        transaction_but.setOnAction(_ -> onTransaction());
        acc_but.setOnAction(_ -> onAccounts());
        logout_but.setOnAction(_ -> onLogout());
    }
    private void onDashboard(){
        Model.getInstance().getView().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }
    private void onTransaction(){
        Model.getInstance().getView().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTION);
    }
    private void onAccounts(){
        Model.getInstance().getView().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }
    private void onLogout() {
        // Get Stage
        Stage stage = (Stage) dashboard_but.getScene().getWindow();
        // Close the client window
        Model.getInstance().getView().closeStage(stage);
        // Show login window
        Model.getInstance().getView().showLoginWindow();
        // Set Client login Success Flag to false
        Model.getInstance().setClientLoginSuccessFlag(false);
    }
}


