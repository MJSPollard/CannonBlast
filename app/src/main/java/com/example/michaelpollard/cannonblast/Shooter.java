package com.example.michaelpollard.cannonblast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Michael Pollard on 3/27/2017.
 */

public class Shooter {

    private Bitmap shooterImage;
    private Bitmap shooterImageResized;



    private int xVal;
    private int yVal;

    public Shooter(Context context, int screenX, int screenY){
        shooterImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.shooter);
        shooterImageResized = Bitmap.createScaledBitmap(shooterImage, 300, 300, false);

        xVal = 0;
        yVal = screenY * 4/9;
    }

    public Bitmap getShooterImage() {
        return shooterImageResized;
    }

    public int getxVal(){
        return xVal;
    }
    public int getyVal() {
        return yVal;
    }

}
