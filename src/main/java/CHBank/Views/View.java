package CHBank.Views;

import CHBank.Controller.Client.ClientController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class View {
    // Client views

    private final StringProperty clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane transactionsView;
    private AnchorPane accountsView;

    //
    public View() {
        this.clientSelectedMenuItem = new SimpleStringProperty("");
    }
    /* Client Views Section*/
    public StringProperty getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getTransactionsView() {
        if (transactionsView == null) {
            try {
                transactionsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Transaction.fxml")).load();
                transactionsView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Transaction.css")).toExternalForm());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return transactionsView;
    }

    public AnchorPane getAccountsView() {
        if (accountsView == null) {
            try {
                accountsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Accounts.fxml")).load();
                accountsView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Accounts.css")).toExternalForm());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return accountsView;
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        List<String> stylesheets = new ArrayList<String>();
        stylesheets.add("/Styles/login.css");
        createStage(loader,stylesheets);
    }

    public void showClientWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        List<String> stylesheets = new ArrayList<String>();
        stylesheets.add("/Styles/Dashboard.css");
        stylesheets.add("/Styles/ClientMenu.css");
        createStage(loader, stylesheets);
    }


    private void createStage(FXMLLoader loader, List<String> stylesheets) {
        try {
            Scene scene = new Scene(loader.load());
            for (String stylesheet : stylesheets) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(stylesheet)).toExternalForm());
            }
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setTitle("CHBank");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStage (Stage stage) {
        stage.close();
    }
}
