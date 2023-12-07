//package com.example.demo;
//
//import javafx.animation.TranslateTransition;
//import javafx.scene.layout.Pane;
//import javafx.scene.shape.Rectangle;
//import javafx.util.Duration;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//public class Platform2 {
//
//    public static final double PLATFORM_HEIGHT = 150;
//    public static double platform_distance;
//    public static ArrayList<Double> newrect = new ArrayList<Double>();
//
//    private Pane root;
//    Rectangle newRectangle;
//    Rectangle oldRectangle;
//
//    static int count;
//
//    public Platform2(Pane root) {
//        this.root = root;
//    }
//
//    public void initialize() {
//        Random rand = new Random();
//        int random1 = -1;
//        int random2 = -2;
//        while (random1 > random2) {
//            random1 = rand.nextInt(11, 100);
//            random2 = rand.nextInt(11, random1 + 6);
//        }
//        newrect.add((double) random1);
//        newrect.add((double) random2);
//        oldRectangle = new Rectangle(0, root.getHeight() - PLATFORM_HEIGHT, root.getWidth() * (0.1), root.getHeight() - PLATFORM_HEIGHT);
//        root.getChildren().add(oldRectangle);
//        newRectangle = new Rectangle(root.getWidth() * ((double) random1 / 100), root.getHeight() - PLATFORM_HEIGHT, root.getWidth() * ((double) random2 / 100), root.getHeight() - PLATFORM_HEIGHT);
//        root.getChildren().add(newRectangle);
//        platform_distance = root.getWidth() * ((newrect.get(0) / 100) - 0.1);
//    }
//
//    public void nextPlatform(Stickmangame stickmangame) {
//        /*oldRectangle.setTranslateX(-1 * (platform_distance + newRectangle.getWidth()));
//        newRectangle.setTranslateX(-1 * (platform_distance + newRectangle.getWidth()));
//        insertNew(stickmangame);*/
//        TranslateTransition transition = new TranslateTransition(Duration.seconds(3));
//        transition.setByX(-1 * (platform_distance + oldRectangle.getWidth()));
//
//        // Play the transition
//        transition.setNode(oldRectangle);
//
//
//        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(3));
//        transition2.setByX(-1 * (platform_distance + newRectangle.getWidth()));
//
//        // Play the transition for the second rectangle with a slight delay
//        transition2.setNode(newRectangle);
//        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(3));
//        transition3.setByX(-1 * (platform_distance + newRectangle.getWidth()));
//        transition3.setNode(stickmangame.stickman.stick);
//        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(3));
//        transition4.setByX(-1 * (stickmangame.stickman.stickmanX - root.getWidth()*0.1)-60);
//        transition4.setNode(stickmangame.stickman.stickman);
//        //transition2.setDelay(Duration.seconds(0.5));
//        transition.play();
//        transition2.play();
//        transition3.play();
//        transition4.play();
//        transition2.setOnFinished(e->insertNew(stickmangame));
//    }
//
//    private void insertNew(StickmanGame stickmangame) {
//        /*Rectangle temprect=oldRectangle;
//        this.oldRectangle = newRectangle;*/
//        //this.newRectangle=temprect;
//        if (count%2==0){
//            root.getChildren().remove(oldRectangle);
//            Random rand = new Random();
//            int rand1 = -1;
//            int rand2 = -2;
//            while (rand1 > rand2) {
//                rand1 = rand.nextInt(11, 100);
//                rand2 = rand.nextInt(11, rand1 + 6);
//            }
//            ArrayList<Double> temp = new ArrayList<>();
//            newrect.set(0,(double) rand1);
//            newrect.set(1,(double) rand2);
//            oldRectangle = new Rectangle(
//                    root.getWidth() * newrect.get(0) / 100,
//                    oldRectangle.getY(),
//                    root.getWidth() * newrect.get(1) / 100,
//                    oldRectangle.getY()
//            );
//
//            root.getChildren().add(oldRectangle);
//        }
//
//        else{
//            root.getChildren().remove(newRectangle);
//            Random rand = new Random();
//            int rand1 = -1;
//            int rand2 = -2;
//            while (rand1 > rand2) {
//                rand1 = rand.nextInt(11, 100);
//                rand2 = rand.nextInt(11, rand1 + 6);
//            }
//            ArrayList<Double> temp = new ArrayList<>();
//            newrect.set(0,(double) rand1);
//            newrect.set(1,(double) rand2);
//            newRectangle = new Rectangle(
//                    root.getWidth() * newrect.get(0) / 100,
//                    oldRectangle.getY(),
//                    root.getWidth() * newrect.get(1) / 100,
//                    oldRectangle.getY()
//            );
//
//            root.getChildren().add(newRectangle);
//        }
//
//        /*Rectangle temprect=oldRectangle;
//        this.oldRectangle = newRectangle;
//        this.newRectangle=temprect;*/
//
//        /*this.oldRectangle = newRectangle;
//        this.newRectangle = tempRect;*/
//        //temprect=null;
//        this.count+=1;
//        stickmangame.stickSpawned = false;
//        stickmangame.isWalking = false;
//        stickmangame.stickman.resetPosition(stickmangame.platform,stickmangame);
//        platform_distance = root.getWidth() * ((newrect.get(0) / 100) - 0.1);
//    }
//
//    public double getDistance() {
//        return platform_distance;
//    }
//}
