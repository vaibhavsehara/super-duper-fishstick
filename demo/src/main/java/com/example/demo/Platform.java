package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {

    private Rectangle rectangle;
    private double x;
    private double width;
    private final double height;
    private double y;

    public Platform(double width, double height, double x, double y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        rectangle = new Rectangle(width, height, Color.BLACK); // Adjust color as needed

        // Set initial position of the pillar
        rectangle.setTranslateX(x);
        rectangle.setTranslateY(y); // Set the initial y-coordinate based on your game design
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public double getX() {
        return x;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getY() {
        return y;
    }

    public double calculateDistanceToNextPlatform(Platform nextPlatform) {
        return nextPlatform.getX() - (this.x + this.width);
    }
}