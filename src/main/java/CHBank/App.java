package CHBank;

import CHBank.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;



public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Model.getInstance().getView().showLoginWindow();
    }
}
