package de.wasserball.wabaclock.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import de.wasserball.wabaclock.R;

public class Boards extends AppCompatActivity {

    View overlayForNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boards);

        overlayForNavigationBar = findViewById(R.id.boardsLayout);
        hideNavigationBar();
    }

    private void hideNavigationBar() {
        /*overlayForNavigationBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
         */
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
    public void openNewFullBoard(View view){
        Intent intent = new Intent(this, NewFullBoard.class);
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
    public void openZN2(View view){
        Intent intent = new Intent(this, ShotclockRemoteControl.class);
        startActivity(intent);
    }

}
