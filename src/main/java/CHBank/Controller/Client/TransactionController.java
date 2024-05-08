package CHBank.Controller.Client;

import CHBank.Models.Model;
import CHBank.Models.Transaction;
import CHBank.Views.AlertMessage;
import CHBank.Views.TransactionCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    public ListView<Transaction> transaction_Listview;
    public DatePicker dayFrom_field;
    public DatePicker dayTo_field;
    public Button search_button;
    public Button export_button;

    Model model = Model.getInstance();
    AlertMessage alertMessage = model.getView().getAlertMessage();
    ObservableList<Transaction> searchedTrans ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDateFormat(dayFrom_field);
        setDateFormat(dayTo_field);
        updateListView();
        search_button.setOnAction(_ -> searchTransactions());
        export_button.setOnAction(_ -> setExport_button());
    }

    private void initData(){
        model.setAllTransactions();
    }

    private void updateListView() {
        initData();
        transaction_Listview.setItems(model.getAllTransactions());
        transaction_Listview.setCellFactory(_ -> new TransactionCellFactory());
    }

    public void searchTransactions(){
        LocalDate dayFrom = dayFrom_field.getValue();
        LocalDate dayTo = dayTo_field.getValue();
        LocalDate firstDayOf2000 = LocalDate.of(2000, 1, 1);
        LocalDate today = LocalDate.now();

        if(dayFrom == null && dayTo == null){
            updateListView();
            return;
        }

        if(dayFrom == null){
            transaction_Listview.setItems(model.searchTransactionsBetweenDates(firstDayOf2000, today));
            return;
        }

        if(dayTo == null){
            transaction_Listview.setItems(model.searchTransactionsBetweenDates(dayFrom, today));
            return;
        }

        if (dayFrom.isAfter(dayTo)) {
            alertMessage.errorMessage("Start date cannot be after end date.");
            return;
        }
        searchedTrans = model.searchTransactionsBetweenDates(dayFrom, today);
        transaction_Listview.setItems(searchedTrans);
    }

    public void exportTransactions() {
        try {
            if(searchedTrans == null){
                alertMessage.errorMessage("Nothing to export.");
                return;
            }
            String fileName = "transactions_" + dayFrom_field.toString() + "_to_" + dayTo_field.toString() + ".csv";
            FileWriter writer = new FileWriter(fileName);

            writer.write("Sender,Receiver,Amount,Date,Message\n");

            for (Transaction transaction : searchedTrans) {
                writer.write(transaction.senderProperty().get() + ",");
                writer.write(transaction.receiverProperty().get() + ",");
                writer.write(transaction.amountProperty().get() + ",");
                writer.write(transaction.dateProperty().get() + ",");
                writer.write(transaction.messageProperty().get() + "\n");
            }
            writer.close();
            alertMessage.successMessage("Transactions exported");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setExport_button(){
        boolean confirmed = alertMessage.confirmMessage("Do you want to export these/ this Transactions");
        if (confirmed) {
            exportTransactions();
        }
    }

    private void setDateFormat(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                return localDate != null ? dateFormatter.format(localDate) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
            }
        });
    }
}
