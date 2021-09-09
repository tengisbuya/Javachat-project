package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Login to JavaChat");

        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("sample.fxml"));

        Pane myPane = (Pane)myLoader.load();

        Controller controller = (Controller) myLoader.getController();
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
        System.out.println("start");
    }


    public static void main(String[] args) {
        launch(args);
        System.out.println("main");
    }
}
