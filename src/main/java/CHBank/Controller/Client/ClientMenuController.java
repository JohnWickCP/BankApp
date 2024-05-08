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

    Model model = Model.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {addListener();}

    public void addListener(){
        dashboard_but.setOnAction(_ -> setDashboard_but());
        transaction_but.setOnAction(_ -> setTransaction_but());
        acc_but.setOnAction(_ -> setAcc_but());
        logout_but.setOnAction(_ -> setLogout_but());
    }

    private void setDashboard_but(){
        model.getView().getClientSelectedOptions().set(ClientMenuOptions.DASHBOARD);
    }
    private void setTransaction_but(){
        model.getView().getClientSelectedOptions().set(ClientMenuOptions.TRANSACTION);
    }
    private void setAcc_but(){
        model.getView().getClientSelectedOptions().set(ClientMenuOptions.ACCOUNTS);
    }

    private void setLogout_but(){
        boolean confirmed = model.getView().getAlertMessage().confirmMessage("Do you want to logout?");
        if(confirmed){
            Stage stage = (Stage) dashboard_but.getScene().getWindow();
            model.getView().closeStage(stage);
            model.getView().showLoginWindow();
            model.setClientLoginSuccessFlag(false);
        }
    }
}
