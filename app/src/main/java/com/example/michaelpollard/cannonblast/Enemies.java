package com.example.michaelpollard.cannonblast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Michael Pollard on 3/20/2017.
 * this class handles everything to do with the enemies
 */

public class Enemies {

    private Bitmap enemyImage;
    private Bitmap enemyImageResized;
    private int xVal;
    private int yVal;
    private int speed = 0;
    private int topY;
    private int bottomY;
    private boolean goingDown;
    private int upOrDown = 0;
    private Rect hitBox;
    Random random = new Random();

    /**
     * constructor for enemies that initializes things
     * @param context - environment data
     * @param screenX - screen width
     * @param screenY - screen height
     */
    public Enemies(Context context, int screenX, int screenY){

        //creates and resizes the enemy images
        enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.blockerrain);
        enemyImageResized = Bitmap.createScaledBitmap(enemyImage, 30, 200, false);

        //sets the bounds of the y value of the screen
        topY = 0;
        bottomY = screenY - enemyImageResized.getHeight();

        //randomizes the enemies starting y positions
        yVal = random.nextInt(bottomY) - enemyImageResized.getHeight();

        //randomizes the x location of the enemies within a certain range
        xVal = random.nextInt(screenX * 1/4) + (screenX * 2/3) + 50;

        //intializes a hit box detector rectangle that wraps the enemy
        hitBox = new Rect(xVal, yVal, enemyImageResized.getWidth(), enemyImageResized.getHeight());

        //if 1, then enemy starts with going up, if 0, it starts going down
        upOrDown = random.nextInt(2);
        if(upOrDown == 0){
            goingDown = false;
        }
        else{
            goingDown = true;
        }
    }

    /**
     * Method that updates the enemies position on the screen
     */
    public void update(){

        //enemy is traveling down
        if(goingDown){
            yVal += speed;
        }
        //enemy is traveling up
        else{
            yVal -= speed;
        }

        //when enemy hits bottom of screen, sets it to go up
        if(yVal >= bottomY){
            goingDown = false;
        }

        //when enemy hits top of screen, sets it to go down
        if(yVal <= topY){
            goingDown = true;
        }

        //update the hitbox with the position of the blocker
        hitBox.top = yVal;
        hitBox.bottom = yVal + enemyImageResized.getHeight();
        hitBox.left = xVal;
        hitBox.right = xVal + enemyImageResized.getWidth();
    }

    /**
     * setters and getters
     */
    public void setLevel(int level){
        //randomizes the speed of each enemy depending on level
        switch(level){
            case 1:
                speed = random.nextInt(10) + 25;
                break;
            case 2:
                speed = random.nextInt(15) + 27;
                break;
            case 3:
                speed = random.nextInt(15) + 29;
                break;
            case 4:
                speed = random.nextInt(15) + 31;
                break;
            case 5:
                speed = random.nextInt(20) + 32;
                break;
        }
    }

    //setters and getters
    public void setXVal(int x){
        xVal = x;
    }

    public Bitmap getenemyImage() { return enemyImageResized;}

    public Rect getHitBox(){
        return hitBox;
    }

    public int getxVal() {
        return xVal;
    }

    public int getyVal() {
        return yVal;
    }
}
