package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {

    private Rectangle rectangle;
    private double x;
    private double width;
    private double height;

    public Platform(double width, double height, double x) {
        this.width = width;
        this.height = height;
        this.x = x;

        rectangle = new Rectangle(width, height, Color.GREEN); // Adjust color as needed

        // Set initial position of the pillar
        rectangle.setTranslateX(x);
        rectangle.setTranslateY(200 - height); // Set the initial y-coordinate based on your game design
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
        return rectangle.getTranslateY();
    }

}
