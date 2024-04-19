package CHBank.Controller;

import CHBank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox acc_selector;
    public Label payee_add_label;
    public TextField payee_add_field;
    public Label pass_add_label;
    public TextField pass_add_field;
    public Button login_button;
    public Label error_label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_button.setOnAction(event -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) error_label.getScene().getWindow();
        Model.getInstance().getView().closeStage(stage);
        Model.getInstance().getView().showClientWindow();
    }



}
