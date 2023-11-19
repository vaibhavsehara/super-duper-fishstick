package com.example.demo;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MoveTo;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Line;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Path;


public class game extends Application {

    private ImageView character;
    private Pane root;
    private MediaPlayer mediaPlayer;
    private Line currentStickLine;
    private PathTransition fallTransition;
    private TranslateTransition fallTranslateTransition = new TranslateTransition();

    private double stickGrowthRate = 15; // Adjust the growth rate as needed

    private TranslateTransition translateTransition;
    private boolean isSpaceBarPressed = false;
    private Scene scene = createGameScene();

    @Override
    public void start(Stage primaryStage) {
        Image logoicon = new Image("snake.jpg");

        // Load the video file
        Media media = new Media(getClass().getResource("/bgAnimation.mp4").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the video indefinitely

        // Create a MediaView to display the video
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(1600);
        mediaView.setFitHeight(800);
        mediaView.setPreserveRatio(false);

        Image image = new Image("/character.png"); // Replace with the actual image for your character
        character = new ImageView(image);

        // You can set the size of the image view as needed
        character.setFitWidth(100);
        character.setFitHeight(200);

        translateTransition = new TranslateTransition(Duration.millis(200), character);

        // Set the initial position of the character
        character.setTranslateX(200);

        root = new Pane();

        // Create a StackPane and add the character (ImageView) and media view to it
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(mediaView);

        Button button = new Button("Go to Menu");
        button.setOnMouseClicked( event -> {
            switchToMenu(primaryStage);
        });


        // Create platforms
        Platform platform1 = new Platform(300, 20, 200,500); // Increased width of the platform
        Platform platform2 = new Platform(300, 20, 400, 500); // Increased width of the platform

        // Add the stackPane to the root pane
        root.getChildren().add(stackPane);
        root.getChildren().add(button);
//         Add platform rectangles to the root
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


            if (event.getCode() == KeyCode.A && event.isShiftDown()) {
                // Diagonal movement (left-up)
                translateSmoothly(-2, -2);
            }
            if (event.getCode() == KeyCode.D && event.isShiftDown()) {
                // Diagonal movement (right-up)
                translateSmoothly(2, -2);
            }
            if (event.getCode() == KeyCode.A && event.isControlDown()) {
                // Diagonal movement (left-down)
                translateSmoothly(-2, 2);
            }
            if (event.getCode() == KeyCode.D && event.isControlDown()) {
                // Diagonal movement (right-down)
                translateSmoothly(2, 2);
            }

            if (event.getCode() == KeyCode.R) {
                resetGame();
            }
        });


        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isSpaceBarPressed = false;
                stopFallingStick();
                fallStick();
            }
        });
        button.setFocusTraversable(false);
        button.setOnMouseEntered(event -> button.requestFocus());
        button.setOnMouseExited(event -> button.getParent().requestFocus());
        primaryStage.setTitle("Stick Hero Game");
        primaryStage.getIcons().add(logoicon);
        primaryStage.setScene(scene);
        primaryStage.show();


        mediaPlayer.play();
    }

    private void translateSmoothly(double deltaX, double deltaY) {
        translateTransition.setByX(deltaX);
        translateTransition.setByY(deltaY);
        translateTransition.play();
    }

    private void createStick() {
        // Create a stick starting from the character's position if it doesn't exist
        if (currentStickLine == null) {
            currentStickLine = new Line(character.getTranslateX() + character.getFitWidth() / 2,
                    character.getTranslateY() + character.getFitHeight(), character.getTranslateX() + character.getFitWidth() / 2,
                    character.getTranslateY() + character.getFitHeight());
            root.getChildren().add(currentStickLine);
        }

        // Set up a timeline to continuously grow the stick while space bar is held down
        Timeline stickTimeline = new Timeline(new KeyFrame(
                Duration.millis(50),
                ae -> {
                    if (isSpaceBarPressed) {
                        growStick();
                    }
                }));
        stickTimeline.setCycleCount(Timeline.INDEFINITE);
        stickTimeline.play();

    }

    private void growStick() {
        if (currentStickLine != null) {
            KeyValue keyValue = new KeyValue(currentStickLine.endYProperty(), currentStickLine.getEndY() + stickGrowthRate);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
            Timeline growthTimeline = new Timeline(keyFrame);
            growthTimeline.play();
        }
    }

    private void stopFallingStick() {
        if (fallTransition != null) {
            fallTransition.stop();
        }
    }

    private void fallStick() {
        if (currentStickLine != null) {
            double startX = currentStickLine.getStartX();
            double startY = currentStickLine.getStartY();
            double endY = currentStickLine.getEndY();

            Path path = createFallingPath(startX, startY, endY - startY);

            fallTransition = new PathTransition();
            fallTransition.setNode(currentStickLine);
            fallTransition.setPath(path);
            fallTransition.setCycleCount(1);
            fallTransition.setInterpolator(javafx.animation.Interpolator.LINEAR);
            fallTransition.setDuration(Duration.seconds(2));

            // Initialize the fallTranslateTransition
            fallTranslateTransition = new TranslateTransition(Duration.seconds(2), character);

            // Move the character to the right by the length of the stick
            fallTranslateTransition.setByX(endY - character.getTranslateX());

            fallTransition.setOnFinished(event -> {
                // Play the character movement transition after the stick falls
                fallTranslateTransition.play();
            });

            fallTransition.play();
        }
    }

    private Path createFallingPath(double startX, double startY, double height) {
        Path path = new Path();
        path.getElements().add(new MoveTo(startX, startY));

        // Create a quarter circle
        for (double t = 0; t <= Math.PI / 2; t += 0.01) {
            double x = startX + height * Math.sin(t);
            double y = startY + height * (1 - Math.cos(t));
            path.getElements().add(new javafx.scene.shape.LineTo(x, y));
        }

        return path;
    }





    private void resetGame() {
        // Implement the logic to reset the game to its initial state
        // You may need to stop animations, clear elements, and set initial positions.
        root.getChildren().remove(currentStickLine);
        currentStickLine = null;
        translateTransition.stop();
        character.setTranslateX(200); // Set the initial position of the character
    }
    private void switchToMenu(Stage primaryStage) {
        StackPane menuPane = new StackPane();
        Scene menuScene = new Scene(menuPane, 800, 800);

        Image bgimage = new Image("/menubg.jpeg");
        ImageView bgimageView = new ImageView(bgimage);
        bgimageView.setFitWidth(1600);
        bgimageView.setFitHeight(800);
        bgimageView.setPreserveRatio(false);
        menuPane.getChildren().add(bgimageView);

        Button button = new Button("Go to Game");
        button.setOnMouseClicked( event ->{

            primaryStage.setScene( scene );
        });
        button.setFocusTraversable(false);
        button.setOnMouseEntered(event -> button.requestFocus());
        button.setOnMouseExited(event -> button.getParent().requestFocus());
        menuPane.getChildren().add(button);
        primaryStage.setScene(menuScene);
    }
    private Scene createGameScene() {
        // Implement logic to create and return the game scene
        Pane gamePane = createGamePane();
        return new Scene(gamePane, 800, 800);
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
