package com.example.gamehub.model;

import android.graphics.Rect;

public class Controls {

    public enum Button {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    private int x;
    private int y;
    private Rect[] buttons;

    public Controls(int posXInit, int posYInit, int buttonSize) {
        x = posXInit;
        y = posYInit;
        buttons = new Rect[4];
        buttons[0] = new Rect(
                x,
                y + buttonSize,
                x + buttonSize,
                y + (buttonSize * 2));
        buttons[1] = new Rect(
                x + buttonSize,
                y,
                x + (buttonSize * 2),
                y + buttonSize);
        buttons[2] = new Rect(
                x + (buttonSize * 2),
                y + buttonSize,
                x + (buttonSize * 3),
                y + (buttonSize * 2));
        buttons[3] = new Rect(
                x + buttonSize,
                y + (buttonSize * 2),
                x + (buttonSize * 2),
                y + (buttonSize * 3));
    }
    public Rect getButton(Button button) {
        Rect returnButton;
        switch (button) {
            case LEFT:
                returnButton = buttons[0];
                break;
            case UP:
                returnButton = buttons[1];
                break;
            case RIGHT:
                returnButton = buttons[2];
                break;
            default:
                returnButton = buttons[3];
                break;
        }
        return returnButton;
    }
    public Rect[] getButtons() {
        return buttons;
    }
}
