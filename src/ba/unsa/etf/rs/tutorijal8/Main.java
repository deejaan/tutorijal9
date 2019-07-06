package ba.unsa.etf.rs.tutorijal8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        TransportDAO model = TransportDAO.getInstance();
        model.ucitajBuseve();
        model.ucitajVozace();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transport.fxml"));
        loader.setController(new Controller(model));
        Parent root = loader.load();
        primaryStage.setTitle("Transport");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}