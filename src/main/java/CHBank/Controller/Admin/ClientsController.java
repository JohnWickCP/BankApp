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

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;
    public TextField address_field;
    public Button search_button;
    public Button Delete_button;
    public Button refresh_button;

    private Client client;

    Model model = Model.getInstance();
    AlertMessage alertMessage = model.getView().getAlertMessage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defaultSomething();
        search_button.setOnAction(_ -> onSearchButtonClicked());
        Delete_button.setOnAction(_ -> onDeleteButtonClicked());
        refresh_button.setOnAction(_ -> onRefreshButtonClicked());
    }
    private void initData (){
        model.setClients();
    }

    public void onSearchButtonClicked() {
        String address = address_field.getText().trim();

        ObservableList<Client> searchResult = model.searchClients(address);
        if (searchResult.isEmpty()) {
            if (!address.isEmpty()) {
                alertMessage.errorMessage("No client found with the given address.");
            }
            else {
                defaultSomething();
            }
            return;
        }

        clients_listview.setItems(searchResult);
        clients_listview.setCellFactory(_ -> new ClientCellFactory());
        client = searchResult.getFirst();

    }

    public void defaultSomething(){
        initData();
        clients_listview.setItems(model.getClients());
        clients_listview.setCellFactory(_ -> new ClientCellFactory());
    }

    public void onDeleteButtonClicked() {
        boolean confirmed = alertMessage.confirmMessage("Are you sure you want to delete this client?");
        if (confirmed) {
            String address = client.pAddressProperty().get();
            Model.getInstance().getDatabaseDriver().deleteClient(address);

            address_field.setText("");
            alertMessage.successMessage("Client deleted successfully.");
            defaultSomething();
        }

    }

    public void onRefreshButtonClicked() {
        String address = client.pAddressProperty().get();
        model.refreshLimitations(address);
    }

}