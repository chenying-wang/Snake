package com.curriculum.design.snake;

public class Grid{

    // columns and rows of grid
    final static  int GRID_WIDTH = 50, GRID_HEIGHT = 80;

    private Square gridSquare;
    private short[] axis = new short[2];
    private int hexColor;
    private float[] GLCoords = new float[12], GLColor = new float[4];


    Grid(short[] axis, int color) {
        setAxis(axis);
        setHexColor(color);
    }

    public void draw() {
        updateSquare();
        gridSquare.draw();
    }

    public short[] getAxis() {
        return axis;
    }

    public int getColor() {
        return hexColor;
    }

    public void setAxis(short[] axis) {
        this.axis = axis;
        this.axis[0] %= GRID_WIDTH;
        this.axis[1] %= GRID_HEIGHT;
    }

    public void setHexColor(int color) {
        this.hexColor = color;
    }

    private void updateSquare() {
        // update coords and color
        axisToCoord();
        hexColorToGLColor();
        this.gridSquare = new Square(this.GLCoords, this.GLColor);
    }

    private void axisToCoord() {
        // convert axis to GLCoords
        float left, right,top, bottom;
        left = 2.0f * this.axis[0] / GRID_WIDTH - 1.0f;
        right = 2.0f * (this.axis[0]+1) / GRID_WIDTH - 1.0f;
        top = 2.0f * this.axis[1] / GRID_HEIGHT - 1.0f;
        bottom = 2.0f * (this.axis[1]+1) / GRID_HEIGHT - 1.0f;
        this.GLCoords = new float[]{
            left, top, 0.0f,
            right, top, 0.0f,
            right, bottom, 0.0f,
            left, bottom, 0.0f
        };
    }

    private void hexColorToGLColor() {
        // convert hexadecimal color to GLColor
        float red, green, blue;
        red = (float)((this.hexColor >> 16) & 0xff);
        green = (float)((this.hexColor >> 8) & 0xff);
        blue = (float)(this.hexColor & 0xff);

        red /= 255.0f;
        green /= 255.0f;
        blue /= 255.0f;

        this.GLColor[0] = red;
        this.GLColor[1] =  green;
        this.GLColor[2] =  blue;
        this.GLColor[3] =  1.0f;
    }

}
