package CHBank.Views;

import CHBank.Controller.Client.ClientController;
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
    private AnchorPane dashboardView;

    public View() {}

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
