package CHBank.Controller.Client;

import CHBank.Models.Model;
import CHBank.Models.Transaction;
import CHBank.Views.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    public ListView<Transaction> transaction_Listview;
    public TextField dayFrom_field;
    public TextField dayTo_field;
    public Button export_button;
    public Button search_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defaultSomething();
        search_button.setOnAction(e -> searchTransactions());
        export_button.setOnAction(e -> exportTransactions());
    }

    private void initData(){
        Model.getInstance().setAllTransactions();
    }
    public void defaultSomething(){
        initData();
        transaction_Listview.setItems(Model.getInstance().getAllTransactions());
        transaction_Listview.setCellFactory(_ -> new TransactionCellFactory());

    }

    // Method to handle search button action
    public void searchTransactions() {

        String fromText = dayFrom_field.getText().trim();
        String toText = dayTo_field.getText().trim();

        if (fromText.isEmpty() && toText.isEmpty()) {
            defaultSomething(); // Refresh the page
            return;
        }

        try {
            LocalDate dayFrom = LocalDate.parse(fromText);
            LocalDate dayTo = LocalDate.parse(toText);

            if (dayFrom.isAfter(dayTo)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Start date cannot be after end date.");
                return;
            }

            transaction_Listview.setItems(Model.getInstance().searchTransactionsBetweenDates(dayFrom, dayTo));
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid date format. Please use YYYY-MM-DD format.");
        }
    }

    // Method to handle export button action
    public void exportTransactions() {
        try {
            LocalDate dayFrom = LocalDate.parse(dayFrom_field.getText());
            LocalDate dayTo = LocalDate.parse(dayTo_field.getText());

            if (dayFrom.isAfter(dayTo)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Start date cannot be after end date.");
                return;
            }

            String fileName = "transactions_" + dayFrom.toString() + "_to_" + dayTo.toString() + ".xlsx";
            FileWriter writer = new FileWriter(fileName);

            // Write header to CSV file
            writer.write("Sender,Receiver,Amount,Date,Message\n");

            // Write transactions to CSV file
            for (Transaction transaction : Model.getInstance().searchTransactionsBetweenDates(dayFrom, dayTo)) {
                writer.write(transaction.senderProperty().get() + ",");
                writer.write(transaction.receiverProperty().get() + ",");
                writer.write(transaction.amountProperty().get() + ",");
                writer.write(transaction.dateProperty().get() + ",");
                writer.write(transaction.messageProperty().get() + "\n");
            }

            writer.close();
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Transactions have been exported to " + fileName);
        } catch (DateTimeParseException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing the request.");
        }
    }

    // Method to show an alert
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
