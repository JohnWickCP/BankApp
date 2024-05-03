package CHBank.Controller.Client;

import CHBank.Models.Model;
import CHBank.Models.Transaction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {
    public FontAwesomeIcon in_icon;
    public FontAwesomeIcon out_icon;
    public Label trans_date_label;
    public Label sender_label;
    public Label receiver_label;
    public Label amount_label;
    public Button message_button;

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
        message_button.setOnAction(_ -> Model.getInstance().getView().showMessageWindow(transaction.senderProperty().get(), transaction.messageProperty().get()));
        transactionIcon();
    }

    private void transactionIcon(){
        if(transaction.senderProperty().get().equals(Model.getInstance().getClient().pAddress().get())){
            in_icon.setFill(Color.rgb(240, 240, 240));
            out_icon.setFill(Color.RED);
        } else {
            in_icon.setFill(Color.GREEN);
            out_icon.setFill(Color.rgb(240, 240 ,240));
        }
    }
}
