package com.example.demo;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Affine;
import javafx.util.Duration;

public class game extends Application {

    private Image characterImage;
    private ImageView character;
    private double completedStickLength = 0;
    Platform platform1 = new Platform(300, 500, 200, 500); // Increased width of the platform
    Platform platform2 = new Platform(300, 500, 700, 500);
    private Pane root;
    private boolean canCreateStick = true;
    private double platformDistance; // Adjust the distance between platforms as needed
    private Line currentStickLine;
    private double defaultStickManX = 0;
    private double stickGrowthRate = 10; // Adjust the growth rate as needed

    private TranslateTransition translateTransition;
    private boolean isSpaceBarPressed = false;
    private boolean isStickCreated = false;
    private Scene scene = createGameScene();
    private boolean isCharacterMoving = false;

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        Image logoIcon = new Image("snake.jpg");


        // Load the video file
        Image gameBg = new Image("bg3.jpeg");
        ImageView gameBgImage = new ImageView(gameBg);
        gameBgImage.setFitWidth(1600);
        gameBgImage.setFitHeight(800);
        gameBgImage.setPreserveRatio(false);

        characterImage = new Image("/character.png");

        // Set the initial position of the character
        defaultStickManX = platform1.getX() + platform1.getWidth();
        character = new ImageView(characterImage);
        character.setFitWidth(50);
        character.setFitHeight(100);
        character.setTranslateX(defaultStickManX);
        character.setTranslateY(350);
        translateTransition = new TranslateTransition(Duration.millis(400), character);
        root = new Pane();

        // Create a StackPane and add the character (ImageView) and media view to it
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(gameBgImage);

        Button button = new Button("Go to Menu");
        button.setOnMouseClicked(event -> {
            switchToMenu(primaryStage);
        });

        // Add the stackPane to the root pane
        root.getChildren().add(stackPane);
        root.getChildren().add(button);
        // Add platform rectangles to the root
        root.getChildren().add(platform1.getRectangle());
        root.getChildren().add(platform2.getRectangle());

        // Add the character to the root pane
        root.getChildren().add(character);

        // Create the scene and set the root node
        scene = new Scene(root, 800, 800);

        // Set up key event handlers
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isSpaceBarPressed = true;
                createStick();
            }

            if (event.getCode() == KeyCode.R) {
                reset();
            }
            if (event.getCode() == KeyCode.Q) {
                switchPlatform();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isSpaceBarPressed = false;
                stopFallingStick();
                checkDistanceAndMove(); // Check distance and move character
            }
        });
        button.setFocusTraversable(false);
        button.setOnMouseEntered(event -> button.requestFocus());
        button.setOnMouseExited(event -> button.getParent().requestFocus());
        primaryStage.setTitle("Stick Hero Game");
        primaryStage.getIcons().add(logoIcon);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void fallStick() {
        if (currentStickLine != null) {
            // Calculate the length of the original stick
            double stickLength = Math.abs(currentStickLine.getEndY() - currentStickLine.getStartY());

            // Calculate the end point of the new horizontal stick
            double endX = currentStickLine.getStartX() + stickLength;
            double endY = currentStickLine.getStartY();

            // Create a new horizontal stick with the same start point and length as the original stick
            Line horizontalStick = new Line(currentStickLine.getStartX(), currentStickLine.getStartY(), endX, endY);
            horizontalStick.setStrokeWidth(currentStickLine.getStrokeWidth());
            horizontalStick.setStroke(currentStickLine.getStroke());

            // Store the vertical stick in a separate variable
            Line verticalStick = currentStickLine;
            root.getChildren().remove(currentStickLine);

            // Update the current stick line to the new horizontal stick
            Line horizontalStickLine = horizontalStick;

            // Add the new horizontal stick to the root children
            root.getChildren().add(horizontalStickLine);

            // Remove the original stick from the root children

        }
    }




    private void translateSmoothly(double deltaX, double deltaY) {
        translateTransition.setByX(deltaX);
        translateTransition.setByY(deltaY);
        translateTransition.play();
    }

    private double calculatePlatformDistance() {
        return platform2.getX() - (platform1.getX() + platform1.getWidth());
    }
    private void reset() {
        // Call initializeGame to reset the game to its initial state
        root.getChildren().remove(currentStickLine);
        character.setTranslateX(defaultStickManX);

    }
    private void createStick() {
        if (canCreateStick && !isCharacterMoving) {
            if (canCreateStick) {
                completedStickLength = 0;
                if (currentStickLine == null) {
                    double startX = platform1.getX() + platform1.getWidth();
                    double startY = platform1.getY(); // Changed to top edge

                    currentStickLine = new Line(startX, startY, startX, startY);
                    root.getChildren().add(currentStickLine);

                    translateTransition = new TranslateTransition(Duration.millis(400), character);
                    translateTransition.setByX(-character.getTranslateX() + currentStickLine.getStartX());
                    translateTransition.setByY(-character.getTranslateY() + currentStickLine.getStartY());

                    canCreateStick = false;
                    isStickCreated = false;

                    Timeline stickTimeline = new Timeline(new KeyFrame(
                            Duration.millis(50),
                            ae -> {
                                if (isSpaceBarPressed) {
                                    growStick();
                                } else {
                                    stopFallingStick();
                                    checkDistanceAndMove(); // Check distance and move character
                                }
                            }));
                    stickTimeline.setCycleCount(Timeline.INDEFINITE);
                    stickTimeline.play();
                }
            }
        }
    }

    private void stopFallingStick() {
        if (translateTransition != null) {
            translateTransition.stop();
        }
        fallStick();
        isStickCreated = true;

    }
    private void growStick() {
        if (currentStickLine != null) {
            // Calculate the new endY position for the stick
            double newEndY = currentStickLine.getEndY() - stickGrowthRate;

            // Ensure that the newEndY is not above the starting point
            if (newEndY >= 0) {
                KeyValue keyValue = new KeyValue(currentStickLine.endYProperty(), newEndY);
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.10), keyValue);
                Timeline growthTimeline = new Timeline(keyFrame);

                growthTimeline.setOnFinished(event -> {
                    // Update completedStickLength after the stick growth is complete
                    completedStickLength = currentStickLine.getStartY() - currentStickLine.getEndY();
                    checkStickCompletion();
                });

                growthTimeline.play();
            } else {
                // If the newEndY is above the starting point, stop growing the stick
                stopFallingStick();
            }
        }
    }


    private void checkStickCompletion() {
        platformDistance = platform1.calculateDistanceToNextPlatform(platform2);
        if (completedStickLength >= platformDistance && isStickCreated) {

            // Stick is long enough, perform character movement
            double endX = character.getTranslateX() + platformDistance + platform2.getWidth();
            double endY = character.getTranslateY();
            moveCharacter(endX, endY);
        }
    }



    private void checkDistanceAndMove() {
        if (currentStickLine != null && isStickCreated) {
            platformDistance = platform1.calculateDistanceToNextPlatform(platform2);
            if (completedStickLength >= platformDistance) {
                double endX = platform2.getX() + platform2.getWidth();
                double endY = character.getTranslateY();
                moveCharacter(endX, endY);
            } else {
                resetGame();
            }
        }
    }


    private void moveCharacter(double endX, double endY) {
        TranslateTransition moveTransition = new TranslateTransition(Duration.seconds(2), character);
        moveTransition.setToX(endX);
        moveTransition.setToY(endY);
        moveTransition.setOnFinished(event -> {
            switchPlatform();
            // Reset the game state for the next platform
            canCreateStick = true;
            currentStickLine = null;
        });
        moveTransition.play();
    }



    private void resetGame() {
        // Properly reset the character's position
        character.setTranslateX(defaultStickManX);
        character.setTranslateY(350);

        // Implement the logic to reset the game to its initial state
        // You may need to stop animations, clear elements, and set initial positions.
//        root.getChildren().remove(currentStickLine);
//        currentStickLine = null;

        // Reset the flag when the game is reset
        canCreateStick = true;
    }

    private void switchPlatform() {
        if (currentStickLine != null) {
            root.getChildren().remove(currentStickLine);
        }

        // Check if a new platform is needed
        if (character.getTranslateX() >= platform2.getX() + platform2.getWidth()) {
            generateNewPlatform();
        }

        // Reset the game state
//        currentStickLine = null;
        canCreateStick = true;
    }

    private void generateNewPlatform() {
        // Generate a new platform with random width and position
        platform1 = platform2;

        // Generate a random gap between 50 and 500
        double gap = 50 + Math.random() * 450;

        platform2 = new Platform(300, 500, platform2.getX() + platform2.getWidth() + gap, 500);

        // Add the new platform to the root
        root.getChildren().add(platform2.getRectangle());

        // Move the character to the starting position of the new platform
        double startX = platform1.getX() + platform1.getWidth();
        double startY = platform1.getY();
        character.setTranslateX(startX);
        character.setTranslateY(startY);
    }


    private void switchToMenu(javafx.stage.Stage primaryStage) {
        VBox menuPane = new VBox(10); // 10 is the spacing between elements
        menuPane.setAlignment(Pos.CENTER); // Align elements to the center

        Image bgImage = new Image("/menubg.jpeg");
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(1600);
        bgImageView.setFitHeight(800);
        bgImageView.setPreserveRatio(false);

        // Create buttons
        Button playButton = new Button("Play");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        // Apply CSS styles to the buttons
        playButton.getStyleClass().add("button");
        settingsButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("button");
        playButton.setStyle("-fx-background-color: #ff0000;");

        // Set button actions
        playButton.setOnAction(event -> primaryStage.setScene(scene));
        settingsButton.setOnAction(event -> {
            // Handle settings action
        });
        exitButton.setOnAction(event -> javafx.application.Platform.exit());

        // Add buttons to the pane
        menuPane.getChildren().addAll(playButton, settingsButton, exitButton);

        // Create a StackPane and add the ImageView and VBox to it
        StackPane stackPane = new StackPane(bgImageView, menuPane);

        Scene menuScene = new Scene(stackPane, 1600, 800);
        menuScene.getStylesheets().add(getClass().getResource("/menustyle.css").toExternalForm());

        primaryStage.setScene(menuScene);
    }
    private Scene createGameScene() {
        // Implement logic to create and return the game scene
        Pane gamePane = createGamePane();
        return new Scene(gamePane, 1600, 800);
    }

    private Pane createGamePane() {
        // Implement logic to create and return the game pane
        Pane gamePane = new Pane();
        // Add game elements and set up event handlers as needed.
        return gamePane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
