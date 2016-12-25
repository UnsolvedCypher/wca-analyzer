package me.matthewmcmillan.wcaanalyzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

public class Main extends Application {
    public static ArrayList<Competition> comps;
    public static ArrayList<Event> events;
    public static String competitorName;
    public static String WCAID;
    public static Scene mainScene;
    public static Parent mainParent;
    public static Stage mainStage;
    public static StartPageController mainController;
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getResource("StartPage.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) fxmlLoader.load(location.openStream());
        mainParent = root;
        mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        mainStage = primaryStage;
        StartPageController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        controller.showStage();
        mainController = controller;
    }






    public static void main(String[] args) {
        launch(args);
    }
}
