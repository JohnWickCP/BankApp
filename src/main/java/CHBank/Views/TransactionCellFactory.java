package CHBank.Views;

import CHBank.Controller.Client.TransactionCellController;
import CHBank.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;


import java.util.Objects;

public class TransactionCellFactory extends ListCell<Transaction> {
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Client/TransactionCell.fxml"));
            TransactionCellController controller = new TransactionCellController(transaction);
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
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/TransactionCell.css")).toExternalForm());
            }
        }
    }
}
