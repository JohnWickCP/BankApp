package CHBank.Controller.Admin;

import java.text.Normalizer;
import java.util.regex.Pattern;
import CHBank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField firstname_filed;
    public TextField lastname_file;
    public TextField password_filed;
    public CheckBox pAddress_box;
    public Label pAddress_label;
    public CheckBox check_acc_box;
    public TextField check_amount_field;
    public CheckBox save_acc_box;
    public TextField save_amount_filed;
    public Button create_client_button;
    public Label error_label;

    private String payeeAddress;
    private boolean createCheckingAccountFlag= false;
    private boolean createSavingsAccountFlag = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_button.setOnAction(_ -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            }
        });
        check_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                createCheckingAccountFlag = true;
            }
        });
        save_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                createSavingsAccountFlag = true;
            }
        });
    }

    private void createClient() {
        // Create Checking Account
        if (createCheckingAccountFlag) {
            createAccount("Checking");
        }

        // Create Savings Account
        if (createSavingsAccountFlag) {
            createAccount("Savings");
        }

        // Create Client
         String fName = firstname_filed.getText();
        String lName = lastname_file.getText();
        String pass = password_filed.getText();
        Model.getInstance().getDatabaseDriver().creteClient(fName, lName, payeeAddress, pass, LocalDate.now());
        error_label.setStyle("-fx-text-fill: blue; -fx-font-size: 1.3em; -fx-font-weight: bold;");
        error_label.setText("Client Created Successfully");
        emptyFields();
    }
    private void createAccount(String accountType){
        double balance = Double.parseDouble(check_amount_field.getText());

        // Generate Account Number
        String firstSection = "3210";
        String lastSection = Integer.toString((new Random()).nextInt(9999) + 1000);
        String accountNumber = firstSection + " " + lastSection;

        // Create the checking or saving account

        if(accountType.equals("Checking")){
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accountNumber, 10, balance);
        } else {
            Model.getInstance().getDatabaseDriver().createSavingsAccount(payeeAddress, accountNumber, 2000, balance);
        }
    }
    private void  onCreatePayeeAddress(){
        if (firstname_filed.getText() != null && lastname_file.getText() != null) {
            pAddress_label.setText(payeeAddress);
        }
    }

    private String createPayeeAddress() {
        int id = Model.getInstance().getDatabaseDriver().getLastClientId() + 1;
        String firstName = firstname_filed.getText().trim();
        String lastName = lastname_file.getText().trim();

        // Loại bỏ các dấu từ các tên
        firstName = removeAccents(firstName);
        lastName = removeAccents(lastName);

        char firstChar = Character.toLowerCase(firstName.charAt(0));
        String address = "@" + firstChar + lastName + id;
        return address.replaceAll("\\s+", "");
    }

    private void emptyFields(){
        firstname_filed.clear();
        lastname_file.clear();
        password_filed.clear();
        pAddress_box.setSelected(false);
        check_acc_box.setSelected(false);
        check_amount_field.clear();
        save_acc_box.setSelected(false);
        save_amount_filed.clear();
    }

    // Phương thức để loại bỏ các dấu từ các chuỗi
    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
