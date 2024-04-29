package CHBank.Views;

import CHBank.Controller.Admin.ClientCellController;
import CHBank.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;

import java.util.Objects;

public class ClientCellFactory extends ListCell<Client> {
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if (empty || client == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCell.fxml"));
            ClientCellController controller = new ClientCellController(client);
            fxmlLoader.setController(controller);
            setText(null);
            try {
                setGraphic(fxmlLoader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Scene scene = getScene();
            if (scene != null) {
                // Thêm tập tin CSS vào Scene
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/ClientCell.css")).toExternalForm());
            }
        }
    }
}
