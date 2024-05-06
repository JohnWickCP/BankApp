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

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;
    public TextField address_field;
    public Button search_button;
    public Button Delete_button;
    public Button refresh_button;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defaultSomething();
        search_button.setOnAction(_ -> onSearchButtonClicked());
        Delete_button.setOnAction(_ -> onDeleteButtonClicked());
        refresh_button.setOnAction(_ -> onRefreshButtonClicked());
    }
    private void initData (){
        Model.getInstance().setClients();
    }

    public void onSearchButtonClicked() {
        String address = address_field.getText().trim();

        ObservableList<Client> searchResult = Model.getInstance().searchClients(address);
        if (searchResult.isEmpty()) {
            if (!address.isEmpty()) {
                showAlert("No client found with the given address.");
            }
           else {
               defaultSomething();
            }
            return;
        }

        clients_listview.setItems(searchResult);
        clients_listview.setCellFactory(e -> new ClientCellFactory());
        client = searchResult.getFirst();

    }

    public void defaultSomething(){
        initData();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(_ -> new ClientCellFactory());
    }

    public void onDeleteButtonClicked() {
        String address = client.pAddress().get();
        Model.getInstance().getDatabaseDriver().deleteClient(address);

        address_field.setText("");
        showAlertReal(Alert.AlertType.INFORMATION, "Client deleted.", "Client deleted successfully.");
        defaultSomething();
    }

    public void onRefreshButtonClicked() {
        String address = client.pAddress().get();
        Model.getInstance().refreshLimitations(address);
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlertReal(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}