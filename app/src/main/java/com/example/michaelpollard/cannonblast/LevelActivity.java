package com.example.michaelpollard.cannonblast;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * This Activity handles the starting layout and lets the user decide what to do
 */
public class LevelActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button highScoreButton;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //puts the app in full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_level);

        //initializes buttons
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        highScoreButton = (Button) findViewById(R.id.highScoreButton);

        //sets listeners for each button
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        highScoreButton.setOnClickListener(this);

        //loads sound for when button is pressed
        mp = MediaPlayer.create(this, R.raw.buttonpressed);


    }

    /**
     * Method that creates the options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    /**
     * method the allows the options menu to work
     * @param item - an item of the menu
     * @return a boolean value after executing
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.help:
                Intent myIntent = new Intent(this, HelpActivity.class);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method that checks button pressed and selects activity from there
     * @param v - the identity of thebutton pressed
     */
    @Override
    public void onClick(View v){

        if(v == button1) {
            mp.start();
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("levelValue", "1");
            startActivity(myIntent);
        }
        if(v == button2) {
            mp.start();
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("levelValue", "2");
            startActivity(myIntent);
        }
        if(v == button3) {
            mp.start();
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("levelValue", "3");
            startActivity(myIntent);
        }
        if(v == button4) {
            mp.start();
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("levelValue", "4");
            startActivity(myIntent);
        }
        if(v == button5) {
            mp.start();
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("levelValue", "5");
            startActivity(myIntent);
        }
        if(v == highScoreButton){
            mp.start();
            startActivity(new Intent(this, HighScoreActivity.class));
        }
    }


}
