package com.example.michaelpollard.cannonblast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Michael Pollard on 3/13/2017.
 * this class handles everything to do with the blocker
 */

public class Blocker {
    private Bitmap blockerImage;
    private Bitmap blockerImageResized;
    private int xVal;
    private int yVal;
    private int speed = 0;
    private boolean goingDown = true;
    private int topY;
    private int bottomY;
    private Rect hitBox;
    int upOrDown = 0;

    /**
     * constructor for blocker that initializes things
     * @param context - environment data
     * @param screenX - screen width
     * @param screenY - screen height
     */
    public Blocker(Context context, int screenX, int screenY){
        Random random = new Random();

        //creates and resizes the blocker image
        blockerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.blockerfire);
        blockerImageResized = Bitmap.createScaledBitmap(blockerImage, 60, 700, false);

        //sets the bounds for top and bottom of screen
        topY = 0;
        bottomY = screenY - blockerImageResized.getHeight();

        //sets blocker to (5/8) distance of the screen from the left-hand side
        xVal = screenX * 5 / 8;

        //randomizes initial y location blocker
        yVal = random.nextInt(bottomY) - blockerImageResized.getHeight();

        //if 1, then blocker starts with going up, if 0, it starts going down
        upOrDown = random.nextInt(2);
        if(upOrDown == 0){
            goingDown = false;
        }
        else{
            goingDown = true;
        }

        //intializes a rectangle around the bitmap for hit detection
        hitBox = new Rect(xVal, yVal, blockerImageResized.getWidth(), blockerImageResized.getHeight());

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
        hitBox.bottom = yVal + blockerImageResized.getHeight();
        hitBox.left = xVal;
        hitBox.right = xVal + blockerImageResized.getWidth();
    }

    /**
     * setters and getters
     */
    public void setLevel(int level){
        //alters speed of block according to level
        switch(level){
            case 1:
                speed = 10;
                break;
            case 2:
                speed = 16;
                break;
            case 3:
                speed = 22;
                break;
            case 4:
                speed = 28;
                break;
            case 5:
                speed = 34;
                break;
        }
    }
    public Bitmap getblockerImage() { return blockerImageResized;}

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
