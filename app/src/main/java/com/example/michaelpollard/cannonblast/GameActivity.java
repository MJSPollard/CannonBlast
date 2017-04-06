package com.example.michaelpollard.cannonblast;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Michael Pollard on 3/10/2017.
 * this Activity controls the main game stuff
 */
public class GameActivity extends AppCompatActivity{

    private GameView gameView;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //sets full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //gets the screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //intialize gameview class
        gameView = new GameView(this, size.x, size.y);

        //tranfsers the data of the level chosen from level activity to game activity
        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("levelValue");
        gameView.setLevel(level);


        //displays gameview stuff
        setContentView(gameView);

    }

    //pauses the game
    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
    }

    //resumes the game
    @Override
    public void onResume(){
        super.onResume();
        gameView.resume();
    }

}
