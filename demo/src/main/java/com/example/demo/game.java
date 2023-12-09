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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class game extends Application {

    private Image characterImage;
    private int movementScore = 0;
    private int Score = 0;
    private ImageView character;
    private ImageView ScoreSigil;
    private double completedStickLength = 0;
    private Timeline timeline;
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
    private int highScore = 0;
    Label highScoreLabel;
    private boolean characterCollided = false;
    Label movementScoreLabel = new Label(String.valueOf(movementScore));
    Label SigilLabel= new Label(String.valueOf(Score));
    private Scene createGameScene( javafx.stage.Stage primaryStage){

        return new Scene(root, 1600, 800);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        Media bgMusic = new Media(getClass().getResource("/02 THE STORM.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(bgMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
        mediaPlayer.play();
        highScoreLabel = new Label("High Score: 0");
        highScoreLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #011627;"); // adjust the style as needed



        movementScoreLabel.setStyle("-fx-font-size: 100; -fx-text-fill: #011627;"); // adjust the style as needed

        HBox movementscoreBox = new HBox(movementScoreLabel);
        movementscoreBox.setAlignment(Pos.CENTER);

        ScoreSigil = new ImageView(new Image("anemo.png"));
        ScoreSigil.setFitHeight(30);
        ScoreSigil.setFitWidth(30);

        SigilLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #011627;"); // adjust the style as needed

// Position the ScoreSigil and scoreLabel at the top right
        HBox scoreBox = new HBox(ScoreSigil, SigilLabel);
        scoreBox.setAlignment(Pos.TOP_RIGHT);

        Image menuImage = new Image("quit6.png"); // Replace "menu.png" with your image file
        ImageView menuImageView = new ImageView(menuImage);
        menuImageView.setLayoutX(0); // Set the X coordinate
        menuImageView.setLayoutY(0); // Set the Y coordinate
        menuImageView.setOnMouseClicked(event -> {
            // Switch to the menu
            switchToMenu(primaryStage);
        });


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


        // Add the stackPane to the root pane
        root.getChildren().add(stackPane);
        Sigil.setY(platform1.getY() + 10);
        root.getChildren().add(menuImageView);
        root.getChildren().add(scoreBox);
        AnchorPane.setTopAnchor(scoreBox, 10.0); // 10 units down from the top edge
        AnchorPane.setRightAnchor(scoreBox, 10.0);
//        root.getChildren().add(highScoreLabel);
//        AnchorPane.setTopAnchor(highScoreLabel, 90.0); // 90 units down from the top edge
//        AnchorPane.setRightAnchor(highScoreLabel, 10.0); // 10 units from the right edge
        root.getChildren().add(movementScoreLabel);
        AnchorPane.setTopAnchor(movementScoreLabel, 50.0); // 50 units down from the top edge
        AnchorPane.setLeftAnchor(movementScoreLabel, 800.0); // 10 units from the left edge

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
                resetGame();
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
    private void writeHighScoreToFile(int highScore) {
        try {
            FileWriter writer = new FileWriter("highscore.txt");
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int readHighScoreFromFile() {
        try {
            FileReader reader = new FileReader("highscore.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String highScoreString = bufferedReader.readLine();
            bufferedReader.close();
            return Integer.parseInt(highScoreString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
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

            if (timeline != null) {
                timeline.stop();
            }

            // Create a Timeline with the KeyFrame
             timeline = new Timeline(kf);

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
                    if (!isCharacterMoved) {
                        movementScore += 1;
                        movementScoreLabel.setText(String.valueOf(movementScore));
                        isCharacterMoved = true;
                    }
                } else {
                    fallCharacter();
                }
            }
        }
        if (!isSigilCollected && character.getBoundsInParent().intersects(Sigil.getBoundsInParent()) && Inverted)  {
            Score+= 1; // assuming you have a score variable
            SigilLabel.setText(String.valueOf(Score));
            root.getChildren().remove(Sigil);
            isSigilCollected = true;
            if (Score > highScore) {
                highScore = Score;
                writeHighScoreToFile(highScore);
            }
        }

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
    private void resetAndStartGame(javafx.stage.Stage primaryStage) {
        // Reset the game
        resetGame();

        // Set the scene back to the game scene
        primaryStage.setScene(scene);
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
        isSigilCollected = false;
    }
    private void showRetryScreen(javafx.stage.Stage primaryStage) {
        highScore = readHighScoreFromFile();
        // Create a new pane for the retry screen
        VBox retryPane = new VBox(10); // 10 is the spacing between elements
        retryPane.setAlignment(Pos.CENTER); // Align elements to the center

        Button menuButton = new Button("Menu");
        menuButton.setLayoutX(580); // Set the X coordinate
        menuButton.setLayoutY(560); // Set the Y coordinate
        menuButton.setPrefHeight(100);
        menuButton.setPrefWidth(60);
        menuButton.setOpacity(0);
        menuButton.setOnMouseClicked(event -> {
            // Switch to the menu
            switchToMenu(primaryStage);
        });

        Button retryButton = new Button("Retry");
        retryButton.setLayoutX(960); // Set the X coordinate
        retryButton.setLayoutY(560); // Set the Y coordinate
        retryButton.setPrefHeight(100);
        retryButton.setPrefWidth(60);
        retryButton.setOpacity(0);
        retryButton.setOnMouseClicked(event -> {
            resetAndStartGame( primaryStage);

            // Set the scene back to the game scene

        });


        Image retryBgImage = new Image("mondstat.jpeg");
        ImageView retryBgImageView = new ImageView(retryBgImage);
        retryBgImageView.setOpacity(0.7);
        retryBgImageView.setFitWidth(1600);
        retryBgImageView.setFitHeight(800);
        retryBgImageView.setPreserveRatio(false);

        // Create a retry button
        Image retryImage = new Image("RETRY.png"); // Replace "RETRY.png" with your image file
        ImageView retryImageView = new ImageView(retryImage);
        retryImageView.setOnMouseClicked(event -> {
            // Reset the game
            reset();

            // Set the scene back to the game scene
            primaryStage.setScene(scene);
        });

        // Disable the retry button
        retryImageView.setDisable(true);

        // Add the retry button to the retry pane
        retryPane.getChildren().add(retryImageView);

        // Create a StackPane and add the ImageView and VBox to it
        StackPane stackPane = new StackPane();
        Pane scorePane = new Pane();

        stackPane.getChildren().add(retryImageView);
        stackPane.getChildren().add(scorePane);

        // Create labels to display the current score and high score
        Label scoreLabel = new Label(String.valueOf(Score));
        scoreLabel.setLayoutX(580); // Set the X coordinate
        scoreLabel.setLayoutY(300); // Set the Y coordinate

        scoreLabel.setStyle("-fx-font-size: 100; -fx-text-fill: #011627;"); // adjust the style as needed

        Label highScoreLabel = new Label(String.valueOf(highScore));
        highScoreLabel.setLayoutX(900); // Set the X coordinate
        highScoreLabel.setLayoutY(300); // Set the Y coordinate
        highScoreLabel.setStyle("-fx-font-size: 100; -fx-text-fill: #011627;"); // adjust the style as needed
        // Add the score labels to the stack pane
        scorePane.getChildren().add(scoreLabel);
        scorePane.getChildren().add(highScoreLabel);
        scorePane.getChildren().add(menuButton);
        scorePane.getChildren().add(retryButton);




        // Create a new scene for the retry screen
        Scene retryScene = new Scene(stackPane, 1600, 800);

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
        gap = 30 + Math.random() * 200;
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
    private void resetGame() {

        // Reset all the variables to their initial values
        movementScore = 0;
        Score = 0;
        completedStickLength = 0;
        isMoving = false;
        isCharacterAtEndPoint = false;
        isSpaceBarLocked = false;
        canCreateStick = true;
        isSigilCollected = false;
        isSpaceBarPressed = false;
        isCharacterMoved = false;
        isComponentsMoved = false;
        isStickCreated = false;
        isCharacterMoving = false;
        characterCollided = false;

        // Remove all nodes from the root pane
        root.getChildren().clear();
        // Recreate the game scene
        scene = createGameScene();

        Image gameBg = new Image("mondstat.jpeg");
        ImageView gameBgImage = new ImageView(gameBg);
        gameBgImage.setFitWidth(1600);
        gameBgImage.setFitHeight(800);
        gameBgImage.setPreserveRatio(false);
        root.getChildren().add(gameBgImage);
        // Reset the character's position
        character.setTranslateX(defaultStickManX-50);
        character.setTranslateY(435);

        // Add the character back to the root pane
        root.getChildren().add(character);
        // Reset the score labels
        movementScoreLabel.setText(String.valueOf(movementScore));
        SigilLabel.setText(String.valueOf(Score));

        // Add the score labels back to the root pane
        root.getChildren().add(movementScoreLabel);
        root.getChildren().add(SigilLabel);

        // Recreate the platforms and add them to the root pane
        platform1 = new Platform(300, 500, 200, 500);
        platform2 = new Platform(100, 500, 700, 500);
        root.getChildren().add(platform1.getRectangle());
        root.getChildren().add(platform2.getRectangle());

        // Recreate the Sigil and add it to the root pane
        Sigil = new ImageView(new Image("anemo.png"));
        Sigil.setFitHeight(20);
        Sigil.setFitWidth(20);
        double gapStart = platform1.getX() + platform1.getWidth();
        double gapEnd = platform2.getX();
        double gapMiddle = (gapStart + gapEnd) / 2;
        Sigil.setX(gapMiddle);
        Sigil.setY(platform1.getY() + 10);
        root.getChildren().add(Sigil);


        HBox scoreBox = new HBox(ScoreSigil, SigilLabel);
        scoreBox.setAlignment(Pos.TOP_RIGHT);

        // Add the scoreBox to the root pane
        root.getChildren().add(scoreBox);
        AnchorPane.setTopAnchor(scoreBox, 10.0); // 10 units down from the top edge
        AnchorPane.setLeftAnchor(scoreBox, 0.0); // 0 units from the left edge
        AnchorPane.setRightAnchor(scoreBox, 0.0); // 0 units from the right edge

    }

    private void switchToMenu(javafx.stage.Stage primaryStage) {
        // Create a new pane for the menu screen
        Pane menuPane = new Pane();

        Image bgImage = new Image("mondstat.jpeg");
        ImageView bgImageView = new ImageView(bgImage);
//        bgImageView.setOpacity(0.7);
        bgImageView.setFitWidth(1600);
        bgImageView.setFitHeight(800);
        bgImageView.setPreserveRatio(false);

        Image image1 = new Image("quit6.png"); // Replace with your image file
        ImageView imageView1 = new ImageView(image1);
        imageView1.setLayoutX(0); // Top left
        imageView1.setLayoutY(0);

        imageView1.setOnMouseClicked(event -> {
            // Exit the application
            javafx.application.Platform.exit();
        });

        Image image2 = new Image("STICK HERO.png"); // Replace with your image file
        ImageView imageView2 = new ImageView(image2);
        imageView2.setLayoutX(600); // Top center
        imageView2.setLayoutY(0);

        Image image3 = new Image("aether.png"); // Replace with your image file
        ImageView imageView3 = new ImageView(image3);
        imageView3.setLayoutX(400); // Just left to the middle image
        imageView3.setLayoutY(0);

        imageView3.setFitHeight(100);
        imageView3.setFitWidth(100);
        imageView2.setFitHeight(100);

        imageView1.setFitHeight(100);
        imageView1.setFitWidth(100);
        // Load the image and create an ImageView object
        Image playImage = new Image("Group 11.png"); // Replace "play.png" with your image file
        ImageView playImageView = new ImageView(playImage);
        playImageView.setLayoutX(600); // Set the X coordinate
        playImageView.setLayoutY(400); // Set the Y coordinate

        // Set an onMouseClicked event to the ImageView
        playImageView.setOnMouseClicked(event -> {
            primaryStage.setScene(scene);
            javafx.application.Platform.runLater(() -> primaryStage.setMaximized(true));

        });

        // Add the ImageView and VBox to the menuPane
        menuPane.getChildren().addAll(bgImageView, imageView1, imageView2, imageView3, playImageView);

        Scene menuScene = new Scene(menuPane, 1600, 800);
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