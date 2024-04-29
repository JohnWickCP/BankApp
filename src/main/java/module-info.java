module com.example.bankapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens CHBank to javafx.fxml;
    exports CHBank.Controller;
    exports CHBank;
    exports CHBank.Controller.Client;
    exports CHBank.Controller.Admin;
    exports CHBank.Views;
    exports CHBank.Models;


}