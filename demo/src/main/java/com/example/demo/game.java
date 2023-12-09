package com.example.demo;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.util.Duration;

public class game extends Application {

    private Image characterImage;
    private double count = 0;
    private double Score = 0;
    private ImageView character;
    private ImageView ScoreSigil;
    private double completedStickLength = 0;
    private Timeline stickTimeline;
    Platform platform1 = new Platform(300, 500, 200, 500); // Increased width of the platform
    Platform platform2 = new Platform(100, 500, 700, 500);
    Platform platform3;
    private AnchorPane root;
    private boolean Inverted = false;
    private Line createdStick;
    private boolean isMoving = false;
    private boolean isCharacterAtEndPoint = false;
    private boolean isSpaceBarLocked = false;
    private boolean canCreateStick = true;
    private double platformDistance; // Adjust the distance between platforms as needed
    private Line currentStickLine;
    private double defaultStickManX = 0;
    private double stickGrowthRate = 10; // Adjust the growth rate as needed

    private TranslateTransition translateTransition;
    ImageView Sigil;
    private boolean isSigilCollected = false;
    private boolean isSpaceBarPressed = false;
    private boolean isCharacterMoved = false;
    private boolean isComponentsMoved = false;

    private boolean isStickCreated = false;
    private Scene scene = createGameScene();
    private boolean isCharacterMoving = false;
    private double gap;
    private boolean characterCollided = false;
    private Scene createGameScene( javafx.stage.Stage primaryStage){

        return new Scene(root, 1600, 800);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        ScoreSigil = new ImageView(new Image("anemo.png"));
        ScoreSigil.setFitHeight(30);
        ScoreSigil.setFitWidth(30);
        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #011627;"); // adjust the style as needed

// Position the ScoreSigil and scoreLabel at the top right
        HBox scoreBox = new HBox(ScoreSigil, scoreLabel);
        scoreBox.setAlignment(Pos.TOP_RIGHT);

// Add the scoreBox to the root pane


// Update the scoreLabel text whenever the score changes
// This should be done in your game loop or wherever you update the score


        Sigil = new ImageView(new Image("anemo.png"));
        Sigil.setFitHeight(20);
        Sigil.setFitWidth(20);


        double gapStart = platform1.getX() + platform1.getWidth();
        double gapEnd = platform2.getX();
        double gapMiddle = (gapStart + gapEnd) / 2;
        Sigil.setX(gapMiddle);
        Sigil.setY(platform1.getY() + 10);

        Image logoIcon = new Image("snake.jpg");

        platform3 = new Platform(300, 400, 1200, 500);
        // Load the video file
        Image gameBg = new Image("mondstat.jpeg");
        ImageView gameBgImage = new ImageView(gameBg);
        gameBgImage.setFitWidth(1600);
        gameBgImage.setFitHeight(800);
        gameBgImage.setPreserveRatio(false);

        characterImage = new Image("Lumine.png");

        // Set the initial position of the character
        defaultStickManX = platform1.getX() + platform1.getWidth();
        character = new ImageView(characterImage);
        character.setFitWidth(50);
        character.setFitHeight(70);
        character.setTranslateX(defaultStickManX-50);
        character.setTranslateY(435);
        translateTransition = new TranslateTransition(Duration.millis(400), character);
        root = new AnchorPane();

        // Create a StackPane and add the character (ImageView) and media view to it
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(gameBgImage);

        Button button = new Button("Go to Menu");
        button.setOnMouseClicked(event -> {
            switchToMenu(primaryStage);
        });

        // Add the stackPane to the root pane
        root.getChildren().add(stackPane);
        Sigil.setY(platform1.getY() + 10);
        root.getChildren().add(button);
        root.getChildren().add(scoreBox);
        AnchorPane.setTopAnchor(scoreBox, 10.0); // 10 units down from the top edge
        AnchorPane.setRightAnchor(scoreBox, 10.0);

        scoreLabel.setText("Score: " + Score);
        // Add platform rectangles to the root
        root.getChildren().add(platform1.getRectangle());
        root.getChildren().add(platform2.getRectangle());
        root.getChildren().add(Sigil);

        // Add the character to the root pane
        root.getChildren().add(character);

        // Create the scene and set the root node
        scene = new Scene(root, 800, 800);

        // Set up key event handlers
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isCharacterMoved = false;
                isSpaceBarPressed = true;
                if(canCreateStick && !isSpaceBarLocked){
                createStick();
            }}

            if (event.getCode() == KeyCode.R) {
                reset();
            }
            if (event.getCode() == KeyCode.Q) {
                switchPlatform();
            }
            if (event.getCode() == KeyCode.I){
                if(!Inverted && isCharacterBetweenGap()){
               character.setRotate(180);
               character.setLayoutY(character.getLayoutY()+60);
            }else if (Inverted && isCharacterBetweenGap()){
                    character.setRotate(0);
                    character.setLayoutY(character.getLayoutY()-60);
                }
                Inverted = !Inverted;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isSpaceBarLocked = true;
                isSpaceBarPressed = false;
                stopFallingStick();

            }
        });
        button.setFocusTraversable(false);
        button.setOnMouseEntered(event -> button.requestFocus());
        button.setOnMouseExited(event -> button.getParent().requestFocus());
        primaryStage.setTitle("Stick Hero Game");
        primaryStage.getIcons().add(logoIcon);
        primaryStage.setScene(scene);
        javafx.application.Platform.runLater(() -> primaryStage.setMaximized(true));
        switchToMenu(primaryStage);
        primaryStage.show();
    }


    private void fallStick() {
        canCreateStick = false;
        // Calculate the length of the stick
        double length = Math.sqrt(Math.pow(createdStick.getStartX() - createdStick.getEndX(), 2) +
                Math.pow(createdStick.getStartY() - createdStick.getEndY(), 2));

        // Calculate the endX property of the stick after rotation
        double endX = currentStickLine.getStartX() + length * Math.cos(Math.toRadians(90));

        // Create a KeyValue for the endX and endY properties of the stick
        KeyValue kvX = new KeyValue(currentStickLine.endXProperty(), createdStick.getEndX() + length);
        KeyValue kvY = new KeyValue(currentStickLine.endYProperty(), currentStickLine.getStartY());

        // Create a KeyFrame with the KeyValue and a duration of 0.5 seconds
        KeyFrame kf = new KeyFrame(Duration.seconds(0.2), kvX, kvY);

        // Create a Timeline with the KeyFrame
        Timeline timeline = new Timeline(kf);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event ->{
                checkDistanceAndMove();
            });
            // Call the checkDistanceAndMove method here after the stick has completed its fall animat

        // Play the Timeline
        timeline.play();
        pause.play();


    }

    private void reset() {
        // Reset the character's position
        character.setTranslateX(defaultStickManX);
        character.setTranslateY(400); // Assuming 350 is the initial Y position of the character

        // Allow the creation of a new stick
        canCreateStick = true;

        // Remove the current stick line if it exists
        if (currentStickLine != null) {
            root.getChildren().remove(currentStickLine);
            currentStickLine = null;
        }

        // Reset the completed stick length
        completedStickLength = 0;
        root.getChildren().remove(platform3.getRectangle());
        root.getChildren().remove(platform2.getRectangle());

        platform1 = new Platform(300, 400, 200, 500); // Increased width of the platform
        platform2 = new Platform(300, 400, 700, 500);


    }

    private void createStick() {
        if(isMoving || !canCreateStick){
            return;
        }
        if ( !isMoving) {
            if (canCreateStick) {
                if (currentStickLine == null) {
                    completedStickLength = 0;
                    double startX = platform1.getX() + platform1.getWidth();
                    double startY = platform1.getY(); // Changed to top edgew

                    currentStickLine = new Line(startX, startY, startX, startY);
                    currentStickLine.setEndX(platform1.getX() + platform1.getWidth());
                    currentStickLine.setEndY(platform1.getY());

                    root.getChildren().add(currentStickLine);

                    translateTransition = new TranslateTransition(Duration.millis(400), character);
                    translateTransition.setByX(-character.getTranslateX() + currentStickLine.getStartX());
                    translateTransition.setByY(-character.getTranslateY() + currentStickLine.getStartY());
                    if (stickTimeline != null) {
                        stickTimeline.stop();
                        stickTimeline = null;
                    }
                    stickTimeline = new Timeline(new KeyFrame(
                            Duration.millis(50),
                            ae -> {
                                if (currentStickLine != null) {
                                    if (isSpaceBarPressed && !isSpaceBarLocked) {
                                        growStick();
                                        createdStick = new Line(currentStickLine.getStartX(), currentStickLine.getStartY(), currentStickLine.getEndX(), currentStickLine.getEndY());

                                    } else {
                                        stopFallingStick();
                                    }
                                }
                            }));
                    stickTimeline.setCycleCount(Timeline.INDEFINITE);
                    stickTimeline.play();
                }
                canCreateStick = false;
            }
        }
    }
    private boolean isCharacterBetweenGap() {
        double characterX = character.getTranslateX();
        double gapStart = platform1.getX() + platform1.getWidth();
        double gapEnd = platform2.getX();
        return characterX > gapStart && characterX < gapEnd;
    }
    private void stopFallingStick() {
        if (translateTransition != null) {
            translateTransition.stop();
        }
        if (currentStickLine != null) {
            fallStick();
        }
        isStickCreated = true;
    }


    private void growStick() {
        if (currentStickLine != null && !isMoving) {
            double newEndY = currentStickLine.getEndY() - stickGrowthRate;
            if (newEndY >= 0) {
                KeyValue keyValue = new KeyValue(currentStickLine.endYProperty(), newEndY);
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.10), keyValue);
                Timeline growthTimeline = new Timeline(keyFrame);

                growthTimeline.setOnFinished(event -> {
                    completedStickLength = currentStickLine.getStartY() - currentStickLine.getEndY();
                    checkStickCompletion();
                });

                growthTimeline.play();
            } else {
                stopFallingStick();
            }
        }
    }


    private void checkStickCompletion() {
        platformDistance = platform1.calculateDistanceToNextPlatform(platform2);
        if (completedStickLength >= platformDistance && isStickCreated) {

            // Stick is long enough, perform character movement
            double endX = character.getTranslateX() + platformDistance + platform2.getWidth() - 60;
            double endY = character.getTranslateY();
//            moveAllComponents();
        }
    }


    private void moveCharacter(double endX, double endY) {
        if (!isMoving && !isCharacterAtEndPoint &&!isCharacterMoved) {
            isMoving = true;

            // Create a KeyValue for the X and Y properties of the character
            KeyValue kvX = new KeyValue(character.translateXProperty(), endX);
            KeyValue kvY = new KeyValue(character.translateYProperty(), endY);

            // Create a KeyFrame with the KeyValue and a duration of 2 seconds
            KeyFrame kf = new KeyFrame(Duration.seconds(2), kvX, kvY);

            // Create a Timeline with the KeyFrame
            Timeline timeline = new Timeline(kf);

            // Set the onFinished event handler for the Timeline
            timeline.setOnFinished(event -> {
                isMoving = false;
                isCharacterAtEndPoint = true;
                isCharacterMoved = true;
                if( !characterCollided){
                moveAllComponents();}
                double updatedX = platform1.getX();
                double updatedY = platform1.getY();
                canCreateStick = true;
            });

            // Play the Timeline
            timeline.play();
        }
    }

    private void checkDistanceAndMove() {
        if(!isCharacterMoved){
            if (currentStickLine != null && isStickCreated ) {
                platformDistance = platform1.calculateDistanceToNextPlatform(platform2);
                double distanceToEndOfNextPlatform = platformDistance + platform2.getWidth();
                if (completedStickLength >= platformDistance && completedStickLength <= distanceToEndOfNextPlatform) {
                    double endX = platform2.getX() + platform2.getWidth() - 60;
                    double endY = character.getTranslateY();

                    moveCharacter(endX, endY);
                } else {
                    fallCharacter();
                }
            }
        }
        if (character.getBoundsInParent().intersects(Sigil.getBoundsInParent()) && Inverted)  {
            // Step 5: If the character has collected the sigil, increase the score and remove the sigil from the root pane
            Score++; // assuming you have a score variable
            root.getChildren().remove(Sigil);}
        if (character.getBoundsInParent().intersects((platform2.getRectangle().getBoundsInParent())) && Inverted) {
            if (character.getTranslateX() + character.getFitWidth() >= platform2.getX()) {
                StopCharacter();
                fallCharacterVertically();
                characterCollided = true;
            }
        }
    }
    private void StopCharacter() {
        if (translateTransition != null) {
            translateTransition.stop();
        }
    }
    private void fallCharacterVertically() {
        // Set the character's X position to the leftmost edge of the platform
        character.setTranslateX(platform2.getX());

        // Calculate the distance to fall
        double distance = root.getHeight() - character.getTranslateY();

        // Calculate the time it takes for the character to fall
        double time = 2; // Adjust the speed as needed

        // Create a KeyValue for the falling action
        KeyValue fallKeyValue = new KeyValue(character.translateYProperty(), character.getTranslateY() + distance);

        // Create a KeyFrame for the falling action
        KeyFrame fallKeyFrame = new KeyFrame(Duration.seconds(time), fallKeyValue);

        // Create a Timeline for the falling action
        Timeline fallTimeline = new Timeline(fallKeyFrame);

        fallTimeline.setOnFinished(fallEvent -> {
            // Remove the character from the root pane once it has fallen
            root.getChildren().remove(character);

            // Show the retry screen
            if (root.getScene().getWindow() instanceof javafx.stage.Stage) {
                showRetryScreen((javafx.stage.Stage) root.getScene().getWindow());
            }
        });

        fallTimeline.play();
    }
    private void moveAllComponents() {
        if (!isMoving) {
            isMoving = true;

            // Calculate the distance to move
            double distance = platform2.getX();

            // Calculate the time it takes for the character to complete its journey
            double time = 1; // Adjust the speed as needed

            // Create and play the transition for the character
            TranslateTransition characterTransition = new TranslateTransition(Duration.seconds(time), character);
            characterTransition.setByX(-distance);
            characterTransition.setOnFinished(event -> {
                isMoving = false;
                canCreateStick = true;
                isCharacterAtEndPoint = true;
                isStickCreated = false;
                isCharacterMoved = true;
                root.getChildren().remove(currentStickLine);
                currentStickLine = null;
                switchPlatform();
            });
            characterTransition.play();

            TranslateTransition stickTransition = new TranslateTransition(Duration.seconds(time), currentStickLine);
            stickTransition.setByX(-distance);
            stickTransition.play();
            System.out.println(platform1.getX());
            System.out.println(platform2.getX());
            // Create and play the transition for platform1
            TranslateTransition platform1Transition = new TranslateTransition(Duration.seconds(time), platform1.getRectangle());
            platform1Transition.setByX(-distance);
            platform1Transition.setOnFinished(event -> {

                isMoving = false;
                isComponentsMoved = true;

                // Update the position of the sigil and the start of the stick based on the new positions of the platforms
//                double gapStart = platform1.getX() + platform1.getWidth();
//                double gapEnd = platform2.getX();
//                double gapMiddle = (gapStart + gapEnd) / 2;
//                Sigil.setX(gapMiddle);
//                Sigil.setY(platform1.getY() + 10);
//                root.getChildren().add(Sigil);
                platform1.setX(platform2.getX() - gap-300);
                System.out.println(platform1.getX());
                if (currentStickLine != null) {
                    currentStickLine.setStartX(platform1.getX() + platform1.getWidth());
                    currentStickLine.setStartY(platform1.getY());
                }
                System.out.println(platform1.getX());

            });
            platform1Transition.play();

            // Create and play the transition for platform2
            TranslateTransition platform2Transition = new TranslateTransition(Duration.seconds(time), platform2.getRectangle());
            platform2Transition.setByX(-distance);
            platform2Transition.setOnFinished(event ->{
//                platform2.setX(platform2.getX()- distance);
                System.out.println(platform2.getX());
            });
            platform2Transition.play();

            // Create and play the transition for platform3
            TranslateTransition platform3Transition = new TranslateTransition(Duration.seconds(time), platform3.getRectangle());
//            platform3Transition.setByX(-distance);

            platform3Transition.play();
        }
    }



    private void switchPlatform() {
        if (currentStickLine != null) {
            root.getChildren().remove(currentStickLine);
            currentStickLine = null;
        }

        // Check if a new platform is needed

            generateNewPlatform();


        // Reset the game state
        if (isComponentsMoved) {
            canCreateStick = true;
        }
        Inverted = false;
        isSpaceBarLocked = false;

        // Create a new stick at the right edge of the new platform
        resetAllBoolean();
    }
    private void resetAllBoolean() {
        isMoving = false;
        isCharacterAtEndPoint = false;
        isSpaceBarLocked = false;
        canCreateStick = true;
        isStickCreated = false;
        isCharacterMoved = false;
        isComponentsMoved = false;
        Inverted = false;
        isSpaceBarPressed = false;
        characterCollided = false;
    }

    private void showRetryScreen(javafx.stage.Stage primaryStage) {
        // Create a new pane for the retry screen
        Pane retryPane = new Pane();

        ImageView retryBgImage = new ImageView(new Image("/retry.jpeg"));
        retryBgImage.setFitWidth(800);
        retryBgImage.setFitHeight(800);
        retryBgImage.setPreserveRatio(false);

        // Create a retry button
        Button retryButton = new Button("Retry");
        retryButton.setLayoutX(375); // Adjust the position as needed
        retryButton.setLayoutY(375); // Adjust the position as needed

        StackPane stackPane = new StackPane(retryBgImage, retryButton);
        // Set the action for the retry button
        retryButton.setOnAction(event -> {
            // Reset the game
            reset();

            // Set the scene back to the game scene
            primaryStage.setScene(scene);

        });

        // Add the retry button to the retry pane
        retryPane.getChildren().add(retryButton);

        // Create a new scene for the retry screen
        Scene retryScene = new Scene(retryPane, 800, 800);

        // Set the scene of the primary stage to the retry scene
        primaryStage.setScene(retryScene);
    }

    private void fallCharacter() {
        // Calculate the distance to fall
        double distance = root.getHeight() - character.getTranslateY();

        // Calculate the time it takes for the character to fall
        double time = 2; // Adjust the speed as needed

        // Create a KeyValue for the moving action
        KeyValue moveKeyValue = new KeyValue(character.translateXProperty(), character.getTranslateX() + completedStickLength +40);
        // Create a KeyFrame for the moving action
        KeyFrame moveKeyFrame = new KeyFrame(Duration.seconds(time), moveKeyValue);

        // Create a Timeline for the moving action
        Timeline moveTimeline = new Timeline(moveKeyFrame);

        moveTimeline.setOnFinished(event -> {
            // Once the character has moved to the end of the stick, start the falling animation
            // Create a KeyValue for the falling action
            KeyValue fallKeyValue = new KeyValue(character.translateYProperty(), character.getTranslateY() + distance);

            // Create a KeyFrame for the falling action
            KeyFrame fallKeyFrame = new KeyFrame(Duration.seconds(time), fallKeyValue);

            // Create a KeyValue for the rotation action
            KeyValue rotateKeyValue = new KeyValue(character.rotateProperty(), 30); // Adjust the angle as needed

            // Create a KeyFrame for the rotation action
            KeyFrame rotateKeyFrame = new KeyFrame(Duration.seconds(time), rotateKeyValue);

            // Create a Timeline for the falling and rotation actions
            Timeline fallTimeline = new Timeline(fallKeyFrame, rotateKeyFrame);

            fallTimeline.setOnFinished(fallEvent -> {
                // Remove the character from the root pane once it has fallen
                root.getChildren().remove(character);

                // Show the retry screen
                if (root.getScene().getWindow() instanceof javafx.stage.Stage) {
                    showRetryScreen((javafx.stage.Stage) root.getScene().getWindow());
                }
            });

            fallTimeline.play();
        });

        moveTimeline.play();
    }
    private void generateNewPlatform() {
        // Remove the old platform from the root
        if (platform1 != null) {
            root.getChildren().remove(platform1.getRectangle());
        }
        if (Sigil != null) {
            root.getChildren().remove(Sigil);
        }

        // Move the current platform to the position of the old platform
        platform1 = platform2;

        // Generate a new platform with random width and position
        double randomOffset = Math.random() * 100;
        gap = 50 + Math.random() * 300;
        System.out.println("gap = " + gap);
        double wid = 100 + Math.random() * 200;
        platform2 = new Platform(wid, 500, 300+ + gap, 500);

        Sigil = new ImageView(new Image("anemo.png"));
        Sigil.setFitHeight(20);
        Sigil.setFitWidth(20);

        double gapStart = platform1.getX() + platform1.getWidth();
        double gapEnd = platform2.getX();
        double gapMiddle = gapEnd -10 - randomOffset;
        Sigil.setX(gapMiddle);
        Sigil.setY(platform1.getY() + 10);

        root.getChildren().add(Sigil);

        // Add the new platform to the root
        root.getChildren().add(platform2.getRectangle());


    }

    private void switchToMenu(javafx.stage.Stage primaryStage) {
        VBox menuPane = new VBox(10); // 10 is the spacing between elements
        menuPane.setAlignment(Pos.CENTER); // Align elements to the center

        Image bgImage = new Image("mondstat.jpeg");
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setOpacity(0.7);
        bgImageView.setFitWidth(1600);
        bgImageView.setFitHeight(800);
        bgImageView.setPreserveRatio(false);

        // Load the image and create an ImageView object
        Image playImage = new Image("START.png"); // Replace "play.png" with your image file
        ImageView playImageView = new ImageView(playImage);

        // Set an onMouseClicked event to the ImageView
        playImageView.setOnMouseClicked(event -> {
            primaryStage.setScene(scene);
            javafx.application.Platform.runLater(() -> primaryStage.setMaximized(true));

        });

        // Add the ImageView to the menuPane
        menuPane.getChildren().add(playImageView);

        // Create a StackPane and add the ImageView and VBox to it
        StackPane stackPane = new StackPane(bgImageView, menuPane);

        Scene menuScene = new Scene(stackPane, 1600, 800);
        menuScene.getStylesheets().add(getClass().getResource("/menustyle.css").toExternalForm());

        primaryStage.setScene(menuScene);
        javafx.application.Platform.runLater(() -> primaryStage.setMaximized(true));

    }
    private Scene createGameScene() {
        // Create the game pane
        Pane gamePane = createGamePane();

        // Set the scene size to 1600x800
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
