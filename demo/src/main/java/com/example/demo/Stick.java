package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Stick {

    private Line line;
    private double startX;
    private double startY;
    private double length;
    private Timeline timeline;

    public Stick(double startX, double startY, double length) {
        this.startX = startX;
        this.startY = startY;
        this.length = length;

        // Create a line representing the initial stick position
        line = new Line(startX, startY, startX, startY);

        // Create a KeyValue for the stick's end Y property
        KeyValue keyValue = new KeyValue(line.endYProperty(), startY - length);

        // Create a KeyFrame with the desired duration and the KeyValue
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);

        // Create a Timeline with the KeyFrame
        timeline = new Timeline(keyFrame);
        timeline.setOnFinished(event -> {
            // Stick animation is complete, you can handle any logic here
        });
    }

    public Line getLine() {
        return line;
    }

    public void extendStick() {
        // Reset the line's end position to the start position
        line.setEndY(startY);
        // Create a new KeyValue for the stick's end Y property, extending it downward
        KeyValue keyValue = new KeyValue(line.endYProperty(), startY - length);
        // Create a new KeyFrame with the desired duration and the new KeyValue
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
        // Set the KeyFrame to the Timeline
        timeline.getKeyFrames().setAll(keyFrame);
    }

    public void playAnimation() {
        timeline.play();
    }

    public void stopAnimation() {
        timeline.stop();
    }
}
