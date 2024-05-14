package CHBank.Controller.Admin;

import CHBank.Models.Model;
import CHBank.Views.AlertMessage;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    
    private boolean createCheckingAccountFlag = false;
    private boolean createSavingsAccountFlag = false;
    private final AlertMessage alertMessage = Model.getInstance().getView().getAlertMessage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_button.setOnAction(_ -> onCreateClient());
        pAddress_box.selectedProperty().addListener((_, _, newValue) -> {
            if (newValue) {
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            }
        });
        check_acc_box.selectedProperty().addListener((_, _, newValue) -> {
            if (newValue) {
                createCheckingAccountFlag = true;
            }
        });
        save_acc_box.selectedProperty().addListener((_, _, newValue) -> {
            if (newValue) {
                createSavingsAccountFlag = true;
            }
        });
    }

    private void onCreateClient(){

        if(firstname_filed.getText().isEmpty()){
            error_label.setText("Please enter first name");
            return;
        }
        if(lastname_file.getText().isEmpty()){
            error_label.setText("Please enter last name");
            return;
        }
        if(password_filed.getText().isEmpty()){
            error_label.setText("Please enter password");
            return;
        }

        if(check_amount_field.getText().isEmpty()){
            error_label.setText("You need a checking account amount");
            return;
        }

        if(save_amount_filed.getText().isEmpty()){
            error_label.setText("You need a saving account amount");
            return;
        }

        if(pAddress_box.isSelected()){
            pAddress_label.setText("Address");
        } else {
            error_label.setText("You must select a valid address");
            return;
        }

        if (createCheckingAccountFlag) {
            createAccount("Checking");
        }
        if (createSavingsAccountFlag) {
            createAccount("Savings");
        }

        String fName = firstname_filed.getText();
        String lName = lastname_file.getText();
        String pass = password_filed.getText();

        boolean confirmed = alertMessage.confirmMessage("Do you want to create a new client?");
        if (confirmed) {
            Model.getInstance().getDatabaseDriver().creteClient(fName, lName, payeeAddress, pass, LocalDate.now());
            Model.getInstance().getView().getAlertMessage().successMessage("Client created successfully");
            emptyFields();
        }

    }

    private void  onCreatePayeeAddress(){
        if (firstname_filed.getText() != null && lastname_file.getText() != null) {
            pAddress_label.setText(payeeAddress);
        }
    }

    private void createAccount(String accountType) {
        double balance = Double.parseDouble(check_amount_field.getText());

        // Generate Account Number
        String firstSection = "3210";
        String lastSection;
        String accountNumber;
        boolean accountExists;

        do {
            lastSection = Integer.toString((new Random()).nextInt(9999) + 1000);
            accountNumber = firstSection + " " + lastSection;
            // Kiểm tra xem tài khoản đã tồn tại trong cơ sở dữ liệu hay chưa
            accountExists = Model.getInstance().getDatabaseDriver().isAccountExists(accountNumber, accountType);
        } while (accountExists);

        if (accountType.equals("Checking")) {
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accountNumber, 10, balance);
        } else {
            Model.getInstance().getDatabaseDriver().createSavingsAccount(payeeAddress, accountNumber, 2000, balance);
        }
    }

    private String createPayeeAddress() {
        int id = Model.getInstance().getDatabaseDriver().getLastClientId() + 1;
        String firstName = firstname_filed.getText().trim();
        String lastName = lastname_file.getText().trim();


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
        pAddress_label.setText("");
        pAddress_box.setSelected(false);
        check_acc_box.setSelected(false);
        check_amount_field.clear();
        save_acc_box.setSelected(false);
        save_amount_filed.clear();
    }

    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
