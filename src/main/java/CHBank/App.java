package CHBank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Scene scene = new Scene(fxmlLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Login.fxml")).openStream()));
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/login.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Bank");
        stage.show();
    }
}
