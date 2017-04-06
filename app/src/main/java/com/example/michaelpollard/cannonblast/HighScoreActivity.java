package com.example.michaelpollard.cannonblast;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * this classes handled the shared preferences
 */
public class HighScoreActivity extends AppCompatActivity {

    TextView tv, tv1,tv2,tv3, tv4, tv5;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //puts it in full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_high_score);

        //initializes textviews of scores
        tv= (TextView) findViewById(R.id.tv);
        tv1= (TextView) findViewById(R.id.tv1);
        tv2= (TextView) findViewById(R.id.tv2);
        tv3= (TextView) findViewById(R.id.tv3);
        tv4= (TextView) findViewById(R.id.tv4);
        tv5= (TextView) findViewById(R.id.tv5);


        sharedPreferences  = getSharedPreferences("scores", Context.MODE_PRIVATE);

        //sets score values - this way was fast than using string resource
        tv1.setText("1. "+sharedPreferences.getInt("score1",0));
        tv2.setText("2. "+sharedPreferences.getInt("score2",0));
        tv3.setText("3. "+sharedPreferences.getInt("score3",0));
        tv4.setText("4. "+sharedPreferences.getInt("score4",0));
        tv5.setText("5. "+sharedPreferences.getInt("score5",0));

    }
}
