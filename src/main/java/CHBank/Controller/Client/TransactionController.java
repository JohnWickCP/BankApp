package CHBank.Controller.Client;

import CHBank.Models.Model;
import CHBank.Models.Transaction;
import CHBank.Views.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    public ListView<Transaction> transaction_Listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAllTransactions();
        transaction_Listview.setItems(Model.getInstance().getAllTransactions());
        transaction_Listview.setCellFactory(_ -> new TransactionCellFactory());
    }

    private void initAllTransactions(){
        if(Model.getInstance().getAllTransactions().isEmpty()){
            Model.getInstance().setAllTransactions();
        }
    }
}
