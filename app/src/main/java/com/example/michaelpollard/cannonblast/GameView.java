package com.example.michaelpollard.cannonblast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michael Pollard on 3/10/2017.
 */
public class GameView extends SurfaceView implements Runnable {

    //miscellaneous variables
    volatile boolean isPlaying = true;
    private Thread gameThread = null;
    private String level = "";
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private MotionEvent e;
    private Context context;
    private Activity activity;
    private int currentScore = 0;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private boolean backFix = true;

    //variables concerned with sound
    private MediaPlayer backgroundSound;
    private MediaPlayer blastsound1;
    private MediaPlayer blastsound2;
    private MediaPlayer explosionSound1;
    private MediaPlayer explosionSound2;
    private MediaPlayer lostSound;
    private MediaPlayer wonSound;
    private MediaPlayer deflectSound;

    //variables concerned with objects on screen
    private ArrayList<Enemies> enemy = new ArrayList<>();
    private ArrayList<GameBackground> background = new ArrayList<>();
    private int enemyNumber = 0;
    private Boolean flag = true;
    private Blocker blocker;
    private CannonBall ball;
    private int cannonSize;
    private Shooter shooter;

    //variables concerned with score
    int highScores[] = new int[5];
    private SharedPreferences sharedPreferences;
    private int shotsFired;

    //variables concerned with time
    private ScheduledExecutorService executorService;
    private int timeLeft = 11;
    private int totaltime = 0;


    //paints for drawn objects
    private Paint textPaint = new Paint();
    private Paint starPaint = new Paint();
    private Paint paint = new Paint();

    /**
     *
     * @param context - the context - represents environment data
     * @param screenX - the width of the screen
     * @param screenY - the height of the screen
     */
    public GameView(Context context, int screenX, int screenY){
        super(context);

        //loads the music
        backgroundSound = MediaPlayer.create(context, R.raw.battlemusic);
        blastsound1 = MediaPlayer.create(context, R.raw.blastsounds);
        blastsound2 = MediaPlayer.create(context, R.raw.blastsounds);
        explosionSound1 = MediaPlayer.create(context, R.raw.explosion);
        explosionSound2 = MediaPlayer.create(context, R.raw.explosion);
        lostSound = MediaPlayer.create(context, R.raw.lostsound);
        wonSound = MediaPlayer.create(context, R.raw.wonsound);
        deflectSound = MediaPlayer.create(context, R.raw.deflectsound);

        //initialize other stuff
        screenWidth = screenX;
        screenHeight = screenY;
        activity = (Activity) context;
        this.context = context;
        blocker = new Blocker(context, screenX, screenY);
        ball = new CannonBall(context, screenX, screenY);
        shooter = new Shooter(context, screenX, screenY);
        cannonSize = screenY / 16;
        textPaint.setTextSize(screenX/26);
        surfaceHolder = getHolder();

        //add number of stars to arraylist for background
        int stars = 100;
        for(int i = 0; i < stars; i++){
            GameBackground s = new GameBackground(screenX, screenY);
            background.add(s);
        }

        //sets the color of all the objects on screen
        textPaint.setColor(Color.WHITE);
        paint.setColor(Color.WHITE);
        starPaint.setColor(Color.WHITE);

        //initializing shared Preferences
        sharedPreferences = context.getSharedPreferences("scores",Context.MODE_PRIVATE);


        //initializing the array high scores with the previous values
        highScores[0] = sharedPreferences.getInt("score1",0);
        highScores[1] = sharedPreferences.getInt("score2",0);
        highScores[2] = sharedPreferences.getInt("score3",0);
        highScores[3] = sharedPreferences.getInt("score4",0);
        highScores[4] = sharedPreferences.getInt("score5",0);

        //decrements the timeleft by 1 every second
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                totaltime++;
                timeLeft--;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Method that lets this activity know the level that was chosen
     * @param levels - sets the level choice from level Activity
     */
    public void setLevel(String levels){
        this.level = levels;
    }


    /**
     * Method that sets the amount of enemies screen
     * according to the current level
     * and alters speed of blocker and enemies by level
     */
    public void createEnemies(){
        if(flag){
            //sets the amount of enemies in the game
            switch (level) {
                case "1":
                    enemyNumber = 2;
                    blocker.setLevel(1);

                    break;
                case "2":
                    enemyNumber = 4;
                    blocker.setLevel(2);

                    break;
                case "3":
                    enemyNumber = 6;
                    blocker.setLevel(3);

                    break;
                case "4":
                    enemyNumber = 8;
                    blocker.setLevel(4);

                    break;
                case "5":
                    enemyNumber = 10;
                    blocker.setLevel(5);

                    break;
                default:
                    System.out.println("There was an error");
                    break;
            }
        }
        for(int i = 0; i < enemyNumber; i++){
            Enemies en = new Enemies(context, screenWidth, screenHeight);
            en.setLevel(Integer.parseInt(level));
            enemy.add(en);
        }
    }

    /**
    * Method that handles the game over dialog popup and its functions
    * @param hasWon is an indicator as to whether user won or lost
    */
    private void gameOver(boolean hasWon){
        String message = "";
        if(backFix) {
            //checks to see if the current score should be a highscore
            for (int i = 0; i < 5; i++) {
                if (highScores[i] < currentScore) {

                    highScores[i] = currentScore;
                    break;
                }
            }

            //storing the scores through shared Preferences

            SharedPreferences.Editor e = sharedPreferences.edit();
            for (int i = 0; i < 5; i++) {

                int scoreName = i + 1;
                e.putInt("score" + scoreName, highScores[i]);
            }
            e.apply();

            //checks whether the player has won or lost and picks a message accordingly
            if (hasWon) {
                message = getResources().getString(R.string.won);
            } else {
                message = getResources().getString(R.string.lost);
            }

            //ends the game loop and timer
            executorService.shutdown();
            isPlaying = false;
        }
        executorService.shutdown();
        isPlaying = false;
        backFix = false;
        //shows the end game dialog message
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(message);
        alertDialogBuilder.setMessage(getResources().getString(R.string.result, totaltime - 1, shotsFired, currentScore)).setCancelable(false)
                //brings the user back to the main menu
                .setPositiveButton(R.string.menu, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               context.startActivity(new Intent(context,LevelActivity.class));
                           }
        })
                //shows the user the current highscores in sharedpreferences
                .setNegativeButton(R.string.highscores, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(context,HighScoreActivity.class));
                    }
                });

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    /**
    * Method that controls the gameloop and its functions
    */
    @Override
    public void run(){
        //sets enemies on screen if they havn't been already
        if(flag){
            createEnemies();
            flag = false;
        }
        //the main loop that controls everything
        while(isPlaying){
            update();
            draw();
            setFPS();
        }
    }

    /**
    * Method that updates an objects position on the screen
    * while checking for collisions and game overs
    */
    public void update(){

        //updates the position of the background stars
        for (GameBackground stars: background){
            stars.update();
        }

        //updates the position of the blocker
        blocker.update();

        //updates the position of the cannonball
        ball.update(e);

        //updates the position of the enemy
        for (Enemies i: enemy){
            i.update();
        }

        detectCollisions();

        //if the player has destroyed all enemies, player has won
        if(enemyNumber <= 0){
            backgroundSound.stop();
            wonSound.start();
            gameOver(true);
        }
        //if the player has run out of time, player has lost
        if(timeLeft <= 0.0){
            backgroundSound.stop();
            lostSound.start();
            gameOver(false);
        }

        //checks to see if ball has gone outside the screen
        if(ball.getxVal() > screenWidth || ball.getxVal() < 0 || ball.getyVal() > screenHeight || ball.getyVal() < 0){
            ball.setDeflected(false);
            ball.setBallOnScreen(false);
        }
    }

    /**
     * method that detects whether the ball has hit the blocker or an enemy
     */
    public void detectCollisions() {
            boolean checkHitBlocker = Rect.intersects(ball.getHitBox(), blocker.getHitBox());

            //checks if the ball hits the blocker
            if (checkHitBlocker) {
                //subtract 2 seconds when hit, plays sound, deflects ball and resets hitbox
                currentScore -= 15 * Integer.parseInt(level);
                timeLeft -= 2;
                deflectSound.start();
                ball.setDeflected(true);
                ball.setHitBox();

            }

            //checks if the ball hits any enemies
            for(Enemies i: enemy) {
                boolean checkHitEnemy = Rect.intersects(ball.getHitBox(), i.getHitBox());
                if (checkHitEnemy) {
                    //add three seconds when hit and subtract number of enemies
                    timeLeft += 3;
                    enemyNumber--;

                    //stops no sound when hit issue
                    if(explosionSound1.isPlaying()) {
                        explosionSound2.start();
                    }
                    else{
                        explosionSound1.start();
                    }

                    //move ball and enemy and hitbox off of the screen
                    ball.setBallOnScreen(false);
                    i.setXVal(screenWidth * 10);
                    ball.setHitBox();
                    currentScore += 10 * Integer.parseInt(level);
                }
            }
    }

    /**
    * Method that draws the shapes at their updated positions
     */
    public void draw(){
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);


            //draws game background of stars - inspired by bubblegame
            for(GameBackground stars: background){
                starPaint.setStrokeWidth((float)stars.getSize());
                canvas.drawPoint(stars.getX(), stars.getY(), starPaint);
            }

            //draws the text for keeping track of time on the screen
            canvas.drawText(getResources().getString(R.string.Time, timeLeft), screenWidth/24, screenHeight * 1/20, textPaint);

            canvas.drawText(getResources().getString(R.string.topscore, currentScore), screenWidth/24, screenHeight * 1/12, textPaint);

            //draws the cannon base - need to change to bitmap
            //canvas.drawCircle(0, screenHeight / 2, cannonSize, paint);

            canvas.drawBitmap(shooter.getShooterImage(),shooter.getxVal(), shooter.getyVal(), paint);

            //draws the blocker onto the screen at it's current position
            canvas.drawBitmap(blocker.getblockerImage(),blocker.getxVal(), blocker.getyVal(), paint);

            //draws the current enemy;
            for(Enemies i: enemy) {
                canvas.drawBitmap(i.getenemyImage(), i.getxVal(), i.getyVal(), paint);
            }

            //draws the cannonball on the screen if it should be on the screen
            if(ball.getBallOnScreen()){
                canvas.drawBitmap(ball.getBallImage(),ball.getxVal(), ball.getyVal(), paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
    * Method that sets the fps of the game to roughly 60fps
     */
    public void setFPS(){
        try {
            gameThread.sleep(17);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
    * Method that pauses the game from running
    */
    public void pause(){
        backgroundSound.pause();
        isPlaying = false;
        try{
            gameThread.join();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
    * Method to restart gameplay after pause
    */
    public void resume() {
    backgroundSound.start();
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
    * Method for reading a touch on the screen
    * @param event - the event of the touch and it's data
    */
    @Override
    public boolean onTouchEvent (MotionEvent event){
        //saves action type in a variable
        if(!ball.getBallOnScreen()) {
        int TouchAction = event.getAction();
        //checks to see if the user taps the screen
            if(TouchAction == MotionEvent.ACTION_DOWN) {
                if(blastsound1.isPlaying()){
                    blastsound2.start();
                }else {
                    blastsound1.start();
                }
                shotsFired++;
                e = event;
                ball.setAngle(event);
                ball.setBallOnScreen(true);
            }
        }
        return true;
    }
}
