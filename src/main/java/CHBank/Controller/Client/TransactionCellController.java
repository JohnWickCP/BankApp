package CHBank.Controller.Client;

import CHBank.Models.Transaction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {
    public FontAwesomeIcon in_icon;
    public FontAwesomeIcon out_icon;
    public Label trans_date_label;
    public Label sender_label;
    public Label receiver_label;
    public Label amount_label;

    private final Transaction transaction;
    public TransactionCellController(Transaction transaction) {
        this.transaction = transaction;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sender_label.textProperty().bind(transaction.senderProperty());
        receiver_label.textProperty().bind(transaction.receiverProperty());
        amount_label.textProperty().bind(transaction.amountProperty().asString());
        trans_date_label.textProperty().bind(transaction.dateProperty().asString());
    }
}
