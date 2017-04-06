package com.example.michaelpollard.cannonblast;

import java.util.Random;

/**
 * Created by Michael Pollard on 3/13/2017.
 * handles everything to do with the dynamic star background - inspired by bubble game workshop
 */
public class GameBackground {
    private int xVal;
    private int yVal;
    private int speed;
    private int maxX;
    private int maxY;
    Random random = new Random();


    /**
     * constructor for star background stuff
     * @param screenX - screenWidth
     * @param screenY - screenHeight
     */
    public GameBackground(int screenX, int screenY){

        //sets bounds for the stars
        maxX = screenX;
        maxY = screenY;

        //randomizes the speed of the stars
        speed = random.nextInt(21) + 2;

       //randomizes starting locations
        xVal = random.nextInt(maxX);
        yVal = random.nextInt(maxY);
    }

    /**
     * updates the position of the stars on the screen
     */
    public void update(){

        xVal -= speed;

        //if star reaches left edge of screen
        if(xVal < 0){
            xVal = maxX;
            yVal = random.nextInt(maxY);
            speed = random.nextInt(21) + 2;
        }
    }

    /**
     * Method for calculating random star sizes
     * @return -final star size is returned
     */
    public double getSize(){
        double minX = 0.1f;
        double maxX = 6.0f;
        double starSize = random.nextGaussian() * (maxX - minX) + minX;
        return starSize;
    }

    //getters
    public int getX(){
        return xVal;
    }
    public int getY(){
        return yVal;
    }
}
