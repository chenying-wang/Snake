package com.curriculum.design.snake;


import static java.lang.Math.abs;

public class Control {
    static int direction ;
    static int direction_old;
    private float x,y;

    public void setco(float x,float y){
        this.x = x;
        this.y = y;
        float co = abs(x) - abs(y);
        if (co<0){
            if (y <0){
                direction = 1;
            }
            else direction = -1;
        }
        else if (co >0){
            if (x>0){
                direction = 2;
            }
            else  direction =-2;
        }
        else ;
    }
}
