package me.matthewmcmillan.wcaanalyzer;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Main extends Application {
    public static ArrayList<Competition> comps;
    public static ArrayList<Event> events;
    public static String competitorName;
    public static String WCAID;
    public static Scene mainScene;
    public static Parent mainParent;
    public static Stage mainStage;
    public static final String VERSION = "2.1";
    public static StartPageController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale.setDefault(Locale.US);
        URL location = getClass().getResource("StartPage.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());
        mainParent = root;
        mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        mainStage = primaryStage;
        mainStage.setResizable(false);
        StartPageController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        controller.reset();
        controller.showStage();
        mainController = controller;
    }

    public static void main(String[] args) {
        launch(args);
    }



    public static StringConverter<Number> dateStringConverter = new StringConverter<Number>() {
        @Override
        public String toString(Number object) {
            // add a day to correct for leap years
            return LocalDate.ofEpochDay((object).longValue() + 1).format(DateTimeFormatter.ofPattern("yyyy"));
        }

        @Override
        public Number fromString(String string) {
            return LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy")).toEpochDay();
        }
    };

    public static StringConverter<Double> doubleDateStringConverter = new StringConverter<Double>() {
        @Override
        public String toString(Double object) {
            // add a day to correct for leap years
            return LocalDate.ofEpochDay((object).longValue() + 1).format(DateTimeFormatter.ofPattern("yyyy"));
        }

        @Override
        public Double fromString(String string) {
            return (double)LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy")).toEpochDay();
        }
    };
}
