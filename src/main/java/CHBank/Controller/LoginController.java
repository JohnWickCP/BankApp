package CHBank.Controller;

import CHBank.Views.AccountType;
import CHBank.Models.Model;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> acc_selector;
    public Label payee_add_label;
    public TextField payee_add_field;
    public Label pass_add_label;
    public Button login_button;
    public PasswordField pass_add_field;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.getSelectionModel().select(AccountType.CLIENT);
        acc_selector.setValue(Model.getInstance().getView().getLoginAccountType());
        acc_selector.valueProperty().addListener(_ ->setAcc_selector());
        login_button.setOnAction(_ -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) login_button.getScene().getWindow();
        String p_address = payee_add_field.getText().trim();
        String p_password = pass_add_field.getText().trim();

        if(p_address.isEmpty() || p_password.isEmpty()) {
            Model.getInstance().getView().getAlertMessage().errorMessage("Please fill all the fields");
            return;
        }

        if (Model.getInstance().getView().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().evaluatedClientCred(p_address, p_password);
            if (Model.getInstance().getClientLoginSuccessFlag()){
                Model.getInstance().getView().getAlertMessage().successMessage("Login successfully");
                Model.getInstance().getView().showClientWindow();
                Model.getInstance().getView().closeStage(stage);
            } else {
                Model.getInstance().getView().getAlertMessage().errorMessage("Login failed! Wrong Payee Address or Password");
                emptyFields();
            }
        } else {
            Model.getInstance().evaluateAdminCred(p_address, p_password);
            if (Model.getInstance().getAdminLoginSuccessFlag()){
                Model.getInstance().getView().getAlertMessage().successMessage("Login successfully");
                Model.getInstance().getView().showAdminWindow();
                Model.getInstance().getView().closeStage(stage);
            } else {
                Model.getInstance().getView().getAlertMessage().errorMessage("Login failed! Wrong Username or Password");
                emptyFields();
            }
        }
    }
    private void setAcc_selector(){
        Model.getInstance().getView().setLoginAccountType(acc_selector.getValue());
        if (acc_selector.getValue() == AccountType.ADMIN) {
            payee_add_label.setText("Username");
        }
    }
    private void emptyFields(){
        payee_add_field.setText("");
        pass_add_field.setText("");
    }
}
