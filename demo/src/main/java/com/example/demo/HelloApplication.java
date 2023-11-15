package com.example.demo;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

public class HelloApplication extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Stage stage = new Stage();

        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/StickmanStyle.css").toExternalForm());


        Image logoicon = new Image("snake.jpg");


        StickMan stickman = new StickMan();
        stickman.setLayoutX(50);
        stickman.setLayoutY(315);

        stickman.getStyleClass().add("StickMan");

        root.getChildren().add(stickman);

        primaryStage.getIcons().add(logoicon);
        primaryStage.setTitle("StickMan Game");

//        primaryStage.setResizable(false);

//        primaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitHint("Fill in the blank to Exit Full Screen           shashwat is ______ . 1. gay   2.straight   3.Lesbian");
//        primaryStage.setFullScreenExitKeyCombination(KeyCombination.valueOf("1"));

        primaryStage.setScene(scene);
        BackgroundImage backgroundImage = new BackgroundImage(new Image("/Wano Kuni.jpg", 800, 1000, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        root.setBackground(background);
        primaryStage.show();

    }
}
