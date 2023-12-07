//package com.example.demo;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.Pane;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.util.ArrayList;
//
//public class Stickmangame extends Application {
//
//    private static final double PLATFORM_HEIGHT = 150;
//    private static double platform_distance;
//
//    AnchorPane root;
//    Stickman2 stickman;
//    Platform platform;
//
//    int status=1;
//    private Timeline growingTimeline;
//    private Timeline walkingTimeline;
//
//    boolean stickSpawned = false;
//    boolean isWalking = false;
//
//    boolean growingInProgress;
//
//    Scene scene;
//
//
//    ArrayList<Double> newrect = new ArrayList<Double>();
//
//    @Override
//    public void start(Stage primaryStage) {
//        root = new AnchorPane();
//        scene = new Scene(root, 800, 600);
//
//        initializePlatform();
//        initializeStickman();
//
//        Button button = new Button("Start Game");
//        Pane root1=new Pane();
//        Scene scene1 = new Scene(root1, 800, 400);
//        root1.getChildren().add(button);
//
//        button.setOnAction(e -> {
//            primaryStage.setScene(scene);
//            primaryStage.setResizable(false);
//            startGameLoop();
//        });
//
//        primaryStage.setTitle("Stickman Game");
//        primaryStage.setScene(scene1);
//        primaryStage.setResizable(false);
//        primaryStage.show();
//    }
//
//
//    private void initializePlatform() {
//        platform = new Platform(root);
//        platform.initialize();
//        platform_distance = platform.getDistance();
//    }
//
//    private void initializeStickman() {
//        stickman = new Stickman(root);
//        stickman.initialize();
//    }
//
//    private void startGameLoop() {
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.SPACE && !stickSpawned && !growingInProgress) {
//                startGrowingStick();
//                stickSpawned = true;
//                growingInProgress = false;
//            }
//        });
//
//        scene.setOnKeyReleased(event -> {
//            if (event.getCode() == KeyCode.SPACE && !growingInProgress) {
//                growingInProgress=true;
//                stopGrowingStick(platform, this);
//            }
//            startGameLoop();
//        });
//
//    }
//
//    /*private void gameloop() {
//        Timeline gameLoopTimeline = new Timeline(
//                new KeyFrame(Duration.millis(10), e -> {
//                    if (isWalking && !stickman.isWalking()) {
//                        stickSpawned = false;
//                        isWalking = false;
//                        stickman.resetPosition(platform);
//                        startGrowingStick();  // Start growing stick again after the platform shifts
//                    }
//                })
//        );
//        gameLoopTimeline.setCycleCount(Timeline.INDEFINITE);
//        gameLoopTimeline.play();
//    }*/
//
//    private void startGrowingStick() {
//        stickman.startGrowing();
//        growingTimeline = new Timeline(
//                new KeyFrame(Duration.millis(10), e -> growStick())
//        );
//        growingTimeline.setCycleCount(Timeline.INDEFINITE);
//        growingTimeline.play();
//    }
//
//    private void growStick() {
//        stickman.grow();
//    }
//
//    private void stopGrowingStick(Platform platform, StickmanGame stickmangame) {
//        growingTimeline.stop();
//        stickman.stopGrowing(platform, this);
//        stickman.startWalking(platform, this);
//        isWalking = true;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
