package CHBank.Controller.Admin;

import CHBank.Models.Client;
import CHBank.Models.Model;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_button.setOnAction(e -> onClientSearch());
        deposit_button .setOnAction(e -> onDepositButtonClicked());
    }

    public void onClientSearch(){
        ObservableList<Client> searchResult = Model.getInstance().searchClients(pAddress_field.getText());
        result_listview.setItems(searchResult);
        result_listview.setCellFactory(e -> new ClientCellFactory());
        client = searchResult.getFirst();
    }
    private void onDepositButtonClicked(){
        double amount = Double.parseDouble(Amount_filed.getText());
        double newBalance = amount + client.SavingAccount().get().balanceProperty().get();
        if (Amount_filed.getText() != null ){
            Model.getInstance().getDatabaseDriver().depositSavings(client.pAddress().get(), newBalance);
        }
        emptyFields();
    }

    private void emptyFields(){
        pAddress_field.setText("");
        Amount_filed.setText("");
    }
}
