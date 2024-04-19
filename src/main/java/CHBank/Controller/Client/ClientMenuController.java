package CHBank.Controller.Client;

import CHBank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_but;
    public Button transaction_but;
    public Button acc_but;
    public Button profile_but;
    public Button logout_but;
    public Button report_but;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }
    public void addListener(){
        dashboard_but.setOnAction(event -> onDashboard());
        transaction_but.setOnAction(actionEvent -> onTransaction());
    }
    private void onDashboard(){
        Model.getInstance().getView().getClientSelectedMenuItem().set("Dashboard");
    }
    private void onTransaction(){
        Model.getInstance().getView().getClientSelectedMenuItem().set("Transaction");
    }
}


