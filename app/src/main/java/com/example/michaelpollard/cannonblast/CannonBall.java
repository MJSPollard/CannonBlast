package com.example.michaelpollard.cannonblast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by Michael Pollard on 3/13/2017.
 * This class handles everything to do with the cannonball
 */
public class CannonBall {

    private Bitmap ballImage;
    private int xVal = 0;
    private int yVal = 0;
    private int speed = 60;
    private boolean ballScreen = false;
    private Bitmap ballImageResized;
    int screenHeight = 0;
    int screenWidth = 0;
    private Rect hitBox;
    private boolean deflected = false;

    /**
     * Constructor for cannonball, intializes things
     * @param context - represents environment data
     * @param screenX - screen width
     * @param screenY - screen height
     */
    public CannonBall(Context context, int screenX, int screenY){

        //gets the screen height and width
        screenWidth = screenX;
        screenHeight = screenY;

        //converts png image into bitmap so it can be used
        ballImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball1);

        //resizes ball according to players screen size
        ballImageResized = Bitmap.createScaledBitmap(ballImage,  120, 120, false);

        //sets the size of the hit box detector
        hitBox = new Rect(xVal, yVal, ballImageResized.getWidth(), ballImageResized.getHeight());

    }


    /**
     * Method that - used stuff from a vector2d class
     * @param event - the motionevent of the touch
     * @return - returns the angle of the touch
     */
    public float setAngle(MotionEvent event){
        float angle;
        float center = ((screenHeight / 2) - event.getY());
        if(center == 0){
            center = 1;
        }
        angle = (float) Math.atan(event.getX() / center);
        if(event.getY() > screenHeight / 2) {
            angle += Math.PI; // adjust the angle
        }
        return angle;
    }

    /**
     * Method that updates the location of the ball
     * @param event - the motionevent of the touch
     */
    public void update(MotionEvent event){

        if(ballScreen && !deflected) {
            float angle = setAngle(event);
            xVal += speed * Math.sin(angle);
            yVal += -speed * Math.cos(angle);

            //update the hitbox location with the ball as it moves
            hitBox.top = yVal;
            hitBox.bottom = yVal + ballImageResized.getHeight();
            hitBox.left = xVal;
            hitBox.right = xVal + ballImageResized.getWidth();
        }
        //if the ball hits the blocker the x velocity is reversed
        if(ballScreen && deflected){
            float angle = setAngle(event);
            xVal += -speed * Math.sin(angle);
            yVal += -speed * Math.cos(angle);
        }
    }


    /**
     * setters and getters
     */
    public void setBallOnScreen(boolean check){
        //sets start location of the ball
        xVal = (screenWidth * 1/12);
        yVal = (screenHeight / 2);
        ballScreen = check;
    }

    public void setHitBox() {
        hitBox = new Rect(0, 0, ballImageResized.getWidth(), ballImageResized.getHeight());
    }

    public void setDeflected(boolean deflected){
        this.deflected = deflected;
    }

    public Bitmap getBallImage() { return ballImageResized;}

    public Rect getHitBox(){
        return hitBox;
    }

    public int getxVal() {
        return xVal;
    }

    public int getyVal() {
        return yVal;
    }

    public boolean getBallOnScreen(){
        return ballScreen;
    }
}
