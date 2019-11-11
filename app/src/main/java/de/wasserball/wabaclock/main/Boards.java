package de.wasserball.wabaclock.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.tvdarmsheim.wabaclock.R;

public class Boards extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boards);
    }

    public void onDoneClicked(View view){
        finish();
    }

    public void openShotclock(View view){
        Intent intent = new Intent(this, Shotclock.class);
        startActivity(intent);
    }
    public void openFullBoard(View view){
        Intent intent = new Intent(this, FullBoard.class);
        startActivity(intent);
    }
    public void openMainTime(View view){
        Intent intent = new Intent(this, MainTimeBoard.class);
        startActivity(intent);
    }
    public void openTimeAndScoreBoard(View view){
        Intent intent = new Intent(this, TimeAndScoreBoard.class);
        startActivity(intent);
    }
    public void openScoreBoard(View view){
        Intent intent = new Intent(this, Scoreboard.class);
        startActivity(intent);
    }

}
