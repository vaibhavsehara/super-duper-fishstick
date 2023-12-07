//package com.example.demo;
//
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Line;
//import javafx.scene.transform.Rotate;
//import javafx.util.Duration;
//
//import java.util.Random;
//
//import static com.example.demo18.Platform.PLATFORM_HEIGHT;
//import static com.example.demo18.Platform.platform_distance;
//
//public class Stickman2{
//
//    private static final double STICKMAN_SIZE = 60;
//    public boolean stickmanfall;
//
//    private Pane root;
//    ImageView stickman;
//    Line stick;
//
//    private boolean endgame=false;
//
//    private double stickHeight = 0;
//    double stickmanX = 0;
//    private double stickmanY = 0;
//
//    ImageView cherry;
//
//    Random random;
//
//    private boolean isWalking = false;
//
//    private Timeline walkingTimeline;
//
//    private Timeline fallingTimeline;
//
//    private Timeline rotatingTimeline;
//
//    boolean reverse=false;
//
//    boolean isreversible=false;
//
//    private boolean isrotating;
//
//    private double positionX=0,positionY=0;
//
//    public Stickman(Pane root) {
//        this.root = root;
//    }
//
//    public void initialize() {
//        Image stickmanImage = new Image("file:C:/Users/syash/OneDrive/Pictures/Stick_Figure.png");
//        stickman = new ImageView(stickmanImage);
//        stickman.setFitHeight(STICKMAN_SIZE);
//        stickman.setFitWidth(STICKMAN_SIZE);
//        stickman.setLayoutY(root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE);
//        root.getChildren().add(stickman);
//        cherry=new ImageView();
//        cherry.setImage(new Image("file:C:\\Users\\syash\\Downloads\\cherry.png"));
//        cherry.setFitHeight(20);
//        cherry.setFitWidth(20);
//        cherry.setY(root.getHeight() - PLATFORM_HEIGHT);
//        random=new Random();
//        int temp=random.nextInt(5,90);
//        cherry.setX(root.getWidth()*0.1+platform_distance*temp/100);
//        root.getChildren().add(cherry);
//    }
//
//    public void startGrowing() {
//        stick = new Line(stickmanX + STICKMAN_SIZE / 2 + 50, root.getHeight() - PLATFORM_HEIGHT,
//                stickmanX + STICKMAN_SIZE / 2 + 50, root.getHeight() - PLATFORM_HEIGHT);
//        stick.setStroke(Color.BLACK);
//        root.getChildren().add(stick);
//    }
//
//    public void grow() {
//        stickHeight += 1;
//        stick.setEndY(root.getHeight() - PLATFORM_HEIGHT - stickHeight);
//    }
//
//    public void stopGrowing(Platform platform, StickmanGame stickmangame) {
//        /*isrotating=true;
//        positionX=stick.getStartX();
//        positionY=stick.getEndY();
//        rotatingTimeline = new Timeline(
//                new KeyFrame(Duration.millis(10), e -> rotatestick(platform, stickmangame))
//        );
//        rotatingTimeline.setCycleCount(Timeline.INDEFINITE);
//        rotatingTimeline.play();*/
//        Rotate rotate = new Rotate();
//        rotate.setPivotX(stickmanX + STICKMAN_SIZE / 2 + 50);
//        rotate.setPivotY(root.getHeight() - PLATFORM_HEIGHT);
//        stick.getTransforms().add(rotate);
//        rotate.setAngle(90);
//        isWalking = true;
//        startWalking(platform, stickmangame);
//    }
//
//    private void rotatestick(Platform platform, StickmanGame stickmangame) {
//
//// Calculate the corresponding Y position based on the fixed length
//
//// Calculate the new position using trigonometry
//        double radius = stickHeight;  // You can adjust the radius as needed
//        positionX += 5;  // Assuming you're incrementing positionX
//        positionY = root.getHeight() - PLATFORM_HEIGHT - stickHeight - Math.sqrt(radius * radius - Math.pow(positionX, 2));
//        stick.setEndX(positionX);
//        stick.setEndY(positionY);
//
//        if(stick.getEndY()==PLATFORM_HEIGHT){
//            rotatingTimeline.stop();
//            isrotating=false;
//            startWalking(platform,stickmangame);
//        }
//    }
//
//    public void startWalking(Platform platform, StickmanGame stickmangame) {
//        this.isreversible=true;
//        if (walkingTimeline == null || !walkingTimeline.getStatus().equals(Animation.Status.RUNNING)) {
//            walkingTimeline = new Timeline(
//                    new KeyFrame(Duration.millis(10), e -> {
//                        stickmangame.stickman.stickman.setOnKeyPressed(event -> {
//                            if (event.getCode() == KeyCode.SPACE && isreversible && isWalking) {
//                                reverse = !reverse; // Toggle the direction
//
//                                if (reverse) {
//                                    stickman.setRotate(180);
//                                    stickman.setLayoutY(stickman.getLayoutY() + 60); // Move down
//                                } else {
//                                    stickman.setRotate(0); // Assuming the original rotation is 0
//                                    stickman.setLayoutY(stickman.getLayoutY() - 60); // Move up
//                                }
//                            }
//                        });
//                        stickman.requestFocus();
//                        walkStickman(platform, stickmangame);
//                    })
//            );
//            walkingTimeline.setCycleCount(Timeline.INDEFINITE);
//            walkingTimeline.play();
//
//        }
//    }
//
//    private void walkStickman(Platform platform, StickmanGame stickmangame) {
//        if (Platform.count % 2 == 0) {
//            if (isWalking) {
//                if (stickmanX > stickHeight+STICKMAN_SIZE && stickHeight < platform_distance) {
//                    isWalking=false;
//                    walkingTimeline.pause();
//                    stickmanfall(platform,stickmangame);
//                    isreversible=false;
//                } else {
//                    stickmanX += 1;
//                    stickman.setTranslateX(stickmanX);
//                    System.out.println(stickmanX);
//                    System.out.println(cherry.getX());
//                    if (stickmanX-cherry.getX()>=-1 && stickmanX-cherry.getX()<=1){
//                        root.getChildren().remove(cherry);
//                    }
//                    if (stickmanX >= (root.getWidth() * (Platform.newrect.get(0)) / 100)) {
//                        isWalking = false;
//                        isreversible=false;
//                        walkingTimeline.pause();
//                        platform.nextPlatform(stickmangame);
//                        //stickman.setTranslateX(-1 * (stick.getEndX() - stick.getStartX()));
//                        //stick.setTranslateX(-1 * (platform_distance + platform.newRectangle.getWidth()));
//                    }
//                }
//            }
//        } else {
//            if (isWalking) {
//                if (stickmanX > stickHeight+STICKMAN_SIZE && stickHeight < platform_distance) {
//                    isWalking=false;
//                    isreversible=false;
//                    walkingTimeline.pause();
//                    stickmanfall(platform,stickmangame);
//                } else {
//                    stickmanX += 1;
//                    stickman.setTranslateX(stickmanX);
//                    System.out.println(stickman.getX());
//                    System.out.println(cherry.getX());
//                    if (stickman.getX()==cherry.getX()){
//                        root.getChildren().remove(cherry);
//                    }
//                    if (stickmanX >= (root.getWidth() * (Platform.newrect.get(0)) / 100)) {
//                        isWalking = false;
//                        isreversible=false;
//                        walkingTimeline.pause();
//                        platform.nextPlatform(stickmangame);
//                        //stickman.setTranslateX(-1 * (stick.getEndX() - stick.getStartX()));
//                        //stick.setTranslateX(-1 * (platform_distance + platform.newRectangle.getWidth()));
//                    }
//                }
//            }
//        }
//
//
//
//
//
//    }
//
//    private void stickmanfall(Platform platform,StickmanGame stickmanGame) {
//        fallingTimeline = new Timeline(
//                new KeyFrame(Duration.millis(10), e -> fallStickman(platform,stickmanGame))
//        );
//        fallingTimeline.setCycleCount(Timeline.INDEFINITE);
//        fallingTimeline.play();
//        System.out.println("game end");
//    }
//
//
//    private void fallStickman(Platform platform,StickmanGame stickmanGame) {
//        stickmanY += 10;
//        stickman.setTranslateY(stickmanY);
//    }
//
//
//    public void resetPosition(Platform platform,StickmanGame stickmanGame) {
//
//        isWalking = false;
//        stickHeight = 0;
//        stickmanX=0;
//        stick.setEndY(root.getHeight() - PLATFORM_HEIGHT - stickHeight);
//        stickmanGame.growingInProgress=false;
//        root.getChildren().remove(stick);
//        stickmanGame.growingInProgress=false;
//    }
//
//    public boolean isWalking() {
//        return isWalking;
//    }
//
//    public boolean checkContinue() {
//        return stick.getEndX() - stick.getStartX() < platform_distance;
//    }
//}