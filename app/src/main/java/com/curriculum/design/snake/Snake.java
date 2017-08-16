package com.curriculum.design.snake;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Snake {

    private final static int HEAD = 0xff4081, BODY = 0xeeff00, FOOD = 0x1e90ff;

    LinkedList<Grid> snake = new LinkedList<>();
    private Control mc = new Control();
    private static Grid cofood = new Grid(new short[]{0, 0}, BODY);

    Snake(){
        Grid firstNode = new Grid(new short[]{Grid.GRID_WIDTH/2, Grid.GRID_HEIGHT/2}, HEAD);
        snake.addFirst(firstNode);

        foodCreate();
    }

    void foodCreate(){

        Random rand = new Random();
        short foodAxis[] = new short[]{0,0};
        do {
            foodAxis[0] =(short) rand.nextInt(Grid.GRID_WIDTH);
            foodAxis[1] =(short) rand.nextInt(Grid.GRID_HEIGHT);
            cofood.setAxis(foodAxis);
        }while (snake.contains(cofood));
        cofood.setHexColor(FOOD);

    }

    boolean addHead (Grid head) {
        Grid oldFirst = snake.getFirst();
        oldFirst.setHexColor(BODY);
        snake.set(0, oldFirst);
        head.setHexColor(HEAD);
        snake.addFirst(head);
        return true;
    }

    boolean removeTail () {
        snake.removeLast();
        return true;
    }

    boolean testOver(Grid test, int direction) {
        for (Grid node : snake) {
            if ((Arrays.equals(node.getAxis(), test.getAxis()))
                    &&(!Arrays.equals(node.getAxis(), snake.getLast().getAxis()))){
                //Game Over
                return false;
            }
        }

        if (((snake.getFirst().getAxis()[0]==Grid.GRID_WIDTH-1)&&(direction == 2))
                ||((snake.getFirst().getAxis()[1]==Grid.GRID_HEIGHT-1)&&(direction == 1))
                ||(test.getAxis()[0]<0)||(test.getAxis()[1]<0)) {
            //Game Over
            return false;
        }
        return true;
    }

    void drawAll(){
        for (Grid node : snake) {
            node.draw();
        }

        cofood.draw();
    }

    void move(int direction) {
        if (direction!=0){
            if (((mc.direction_old + direction) == 0) && (snake.size() > 1)) {
                direction = mc.direction_old;
            }
            Grid nextGrid = new Grid(new short[]{0, 0}, BODY);
            Grid oldHead = snake.getFirst();
            short[] oldAxis = new short[]{0,0};
            System.arraycopy(oldHead.getAxis(), 0 , oldAxis, 0, 2);
            switch (direction) {
                case 1:
                    oldAxis[1]++;
                    break;
                case -1:
                    oldAxis[1]--;
                    break;
                case 2:
                    oldAxis[0]++;
                    break;
                case -2:
                    oldAxis[0]--;
                    break;
            }
            nextGrid.setAxis(oldAxis);
            if (testOver(nextGrid, mc.direction)) {
                addHead(nextGrid);
                if (Arrays.equals(nextGrid.getAxis(), cofood.getAxis())) {
                    foodCreate();
                } else {
                    removeTail();
                }
            }
            else {
                gameOver();
            }
            mc.direction_old = direction;
        }

    }
    int getSize(){
        return snake.size();
    }

    void gameOver(){
        snake.clear();
        Grid firstNode = new Grid(new short[]{Grid.GRID_WIDTH/2, Grid.GRID_HEIGHT/2}, HEAD);
        snake.addFirst(firstNode);
        mc.direction = 0;
        mc.direction_old = 0;
    }

}


