package CHBank.Controller.Admin;

import CHBank.Models.Client;
import CHBank.Models.Model;
import CHBank.Views.ClientCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.collections.ListChangeListener;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(_ -> new ClientCellFactory());

        Model.getInstance().getClients().addListener((ListChangeListener<Client>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    refreshClientList();
                    break;
                }
            }
        });
    }

    private void initData (){
        Model.getInstance().setClients();
    }

    private void refreshClientList() {
        clients_listview.setItems(Model.getInstance().getClients());
    }
}