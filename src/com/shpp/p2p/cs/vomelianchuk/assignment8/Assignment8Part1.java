package com.shpp.p2p.cs.vomelianchuk.assignment8;

import acm.graphics.GObject;

import acm.graphics.GRect;

import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Timer;

/**
 * File: Assignment8Part1.java
 * ---------------------------
 * The program allows you to click on a square
 * to raise it up to the ceiling,
 * then return to the starting place
 */

public class Assignment8Part1 extends WindowProgram {
    /* The number of rows and columns in the grid, respectively. */
    private static final int NUM_ROWS = 10;
    private static final int NUM_COLS = 15;

    /* The horizontal and vertical spacing between the boxes. */
    private static final double BOX_SPACING = 20;

    /* The amount of time to pause between frames (60fps). */
    private static final double PAUSE_TIME = 1000.0 / 60;

    /* The initial velocity of the box. */
    private static final double VELOCITY = -3.0;

    /* Gravitational acceleration. */
    private static final double GRAVITY = 0.01;

    private final ArrayList<GRect> movingBoxes = new ArrayList<>();
    private final Map<GRect, Double> boxSpeeds = new HashMap<>();

    public void run() {
        double boxSize = getBoxSize();
        drawBoxes(boxSize);
        addMouseListeners();
        createTimer();
    }

    /**
     * Creates a timer and starts it for animation
     */
    private void createTimer() {
        Timer timer = new Timer((int) PAUSE_TIME, e -> moveBox());
        timer.start();
    }

    private double getBoxSize() {
        return ((double) getWidth() - (BOX_SPACING * (NUM_COLS - 1))) / NUM_COLS;
    }

    /**
     * Draws a grid of boxes on the screen
     *
     * @param boxSize The size of the box
     */
    private void drawBoxes(double boxSize) {
        double x = 0;
        double y = getHeight() - boxSize;
        Color colorBox = Color.BLACK;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                drawBox(
                        x + (j * (boxSize + BOX_SPACING)),
                        y - (i * (boxSize + BOX_SPACING)),
                        colorBox, boxSize
                );
            }
            colorBox = Color.WHITE;
        }
    }

    /**
     * Draws black box
     *
     * @param x The x coordinate of the upper-left corner of the box
     * @param y The y coordinate of the upper-left corner of the box
     * @param color The color of the box
     * @param boxSize The size of the box
     */
    private void drawBox(double x, double y, Color color, double boxSize) {
        GRect rect = new GRect(x, y, boxSize, boxSize);
        rect.setFilled(true);
        rect.setColor(color);
        add(rect);
    }

    public void mouseClicked(MouseEvent event) {
        GObject object = getElementAt(event.getX(), event.getY());
        if (object instanceof GRect selectedBox) {
            if ((!movingBoxes.contains(selectedBox)) && selectedBox.getColor() == Color.BLACK) {
                selectedBox.sendToFront(); // brings to the fore
                movingBoxes.add(selectedBox); // adds to collection
                boxSpeeds.put(selectedBox, VELOCITY); // sets the speed to -3.0 for the new box
            }

        }
    }

    /**
     * Moves the box to the top edge,
     * then reverses direction and
     * returns the box to its original location
     */
    private void moveBox() {
        /*
        Creates an iterator to go through all the banks
        that are moving in this time period
         */
        Iterator<GRect> iterator = movingBoxes.iterator();
        while (iterator.hasNext()) {
            GRect box = iterator.next();
            if (box != null) {
                double dy = boxSpeeds.get(box); // gets the current speed for each box
                box.move(0, dy);
                boxSpeeds.put(box, dy + GRAVITY); // changes the speed under the influence of gravity

                // When reaches the ceiling, changes direction
                if (box.getY() <= 0 || box.getY() + box.getHeight() >= getHeight()) {
                    boxSpeeds.put(box, -dy); // changes the direction and keep the new speed
                }

                // When returning to the starting place, the movement ends
                if (box.getY() + box.getHeight() >= getHeight()) {
                    box.setLocation(box.getX(), getHeight() - getBoxSize());
                    iterator.remove();
                }
            }
        }
    }
}
