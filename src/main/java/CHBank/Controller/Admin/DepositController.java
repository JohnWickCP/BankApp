package CHBank.Controller.Admin;

import CHBank.Models.Client;
import CHBank.Models.Model;
import CHBank.Views.AlertMessage;
import CHBank.Views.ClientCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
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

    Model model = Model.getInstance();
    AlertMessage alert = model.getView().getAlertMessage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_button.setOnAction(_ -> onClientSearch());
        deposit_button .setOnAction(_ -> onDepositButtonClicked());
    }


    public void onClientSearch(){
        String address = pAddress_field.getText().trim();

        if (address.isEmpty()) {
            alert.errorMessage("Please enter the client's address.");
            return;
        }

        ObservableList<Client> searchResult = model.searchClients(address);
        if (searchResult.isEmpty()) {
            alert.errorMessage("No client found with the given address.");
            return;
        }

        result_listview.setItems(searchResult);
        result_listview.setCellFactory(_ -> new ClientCellFactory());
        client = searchResult.getFirst();
    }

    public void onDepositButtonClicked() {

        String amountText = Amount_filed.getText().trim();
        if (amountText.isEmpty()) {
            alert.errorMessage("Please enter the deposit amount.");
            return;
        }
        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            alert.errorMessage("Please enter a valid number for the deposit amount.");
            return;
        }

        if (amount <= 0) {
            alert.errorMessage("Please enter a deposit amount greater than zero.");
            return;
        }

        String pAddressName =  client.pAddressProperty().get();
        boolean confirmed = alert.confirmMessage("Do you want to deposit " + amount +" to " + pAddressName + "?");

        if (confirmed) {
            double newBalance = amount + client.savingsAccountProperty().get().balanceProperty().get();
            model.getDatabaseDriver().depositSavings(client.pAddressProperty().get(), newBalance);
            alert.successMessage("Successfully! Deposited " + amount + " to " + pAddressName + ".");
            emptyFields();
        }

    }


    private void emptyFields(){
        pAddress_field.setText("");
        Amount_filed.setText("");
    }

}
