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
        if (Model.getInstance().getView().getLoginAccountType() == AccountType.CLIENT) {
            // Evaluate Client Login Credentials
            Model.getInstance().evaluatedClientCred(pass_add_field.getText(), pass_add_label.getText());
            if (Model.getInstance().getClientLoginSuccessFlag()){
                Model.getInstance().getView().showClientWindow();
                // Close the login stage
                Model.getInstance().getView().closeStage(stage);
            } else {
                pass_add_field.setText("");
                pass_add_label.setText("");
                error_label.setText("No Such Login Credentials");

            }
        } else Model.getInstance().getView().showAdminWindow();
    }



}
