package com.example.demo;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class StickMan extends Group {
    public StickMan() {

        Circle head = new Circle(10);
        head.setFill(Color.PURPLE);

        Line body = new Line(0,0,0,50);
        body.setStroke(Color.BLACK);

        Line leftArm = new Line(0,10,-20,10);
        leftArm.setStroke(Color.BLACK);

        Line rightArm = new Line(0, 10, 20, 10);
        rightArm.setStroke(Color.BLACK);

        Line leftLeg = new Line(0, 50, -10, 80);
        leftLeg.setStroke(Color.BLACK);

        Line rightLeg = new Line(0, 50, 10, 80);
        rightLeg.setStroke(Color.BLACK);

        head.getStyleClass().add("head");
        body.getStyleClass().add("body");
        leftArm.getStyleClass().add("arm-leg");
        rightArm.getStyleClass().add("arm-leg");
        leftLeg.getStyleClass().add("arm-leg");
        rightLeg.getStyleClass().add("arm-leg");

        getChildren().addAll(head, body, leftArm, rightArm, leftLeg, rightLeg);
    }

}
