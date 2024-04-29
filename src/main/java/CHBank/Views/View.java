package CHBank.Views;

import CHBank.Controller.Admin.AdminController;
import CHBank.Controller.Client.ClientController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class View {
    private AccountType loginAccountType;
    // Client views

    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane transactionsView;
    private AnchorPane accountsView;

    // Admin views
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane createClientsView;
    private AnchorPane clientsView;
    private AnchorPane depositsView;
    //
    public View() {
        this.loginAccountType = AccountType.CLIENT;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }
    /* Client Views Section*/
    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
                dashboardView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Dashboard.css")).toExternalForm());
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

    /* Admin views section */
    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem(){
        return adminSelectedMenuItem;
    }

    public AnchorPane getCreateClientsView() {
        if (createClientsView == null) {
            try {
                createClientsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/CreateClient.fxml")).load();
                clientsView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/CreateClient.css")).toExternalForm());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return createClientsView;
    }

    public AnchorPane getClientsView() {
        if (clientsView == null) {
            try {
                clientsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clients.fxml")).load();
                clientsView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Clients.css")).toExternalForm());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return clientsView;
    }

    public AnchorPane getDepositsView() {
        if (depositsView == null){
            try {
                depositsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Deposit.fxml")).load();
                depositsView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Deposit.css")).toExternalForm());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return depositsView;
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

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        List<String> stylesheets = new ArrayList<String>();
        stylesheets.add("/Styles/CreateClient.css");
        stylesheets.add("/Styles/AdminMenu.css");
        createStage(loader, stylesheets);
    }

    private void createStage(FXMLLoader loader, List<String> stylesheets) {
        try {
            Scene scene = new Scene(loader.load());
            for (String stylesheet : stylesheets) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(stylesheet)).toExternalForm());
            }
            Stage stage = new Stage();
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/BankIcon.png"))));
            stage.setResizable(false);
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
