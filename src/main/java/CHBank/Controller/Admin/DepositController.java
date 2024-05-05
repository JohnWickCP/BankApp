package CHBank.Controller.Admin;

import CHBank.Models.Client;
import CHBank.Models.Model;
import CHBank.Views.ClientCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    public TextField pAddress_field;
    public Button search_button;
    public ListView<Client> result_listview;
    public TextField Amount_filed;
    public Button deposit_button;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_button.setOnAction(e -> onClientSearch());
        deposit_button .setOnAction(e -> onDepositButtonClicked());
    }

    public void onClientSearch(){
        String address = pAddress_field.getText().trim();

        if (address.isEmpty()) {
            showAlert("Please enter the client's address.");
            return;
        }

        ObservableList<Client> searchResult = Model.getInstance().searchClients(address);
        if (searchResult.isEmpty()) {
            showAlert("No client found with the given address.");
            return;
        }

        result_listview.setItems(searchResult);
        result_listview.setCellFactory(e -> new ClientCellFactory());
        client = searchResult.getFirst();
    }
    public void onDepositButtonClicked() {

        String amountText = Amount_filed.getText().trim();
        if (amountText.isEmpty()) {
            showAlert("Please enter the deposit amount.");
            return;
        }
        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for the deposit amount.");
            return;
        }

        if (amount <= 0) {
            showAlert("Please enter a deposit amount greater than zero.");
            return;
        }

        double newBalance = amount + client.SavingAccount().get().balanceProperty().get();
        Model.getInstance().getDatabaseDriver().depositSavings(client.pAddress().get(), newBalance);

        emptyFields();
    }


    private void emptyFields(){
        pAddress_field.setText("");
        Amount_filed.setText("");
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
