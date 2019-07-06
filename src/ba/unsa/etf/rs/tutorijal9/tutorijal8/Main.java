package ba.unsa.etf.rs.tutorijal9.tutorijal8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavni.fxml"));
        GlavniController ctrl = new GlavniController();
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Vozaci i busevi");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}