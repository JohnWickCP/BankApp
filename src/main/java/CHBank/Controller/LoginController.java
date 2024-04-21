package CHBank.Controller;

import CHBank.Models.Model;
import CHBank.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> acc_selector;
    public Label payee_add_label;
    public TextField payee_add_field;
    public Label pass_add_label;
    public TextField pass_add_field;
    public Button login_button;
    public Label error_label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.ADMIN, AccountType.CLIENT));
        acc_selector.setValue(Model.getInstance().getView().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> Model.getInstance().getView().setLoginAccountType(acc_selector.getValue()));
        login_button.setOnAction(event -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) error_label.getScene().getWindow();
        Model.getInstance().getView().closeStage(stage);
        if (Model.getInstance().getView().getLoginAccountType() == AccountType.ADMIN) {
            Model.getInstance().getView().showAdminWindow();
        } else Model.getInstance().getView().showClientWindow();
    }



}
